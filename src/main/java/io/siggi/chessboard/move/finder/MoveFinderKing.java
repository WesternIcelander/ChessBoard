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

package io.siggi.chessboard.move.finder;

import io.siggi.chessboard.Board;
import io.siggi.chessboard.move.CastleMove;
import io.siggi.chessboard.move.Move;
import io.siggi.chessboard.piece.PieceColor;
import io.siggi.chessboard.piece.PieceType;
import io.siggi.chessboard.position.File;
import io.siggi.chessboard.position.Square;
import io.siggi.chessboard.util.ScratchBoard;

import java.util.ArrayList;
import java.util.List;

public class MoveFinderKing extends MoveFinderPiece {
    @Override
    public void findMoves(Board board, Square startingSquare, List<Move> moves, boolean checkForCheck) {
        findSingleMove(board, startingSquare, moves, -1, -1);
        findSingleMove(board, startingSquare, moves, 0, -1);
        findSingleMove(board, startingSquare, moves, 1, -1);
        findSingleMove(board, startingSquare, moves, -1, 0);
        findSingleMove(board, startingSquare, moves, 1, 0);
        findSingleMove(board, startingSquare, moves, -1, 1);
        findSingleMove(board, startingSquare, moves, 0, 1);
        findSingleMove(board, startingSquare, moves, 1, 1);

        PieceColor color = board.pieces[startingSquare.index].color;
        int rank = color == PieceColor.Black ? 7 : 0;

        if (board.canCastleKingSide(color)) {
            Square rookPosition = board.getRightRookStartingSquare(color);
            Square kingDestination = Square.fromCoordinate(File.G.ordinal(), rank);
            Square rookDestination = Square.fromCoordinate(File.F.ordinal(), rank);
            findCastleMove(board, color, startingSquare, kingDestination, rookPosition, rookDestination, moves, checkForCheck);
        }
        if (board.canCastleQueenSide(color)) {
            Square rookPosition = board.getLeftRookStartingSquare(color);
            Square kingDestination = Square.fromCoordinate(File.C.ordinal(), rank);
            Square rookDestination = Square.fromCoordinate(File.D.ordinal(), rank);
            findCastleMove(board, color, startingSquare, kingDestination, rookPosition, rookDestination, moves, checkForCheck);
        }
    }

    private void findCastleMove(Board board, PieceColor color, Square kingPosition, Square kingDestination, Square rookPosition, Square rookDestination, List<Move> moves, boolean checkForCheck) {
        if (checkForCheck && board.prohibitSelfCheck && board.isInCheck(color)) return;
        List<Square> rookSquares = getSquaresTraveledOn(rookPosition, rookDestination);
        for (Square square : rookSquares) {
            if (board.pieces[square.index] != null && board.pieces[square.index].type != PieceType.King) return;
        }
        List<Square> kingSquares = getSquaresTraveledOn(kingPosition, kingDestination);
        for (Square square : kingSquares) {
            if (board.pieces[square.index] != null && board.pieces[square.index].type != PieceType.Rook) return;
            if (checkForCheck && board.prohibitSelfCheck) {
                Board check = ScratchBoard.open().copyFrom(board);
                try {
                    check.pieces[square.index] = check.pieces[kingPosition.index];
                    check.pieces[kingPosition.index] = null;
                    if (check.isInCheck(color)) return;
                } finally {
                    ScratchBoard.close(check);
                }
            }
        }
        moves.add(new CastleMove(kingPosition, kingDestination, rookPosition, rookDestination, !board.canCastleQueenSide(color), !board.canCastleKingSide(color), board.enPassant));
    }

    private static List<Square> getSquaresTraveledOn(Square start, Square end) {
        List<Square> squares = new ArrayList<>();
        if (start == end) return squares;
        if (start.y != end.y) throw new IllegalArgumentException("Start and end squares must be on the same rank!");
        int direction = start.x < end.x ? 1 : -1;
        Square position = start;
        do {
            squares.add(position = position.getRelative(direction, 0));
        } while (position != end);
        return squares;
    }
}
