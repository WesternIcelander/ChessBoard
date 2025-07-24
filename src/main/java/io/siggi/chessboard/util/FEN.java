/*
    Copyright (C) 2025  Sigurður Jón (Siggi)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.siggi.chessboard.util;

import io.siggi.chessboard.Board;
import io.siggi.chessboard.piece.Piece;
import io.siggi.chessboard.piece.PieceColor;
import io.siggi.chessboard.piece.PieceType;
import io.siggi.chessboard.position.Square;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class FEN {

    private static final Map<Piece, String> pieceToNotation = new HashMap<>();
    private static final Map<String, Piece> notationToPiece = new HashMap<>();

    private static void registerPieceNotation(Piece piece, String notation) {
        pieceToNotation.put(piece, notation);
        notationToPiece.put(notation, piece);
    }

    static {
        registerPieceNotation(Piece.WhitePawn, "P");
        registerPieceNotation(Piece.WhiteRook, "R");
        registerPieceNotation(Piece.WhiteKnight, "N");
        registerPieceNotation(Piece.WhiteBishop, "B");
        registerPieceNotation(Piece.WhiteQueen, "Q");
        registerPieceNotation(Piece.WhiteKing, "K");
        registerPieceNotation(Piece.BlackPawn, "p");
        registerPieceNotation(Piece.BlackRook, "r");
        registerPieceNotation(Piece.BlackKnight, "n");
        registerPieceNotation(Piece.BlackBishop, "b");
        registerPieceNotation(Piece.BlackQueen, "q");
        registerPieceNotation(Piece.BlackKing, "k");
    }

    private static final char[] files = "ABCDEFGHabcdefgh".toCharArray();
    private static final int[] charToFile = new int[256];
    static {
        charToFile['A'] = 0;
        charToFile['B'] = 1;
        charToFile['C'] = 2;
        charToFile['D'] = 3;
        charToFile['E'] = 4;
        charToFile['F'] = 5;
        charToFile['G'] = 6;
        charToFile['H'] = 7;
        charToFile['a'] = 0;
        charToFile['b'] = 1;
        charToFile['c'] = 2;
        charToFile['d'] = 3;
        charToFile['e'] = 4;
        charToFile['f'] = 5;
        charToFile['g'] = 6;
        charToFile['h'] = 7;
    }

    private static String castlingString(Board board) {
        String castling = "KQkq";

        if (board.leftRookFile != 0 || board.rightRookFile != 7) {
            char leftRookWhite = files[Math.max(0, board.leftRookFile)];
            char rightRookWhite = files[Math.max(0, board.rightRookFile)];
            char leftRookBlack = files[8 + Math.max(0, board.leftRookFile)];
            char rightRookBlack = files[8 + Math.max(0, board.rightRookFile)];
            castling = new String(new char[] {rightRookWhite, leftRookWhite, rightRookBlack, leftRookBlack});
        }
        return castling;
    }

    public static String serialize(Board board) {
        StringBuilder sb = new StringBuilder();

        String castling = castlingString(board);

        sb.append(serializeSquares(board.pieces));
        sb.append(" ");
        sb.append(board.nextMove == PieceColor.White ? "w" : "b");
        sb.append(" ");
        int castlePos = sb.length();
        if (board.whiteCastleKingAllowed) sb.append(castling.charAt(0));
        if (board.whiteCastleQueenAllowed) sb.append(castling.charAt(1));
        if (board.blackCastleKingAllowed) sb.append(castling.charAt(2));
        if (board.blackCastleQueenAllowed) sb.append(castling.charAt(3));
        if (sb.length() == castlePos) sb.append("-");
        sb.append(" ");
        if (board.enPassant == null) {
            sb.append("-");
        } else {
            sb.append(board.enPassant.name().toLowerCase(Locale.ROOT));
        }
        sb.append(" ");
        sb.append(board.halfMoves);
        sb.append(" ");
        sb.append(board.move);

        return sb.toString();
    }

    public static Board deserialize(String notation) {
        return deserialize(new Board(), notation);
    }

    public static Board deserialize(Board board, String notation) {
        String[] parts = notation.split(" ");

        try {
            deserializeSquares(board.pieces, parts[0]);
            switch (parts[1]) {
                case "w":
                    board.nextMove = PieceColor.White;
                    break;
                case "b":
                    board.nextMove = PieceColor.Black;
                    break;
                default:
                    throw new IllegalArgumentException("Malformed FEN notation");
            }
            board.whiteCastleKingAllowed = false;
            board.whiteCastleQueenAllowed = false;
            board.blackCastleKingAllowed = false;
            board.blackCastleQueenAllowed = false;
            boolean chess960 = false;
            for (int i = 0; i < parts[2].length(); i++) {
                switch (parts[2].charAt(i)) {
                    case '-':
                        break;
                    case 'K':
                        board.whiteCastleKingAllowed = true;
                        break;
                    case 'Q':
                        board.whiteCastleQueenAllowed = true;
                        break;
                    case 'k':
                        board.blackCastleKingAllowed = true;
                        break;
                    case 'q':
                        board.blackCastleQueenAllowed = true;
                        break;
                    default:
                        i = parts[2].length();
                        chess960 = true;
                }
            }
            chess960:
            if (chess960) {
                int fileCount = 0;
                boolean[] rookFiles = new boolean[8];
                for (int i = 0; i < parts[2].length(); i++) {
                    int file = charToFile[parts[2].charAt(i)];
                    if (!rookFiles[file]) {
                        fileCount += 1;
                        rookFiles[file] = true;
                    }
                    if (fileCount > 2) {
                        break chess960;
                    }
                }
                int leftFile = -1;
                int rightFile = -1;
                if (fileCount == 2) {
                    for (int i = 0; i < 8 && leftFile == -1; i++) {
                        if (rookFiles[i]) leftFile = i;
                    }
                    for (int i = 7; i >= 0 && rightFile == -1; i--) {
                        if (rookFiles[i]) rightFile = i;
                    }
                } else {
                    int rankToCheck = parts[2].charAt(0) >= 'a' ? 7 : 0;
                    int file = charToFile[parts[2].charAt(0)];
                    for (int x = 0; x < 8; x++) {
                        Square square = Square.fromCoordinate(x, rankToCheck);
                        if (board.pieces[square.index] != null && board.pieces[square.index].type == PieceType.King) {
                            if (file < x) {
                                leftFile = file;
                            } else if (file > x) {
                                rightFile = file;
                            }
                        }
                    }
                }
                char whiteLeft = leftFile >= 0 ? files[leftFile] : '\0';
                char whiteRight = rightFile >= 0 ? files[rightFile] : '\0';
                char blackLeft = leftFile >= 0 ? files[8 + leftFile] : '\0';
                char blackRight = rightFile >= 0 ? files[8 + rightFile] : '\0';
                for (int i = 0; i < parts[2].length(); i++) {
                    char c = parts[2].charAt(i);
                    if (c == whiteLeft) board.whiteCastleQueenAllowed = true;
                    if (c == whiteRight) board.whiteCastleKingAllowed = true;
                    if (c == blackLeft) board.blackCastleQueenAllowed = true;
                    if (c == blackRight) board.blackCastleKingAllowed = true;
                }
                board.leftRookFile = leftFile;
                board.rightRookFile = rightFile;
            }
            if (parts[3].equals("-")) {
                board.enPassant = null;
            } else {
                board.enPassant = Square.valueOf(parts[3].toUpperCase(Locale.ROOT));
            }
            board.halfMoves = Integer.parseInt(parts[4]);
            board.move = Integer.parseInt(parts[5]);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Malformed FEN notation");
        }
        return board;
    }

    public static String serializeSquares(Piece[] pieces) {
        if (pieces.length != 64) throw new IllegalArgumentException("Array of pieces needs to be of length 64.");
        StringBuilder sb = new StringBuilder();
        for (int rank = 7; rank >= 0; rank--) {
            if (rank != 7) sb.append("/");
            int blanks = 0;
            for (int file = 0; file < 8; file++) {
                int pos = (rank * 8) + file;
                if (pieces[pos] == null) {
                    blanks += 1;
                } else {
                    if (blanks > 0) sb.append(blanks);
                    blanks = 0;
                    sb.append(pieceToNotation.get(pieces[pos]));
                }
            }
            if (blanks > 0) sb.append(blanks);
        }
        return sb.toString();
    }

    public static void deserializeSquares(Piece[] pieces, String notation) {
        if (pieces.length != 64) throw new IllegalArgumentException("Array of pieces needs to be of length 64.");
        Arrays.fill(pieces, null);
        String[] splitNotation = notation.split("/");
        if (splitNotation.length != 8) throw new IllegalArgumentException("Malformed FEN notation");
        for (int rank = 0; rank < splitNotation.length; rank++) {
            int pos = (7 - rank) * 8;
            int end = pos + 8;
            for (int i = 0; i < splitNotation[rank].length(); i++) {
                String character = splitNotation[rank].substring(i, i + 1);
                Piece piece = notationToPiece.get(character);
                if (piece == null) {
                    try {
                        pos += Integer.parseInt(character);
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException("Malformed FEN notation");
                    }
                    continue;
                }
                try {
                    pieces[pos++] = piece;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Malformed FEN notation");
                }
            }
            if (pos != end) throw new IllegalArgumentException("Malformed FEN notation");
        }
    }
}
