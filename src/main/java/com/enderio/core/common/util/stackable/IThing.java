package com.enderio.core.common.util.stackable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.enderio.core.common.util.NNList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

interface IThing {

    @NotNull
    NNList<IThing> bake();

    boolean is(@Nullable Item item);

    boolean is(@Nullable ItemStack itemStack);

    boolean is(@Nullable Block block);

    @NotNull
    NNList<Item> getItems();

    @NotNull
    NNList<ItemStack> getItemStacks();

    @NotNull
    NNList<Block> getBlocks();

    interface Zwieback extends IThing {

        @Nullable
        IThing rebake();
    }
}
