package io.siggi.chessboard.piece;

public enum PieceColor {
    White,
    Black;

    public PieceColor otherColor() {
        return this == White ? Black : White;
    }
}
