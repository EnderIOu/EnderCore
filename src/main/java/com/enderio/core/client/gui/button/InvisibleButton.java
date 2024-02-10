package com.enderio.core.client.gui.button;

import net.minecraft.client.Minecraft;

import org.jetbrains.annotations.NotNull;

import com.enderio.core.api.client.gui.IGuiScreen;

public class InvisibleButton extends TooltipButton {

    private static final int DEFAULT_WIDTH = 8;
    private static final int DEFAULT_HEIGHT = 6;

    public InvisibleButton(@NotNull IGuiScreen gui, int id, int x, int y) {
        super(gui, id, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, "");
    }

    public InvisibleButton(@NotNull IGuiScreen gui, int id, int x, int y, int width, int height) {
        super(gui, id, x, y, width, height, "");
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(@NotNull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        updateTooltip(mc, mouseX, mouseY);
    }
}
