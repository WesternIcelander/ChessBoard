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

import io.siggi.chessboard.move.Move;
import io.siggi.chessboard.piece.PieceType;
import io.siggi.chessboard.position.File;
import io.siggi.chessboard.position.Rank;
import io.siggi.chessboard.position.Square;

import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovePredicates {
    private MovePredicates() {
    }

    private static final Pattern MATCH_PATTERN = Pattern.compile("([PRNBQK]?)([a-h]?)([1-8]?)(x?)([a-h][1-8])=?/?\\(?([RNBQ]?)\\)?");

    public static Predicate<Move> parse(String notation) {
        notation = notation.replaceAll("[#+: ]", "");
        notation = notation.replaceAll("=?/?\\(?([QRNB])\\)?$", "$1");
        switch (notation) {
            case "O-O":
            case "0-0":
                return CastleMovePredicate.KING_SIDE;
            case "O-O-O":
            case "0-0-0":
                return CastleMovePredicate.QUEEN_SIDE;
        }
        Matcher matcher = MATCH_PATTERN.matcher(notation);
        if (!matcher.matches()) throw new IllegalArgumentException("Malformed move " + notation);
        String pieceString = matcher.group(1);
        String fileString = matcher.group(2);
        String rankString = matcher.group(3);
        boolean capture = !matcher.group(4).isEmpty();
        String targetSquareString = matcher.group(5);
        String newPieceString = matcher.group(6);
        PieceType piece = pieceString.isEmpty() ? PieceType.Pawn : PieceType.fromSingleLetterNotation(pieceString);
        File file = fileString.isEmpty() ? null : File.valueOf(fileString.toUpperCase(Locale.ROOT));
        Rank rank = rankString.isEmpty() ? null : Rank.valueOf("_" + rankString);
        Square targetSquare = Square.valueOf(targetSquareString.toUpperCase(Locale.ROOT));
        PieceType newPiece = newPieceString.isEmpty() ? null : PieceType.fromSingleLetterNotation(newPieceString);
        return new RegularMovePredicate(piece, file, rank, capture, targetSquare, newPiece);
    }
}
