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
