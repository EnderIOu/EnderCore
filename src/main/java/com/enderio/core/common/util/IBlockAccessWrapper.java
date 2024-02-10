package com.enderio.core.common.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IBlockAccessWrapper implements IBlockAccess {

    protected @NotNull IBlockAccess wrapped;

    public IBlockAccessWrapper(@NotNull IBlockAccess ba) {
        wrapped = ba;
    }

    @Override
    public boolean isSideSolid(@NotNull BlockPos pos, @NotNull EnumFacing side, boolean _default) {
        return wrapped.isSideSolid(pos, side, _default);
    }

    @Override
    public @Nullable TileEntity getTileEntity(@NotNull BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            return wrapped.getTileEntity(pos);
        } else {
            return null;
        }
    }

    @Override
    public @NotNull IBlockState getBlockState(@NotNull BlockPos pos) {
        return wrapped.getBlockState(pos);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getCombinedLight(@NotNull BlockPos pos, int lightValue) {
        return 15 << 20 | 15 << 4;
    }

    @Override
    public boolean isAirBlock(@NotNull BlockPos pos) {
        return wrapped.isAirBlock(pos);
    }

    @Override
    public @NotNull Biome getBiome(@NotNull BlockPos pos) {
        return wrapped.getBiome(pos);
    }

    @Override
    public int getStrongPower(@NotNull BlockPos pos, @NotNull EnumFacing direction) {
        return wrapped.getStrongPower(pos, direction);
    }

    @Override
    public @NotNull WorldType getWorldType() {
        return wrapped.getWorldType();
    }
}
