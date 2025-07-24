package io.siggi.chessboard.position;

public enum CheckType {
    CHECK("+"),
    CHECKMATE("#");

    CheckType(String notationSuffix) {
        this.notationSuffix = notationSuffix;
    }

    public final String notationSuffix;
}
