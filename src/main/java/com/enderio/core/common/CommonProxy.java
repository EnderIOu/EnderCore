package com.enderio.core.common;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.enderio.core.common.util.Scheduler;
import org.jetbrains.annotations.NotNull;

public class CommonProxy {

    protected Scheduler scheduler;

    /**
     * Returns a scheduler for the current side
     * <p>
     * For internal use only, please call {@link Scheduler#instance()} to obtain an {@link Scheduler} instance.
     */
    public @NotNull Scheduler getScheduler() {
        if (scheduler != null) {
            return scheduler;
        }
        return scheduler = new Scheduler(true);
    }

    public World getClientWorld() {
        return null;
    }

    public void throwModCompatibilityError(@NotNull String... msgs) {
        StringBuilder sb = new StringBuilder();
        for (String msg : msgs) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(msg);
        }
        throw new RuntimeException(sb.toString());
    }

    public void onPreInit(@NotNull FMLPreInitializationEvent event) {}
}
