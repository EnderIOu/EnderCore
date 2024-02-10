package com.enderio.core.common.fluid;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapabilityFluidWrapper implements IFluidWrapper {

    private final net.minecraftforge.fluids.capability.IFluidHandler fluidHandler;

    public CapabilityFluidWrapper(IFluidHandler fluidHandler) {
        this.fluidHandler = fluidHandler;
    }

    @Override
    public int offer(FluidStack resource) {
        return fluidHandler.fill(resource, false);
    }

    @Override
    public int fill(FluidStack resource) {
        return fluidHandler.fill(resource, true);
    }

    @Override
    public @Nullable FluidStack drain(FluidStack resource) {
        return fluidHandler.drain(resource, true);
    }

    @Override
    public @Nullable FluidStack getAvailableFluid() {
        return fluidHandler.drain(Integer.MAX_VALUE, false);
    }

    @Override
    public @NotNull List<ITankInfoWrapper> getTankInfoWrappers() {
        List<ITankInfoWrapper> result = new ArrayList<ITankInfoWrapper>();
        IFluidTankProperties[] tankProperties = fluidHandler.getTankProperties();
        if (tankProperties != null) {
            for (IFluidTankProperties iFluidTankProperties : tankProperties) {
                if (iFluidTankProperties != null) {
                    result.add(new InfoWrapper(iFluidTankProperties));
                }
            }
        }
        return result;
    }

    private static class InfoWrapper implements ITankInfoWrapper {

        private final IFluidTankProperties prop;

        InfoWrapper(IFluidTankProperties prop) {
            this.prop = prop;
        }

        @Override
        public IFluidTankProperties getIFluidTankProperties() {
            return prop;
        }

        @Override
        public FluidTankInfo getFluidTankInfo() {
            return new FluidTankInfo(prop.getContents(), prop.getCapacity());
        }
    }
}
