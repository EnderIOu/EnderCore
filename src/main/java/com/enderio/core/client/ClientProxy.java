package com.enderio.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.jetbrains.annotations.NotNull;

import com.enderio.core.client.render.IconUtil;
import com.enderio.core.common.CommonProxy;
import com.enderio.core.common.util.Scheduler;

public class ClientProxy extends CommonProxy {

    @Override
    public @NotNull Scheduler getScheduler() {
        if (scheduler != null) {
            return scheduler;
        }
        return scheduler = new Scheduler(false);
    }

    @Override
    public @NotNull World getClientWorld() {
        return Minecraft.getMinecraft().world;
    }

    @Override
    public void throwModCompatibilityError(@NotNull String... msgs) {
        throw new EnderCoreModConflictException(msgs);
    }

    @Override
    public void onPreInit(@NotNull FMLPreInitializationEvent event) {
        IconUtil.instance.init();
    }
}
