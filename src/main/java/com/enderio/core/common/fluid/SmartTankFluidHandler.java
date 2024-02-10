package com.enderio.core.common.fluid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * Handles IFluidHandler, FluidTank and SmartTank
 *
 */
public abstract class SmartTankFluidHandler {

    protected final @Nonnull IFluidHandler[] tanks;
    private final @Nonnull SideHandler[] sides = new SideHandler[EnumFacing.values().length];
    private final @Nonnull InformationHandler nullSide = new InformationHandler();

    public SmartTankFluidHandler(IFluidHandler... tanks) {
        this.tanks = tanks != null ? tanks : new IFluidHandler[0];
    }

    public boolean has(@Nullable EnumFacing facing) {
        return facing != null && canAccess(facing);
    }

    public IFluidHandler get(@Nullable EnumFacing facing) {
        if (facing == null) {
            return nullSide;
        } else if (has(facing)) {
            if (sides[facing.ordinal()] == null) {
                sides[facing.ordinal()] = new SideHandler(facing);
            }
            return sides[facing.ordinal()];
        } else {
            return null;
        }
    }

    protected abstract boolean canFill(@Nonnull EnumFacing from);

    protected abstract boolean canDrain(@Nonnull EnumFacing from);

    protected abstract boolean canAccess(@Nonnull EnumFacing from);

    private class InformationHandler implements IFluidHandler {

        public InformationHandler() {}

        @Override
        public IFluidTankProperties[] getTankProperties() {
            if (tanks.length == 1) {
                return tanks[0].getTankProperties();
            }
            List<IFluidTankProperties> result = new ArrayList<IFluidTankProperties>();
            for (IFluidHandler smartTank : tanks) {
                IFluidTankProperties[] tankProperties = smartTank.getTankProperties();
                if (tankProperties != null) {
                    for (IFluidTankProperties tankProperty : tankProperties) {
                        result.add(tankProperty);
                    }
                }
            }
            return result.toArray(new IFluidTankProperties[result.size()]);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Override
        @Nullable
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return null;
        }

        @Override
        @Nullable
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }
    }

    private class SideHandler extends InformationHandler {

        private final @Nonnull EnumFacing facing;

        public SideHandler(@Nonnull EnumFacing facing) {
            this.facing = facing;
        }

        @Override
        public IFluidTankProperties[] getTankProperties() {
            if (!canAccess(facing)) {
                return new IFluidTankProperties[0];
            }
            return super.getTankProperties();
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (!canFill(facing)) {
                return 0;
            }
            if (tanks.length == 1) {
                return tanks[0].fill(resource, doFill);
            }
            for (IFluidHandler smartTank : tanks) {
                if (smartTank instanceof SmartTank) {
                    if (((SmartTank) smartTank).canFill(resource)) {
                        return smartTank.fill(resource, doFill);
                    }
                } else if (smartTank instanceof FluidTank) {
                    if (((FluidTank) smartTank).canFill()) {
                        return smartTank.fill(resource, doFill);
                    }
                } else {
                    return smartTank.fill(resource, doFill);
                }
            }
            return 0;
        }

        @Override
        @Nullable
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            if (!canDrain(facing)) {
                return null;
            }
            if (tanks.length == 1) {
                return tanks[0].drain(resource, doDrain);
            }
            for (IFluidHandler smartTank : tanks) {
                if (!(smartTank instanceof FluidTank) || ((FluidTank) smartTank).canDrainFluidType(resource)) {
                    return smartTank.drain(resource, doDrain);
                }
            }
            return null;
        }

        @Override
        @Nullable
        public FluidStack drain(int maxDrain, boolean doDrain) {
            if (!canDrain(facing)) {
                return null;
            }
            if (tanks.length == 1) {
                return tanks[0].drain(maxDrain, doDrain);
            }
            for (IFluidHandler smartTank : tanks) {
                if (!(smartTank instanceof FluidTank) || ((FluidTank) smartTank).canDrain()) {
                    return smartTank.drain(maxDrain, doDrain);
                }
            }
            return null;
        }
    }
}
