package com.enderio.core.common.util.stackable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.enderio.core.common.util.NNList;
import com.enderio.core.common.util.NullHelper;

class StringThing implements IThing {

    private final @NotNull String name;

    StringThing(@Nullable String name) {
        this.name = name != null ? NullHelper.notnullJ(name.trim(), "String.trim()") : "";
    }

    @Override
    public @NotNull NNList<IThing> bake() {
        if (name.isEmpty()) {
            return NNList.emptyList();
        }
        if (Things.aliases.containsKey(name)) {
            return Things.aliases.get(name).bake();
        }

        if (name.contains(",") || name.startsWith("+") || name.startsWith("-")) {
            return compound();
        }

        String mod = "minecraft", ident = name;
        boolean allowItem = true, allowBlock = true, allowOreDict = true;
        if (ident.startsWith("item:")) {
            allowBlock = allowOreDict = false;
            ident = NullHelper.notnullJ(ident.substring("item:".length()), "String.substring()");
        } else if (ident.startsWith("block:")) {
            allowItem = allowOreDict = false;
            ident = NullHelper.notnullJ(ident.substring("block:".length()), "String.substring()");
        } else if (ident.startsWith("oredict:")) {
            allowBlock = allowItem = false;
            ident = NullHelper.notnullJ(ident.substring("oredict:".length()), "String.substring()");
        }
        int meta = -1;
        if (ident.contains(":")) {
            allowOreDict = false;
            String[] split = ident.split(":", 3);
            if (split.length >= 2) {
                if (split[0] != null && !split[0].trim().isEmpty()) {
                    mod = NullHelper.notnullJ(split[0], "variable lost its value from one moment to the next");
                }
                if (split[1] != null && !split[1].trim().isEmpty()) {
                    ident = NullHelper.notnullJ(split[1], "variable lost its value from one moment to the next");
                } else {
                    ident = "null";
                }
                if (split.length >= 3) {
                    if ("*".equals(split[2])) {
                        meta = OreDictionary.WILDCARD_VALUE;
                    } else {
                        try {
                            meta = Integer.parseInt(split[2]);
                        } catch (NumberFormatException e) {
                            return NNList.emptyList();
                        }
                    }
                }
            }
        }
        ResourceLocation resourceLocation = new ResourceLocation(mod, ident);
        if (meta < 0) {
            // this ugly thing seems to be what Forge wants you to use
            if (allowBlock && net.minecraft.block.Block.REGISTRY.containsKey(resourceLocation)) {
                Block block = net.minecraft.block.Block.REGISTRY.getObject(resourceLocation);
                return new BlockThing(block).bake();
            }
            // this ugly thing seems to be what Forge wants you to use
            if (allowItem && net.minecraft.item.Item.REGISTRY.containsKey(resourceLocation)) {
                Item item = net.minecraft.item.Item.REGISTRY.getObject(resourceLocation);
                if (item != null) {
                    return new ItemThing(item).bake();
                }
            }
            if (allowOreDict) {
                return new OreThing(ident).bake();
            }
        } else {
            // this ugly thing seems to be what Forge wants you to use
            if (allowItem && net.minecraft.item.Item.REGISTRY.containsKey(resourceLocation)) {
                Item item = net.minecraft.item.Item.REGISTRY.getObject(resourceLocation);
                if (item != null) {
                    return new ItemStackThing(new ItemStack(item, 1, meta)).bake();
                }
            }
            // this ugly thing seems to be what Forge wants you to use
            if (allowBlock && net.minecraft.block.Block.REGISTRY.containsKey(resourceLocation)) {
                Block block = net.minecraft.block.Block.REGISTRY.getObject(resourceLocation);
                return new ItemStackThing(new ItemStack(block, 1, meta)).bake();
            }
        }
        return NNList.emptyList();
    }

    private @NotNull NNList<IThing> compound() {
        NNList<IThing> positive = new NNList<>(), negative = new NNList<>();

        for (String split : name.split(",\\s*")) {
            if (split.startsWith("-")) {
                negative.addAll(new StringThing(split.substring(1)).bake());
            } else if (split.startsWith("+")) {
                positive.addAll(new StringThing(split.substring(1)).bake());
            } else {
                positive.addAll(new StringThing(split).bake());
            }
        }

        if (negative.isEmpty()) {
            return positive;
        } else if (positive.isEmpty()) {
            return NNList.emptyList();
        } else {
            return new NNList<>(new ExclusionThing(positive, negative));
        }
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
        return String.format("StringThing [name=%s]", name);
    }
}
