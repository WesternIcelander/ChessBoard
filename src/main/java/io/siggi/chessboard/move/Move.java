package io.siggi.chessboard.move;

import io.siggi.chessboard.Board;
import io.siggi.chessboard.piece.PieceColor;

import java.util.List;

public interface Move {
    String notation(Board board);

    List<RegularMove> asRegularMoves();

    boolean takesPiece();

    PieceColor getColor();
}
