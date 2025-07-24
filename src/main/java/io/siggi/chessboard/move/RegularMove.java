package io.siggi.chessboard.move;

import io.siggi.chessboard.Board;
import io.siggi.chessboard.move.predicate.MovePredicates;
import io.siggi.chessboard.piece.Piece;
import io.siggi.chessboard.piece.PieceColor;
import io.siggi.chessboard.piece.PieceType;
import io.siggi.chessboard.position.CheckType;
import io.siggi.chessboard.position.Square;
import io.siggi.chessboard.util.Lazy;
import io.siggi.chessboard.util.ScratchBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public final class RegularMove implements Move {
    public final Square from;
    public final Square to;
    public final Piece piece;
    public final Piece newPiece;
    public final boolean takesPiece;
    public final Square enPassant;
    private final List<RegularMove> moveList;
    private final Lazy<List<String>> possibleNotations;

    public RegularMove(Square from, Square to, Piece piece, Piece newPiece, boolean takesPiece, Square enPassant) {
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.newPiece = newPiece;
        this.takesPiece = takesPiece;
        this.enPassant = enPassant;
        this.moveList = Collections.singletonList(this);
        this.possibleNotations = new Lazy<>(() -> {
            String notationPrefix;
            StringBuilder notationSuffix = new StringBuilder();
            String[] middles = new String[]{
                    "",
                    from.file.name().toLowerCase(Locale.ROOT),
                    from.rank.name().substring(1),
                    from.name().toLowerCase(Locale.ROOT)
            };
            if (piece.type == PieceType.Pawn) {
                notationPrefix = "";
                if (takesPiece) {
                    middles = new String[]{
                            middles[1],
                            middles[3]
                    };
                }
            } else {
                notationPrefix = piece.type.singleLetterNotation;
            }
            if (takesPiece) {
                notationSuffix.append("x");
            }
            notationSuffix.append(to.name().toLowerCase(Locale.ROOT));
            if (newPiece != null) {
                notationSuffix.append("=").append(newPiece.type.singleLetterNotation);
            }
            String suffix = notationSuffix.toString();
            List<String> notations = new ArrayList<>();
            for (String middle : middles) {
                notations.add(notationPrefix + middle + suffix);
            }
            return Collections.unmodifiableList(notations);
        });
    }

    @Override
    public String notation(Board board) {
        Board check = ScratchBoard.open().copyFrom(board).applyMove(this);
        try {
            CheckType checkType = check.getCheckType(piece.color.otherColor());
            String checkSuffix = checkType == null ? "" : checkType.notationSuffix;
            List<Move> moves = board.getAllLegalMoves(piece.color);
            List<String> list = possibleNotations.get();
            for (String possibleNotation : list) {
                Predicate<Move> predicate = MovePredicates.parse(possibleNotation);
                if (count(moves, predicate) == 1) {
                    return possibleNotation + checkSuffix;
                }
            }
            // we shouldn't reach this point
            // but if we somehow do, return the most specific one
            return list.get(list.size() - 1) + checkSuffix;
        } finally {
            ScratchBoard.close(check);
        }
    }

    private static int count(List<Move> moves, Predicate<Move> predicate) {
        int count = 0;
        for (Move move : moves) {
            if (predicate.test(move)) count += 1;
        }
        return count;
    }

    @Override
    public List<RegularMove> asRegularMoves() {
        return moveList;
    }

    @Override
    public boolean takesPiece() {
        return takesPiece;
    }

    @Override
    public PieceColor getColor() {
        return piece.color;
    }

    private String toString = null;

    @Override
    public String toString() {
        if (toString == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (piece != null) {
                if (sb.length() != 1) sb.append(",");
                sb.append("Piece=").append(piece.name());
            }
            if (from != null) {
                if (sb.length() != 1) sb.append(",");
                sb.append("From=");
                sb.append(from.name());
            }
            if (to != null) {
                if (sb.length() != 1) sb.append(",");
                sb.append("To=");
                sb.append(to.name());
            }
            if (enPassant != null) {
                if (sb.length() != 1) sb.append(",");
                sb.append("SkipsSquare=").append(enPassant.name());
            }
            if (takesPiece) {
                if (sb.length() != 1) sb.append(",");
                sb.append("Captures");
            }
            if (newPiece != null) {
                if (sb.length() != 1) sb.append(",");
                sb.append("PromotesTo=").append(newPiece.name());
            }
            sb.append("}");
            sb.insert(0, "RegularMove");
            toString = sb.toString();
        }
        return toString;
    }
}
