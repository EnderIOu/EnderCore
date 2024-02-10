package com.enderio.core.client.gui.button;

import org.jetbrains.annotations.NotNull;

import com.enderio.core.api.client.gui.IGuiScreen;
import com.enderio.core.api.client.render.IWidgetIcon;
import com.enderio.core.client.render.EnderWidget;

public class MultiIconButton extends IconButton {

    private final @NotNull IWidgetIcon unpressed;
    private final @NotNull IWidgetIcon pressed;
    private final @NotNull IWidgetIcon hover;

    public MultiIconButton(@NotNull IGuiScreen gui, int id, int x, int y, @NotNull IWidgetIcon unpressed,
                           @NotNull IWidgetIcon pressed,
                           @NotNull IWidgetIcon hover) {
        super(gui, id, x, y, null);
        this.unpressed = unpressed;
        this.pressed = pressed;
        this.hover = hover;
        setSize(unpressed.getWidth(), unpressed.getHeight());
    }

    @Override
    protected @NotNull IWidgetIcon getIconForHoverState(int hoverState) {
        if (hoverState == 0) {
            return pressed;
        }
        if (hoverState == 2) {
            return hover;
        }
        return unpressed;
    }

    public static @NotNull MultiIconButton createRightArrowButton(@NotNull IGuiScreen gui, int id, int x, int y) {
        return new MultiIconButton(gui, id, x, y, EnderWidget.RIGHT_ARROW, EnderWidget.RIGHT_ARROW_PRESSED,
                EnderWidget.RIGHT_ARROW_HOVER);
    }

    public static @NotNull MultiIconButton createLeftArrowButton(@NotNull IGuiScreen gui, int id, int x, int y) {
        return new MultiIconButton(gui, id, x, y, EnderWidget.LEFT_ARROW, EnderWidget.LEFT_ARROW_PRESSED,
                EnderWidget.LEFT_ARROW_HOVER);
    }

    public static @NotNull MultiIconButton createAddButton(@NotNull IGuiScreen gui, int id, int x, int y) {
        return new MultiIconButton(gui, id, x, y, EnderWidget.ADD_BUT, EnderWidget.ADD_BUT_PRESSED,
                EnderWidget.ADD_BUT_HOVER);
    }

    public static @NotNull MultiIconButton createMinusButton(@NotNull IGuiScreen gui, int id, int x, int y) {
        return new MultiIconButton(gui, id, x, y, EnderWidget.MINUS_BUT, EnderWidget.MINUS_BUT_PRESSED,
                EnderWidget.MINUS_BUT_HOVER);
    }
}
