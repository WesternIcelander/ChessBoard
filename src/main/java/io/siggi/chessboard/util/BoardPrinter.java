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
