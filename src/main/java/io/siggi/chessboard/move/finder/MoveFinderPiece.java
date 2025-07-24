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
        return new RegularMove(startingSquare, newSquare, thisPiece, null, thatPiece != null, null);
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
