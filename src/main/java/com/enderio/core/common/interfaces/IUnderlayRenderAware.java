package com.enderio.core.common.interfaces;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public interface IUnderlayRenderAware {

    void renderItemAndEffectIntoGUI(@NotNull ItemStack stack, int xPosition, int yPosition);
}
