package com.enderio.core.common.util.stackable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.enderio.core.common.util.NNList;

class ItemStackThing implements IThing {

    private final @NotNull ItemStack thing;

    ItemStackThing(@NotNull ItemStack itemStack) {
        this.thing = itemStack;
    }

    @Override
    public @NotNull NNList<IThing> bake() {
        return thing.isEmpty() ? NNList.emptyList() : new NNList<>(this);
    }

    @Override
    public boolean is(@Nullable Item item) {
        return thing.getItem() == item;
    }

    @Override
    public boolean is(@Nullable ItemStack itemStack) {
        return itemStack != null && !itemStack.isEmpty() && thing.getItem() == itemStack.getItem() &&
                (!thing.getHasSubtypes() || thing.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
                        thing.getMetadata() == itemStack.getMetadata());
    }

    @Override
    public boolean is(@Nullable Block block) {
        return block != null &&
                (Item.getItemFromBlock(block) == thing.getItem() || Block.getBlockFromItem(thing.getItem()) == block);
    }

    @Override
    public @NotNull NNList<Item> getItems() {
        return new NNList<>(thing.getItem());
    }

    @Override
    public @NotNull NNList<ItemStack> getItemStacks() {
        return new NNList<>(thing);
    }

    @Override
    public @NotNull NNList<Block> getBlocks() {
        Block block = Block.getBlockFromItem(thing.getItem());
        return block != Blocks.AIR ? new NNList<>(block) : NNList.emptyList();
    }

    @Override
    public String toString() {
        return String.format("ItemStackThing [thing=%s]", thing);
    }
}
