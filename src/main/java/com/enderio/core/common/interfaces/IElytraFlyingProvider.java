package com.enderio.core.common.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public interface IElytraFlyingProvider {

    boolean isElytraFlying(@NotNull EntityLivingBase entity, @NotNull ItemStack itemstack, boolean shouldStop);
}
