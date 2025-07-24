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
