package com.enderio.core.api.client.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IWidgetIcon {

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    @Nullable
    IWidgetIcon getOverlay();

    @NotNull
    IWidgetMap getMap();

    @NotNull
    @SideOnly(Side.CLIENT)
    default TextureAtlasSprite getAsTextureAtlasSprite() {
        return new TAS(this);
    }

    /**
     * TextureAtlasSprite that only has the data needed by Slot for a background image. Won't work anywhere where's
     * animation data is needed.
     *
     */
    @SideOnly(Side.CLIENT)
    class TAS extends TextureAtlasSprite {

        protected TAS(IWidgetIcon icon) {
            super(icon.getMap().getTexture().toString());
            setIconWidth(icon.getWidth());
            setIconHeight(icon.getHeight());
            initSprite(icon.getMap().getSize(), icon.getMap().getSize(), icon.getX(), icon.getY(), false);
        }
    }
}
