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
