package com.enderio.core.api.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;

import org.jetbrains.annotations.NotNull;

import com.enderio.core.client.gui.GhostSlotHandler;
import com.enderio.core.client.gui.widget.GuiToolTip;

public interface IGuiScreen {

    void addToolTip(@NotNull GuiToolTip toolTip);

    boolean removeToolTip(@NotNull GuiToolTip toolTip);

    void clearToolTips();

    int getGuiRootLeft();

    int getGuiRootTop();

    int getGuiXSize();

    int getGuiYSize();

    <T extends GuiButton> @NotNull T addGuiButton(@NotNull T button);

    void removeButton(@NotNull GuiButton button);

    int getOverlayOffsetXLeft();

    int getOverlayOffsetXRight();

    void doActionPerformed(@NotNull GuiButton but) throws IOException;

    @NotNull
    GhostSlotHandler getGhostSlotHandler();
}
