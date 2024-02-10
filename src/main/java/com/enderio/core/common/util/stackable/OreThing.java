package com.enderio.core.common.util.stackable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import com.enderio.core.common.util.NNList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class OreThing implements IThing.Zwieback {

    private final @NotNull String name;
    private @NotNull NonNullList<ItemStack> ores = new NNList<ItemStack>();

    OreThing(@NotNull String name) {
        this.name = name;
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull NNList<IThing> bake() {
        ores = OreDictionary.getOres(name);
        return new NNList<>(this);
    }

    @Override
    public @Nullable IThing rebake() {
        return ores.isEmpty() ? null : this;
    }

    @Override
    public boolean is(@Nullable Item item) {
        for (ItemStack oreStack : ores) {
            if (oreStack.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean is(@Nullable ItemStack itemStack) {
        for (ItemStack oreStack : ores) {
            if (itemStack != null && !itemStack.isEmpty() && itemStack.getItem() == oreStack.getItem() &&
                    (!oreStack.getHasSubtypes() || oreStack.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
                            oreStack.getMetadata() == itemStack.getMetadata())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean is(@Nullable Block block) {
        for (ItemStack oreStack : ores) {
            if (block != null && (Item.getItemFromBlock(block) == oreStack.getItem() ||
                    Block.getBlockFromItem(oreStack.getItem()) == block)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull NNList<Item> getItems() {
        NNList<Item> result = new NNList<Item>();
        for (ItemStack oreStack : ores) {
            if (!oreStack.isEmpty() && !result.contains(oreStack.getItem())) {
                result.add(oreStack.getItem());
            }
        }
        return result;
    }

    @Override
    public @NotNull NNList<ItemStack> getItemStacks() {
        return NNList.wrap(ores);
    }

    @Override
    public @NotNull NNList<Block> getBlocks() {
        NNList<Block> result = new NNList<Block>();
        for (ItemStack oreStack : ores) {
            if (!oreStack.isEmpty()) {
                Block block = Block.getBlockFromItem(oreStack.getItem());
                if (block != Blocks.AIR) {
                    result.add(block);
                }
            }
        }
        return result;
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("OreThing [name=%s, ores=%s]", name, ores);
    }
}
