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
