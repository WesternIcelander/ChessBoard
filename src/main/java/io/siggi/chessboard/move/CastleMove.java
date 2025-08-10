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

package io.siggi.chessboard.move;

import io.siggi.chessboard.Board;
import io.siggi.chessboard.piece.Piece;
import io.siggi.chessboard.piece.PieceColor;
import io.siggi.chessboard.piece.PieceType;
import io.siggi.chessboard.position.CheckType;
import io.siggi.chessboard.position.Square;
import io.siggi.chessboard.util.ScratchBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CastleMove implements Move {
    public final Square kingFrom;
    public final Square kingTo;
    public final Square rookFrom;
    public final Square rookTo;
    public final boolean queenSide;
    private final List<RegularMove> moveList;

    public CastleMove(Square kingFrom, Square kingTo, Square rookFrom, Square rookTo) {
        this.kingFrom = kingFrom;
        this.kingTo = kingTo;
        this.rookFrom = rookFrom;
        this.rookTo = rookTo;
        List<RegularMove> moves = new ArrayList<>();
        PieceColor color = kingFrom.y == 0 ? PieceColor.White : PieceColor.Black;
        this.queenSide = kingTo.x == 2;
        if (kingFrom != kingTo) {
            moves.add(new RegularMove(kingFrom, kingTo, Piece.of(color, PieceType.King), null, null, null, null));
        }
        if (rookFrom != rookTo) {
            moves.add(new RegularMove(rookFrom, rookTo, Piece.of(color, PieceType.Rook), null, null, null, null));
        }
        this.moveList = Collections.unmodifiableList(moves);
    }

    @Override
    public String notation(Board board) {
        Board check = ScratchBoard.open().copyFrom(board).applyMove(this);
        try {
            CheckType checkType = check.getCheckType(board.pieces[kingFrom.index].color.otherColor());
            String checkSuffix = checkType == null ? "" : checkType.notationSuffix;
            String notation = queenSide ? "0-0-0" : "0-0";
            return notation + checkSuffix;
        } finally {
            ScratchBoard.close(check);
        }
    }

    @Override
    public List<RegularMove> asRegularMoves() {
        return moveList;
    }

    @Override
    public Piece capturedPiece() {
        return null;
    }

    @Override
    public Square capturedSquare() {
        return null;
    }

    @Override
    public PieceColor getColor() {
        return kingFrom.y == 0 ? PieceColor.White : PieceColor.Black;
    }

    @Override
    public String toString() {
        return queenSide ? "CastleMove{Side=Queen}" : "CastleMove{Side=King}";
    }
}
