package io.siggi.chessboard.move.predicate;

import io.siggi.chessboard.move.CastleMove;
import io.siggi.chessboard.move.Move;

import java.util.function.Predicate;

public class CastleMovePredicate implements Predicate<Move> {
    public static final CastleMovePredicate KING_SIDE = new CastleMovePredicate(false);
    public static final CastleMovePredicate QUEEN_SIDE = new CastleMovePredicate(true);
    public final boolean queenSide;

    private CastleMovePredicate(boolean queenSide) {
        this.queenSide = queenSide;
    }

    @Override
    public boolean test(Move move) {
        if (!(move instanceof CastleMove)) return false;
        CastleMove m = (CastleMove) move;
        return m.queenSide == queenSide;
    }

    @Override
    public String toString() {
        return queenSide ? "CastleMovePredicate{Side=Queen}" : "CastleMovePredicate{Side=King}";
    }
}
