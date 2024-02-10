package com.enderio.core.common.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.enderio.core.EnderCore;
import com.enderio.core.api.common.util.IProgressTile;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class PacketProgress extends MessageTileEntity<TileEntity> {

    float progress;

    public PacketProgress() {}

    public PacketProgress(@NotNull IProgressTile tile) {
        super(tile.getTileEntity());
        progress = tile.getProgress();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeFloat(progress);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        progress = buf.readFloat();
    }

    public static class Handler implements IMessageHandler<PacketProgress, IMessage> {

        @Override
        public IMessage onMessage(PacketProgress message, MessageContext ctx) {
            TileEntity tile = message.getTileEntity(EnderCore.proxy.getClientWorld());
            if (tile instanceof IProgressTile) {
                ((IProgressTile) tile).setProgress(message.progress);
            }
            return null;
        }
    }
}
