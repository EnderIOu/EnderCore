package com.enderio.core.client.gui;

import com.enderio.core.client.gui.widget.GuiToolTip;
import org.jetbrains.annotations.Nullable;

public interface IDrawingElement {

    @Nullable
    GuiToolTip getTooltip();

    void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY);
}
