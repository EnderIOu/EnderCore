package com.enderio.core.client.handlers;

import javax.annotation.Nonnull;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import com.enderio.core.common.Handlers.Handler;

@Handler
public class ClientHandler {

    private static int ticksElapsed;

    public static int getTicksElapsed() {
        return ticksElapsed;
    }

    @SubscribeEvent
    public static void onClientTick(@Nonnull ClientTickEvent event) {
        if (event.phase == Phase.END) {
            ticksElapsed++;
        }
    }

    private ClientHandler() {}
}
