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

package io.siggi.chessboard.piece;

import io.siggi.chessboard.move.finder.MoveFinder;
import io.siggi.chessboard.move.finder.MoveFinderBishop;
import io.siggi.chessboard.move.finder.MoveFinderKing;
import io.siggi.chessboard.move.finder.MoveFinderKnight;
import io.siggi.chessboard.move.finder.MoveFinderPawn;
import io.siggi.chessboard.move.finder.MoveFinderQueen;
import io.siggi.chessboard.move.finder.MoveFinderRook;

import java.util.List;

public enum PieceType {
    Pawn("P", new MoveFinderPawn()),
    Rook("R", new MoveFinderRook()),
    Knight("N", new MoveFinderKnight()),
    Bishop("B", new MoveFinderBishop()),
    Queen("Q", new MoveFinderQueen()),
    King("K", new MoveFinderKing());

    public final String singleLetterNotation;
    public final MoveFinder moveFinder;

    PieceType(String singleLetterNotation, MoveFinder moveFinder) {
        this.singleLetterNotation = singleLetterNotation;
        this.moveFinder = moveFinder;
    }

    public static PieceType fromSingleLetterNotation(String notation) {
        return PieceTypes.fromSingleLetterNotation(notation);
    }

    public static List<PieceType> getPromotionPieces() {
        return PieceTypes.promotionPieces;
    }
}
