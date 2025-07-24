package io.siggi.chessboard;

public class Comment {
    public final int afterMove;
    public final String content;
    public Comment(int afterMove, String content) {
        this.afterMove = afterMove;
        this.content = content;
    }
}
