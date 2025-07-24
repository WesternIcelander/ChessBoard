package io.siggi.chessboard;

import io.siggi.chessboard.move.Move;

public final class ParsedMove {
    public final String input;
    public final String notation;
    public final Move move;

    public ParsedMove(String input, String notation, Move move) {
        this.input = input;
        this.notation = notation;
        this.move = move;
    }
}
