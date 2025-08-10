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
import io.siggi.chessboard.move.Move;
import io.siggi.chessboard.move.RegularMove;
import io.siggi.chessboard.piece.Piece;
import io.siggi.chessboard.position.Square;

import java.util.List;

public abstract class MoveFinderPiece implements MoveFinder {
    private Move findMove(Board board, Square startingSquare, Square newSquare) {
        Piece thisPiece = board.pieces[startingSquare.index];
        Piece thatPiece = board.pieces[newSquare.index];
        if (thatPiece != null && thisPiece.color == thatPiece.color) return null;
        return new RegularMove(startingSquare, newSquare, thisPiece, null, thatPiece, thatPiece != null ? newSquare : null, null);
    }

    protected void findSingleMove(Board board, Square startingSquare, List<Move> moves, int x, int y) {
        Square newSquare = startingSquare.getRelative(x, y);
        if (newSquare == null) return;
        Move move = findMove(board, startingSquare, newSquare);
        if (move != null) moves.add(move);
    }

    protected void findMoves(Board board, Square startingSquare, List<Move> moves, int x, int y) {
        Square nextSquare = startingSquare;
        while (true) {
            nextSquare = nextSquare.getRelative(x, y);
            if (nextSquare == null) break;
            Move move = findMove(board, startingSquare, nextSquare);
            if (move == null) break;
            moves.add(move);
            if (move.takesPiece()) break;
        }
    }
}
