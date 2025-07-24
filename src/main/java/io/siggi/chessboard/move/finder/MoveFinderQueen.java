package io.siggi.chessboard.move.finder;

import io.siggi.chessboard.Board;
import io.siggi.chessboard.move.Move;
import io.siggi.chessboard.position.Square;

import java.util.List;

public class MoveFinderQueen extends MoveFinderPiece {
    @Override
    public void findMoves(Board board, Square startingSquare, List<Move> moves, boolean checkForCheck) {
        findMoves(board, startingSquare, moves, -1, -1);
        findMoves(board, startingSquare, moves, 0, -1);
        findMoves(board, startingSquare, moves, 1, -1);
        findMoves(board, startingSquare, moves, -1, 0);
        findMoves(board, startingSquare, moves, 1, 0);
        findMoves(board, startingSquare, moves, -1, 1);
        findMoves(board, startingSquare, moves, 0, 1);
        findMoves(board, startingSquare, moves, 1, 1);
    }
}
