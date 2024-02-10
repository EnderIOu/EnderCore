package com.enderio.core.common.util.blockiterators;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.NotNull;

public class PlanarBlockIterator extends CubicBlockIterator {

    public static enum Orientation {

        EAST_WEST,
        NORTH_SOUTH,
        HORIZONTAL;

        public static @NotNull Orientation perpendicular(@NotNull EnumFacing dir) {
            switch (dir) {
                case NORTH:
                case SOUTH:
                    return EAST_WEST;
                case EAST:
                case WEST:
                    return NORTH_SOUTH;
                case DOWN:
                case UP:
                default:
                    return HORIZONTAL;
            }
        }
    }

    private @NotNull Orientation orientation;

    public PlanarBlockIterator(@NotNull BlockPos base, @NotNull Orientation orientation, int radius) {
        super(base, radius);

        this.orientation = orientation;
        switch (orientation) {
            case EAST_WEST:
                curZ = base.getZ();
            case NORTH_SOUTH:
                curX = base.getX();
            case HORIZONTAL:
                curY = base.getY();
        }
    }

    @Override
    public @NotNull BlockPos next() {
        BlockPos coord = new BlockPos(curX, curY, curZ);
        switch (orientation) {
            case EAST_WEST:
                curY = curY == maxY ? minY : curY + 1;
                curX = curY == minY ? curX + 1 : curX;
            case NORTH_SOUTH:
                curY = curY == maxY ? minY : curY + 1;
                curZ = curY == minY ? curZ + 1 : curZ;
            case HORIZONTAL:
                curX = curX == maxX ? minX : curX + 1;
                curZ = curX == minX ? curZ + 1 : curZ;
        }
        return coord;
    }

    @Override
    public boolean hasNext() {
        return curX <= maxX && curY <= maxY && curZ <= maxZ;
    }
}
