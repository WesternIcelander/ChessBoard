package io.siggi.chessboard.move.finder;

import io.siggi.chessboard.Board;
import io.siggi.chessboard.move.Move;
import io.siggi.chessboard.position.Square;

import java.util.List;

public interface MoveFinder {
    void findMoves(Board board, Square startingSquare, List<Move> moves, boolean checkForCheck);
}
