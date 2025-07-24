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

import java.util.HashMap;
import java.util.Map;

import static io.siggi.chessboard.piece.PieceColor.Black;
import static io.siggi.chessboard.piece.PieceColor.White;

class Pieces {

    private static final Map<PieceColor, Map<PieceType, Piece>> pieces;

    static {
        pieces = new HashMap<>();
        pieces.put(White, new HashMap<>());
        pieces.put(Black, new HashMap<>());
        for (Piece piece : Piece.values()) {
            pieces.get(piece.color).put(piece.type, piece);
        }
    }

    static Piece get(PieceColor color, PieceType type) {
        return pieces.get(color).get(type);
    }
}
