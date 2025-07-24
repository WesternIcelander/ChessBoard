package io.siggi.chessboard;

import io.siggi.chessboard.move.Move;
import io.siggi.chessboard.util.DefaultBoards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Game {
    public final String startingPosition;
    public final Map<String, String> tags;
    public final List<Move> moves;
    public final List<Comment> comments;

    public Game(String startingPosition, Map<String, String> tags, List<Move> moves, List<Comment> comments) {
        this.startingPosition = startingPosition == null ? DefaultBoards.STANDARD_CHESS : startingPosition;
        this.tags = Collections.unmodifiableMap(tags == null ? new LinkedHashMap<>() : new LinkedHashMap<>(tags));
        this.moves = Collections.unmodifiableList(new ArrayList<>(moves));
        this.comments = Collections.unmodifiableList(comments == null ? new ArrayList<>() : new ArrayList<>(comments));
    }
}
