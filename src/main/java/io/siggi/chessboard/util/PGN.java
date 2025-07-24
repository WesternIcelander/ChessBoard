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
import io.siggi.chessboard.Comment;
import io.siggi.chessboard.Game;
import io.siggi.chessboard.ParsedMove;
import io.siggi.chessboard.move.CastleMove;
import io.siggi.chessboard.move.Move;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGN {

    public static final Pattern MOVE_PATTERN = Pattern.compile("([PRNBQK]?[a-h]?[1-8]?x?[a-h][1-8]=?/?\\(?[RNBQ]?\\)?|0-0(-0)?|O-O(-O)?)(=[RNBQ])?([+#]?)");
    public static final Pattern TOKEN_PATTERN = Pattern.compile("([1-9][0-9]*\\.|\\{[^}]*+}|;[^\\n]*+|" + MOVE_PATTERN.pattern() + ")", Pattern.DOTALL);
    public static final Pattern TAG_PATTERN = Pattern.compile("\\[(.*)\\w*\"(.*)\"\\]");

    private static final List<String> sevenTagRoster = new ArrayList<>();
    private static final Set<String> sevenTagRosterSet = new HashSet<>();
    private static void addSevenTagRosterItem(String item) {
        sevenTagRoster.add(item);
        sevenTagRosterSet.add(item);
    }
    static {
        addSevenTagRosterItem("Event");
        addSevenTagRosterItem("Site");
        addSevenTagRosterItem("Date");
        addSevenTagRosterItem("Round");
        addSevenTagRosterItem("White");
        addSevenTagRosterItem("Black");
        addSevenTagRosterItem("Result");
    }

    public static Game deserialize(String pgn) {
        try {
            CharArrayReader car = new CharArrayReader(pgn.toCharArray());
            return read(new BufferedReader(car));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Game read(BufferedReader reader) throws IOException {
        String tagSection = null;
        String gameSection = null;
        StringBuilder sb = new StringBuilder();
        boolean inGameSection = false;
        String line;
        reader.mark(1024);
        while ((line = reader.readLine()) != null) {
            if (!inGameSection && line.isEmpty()) {
                inGameSection = true;
                tagSection = sb.toString();
                sb.delete(0, sb.length());
            }
            if (line.startsWith("%")) continue;
            String trimmedLine = line.trim();
            if (inGameSection && trimmedLine.startsWith("[") && trimmedLine.endsWith("\"]")) {
                reader.reset();
                break;
            }
            sb.append(line).append("\n");
            reader.mark(1024);
        }
        if (!inGameSection) return null;
        gameSection = sb.toString();

        Map<String, String> tags = new LinkedHashMap<>();
        Matcher tagMatcher = TAG_PATTERN.matcher(tagSection);
        while (tagMatcher.find()) {
            tags.put(tagMatcher.group(1).trim(), tagMatcher.group(2));
        }

        int move = 0;
        List<Move> moves = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        String startingPosition = tags.get("FEN");
        if (startingPosition == null) startingPosition = DefaultBoards.STANDARD_CHESS;
        Board board = ScratchBoard.open();
        try {
            FEN.deserialize(board, startingPosition);
            Matcher tokenMatcher = TOKEN_PATTERN.matcher(gameSection);
            while (tokenMatcher.find()) {
                String token = tokenMatcher.group();
                if (token.startsWith(";")) {
                    comments.add(new Comment(move, token.substring(1).trim()));
                    continue;
                } else if (token.startsWith("{")) {
                    comments.add(new Comment(move, token.substring(1, token.length() - 1).replace("\n", " ").replaceAll(" {2,}", " ")));
                    continue;
                } else if (token.endsWith(".")) {
                    continue;
                }
                ParsedMove parsedMove = board.parseMove(token, true);
                moves.add(parsedMove.move);
                move += 1;
            }
        } finally {
            ScratchBoard.close(board);
        }
        return new Game(startingPosition, tags, moves, comments);
    }

    public static String serialize(Game game) {
        try {
            CharArrayWriter caw = new CharArrayWriter();
            write(caw, game);
            return caw.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(Writer writer, Game game) throws IOException {
        for (String key : sevenTagRoster) {
            String value = game.tags.get(key);
            if (value == null) continue;
            writeTag(writer, key, value);
        }
        for (Map.Entry<String, String> entry : game.tags.entrySet()) {
            String key = entry.getKey();
            if (key == null) continue;
            if (sevenTagRosterSet.contains(key)) continue;
            String value = entry.getValue();
            if (value == null) continue;
            writeTag(writer, key, value);
        }
        writer.write("\n");
        try (LineWriter lw = new LineWriter(writer, 80)) {
            Board board = ScratchBoard.open();
            try {
                FEN.deserialize(board, game.startingPosition);
                int moveNumber = 0;
                Comment nextComment = game.comments.isEmpty() ? null : game.comments.get(0);
                int comment = 0;
                for (Move move : game.moves) {
                    while (nextComment != null && nextComment.afterMove <= moveNumber) {
                        lw.write("{" + nextComment.content + "}");
                        comment += 1;
                        nextComment = game.comments.size() > comment ? game.comments.get(comment) : null;
                    }
                    String prefix = "";
                    if ((moveNumber % 2) == 0) {
                        prefix = ((moveNumber + 2) / 2) + ".";
                    }
                    String moveNotation = move.notation(board);
                    if (move instanceof CastleMove) moveNotation = moveNotation.replace("0", "O");
                    board.applyMove(move);
                    lw.write(prefix + moveNotation);
                    moveNumber += 1;
                }
                String result = game.tags.get("Result");
                if (result == null) result = "*";
                lw.write(result);
            } finally {
                ScratchBoard.close(board);
            }
        }
    }
    private static void writeTag(Writer writer, String key, String value) throws IOException {
        writer.write("[");
        writer.write(key);
        writer.write(" \"");
        writer.write(value);
        writer.write("\"]\n");
    }
}
