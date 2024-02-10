package com.enderio.core.client.gui;

import org.jetbrains.annotations.Nullable;

import com.enderio.core.client.gui.widget.GuiToolTip;

public interface IDrawingElement {

    @Nullable
    GuiToolTip getTooltip();

    void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY);
}
