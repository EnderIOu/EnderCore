package com.enderio.core.common.util.stackable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.enderio.core.common.util.NNList;

class ItemThing implements IThing {

    private final @NotNull Item thing;

    ItemThing(@NotNull Item item) {
        this.thing = item;
    }

    @Override
    public @NotNull NNList<IThing> bake() {
        return new NNList<>(this);
    }

    @Override
    public boolean is(@Nullable Item item) {
        return item == thing;
    }

    @Override
    public boolean is(@Nullable ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() == thing;
    }

    @Override
    public boolean is(@Nullable Block block) {
        return block != null && (Item.getItemFromBlock(block) == thing || Block.getBlockFromItem(thing) == block ||
                (thing instanceof ItemBlockSpecial && ((ItemBlockSpecial) thing).getBlock() == block));
    }

    @Override
    public @NotNull NNList<Item> getItems() {
        return new NNList<>(thing);
    }

    @Override
    public @NotNull NNList<ItemStack> getItemStacks() {
        return new NNList<>(new ItemStack(thing));
    }

    @Override
    public @NotNull NNList<Block> getBlocks() {
        Block block = Block.getBlockFromItem(thing);
        if (block == Blocks.AIR && thing instanceof ItemBlockSpecial) {
            block = ((ItemBlockSpecial) thing).getBlock();
        }
        return block != Blocks.AIR ? new NNList<>(block) : NNList.emptyList();
    }

    @Override
    public String toString() {
        return String.format("ItemThing [thing=%s]", thing);
    }
}
