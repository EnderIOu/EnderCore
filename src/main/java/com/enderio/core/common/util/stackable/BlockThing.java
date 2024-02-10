package com.enderio.core.common.util.stackable;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;

import com.enderio.core.common.util.NNList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class BlockThing implements IThing {

    private final @NotNull Block thing;
    private final @Nullable Item blockItem;

    public BlockThing(@NotNull Block block) {
        this.thing = block;
        this.blockItem = findBlockItem(block);
    }

    public static @Nullable Item findBlockItem(@NotNull Block block) {
        Item item = Item.getItemFromBlock(block);
        if (item != Items.AIR) {
            return item;
        }
        for (Item candidate : Item.REGISTRY) {
            if (candidate instanceof ItemBlockSpecial && ((ItemBlockSpecial) candidate).getBlock() == block) {
                return candidate;
            }
            if (candidate instanceof ItemBlock && ((ItemBlock) candidate).getBlock() == block) {
                // "bad data" case, but mods may produce it...
                return candidate;
            }
        }
        return null;
    }

    @Override
    public @NotNull NNList<IThing> bake() {
        return new NNList<>(this);
    }

    @Override
    public boolean is(@Nullable Item item) {
        return blockItem == item;
    }

    @Override
    public boolean is(@Nullable ItemStack itemStack) {
        return itemStack != null && !itemStack.isEmpty() && is(itemStack.getItem());
    }

    @Override
    public boolean is(@Nullable Block block) {
        return this.thing == block;
    }

    @Override
    public @NotNull NNList<Item> getItems() {
        return blockItem != null ? new NNList<>(blockItem) : NNList.emptyList();
    }

    @Override
    public @NotNull NNList<ItemStack> getItemStacks() {
        return blockItem != null ? new NNList<>(new ItemStack(blockItem)) : NNList.emptyList();
    }

    @Override
    public @NotNull NNList<Block> getBlocks() {
        return new NNList<>(thing);
    }

    @Override
    public String toString() {
        return String.format("BlockThing [thing=%s, item= %s]", thing, blockItem);
    }
}
