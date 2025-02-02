package com.enderio.core.common.interfaces;

import net.minecraft.entity.monster.EntityCreeper;

import org.jetbrains.annotations.NotNull;

public interface ICreeperTarget {

    /**
     * Determine if the given creeper should blow up when nearby.
     * <p>
     * Note that the creeper stills tracks the target, even if this returns false.
     *
     * @param swellingCreeper
     *                        The creeper that wants to explode
     * @return True if the creeper is allowed to explode, false otherwise.
     */
    boolean isCreeperTarget(@NotNull EntityCreeper swellingCreeper);
}
