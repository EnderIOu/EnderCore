package com.enderio.core.client.gui.button;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IconToggleButton extends IIconButton {

    private boolean selected = false;

    public IconToggleButton(@NotNull FontRenderer fr, int id, int x, int y, @Nullable TextureAtlasSprite icon,
                            @Nullable ResourceLocation texture) {
        super(fr, id, x, y, icon, texture);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int getHoverState(boolean par1) {
        int result = 1;
        if (!enabled || selected) {
            result = 0;
        } else if (par1) {
            result = 2;
        }
        return result;
    }
}
