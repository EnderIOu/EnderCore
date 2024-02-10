package com.enderio.core.common.util.stackable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.enderio.core.common.util.NullHelper;

public interface IProducer {

    default @Nullable Block getBlock() {
        return null;
    };

    default @Nullable Item getItem() {
        return null;
    };

    default @NotNull Block getBlockNN() {
        return NullHelper.notnull(
                NullHelper.notnull(getBlock(), "Block ", this, " is unexpectedly missing").delegate.get(), "Block ",
                this,
                " is unexpectedly missing");
    }

    default @NotNull Item getItemNN() {
        return NullHelper.notnull(
                NullHelper.notnull(getItem(), "Item ", this, " is unexpectedly missing").delegate.get(), "Item ", this,
                " is unexpectedly missing");
    }
}
