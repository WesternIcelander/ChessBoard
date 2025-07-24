package io.siggi.chessboard.move.predicate;

import io.siggi.chessboard.move.Move;
import io.siggi.chessboard.move.RegularMove;
import io.siggi.chessboard.piece.PieceType;
import io.siggi.chessboard.position.File;
import io.siggi.chessboard.position.Rank;
import io.siggi.chessboard.position.Square;

import java.util.function.Predicate;

public class RegularMovePredicate implements Predicate<Move> {

    public final PieceType pieceType;
    public final File fromFile;
    public final Rank fromRank;
    public final boolean capture;
    public final Square targetSquare;
    public final PieceType newType;

    public RegularMovePredicate(PieceType pieceType, File fromFile, Rank fromRank, boolean capture, Square targetSquare, PieceType newType) {
        this.pieceType = pieceType;
        this.fromFile = fromFile;
        this.fromRank = fromRank;
        this.capture = capture;
        this.targetSquare = targetSquare;
        this.newType = newType;
    }

    @Override
    public boolean test(Move move) {
        if (!(move instanceof RegularMove)) return false;
        RegularMove m = (RegularMove) move;
        if (pieceType != null && m.piece.type != pieceType) return false;
        if (fromFile != null && m.from.file != fromFile) return false;
        if (fromRank != null && m.from.rank != fromRank) return false;
        if (pieceType == PieceType.Pawn) {
            if (m.takesPiece() != capture) return false;
        } else {
            if (capture && !m.takesPiece()) return false;
        }
        if (targetSquare != null && m.to != targetSquare) return false;
        if (newType != null && (m.newPiece == null || m.newPiece.type != newType)) return false;
        return true;
    }

    private String toString = null;

    @Override
    public String toString() {
        if (toString == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (pieceType != null) {
                if (sb.length() != 1) sb.append(",");
                sb.append("Piece=").append(pieceType.name());
            }
            if (fromFile != null || fromRank != null) {
                if (sb.length() != 1) sb.append(",");
                sb.append("From=");
                sb.append(fromFile == null ? "?" : fromFile.name());
                sb.append(fromRank == null ? "?" : fromRank.name().substring(1));
            }
            if (targetSquare != null) {
                if (sb.length() != 1) sb.append(",");
                sb.append("To=").append(targetSquare.name());
            }
            if (capture) {
                if (sb.length() != 1) sb.append(",");
                sb.append("Captures");
            } else if (pieceType == PieceType.Pawn) {
                if (sb.length() != 1) sb.append(",");
                sb.append("DoesNotCapture");
            }
            if (newType != null) {
                if (sb.length() != 1) sb.append(",");
                sb.append("PromotesTo=").append(newType.name());
            }
            sb.append("}");
            sb.insert(0, "RegularMovePredicate");
            toString = sb.toString();
        }
        return toString;
    }
}
