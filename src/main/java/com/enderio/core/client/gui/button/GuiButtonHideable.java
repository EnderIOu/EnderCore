package com.enderio.core.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.jetbrains.annotations.NotNull;

import com.enderio.core.api.client.gui.IHideable;

public class GuiButtonHideable extends GuiButton implements IHideable {

    public GuiButtonHideable(int buttonId, int x, int y, @NotNull String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public GuiButtonHideable(int buttonId, int x, int y, int widthIn, int heightIn, @NotNull String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void setIsVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    protected int getHoverState(boolean mouseOver) {
        int b = super.getHoverState(mouseOver);
        if (!isEnabled()) {
            b = 0;
        }
        return b;
    }

    @Override
    public boolean mousePressed(@NotNull Minecraft mc, int mouseX, int mouseY) {
        return isEnabled() && super.mousePressed(mc, mouseX, mouseY);
    }
}
