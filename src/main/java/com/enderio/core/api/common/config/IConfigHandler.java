package com.enderio.core.api.common.config;

import java.io.File;
import java.util.List;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import com.enderio.core.common.config.AbstractConfigHandler.Section;
import org.jetbrains.annotations.NotNull;

public interface IConfigHandler {

    void initialize(@NotNull File cfg);

    @NotNull
    List<Section> getSections();

    @NotNull
    ConfigCategory getCategory(String name);

    @NotNull
    String getModID();

    /**
     * A hook for the {@link FMLInitializationEvent}.
     */
    void initHook();

    /**
     * A hook for the {@link FMLPostInitializationEvent}.
     */
    void postInitHook();
}
