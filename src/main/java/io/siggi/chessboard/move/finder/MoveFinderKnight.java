package io.siggi.chessboard.move.finder;

import io.siggi.chessboard.Board;
import io.siggi.chessboard.move.Move;
import io.siggi.chessboard.position.Square;

import java.util.List;

public class MoveFinderKnight extends MoveFinderPiece {
    @Override
    public void findMoves(Board board, Square startingSquare, List<Move> moves, boolean checkForCheck) {
        findSingleMove(board, startingSquare, moves, -1, -2);
        findSingleMove(board, startingSquare, moves, 1, -2);
        findSingleMove(board, startingSquare, moves, -2, -1);
        findSingleMove(board, startingSquare, moves, 2, -1);
        findSingleMove(board, startingSquare, moves, -2, 1);
        findSingleMove(board, startingSquare, moves, 2, 1);
        findSingleMove(board, startingSquare, moves, -1, 2);
        findSingleMove(board, startingSquare, moves, 1, 2);
    }
}
