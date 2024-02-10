package com.enderio.core.common.fluid;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import com.enderio.core.api.common.util.ITankAccess;
import com.enderio.core.common.util.NNList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TankAccessFluidWrapper implements IFluidWrapper {

    private final ITankAccess tankAccess;

    public TankAccessFluidWrapper(ITankAccess tankAccess) {
        this.tankAccess = tankAccess;
    }

    @Override
    public int offer(FluidStack resource) {
        FluidTank inputTank = tankAccess.getInputTank(resource);
        if (inputTank != null) {
            return inputTank.fill(resource, false);
        }
        return 0;
    }

    @Override
    public int fill(FluidStack resource) {
        FluidTank inputTank = tankAccess.getInputTank(resource);
        if (inputTank != null) {
            tankAccess.setTanksDirty();
            return inputTank.fill(resource, true);
        }
        return 0;
    }

    @Override
    public @Nullable FluidStack drain(FluidStack resource) {
        FluidTank[] outputTanks = tankAccess.getOutputTanks();
        if (outputTanks.length >= 1 && outputTanks[0] != null) {
            tankAccess.setTanksDirty();
            return outputTanks[0].drain(resource, true);
        }
        return null;
    }

    @Override
    public @Nullable FluidStack getAvailableFluid() {
        FluidTank[] outputTanks = tankAccess.getOutputTanks();
        if (outputTanks.length >= 1 && outputTanks[0] != null) {
            return outputTanks[0].getFluid();
        }
        return null;
    }

    @Override
    public @NotNull List<ITankInfoWrapper> getTankInfoWrappers() {
        return NNList.emptyList();
    }
}
