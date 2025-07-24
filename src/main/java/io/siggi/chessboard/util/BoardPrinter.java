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

package io.siggi.chessboard.util;

import io.siggi.chessboard.Board;
import io.siggi.chessboard.piece.Piece;
import io.siggi.chessboard.piece.PieceColor;

import java.util.Locale;

public final class BoardPrinter {
    private BoardPrinter() {
    }

    public static String print(Board board) {
        StringBuilder sb = new StringBuilder();
        for (int rank = 7; rank >= 0; rank--) {
            sb.append(rank + 1).append(" ");
            for (int file = 0; file < 8; file++) {
                int pos = (rank * 8) + file;
                Piece piece = board.pieces[pos];
                if (piece == null) {
                    sb.append(".");
                    continue;
                }
                String l = piece.type.singleLetterNotation;
                if (piece.color == PieceColor.Black) {
                    l = l.toLowerCase(Locale.ROOT);
                }
                sb.append(l);
            }
            sb.append("\n");
        }
        sb.append("  ABCDEFGH");
        return sb.toString();
    }
}
