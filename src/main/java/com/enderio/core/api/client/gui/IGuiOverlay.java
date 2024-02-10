package com.enderio.core.api.client.gui;

import java.awt.Rectangle;

import org.jetbrains.annotations.NotNull;

public interface IGuiOverlay extends IHideable {

    void init(@NotNull IGuiScreen screen);

    @NotNull
    Rectangle getBounds();

    void draw(int mouseX, int mouseY, float partialTick);

    boolean handleMouseInput(int x, int y, int b);

    boolean isMouseInBounds(int mouseX, int mouseY);

    void guiClosed();
}
