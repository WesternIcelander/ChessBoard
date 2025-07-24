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
