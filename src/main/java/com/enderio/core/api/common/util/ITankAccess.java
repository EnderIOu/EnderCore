package com.enderio.core.api.common.util;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ITankAccess {

    /**
     * Find tank to insert fluid.
     *
     * @param forFluidType
     *                     The type of fluid that should be inserted. May be null.
     * @return An internal tank that can take the given type of fluid. If multiple tanks can take the fluid, the first
     *         one that is not full will be returned. If
     *         no tank can take the fluid, returns null.
     */
    @Nullable
    FluidTank getInputTank(FluidStack forFluidType);

    /**
     * Get tank(s) to remove liquid from.
     *
     * @return Tank that can be drained. Tanks are returned in order or priority. If there's no tank, an empty array is
     *         returned.
     */
    @NotNull
    FluidTank[] getOutputTanks();

    /**
     * Will be called after a tank that was returned by one of the other methods was manipulated.
     */
    void setTanksDirty();

    interface ITankData {

        enum EnumTankType {
            STORAGE,
            INPUT,
            OUTPUT;
        }

        @NotNull
        EnumTankType getTankType();

        @Nullable
        FluidStack getContent();

        int getCapacity();
    }

    interface IExtendedTankAccess extends ITankAccess {

        /**
         * Get information on all tanks to be displayed to the user.
         * <p>
         * Please note that this data is read-only and not intended to be used to for interfacing with the machine.
         *
         * @return A list of ITankData objects.
         */
        @NotNull
        List<ITankData> getTankDisplayData();
    }
}
