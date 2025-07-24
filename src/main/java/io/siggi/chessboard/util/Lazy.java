package io.siggi.chessboard.util;

import java.util.function.Supplier;

public final class Lazy<T> {
    public Lazy(Supplier<T> supplier) {
        this(supplier, false);
    }

    public Lazy(Supplier<T> supplier, boolean immutable) {
        this.supplier = supplier;
        this.immutable = immutable;
    }

    private Supplier<T> supplier;
    private T value;
    private final boolean immutable;

    public T get() {
        if (supplier != null) {
            value = supplier.get();
            supplier = null;
        }
        return value;
    }

    public void set(T value) {
        if (immutable) throw new UnsupportedOperationException();
        supplier = null;
        this.value = value;
    }

    public boolean isImmutable() {
        return immutable;
    }
}
