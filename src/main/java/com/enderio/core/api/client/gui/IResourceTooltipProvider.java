package com.enderio.core.api.client.gui;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public interface IResourceTooltipProvider {

    @NotNull
    String getUnlocalizedNameForTooltip(@NotNull ItemStack itemStack);
}
