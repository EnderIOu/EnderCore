package com.enderio.core.client.gui.button;

import com.enderio.core.api.client.gui.IGuiScreen;
import com.enderio.core.client.render.EnderWidget;
import org.jetbrains.annotations.NotNull;

public class CheckBox extends ToggleButton {

    public CheckBox(@NotNull IGuiScreen gui, int id, int x, int y) {
        super(gui, id, x, y, EnderWidget.BUTTON, EnderWidget.BUTTON_CHECKED);
    }
}
