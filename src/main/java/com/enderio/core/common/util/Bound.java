package com.enderio.core.common.util;

import org.apache.http.annotation.Immutable;
import org.jetbrains.annotations.NotNull;

/**
 * An object to represent a bounds limit on a property.
 *
 * @param <T>
 *            The type of the bound.
 */
@Immutable
public final class Bound<T extends Number & Comparable<T>> {

    public static final @NotNull Bound<Double> MAX_BOUND = Bound.of(Double.MIN_VALUE, Double.MAX_VALUE);

    public static @NotNull <T extends Number & Comparable<T>> Bound<T> of(@NotNull T min, @NotNull T max) {
        return new Bound<>(min, max);
    }

    public final @NotNull T min;
    public final @NotNull T max;

    private Bound(final @NotNull T min, final @NotNull T max) {
        this.min = min;
        this.max = max;
    }

    public @NotNull T getMin() {
        return this.min;
    }

    public @NotNull T getMax() {
        return this.max;
    }

    public @NotNull T clamp(@NotNull T val) {
        return val.compareTo(min) < 0 ? min : val.compareTo(max) > 0 ? max : val;
    }

    @Override
    public boolean equals(final java.lang.Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Bound))
            return false;
        final Bound<?> other = (Bound<?>) o;
        final java.lang.Object this$min = this.getMin();
        final java.lang.Object other$min = other.getMin();
        if (!this$min.equals(other$min))
            return false;
        final java.lang.Object this$max = this.getMax();
        final java.lang.Object other$max = other.getMax();
        return this$max.equals(other$max);
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $min = this.getMin();
        result = result * PRIME + ($min.hashCode());
        final java.lang.Object $max = this.getMax();
        result = result * PRIME + ($max.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Bound(min=" + this.getMin() + ", max=" + this.getMax() + ")";
    }

    public @NotNull Bound<T> withMin(final @NotNull T newMin) {
        return this.min == newMin ? this : new Bound<>(newMin, this.max);
    }

    public @NotNull Bound<T> withMax(final @NotNull T newMax) {
        return this.max == newMax ? this : new Bound<>(this.min, newMax);
    }
}
