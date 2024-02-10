package com.enderio.core.common.util;

import java.util.Iterator;

import com.enderio.core.common.util.NNList.NNIterator;
import org.jetbrains.annotations.NotNull;

public class EndlessNNIterator<T> implements Iterable<T>, NNIterator<T> {

    private int index = -1;
    private final @NotNull NNList<T> itOver;

    public EndlessNNIterator(@NotNull NNList<T> itOver) {
        this.itOver = itOver;
        if (itOver.isEmpty()) {
            throw new RuntimeException("Cannot iterate over empty list");
        }
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public @NotNull T next() {
        if (++index >= itOver.size()) {
            index = 0;
        }
        return itOver.get(index);
    }

    @Override
    public void remove() {}
}
