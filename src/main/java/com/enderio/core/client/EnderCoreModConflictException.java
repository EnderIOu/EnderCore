package com.enderio.core.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SideOnly(Side.CLIENT)
public class EnderCoreModConflictException extends CustomModLoadingErrorDisplayException {

    private static final long serialVersionUID = 1L;

    private final @NotNull String[] msgs;

    public EnderCoreModConflictException(@NotNull String[] msgs) {
        super(msgs[0], new RuntimeException());
        this.msgs = msgs;
    }

    @Override
    public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer) {}

    @Override
    public void drawScreen(@Nullable GuiErrorScreen errorScreen, @Nullable FontRenderer fontRenderer, int mouseRelX,
                           int mouseRelY, float tickTime) {
        if (errorScreen == null || fontRenderer == null) {
            return;
        }
        int y = errorScreen.height / 2 - msgs.length * 5;
        for (String msg : msgs) {
            if (msg != null) {
                errorScreen.drawCenteredString(fontRenderer, msg, errorScreen.width / 2, y, 0xFFFFFF);
                y += 10;
            } else {
                y += 5;
            }
        }
    }
}
