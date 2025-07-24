package io.siggi.chessboard.piece;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PieceTypes {

    private static final Map<String, PieceType> singleLetterNotations;

    static {
        singleLetterNotations = new HashMap<>();
        for (PieceType piece : PieceType.values()) {
            singleLetterNotations.put(piece.singleLetterNotation, piece);
        }
    }

    static PieceType fromSingleLetterNotation(String notation) {
        return singleLetterNotations.get(notation);
    }

    private static final PieceType[] promotionPieces_ = new PieceType[]{
        PieceType.Queen,
                PieceType.Rook,
                PieceType.Bishop,
                PieceType.Knight
    };
    static final List<PieceType> promotionPieces = Collections.unmodifiableList(Arrays.asList(promotionPieces_));
}
