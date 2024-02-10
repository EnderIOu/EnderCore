package com.enderio.core.common.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public class CreativeTabsCustom extends CreativeTabs {

    private @NotNull ItemStack displayStack = ItemStack.EMPTY;

    public CreativeTabsCustom(@NotNull String unloc) {
        super(unloc);
    }

    /**
     * @param item
     *             Item to display
     */
    public CreativeTabsCustom setDisplay(@NotNull Item item) {
        return setDisplay(item, 0);
    }

    /**
     * @param item
     *               Item to display
     * @param damage
     *               Damage of item to display
     */
    public CreativeTabsCustom setDisplay(@NotNull Item item, int damage) {
        return setDisplay(new ItemStack(item, 1, damage));
    }

    /**
     * @param display
     *                ItemStack to display
     */
    public CreativeTabsCustom setDisplay(@NotNull ItemStack display) {
        this.displayStack = display.copy();
        return this;
    }

    @Override
    public @NotNull ItemStack createIcon() {
        return displayStack;
    }
}
