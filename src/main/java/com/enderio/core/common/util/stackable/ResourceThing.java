package com.enderio.core.common.util.stackable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.enderio.core.common.util.NNList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ResourceThing implements IThing {

    private final @NotNull ResourceLocation resourceLocation;

    ResourceThing(@NotNull ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    @Override
    public @NotNull NNList<IThing> bake() {
        // this ugly thing seems to be what Forge wants you to use
        if (net.minecraft.block.Block.REGISTRY.containsKey(resourceLocation)) {
            Block block = net.minecraft.block.Block.REGISTRY.getObject(resourceLocation);
            return new BlockThing(block).bake();
        }
        // this ugly thing seems to be what Forge wants you to use
        Item item = net.minecraft.item.Item.REGISTRY.getObject(resourceLocation);
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
        return NNList.<Item>emptyList();
    }

    @Override
    public @NotNull NNList<ItemStack> getItemStacks() {
        return NNList.<ItemStack>emptyList();
    }

    @Override
    public @NotNull NNList<Block> getBlocks() {
        return NNList.<Block>emptyList();
    }

    @Override
    public String toString() {
        return String.format("ResourceThing [resourceLocation=%s]", resourceLocation);
    }
}
