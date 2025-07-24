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
