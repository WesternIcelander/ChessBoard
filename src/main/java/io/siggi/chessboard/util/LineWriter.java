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

import java.io.IOException;
import java.io.Writer;

public class LineWriter implements AutoCloseable {
    private final Writer writer;
    private final int lineLimit;
    private final StringBuilder currentLine = new StringBuilder();
    private boolean closed = false;

    public LineWriter(Writer writer, int lineLimit) {
        if (writer == null) throw new NullPointerException();
        this.writer = writer;
        this.lineLimit = lineLimit;
    }

    public void write(String text) throws IOException {
        if (closed) throw new IOException("Closed");
        String[] pieces = text.split(" ");
        for (String piece : pieces) {
            writeWord(piece);
        }
    }

    private void writeWord(String word) throws IOException {
        int length = currentLine.length();
        if (length == 0) {
            currentLine.append(word);
            return;
        }
        length += 1 + word.length();
        if (length > lineLimit) {
            writeOutLine();
        } else {
            currentLine.append(" ");
        }
        currentLine.append(word);
    }

    private void writeOutLine() throws IOException {
        writer.write(currentLine.toString());
        writer.write("\n");
        currentLine.delete(0, currentLine.length());
    }

    @Override
    public void close() throws IOException {
        if (closed) return;
        closed = true;
        writeOutLine();
    }
}
