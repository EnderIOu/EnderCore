package com.enderio.core.client.gui.widget;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuiToolTip implements com.enderio.core.api.client.gui.IHideable {

    private static final long DELAY = 0;

    protected @NotNull Rectangle bounds;

    private long mouseOverStart;

    protected final @NotNull List<String> text;

    private int lastMouseX = -1;

    private int lastMouseY = -1;

    private boolean visible = true;

    public GuiToolTip(@NotNull Rectangle bounds, String... lines) {
        this.bounds = bounds;
        if (lines != null) {
            text = new ArrayList<>(lines.length);
            Collections.addAll(text, lines);
        } else {
            text = new ArrayList<>();
        }
    }

    public GuiToolTip(@NotNull Rectangle bounds, @Nullable List<String> lines) {
        this.bounds = bounds;
        if (lines == null) {
            text = new ArrayList<>();
        } else {
            text = new ArrayList<>(lines);
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setIsVisible(boolean visible) {
        this.visible = visible;
    }

    public @NotNull Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(@NotNull Rectangle bounds) {
        this.bounds = bounds;
    }

    public void onTick(int mouseX, int mouseY) {
        if (lastMouseX != mouseX || lastMouseY != mouseY) {
            mouseOverStart = 0;
        }

        if (isVisible() && getBounds().contains(mouseX, mouseY)) {

            if (mouseOverStart == 0) {
                mouseOverStart = System.currentTimeMillis();
            }
        } else {
            mouseOverStart = 0;
        }

        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    public boolean shouldDraw() {
        if (!isVisible()) {
            return false;
        }
        updateText();
        if (mouseOverStart == 0) {
            return false;
        }
        return System.currentTimeMillis() - mouseOverStart >= DELAY;
    }

    protected void updateText() {}

    public int getLastMouseX() {
        return lastMouseX;
    }

    public void setLastMouseX(int lastMouseX) {
        this.lastMouseX = lastMouseX;
    }

    public int getLastMouseY() {
        return lastMouseY;
    }

    public void setLastMouseY(int lastMouseY) {
        this.lastMouseY = lastMouseY;
    }

    public void setToolTipText(String... txt) {
        text.clear();
        if (txt != null) {
            Collections.addAll(text, txt);
        }
    }

    public @NotNull List<String> getToolTipText() {
        return text;
    }
}
