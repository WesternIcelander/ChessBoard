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
import io.siggi.chessboard.piece.PieceType;
import io.siggi.chessboard.position.Square;

import java.util.List;

public class MoveFinderPawn implements MoveFinder {

    @Override
    public void findMoves(Board board, Square startingSquare, List<Move> moves, boolean checkForCheck) {
        int direction;
        Piece piece = board.pieces[startingSquare.index];
        switch (piece.color) {
            case White:
                direction = 1;
                break;
            case Black:
                direction = -1;
                break;
            default:
                throw new RuntimeException();
        }

        // we typically won't see a pawn on the last rank in regular play
        // it may occur when looking for valid moves when determining if one player is in check
        if (startingSquare.y == 0 || startingSquare.y == 7) return;

        boolean isOnStartingRank = direction == 1 ? (startingSquare.y == 1) : (startingSquare.y == 6);

        Square forwardSpace = startingSquare.getRelative(0, direction);
        boolean forwardSpaceIsEmpty = board.pieces[forwardSpace.index] == null;
        if (forwardSpaceIsEmpty) {
            addPawnMoves(board, startingSquare, forwardSpace, piece, null, null, null, moves);
        }

        Square twoSpacesForward = startingSquare.getRelative(0, direction * 2);
        boolean canMoveTwoSpaces = isOnStartingRank && forwardSpaceIsEmpty && board.pieces[twoSpacesForward.index] == null;
        if (canMoveTwoSpaces) {
            addPawnMoves(board, startingSquare, twoSpacesForward, piece, null, null, forwardSpace, moves);
        }

        Square attackLeft = startingSquare.getRelative(-1, direction);
        if (attackLeft != null && (board.pieces[attackLeft.index] != null || board.enPassant() == attackLeft)) {
            Square enPassantPawn = board.enPassantPawn();
            Piece capturedPiece = board.pieces[enPassantPawn.index];
            addPawnMoves(board, startingSquare, attackLeft, piece, capturedPiece, enPassantPawn, null, moves);
        }

        Square attackRight = startingSquare.getRelative(1, direction);
        if (attackRight != null && (board.pieces[attackRight.index] != null || board.enPassant() == attackRight)) {
            Square enPassantPawn = board.enPassantPawn();
            Piece capturedPiece = board.pieces[enPassantPawn.index];
            addPawnMoves(board, startingSquare, attackRight, piece, capturedPiece, enPassantPawn, null, moves);
        }
    }

    private void addPawnMoves(Board board, Square startingSquare, Square targetSquare, Piece piece, Piece capturedPiece, Square capturedSquare, Square enPassant, List<Move> moves) {
        if (targetSquare.y == 0 || targetSquare.y == 7) {
            for (PieceType newPiece : PieceType.getPromotionPieces()) {
                moves.add(new RegularMove(startingSquare, targetSquare, piece, Piece.of(piece.color, newPiece), capturedPiece, capturedSquare, enPassant, false, false, board.enPassant));
            }
        } else {
            moves.add(new RegularMove(startingSquare, targetSquare, piece, null, capturedPiece, capturedSquare, enPassant, false, false, board.enPassant));
        }
    }
}
