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

import java.util.ArrayList;
import java.util.List;

public final class ScratchBoard {
    private ScratchBoard() {
    }

    private static final ThreadLocal<ScratchBoard> threadLocal = ThreadLocal.withInitial(ScratchBoard::new);

    private int openBoards = 0;
    private final List<Board> boards = new ArrayList<>();

    public static Board open() {
        return threadLocal.get().doOpen();
    }

    public static void close(Board board) {
        threadLocal.get().doClose(board);
    }

    public static void destroy() {
        threadLocal.remove();
    }

    private Board doOpen() {
        if (boards.size() <= openBoards) {
            boards.add(new Board());
        }
        Board openBoard = boards.get(openBoards);
        openBoards += 1;
        return openBoard;
    }

    private void doClose(Board board) {
        if (openBoards > 0) openBoards -= 1;
        else throw new IllegalStateException("Closing a board when none were open.");
    }
}
