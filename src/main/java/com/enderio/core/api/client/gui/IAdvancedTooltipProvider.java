package com.enderio.core.api.client.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IAdvancedTooltipProvider {

    @SideOnly(Side.CLIENT)
    default void addCommonEntries(@NotNull ItemStack stack, @Nullable EntityPlayer player,
                                  @NotNull List<String> list, boolean flag) {}

    @SideOnly(Side.CLIENT)
    default void addBasicEntries(@NotNull ItemStack stack, @Nullable EntityPlayer player,
                                 @NotNull List<String> list, boolean flag) {}

    @SideOnly(Side.CLIENT)
    default void addDetailedEntries(@NotNull ItemStack stack, @Nullable EntityPlayer player,
                                    @NotNull List<String> list, boolean flag) {}
}
