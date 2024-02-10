package com.enderio.core.common;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MappedCapabilityProvider implements ICapabilityProvider {

    private final Map<Capability<?>, Object> providers = new HashMap<>();

    public MappedCapabilityProvider() {}

    public @NotNull <T> MappedCapabilityProvider add(@Nullable Capability<T> capability, @NotNull T cap) {
        providers.put(capability, cap);
        return this;
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return providers.containsKey(capability);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        return (T) providers.get(capability);
    }
}
