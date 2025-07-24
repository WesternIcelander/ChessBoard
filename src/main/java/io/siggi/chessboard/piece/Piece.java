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

import static io.siggi.chessboard.piece.PieceColor.Black;
import static io.siggi.chessboard.piece.PieceColor.White;
import static io.siggi.chessboard.piece.PieceType.Bishop;
import static io.siggi.chessboard.piece.PieceType.King;
import static io.siggi.chessboard.piece.PieceType.Knight;
import static io.siggi.chessboard.piece.PieceType.Pawn;
import static io.siggi.chessboard.piece.PieceType.Queen;
import static io.siggi.chessboard.piece.PieceType.Rook;

public enum Piece {
    WhitePawn(White, Pawn),
    WhiteRook(White, Rook),
    WhiteKnight(White, Knight),
    WhiteBishop(White, Bishop),
    WhiteQueen(White, Queen),
    WhiteKing(White, King),
    BlackPawn(Black, Pawn),
    BlackRook(Black, Rook),
    BlackKnight(Black, Knight),
    BlackBishop(Black, Bishop),
    BlackQueen(Black, Queen),
    BlackKing(Black, King);

    public final PieceColor color;
    public final PieceType type;

    Piece(PieceColor color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    public static Piece of(PieceColor color, PieceType type) {
        return Pieces.get(color, type);
    }
}
