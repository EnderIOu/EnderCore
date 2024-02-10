package com.enderio.core.common.util.blockiterators;

import java.util.Iterator;

import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractBlockIterator implements Iterable<BlockPos>, Iterator<BlockPos> {

    protected final @NotNull BlockPos base;

    protected AbstractBlockIterator(@NotNull BlockPos base) {
        this.base = base;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("You can't remove blocks silly!");
    }

    @Override
    public @NotNull Iterator<BlockPos> iterator() {
        return this;
    }
}
