package com.enderio.core.common.util.stackable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.enderio.core.common.util.NNList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ProducerThing implements IThing {

    private final @NotNull IProducer producer;

    ProducerThing(@NotNull IProducer producer) {
        this.producer = producer;
    }

    @Override
    public @NotNull NNList<IThing> bake() {
        Block block = producer.getBlock();
        if (block != null) {
            return new BlockThing(block).bake();
        }
        Item item = producer.getItem();
        if (item != null) {
            return new ItemThing(item).bake();
        }
        return NNList.emptyList();
    }

    @Override
    public boolean is(@Nullable Item item) {
        return false;
    }

    @Override
    public boolean is(@Nullable ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean is(@Nullable Block block) {
        return false;
    }

    @Override
    public @NotNull NNList<Item> getItems() {
        return NNList.emptyList();
    }

    @Override
    public @NotNull NNList<ItemStack> getItemStacks() {
        return NNList.emptyList();
    }

    @Override
    public @NotNull NNList<Block> getBlocks() {
        return NNList.emptyList();
    }

    @Override
    public String toString() {
        return "ProducerThing []";
    }
}
