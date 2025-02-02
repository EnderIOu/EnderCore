package com.enderio.core.common.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NNList<E> extends NonNullList<E> {

    public static final @NotNull NNList<EnumFacing> FACING = NNList.of(EnumFacing.class);

    public static final @NotNull NNList<EnumFacing> FACING_HORIZONTAL = new NNList<EnumFacing>(EnumFacing.HORIZONTALS);

    public static final @NotNull NNList<BlockRenderLayer> RENDER_LAYER = NNList.of(BlockRenderLayer.class);

    public static final @NotNull NNList<BlockPos> SHELL = new NNList<>();
    static {
        for (int y = -1; y <= 1; y++) {
            for (int z = -1; z <= 1; z++) {
                for (int x = -1; x <= 1; x++) {
                    if (x != 0 || y != 0 || z != 0) {
                        SHELL.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        Collections.shuffle(SHELL);
    }

    public NNList() {
        this(new ArrayList<E>(), null);
    }

    public NNList(Collection<E> fillWith) {
        this();
        addAll(fillWith);
    }

    public NNList(int size, @NotNull E fillWith) {
        this();
        for (int i = 0; i < size; i++) {
            add(fillWith);
        }
    }

    public NNList(int size, @NotNull Supplier<E> fillWith) {
        this();
        for (int i = 0; i < size; i++) {
            add(fillWith.get());
        }
    }

    @SafeVarargs
    public NNList(E... fillWith) {
        this();
        Collections.addAll(this, fillWith);
    }

    protected NNList(List<E> list, E defaultElement) {
        super(list, defaultElement);
        this.delegate = list;
        this.defaultElement = defaultElement;
    }

    public @NotNull NNList<E> copy() {
        return new NNList<E>(this);
    }

    public static @NotNull <X> NNList<X> wrap(List<X> list) {
        return list instanceof NNList ? (NNList<X>) list : new NNList<X>(list, null);
    }

    public static @NotNull <X extends Enum<?>> NNList<X> of(Class<X> e) {
        return new NNList<>(e.getEnumConstants());
    }

    public static <X extends Enum<?>> void addAllEnum(NNList<? super X> list, Class<X> e) {
        list.addAll(e.getEnumConstants());
    }

    public static @NotNull <T> Collector<T, ?, NNList<T>> collector() {
        return NullHelper.notnullJ(Collectors.toCollection(NNList::new), "Collectors.toCollection");
    }

    /**
     * Finds the element after the given element.
     * <p>
     * Please note that this does do identity, not equality, checks and cannot handle multiple occurrences of the same
     * element in the list.
     *
     * @throws InvalidParameterException
     *                                   if the given element is not part of the list.
     */
    public @NotNull E next(E current) {
        for (int i = 0; i < delegate.size(); i++) {
            if (get(i) == current) {
                if (i + 1 < delegate.size()) {
                    return get(i + 1);
                } else {
                    return get(0);
                }
            }
        }
        throw new InvalidParameterException();
    }

    /**
     * Finds the element before the given element.
     * <p>
     * Please note that this does do identity, not equality, checks and cannot handle multiple occurrences of the same
     * element in the list.
     *
     * @throws InvalidParameterException
     *                                   if the given element is not part of the list.
     */
    public @NotNull E prev(E current) {
        for (int i = 0; i < delegate.size(); i++) {
            if (get(i) == current) {
                if (i > 0) {
                    return get(i - 1);
                } else {
                    return get(delegate.size() - 1);
                }
            }
        }
        throw new InvalidParameterException();
    }

    public NNList<E> apply(@NotNull Callback<E> callback) {
        for (E e : delegate) {
            if (e == null) {
                throw new NullPointerException();
            }
            callback.apply(e);
        }
        return this;
    }

    @FunctionalInterface
    public interface Callback<E> {

        void apply(@NotNull E e);
    }

    public boolean apply(@NotNull ShortCallback<E> callback) {
        for (E e : delegate) {
            if (e == null) {
                throw new NullPointerException();
            }
            if (callback.apply(e)) {
                return true;
            }
        }
        return callback.finish();
    }

    @FunctionalInterface
    public interface ShortCallback<E> {

        boolean apply(@NotNull E e);

        /**
         * This is called if the callback did not signal <code>true</code> for any element to determine the final result
         * of the run.
         */
        default boolean finish() {
            return false;
        }
    }

    @Override
    public @NotNull NNIterator<E> iterator() {
        return new ItrImpl<E>(delegate.iterator());
    }

    /**
     * Creates a fast iterator for read-only lists. Do not use on lists that may be changed.
     */
    public @NotNull NNIterator<E> fastIterator() {
        return new FastItrImpl();
    }

    public interface NNIterator<E> extends Iterator<E> {

        @Override
        @NotNull
        E next();
    }

    private static class ItrImpl<E> implements NNIterator<E> {

        private final Iterator<E> parent;

        public ItrImpl(Iterator<E> iterator) {
            parent = iterator;
        }

        @Override
        public boolean hasNext() {
            return parent.hasNext();
        }

        @Override
        public @NotNull E next() {
            final E next = parent.next();
            if (next == null) {
                throw new NullPointerException();
            }
            return next;
        }

        @Override
        public void remove() {
            parent.remove();
        }
    }

    private class FastItrImpl implements NNIterator<E> {

        int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor != delegate.size();
        }

        @Override
        public @NotNull E next() {
            try {
                return get(cursor++);
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static final @NotNull NNList<Object> EMPTY = new NNList<Object>(Collections.emptyList(), null);

    @SuppressWarnings("unchecked")
    public static @NotNull <X> NNList<X> emptyList() {
        return (NNList<X>) EMPTY;
    }

    @SafeVarargs
    public final NNList<E> addAll(E... el) {
        for (E e : el) {
            add(e);
        }
        return this;
    }

    public static <E, L extends List<E>> L addIf(@NotNull L list, @Nullable E e) {
        if (e != null) {
            list.add(e);
        }
        return list;
    }

    public NNList<E> addIf(@Nullable E e) {
        if (e != null) {
            add(e);
        }
        return this;
    }

    @SuppressWarnings("null")
    @Override
    public <T> @NotNull T[] toArray(T @NotNull [] a) {
        return delegate.toArray(a);
    }

    public NNList<E> removeAllByClass(Class<? extends E> clazz) {
        delegate.removeIf(e -> clazz.isAssignableFrom(e.getClass()));
        return this;
    }

    // The following replaces all super methods to use our own storage

    private final List<E> delegate;
    private final E defaultElement;

    @Override
    @NotNull
    public E get(int p_get_1_) {
        return NullHelper.notnull(delegate.get(p_get_1_), "Unexpect 'null' object in NNList");
    }

    @Override
    public @NotNull E set(int p_set_1_, @NotNull E p_set_2_) {
        return NullHelper.notnull(delegate.set(p_set_1_, Validate.notNull(p_set_2_)),
                "Unexpect 'null' object in NNList");
    }

    @Override
    public void add(int p_add_1_, @NotNull E p_add_2_) {
        delegate.add(p_add_1_, Validate.notNull(p_add_2_));
    }

    @Override
    public @NotNull E remove(int p_remove_1_) {
        return NullHelper.notnull(delegate.remove(p_remove_1_), "Unexpect 'null' object in NNList");
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void clear() {
        if (defaultElement == null) {
            removeRange(0, delegate.size());
        } else {
            Collections.fill(delegate, defaultElement);
        }
    }
}
