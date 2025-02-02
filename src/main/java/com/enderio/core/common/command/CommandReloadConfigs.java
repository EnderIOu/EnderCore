package com.enderio.core.common.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.enderio.core.EnderCore;
import com.enderio.core.common.event.ConfigFileChangedEvent;
import com.enderio.core.common.util.NullHelper;

public class CommandReloadConfigs extends CommandBase {

    public static final @NotNull CommandReloadConfigs SERVER = new CommandReloadConfigs(Side.SERVER);
    public static final @NotNull CommandReloadConfigs CLIENT = new CommandReloadConfigs(Side.CLIENT);

    private static final List<String> validModIDs = new ArrayList<>();

    private final Side side;

    static {
        EnderCore.logger.info("Sending dummy event to all mods");

        for (ModContainer mod : Loader.instance().getActiveModList()) {
            ConfigFileChangedEvent event = new ConfigFileChangedEvent(mod.getModId());
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isSuccessful()) {
                validModIDs.add(mod.getModId());
            }
        }
    }

    private CommandReloadConfigs(Side side) {
        this.side = side;
    }

    @Override
    public @NotNull String getName() {
        return side == Side.SERVER ? "reloadServerConfigs" : "reloadConfigs";
    }

    @Override
    public @NotNull String getUsage(@NotNull ICommandSender p_71518_1_) {
        return "/" + getName() + " <modid> (<modid2> <modid3> ...)";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public @NotNull List<String> getTabCompletions(@NotNull MinecraftServer server, @NotNull ICommandSender sender,
                                                   String @NotNull [] args,
                                                   @Nullable BlockPos pos) {
        if (args.length >= 1) {
            @NotNull
            String[] avail = NullHelper.notnullJ(validModIDs.toArray(new String[0]), "List.toArray()");

            for (int i = 0; i < args.length - 1; i++) {
                avail = ArrayUtils.removeElement(avail, args[i]);
            }

            return getListOfStringsMatchingLastWord(args, avail);
        }

        return super.getTabCompletions(server, sender, args, pos);
    }

    @Override
    public boolean checkPermission(@NotNull MinecraftServer server, @NotNull ICommandSender sender) {
        return sender.getEntityWorld().isRemote || super.checkPermission(server, sender);
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender,
                        String @NotNull [] args) {
        if (side == Side.CLIENT == sender.getEntityWorld().isRemote)
            for (String s : args) {
                boolean validModid = false;
                for (ModContainer mod : Loader.instance().getModObjectList().keySet()) {
                    if (mod.getModId().equals(s)) {
                        validModid = true;
                    }
                }

                if (validModid) {
                    ConfigFileChangedEvent event = new ConfigFileChangedEvent(s);
                    MinecraftForge.EVENT_BUS.post(event);

                    if (event.isSuccessful()) {
                        sendResult(sender, s, "success");
                    } else {
                        sendResult(sender, s, "fail");
                    }
                } else {
                    sendResult(sender, s, "invalid");
                }
            }
    }

    private static void sendResult(ICommandSender player, String modid, String result) {
        player.sendMessage(new TextComponentString(EnderCore.lang.localize("command.config.result." + result, modid)));
    }
}
