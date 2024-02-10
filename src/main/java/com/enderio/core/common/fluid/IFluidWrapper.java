package com.enderio.core.common.fluid;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IFluidWrapper {

    int offer(FluidStack resource);

    int fill(FluidStack resource);

    @Nullable
    FluidStack drain(FluidStack resource);

    @Nullable
    FluidStack getAvailableFluid();

    @NotNull
    List<ITankInfoWrapper> getTankInfoWrappers();

    interface ITankInfoWrapper {

        IFluidTankProperties getIFluidTankProperties();

        FluidTankInfo getFluidTankInfo();
    }
}
