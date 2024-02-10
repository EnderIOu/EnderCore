package com.enderio.core.api.common.util;

import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.enderio.core.common.network.EnderPacketHandler;

public interface IProgressTile {

    float getProgress();

    /**
     * Client-only. Called to set clientside progress for syncing/rendering purposes.
     *
     * @param progress
     *                 The % progress.
     */
    @SideOnly(Side.CLIENT)
    void setProgress(float progress);

    @NotNull
    TileEntity getTileEntity();

    @NotNull
    IMessage getProgressPacket();

    static @Nullable Packet<?> getProgressPacket(Object o) {
        if (o instanceof IProgressTile) {
            return EnderPacketHandler.INSTANCE.getPacketFrom(((IProgressTile) o).getProgressPacket());
        }
        return null;
    }
}
