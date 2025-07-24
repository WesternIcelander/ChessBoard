package io.siggi.chessboard.position;

import static io.siggi.chessboard.position.File.A;
import static io.siggi.chessboard.position.File.B;
import static io.siggi.chessboard.position.File.C;
import static io.siggi.chessboard.position.File.D;
import static io.siggi.chessboard.position.File.E;
import static io.siggi.chessboard.position.File.F;
import static io.siggi.chessboard.position.File.G;
import static io.siggi.chessboard.position.File.H;
import static io.siggi.chessboard.position.Rank._1;
import static io.siggi.chessboard.position.Rank._2;
import static io.siggi.chessboard.position.Rank._3;
import static io.siggi.chessboard.position.Rank._4;
import static io.siggi.chessboard.position.Rank._5;
import static io.siggi.chessboard.position.Rank._6;
import static io.siggi.chessboard.position.Rank._7;
import static io.siggi.chessboard.position.Rank._8;

public enum Square {
    A1(A, _1),
    B1(B, _1),
    C1(C, _1),
    D1(D, _1),
    E1(E, _1),
    F1(F, _1),
    G1(G, _1),
    H1(H, _1),
    A2(A, _2),
    B2(B, _2),
    C2(C, _2),
    D2(D, _2),
    E2(E, _2),
    F2(F, _2),
    G2(G, _2),
    H2(H, _2),
    A3(A, _3),
    B3(B, _3),
    C3(C, _3),
    D3(D, _3),
    E3(E, _3),
    F3(F, _3),
    G3(G, _3),
    H3(H, _3),
    A4(A, _4),
    B4(B, _4),
    C4(C, _4),
    D4(D, _4),
    E4(E, _4),
    F4(F, _4),
    G4(G, _4),
    H4(H, _4),
    A5(A, _5),
    B5(B, _5),
    C5(C, _5),
    D5(D, _5),
    E5(E, _5),
    F5(F, _5),
    G5(G, _5),
    H5(H, _5),
    A6(A, _6),
    B6(B, _6),
    C6(C, _6),
    D6(D, _6),
    E6(E, _6),
    F6(F, _6),
    G6(G, _6),
    H6(H, _6),
    A7(A, _7),
    B7(B, _7),
    C7(C, _7),
    D7(D, _7),
    E7(E, _7),
    F7(F, _7),
    G7(G, _7),
    H7(H, _7),
    A8(A, _8),
    B8(B, _8),
    C8(C, _8),
    D8(D, _8),
    E8(E, _8),
    F8(F, _8),
    G8(G, _8),
    H8(H, _8);

    private static Square[] VALUES = values();

    public final File file;
    public final Rank rank;
    public final int x;
    public final int y;
    public final int index;
    public final int fenIndex;

    Square(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
        this.x = file.ordinal();
        this.y = rank.ordinal();
        this.index = (y * 8) + x;
        int fenY = 7 - y;
        this.fenIndex = (fenY * 8) + x;
    }

    public static Square of(File file, Rank rank) {
        int x = file.ordinal();
        int y = rank.ordinal();
        return VALUES[(y * 8) + x];
    }

    public static Square fromCoordinate(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7)
            throw new IllegalArgumentException("x and y must both be between 0 and 7 (inclusive).");
        return VALUES[(y * 8) + x];
    }

    public static Square fromIndex(int index) {
        if (index < 0 || index > 63) throw new IllegalArgumentException("index must be between 0 and 63 (inclusive).");
        return VALUES[index];
    }

    public static Square fromFenIndex(int index) {
        if (index < 0 || index > 63) throw new IllegalArgumentException("index must be between 0 and 63 (inclusive).");
        int x = index & 7;
        int y = (index >> 3) & 7;
        y = 7 - y;
        return VALUES[(y * 8) + x];
    }

    public Square getRelative(int x, int y) {
        int newX = this.x + x;
        int newY = this.y + y;
        if (newX < 0 || newX > 7 || newY < 0 || newY > 7) return null;
        return VALUES[(newY * 8) + newX];
    }
}
