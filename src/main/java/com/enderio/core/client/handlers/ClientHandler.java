package com.enderio.core.client.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import com.enderio.core.common.Handlers.Handler;
import org.jetbrains.annotations.NotNull;

@Handler
public class ClientHandler {

    private static int ticksElapsed;

    public static int getTicksElapsed() {
        return ticksElapsed;
    }

    @SubscribeEvent
    public static void onClientTick(@NotNull ClientTickEvent event) {
        if (event.phase == Phase.END) {
            ticksElapsed++;
        }
    }

    private ClientHandler() {}
}
