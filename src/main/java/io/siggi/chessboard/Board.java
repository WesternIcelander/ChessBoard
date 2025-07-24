package io.siggi.chessboard;

import io.siggi.chessboard.move.Move;
import io.siggi.chessboard.move.RegularMove;
import io.siggi.chessboard.move.predicate.MovePredicates;
import io.siggi.chessboard.piece.Piece;
import io.siggi.chessboard.piece.PieceColor;
import io.siggi.chessboard.piece.PieceType;
import io.siggi.chessboard.position.CheckType;
import io.siggi.chessboard.position.Rank;
import io.siggi.chessboard.position.Square;
import io.siggi.chessboard.util.ScratchBoard;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public final Piece[] pieces = new Piece[64];
    public PieceColor nextMove = PieceColor.White;
    public boolean whiteCastleKingAllowed = true;
    public boolean whiteCastleQueenAllowed = true;
    public boolean blackCastleKingAllowed = true;
    public boolean blackCastleQueenAllowed = true;
    public Square enPassant = null;
    public int halfMoves = 0;
    public int move = 1;

    public int leftRookFile = 0;
    public int rightRookFile = 7;

    public boolean prohibitSelfCheck = true;

    public Square findKing(PieceColor color) {
        Piece king = Piece.of(color, PieceType.King);
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] == king) return Square.fromIndex(i);
        }
        return null;
    }

    public Square getLeftRookStartingSquare(PieceColor color) {
        return getRookStartingSquare(color, leftRookFile);
    }

    public Square getRightRookStartingSquare(PieceColor color) {
        return getRookStartingSquare(color, rightRookFile);
    }

    private Square getRookStartingSquare(PieceColor color, int file) {
        int rank = color == PieceColor.White ? 0 : 7;
        return Square.fromCoordinate(file, rank);
    }

    public Board copy() {
        Board b = new Board();
        return b.copyFrom(this);
    }

    public Board copyFrom(Board b) {
        System.arraycopy(b.pieces, 0, pieces, 0, pieces.length);
        nextMove = b.nextMove;
        whiteCastleKingAllowed = b.whiteCastleKingAllowed;
        whiteCastleQueenAllowed = b.whiteCastleQueenAllowed;
        blackCastleKingAllowed = b.blackCastleKingAllowed;
        blackCastleQueenAllowed = b.blackCastleQueenAllowed;
        enPassant = b.enPassant;
        halfMoves = b.halfMoves;
        move = b.move;
        return this;
    }

    public List<Move> getAllLegalMoves(PieceColor color) {
        return getAllLegalMoves(color, prohibitSelfCheck);
    }

    private List<Move> getAllLegalMoves(PieceColor color, boolean checkForCheck) {
        if (color == null) color = nextMove;
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] == null || pieces[i].color != color) continue;
            Square square = Square.fromIndex(i);
            PieceType pieceType = pieces[i].type;
            pieceType.moveFinder.findMoves(this, square, moves, checkForCheck);
        }
        if (checkForCheck) {
            Board checkerBoard = ScratchBoard.open();
            try {
                PieceColor fColor = color;
                moves.removeIf(m -> checkerBoard.copyFrom(this).applyMove(m).isInCheck(fColor));
            } finally {
                ScratchBoard.close(checkerBoard);
            }
        }
        return moves;
    }

    public boolean isInCheck(PieceColor color) {
        return getCheckType(color, false) != null;
    }

    public CheckType getCheckType(PieceColor color) {
        return getCheckType(color, true);
    }

    private CheckType getCheckType(PieceColor color, boolean checkForMate) {
        Piece kingPiece = Piece.of(color, PieceType.King);
        List<Move> moves = getAllLegalMoves(color.otherColor(), false);
        check:
        {
            for (Move move : moves) {
                for (RegularMove m : move.asRegularMoves()) {
                    if (pieces[m.to.index] == kingPiece) break check;
                }
            }
            return null;
        }
        if (checkForMate && getAllLegalMoves(color).isEmpty()) {
            return CheckType.CHECKMATE;
        }
        return CheckType.CHECK;
    }

    public ParsedMove parseMove(String notation, boolean apply) {
        List<Move> allMoves = getAllLegalMoves(nextMove);
        allMoves.removeIf(MovePredicates.parse(notation).negate());
        if (allMoves.size() != 1) {
            throw new IllegalArgumentException("Illegal move " + notation);
        }
        Move move = allMoves.get(0);
        String fixedNotation = move.notation(this);
        if (apply) applyMove(move);
        return new ParsedMove(notation, fixedNotation, move);
    }

    public Board applyMove(Move move) {
        Square newEnPassant = null;
        PieceColor color = move.getColor();
        int backRank = color == PieceColor.Black ? 7 : 0;
        Square leftRook = Square.fromCoordinate(leftRookFile, backRank);
        Square rightRook = Square.fromCoordinate(rightRookFile, backRank);
        Square king = findKing(color);
        for (RegularMove m : move.asRegularMoves()) {
            pieces[m.from.index] = null;
        }
        for (RegularMove m : move.asRegularMoves()) {
            boolean pieceWasOnNewSpace = pieces[m.to.index] != null;
            pieces[m.to.index] = m.newPiece == null ? m.piece : m.newPiece;
            if (m.enPassant != null) {
                newEnPassant = m.enPassant;
            }
            if (enPassant != null && move.takesPiece() && !pieceWasOnNewSpace) {
                Square pawn = enPassantPawn();
                if (pawn != null) {
                    pieces[pawn.index] = null;
                }
            }
            if (m.from == king) {
                switch (color) {
                    case White:
                        whiteCastleKingAllowed = whiteCastleQueenAllowed = false;
                        break;
                    case Black:
                        blackCastleKingAllowed = blackCastleQueenAllowed = false;
                        break;
                }
            }
            if (m.from == leftRook) {
                switch (color) {
                    case White:
                        whiteCastleQueenAllowed = false;
                        break;
                    case Black:
                        blackCastleQueenAllowed = false;
                        break;
                }
            }
            if (m.from == rightRook) {
                switch (color) {
                    case White:
                        whiteCastleKingAllowed = false;
                        break;
                    case Black:
                        blackCastleKingAllowed = false;
                        break;
                }
            }
        }
        enPassant = newEnPassant;
        halfMoves += 1;
        if (nextMove == PieceColor.Black) this.move += 1;
        nextMove = nextMove.otherColor();
        return this;
    }

    public Square enPassant() {
        Square expectPawn = enPassantPawn();
        if (expectPawn != null && pieces[expectPawn.index] != null && pieces[expectPawn.index].type == PieceType.Pawn) {
            return enPassant;
        }
        return null;
    }

    public Square enPassantPawn() {
        if (enPassant == null) return null;
        Rank checkRank;
        if (enPassant.rank == Rank._3) {
            checkRank = Rank._4;
        } else if (enPassant.rank == Rank._6) {
            checkRank = Rank._5;
        } else {
            return null;
        }
        return Square.of(enPassant.file, checkRank);
    }
}
