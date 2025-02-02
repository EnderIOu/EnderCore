package com.enderio.core.client.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.enderio.core.client.render.IconUtil;
import com.enderio.core.client.render.RenderUtil;

public class GuiIconRenderer extends Gui {

    public static final int DEFAULT_WIDTH = 24;
    public static final int HWIDTH = DEFAULT_WIDTH / 2;
    public static final int DEFAULT_HEIGHT = 24;
    public static final int HHEIGHT = DEFAULT_HEIGHT / 2;

    protected int hwidth = HWIDTH;
    protected int hheight = HHEIGHT;
    protected int width = DEFAULT_WIDTH;
    protected int height = DEFAULT_HEIGHT;

    protected @NotNull TextureAtlasSprite icon;
    protected @NotNull ResourceLocation texture;

    private final int yPosition;
    private final int xPosition;

    private float alpha = 1.0f;

    public GuiIconRenderer(int x, int y, @NotNull Item item, int itemMeta) {
        xPosition = x;
        yPosition = y;
        icon = IconUtil.getIconForItem(item, itemMeta);
        texture = RenderUtil.BLOCK_TEX;
    }

    public GuiIconRenderer(int x, int y, @NotNull TextureAtlasSprite icon, @NotNull ResourceLocation texture) {
        xPosition = x;
        yPosition = y;
        this.icon = icon;
        this.texture = texture;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        hwidth = width / 2;
        hheight = height / 2;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public @NotNull TextureAtlasSprite getIcon() {
        return icon;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setIcon(@NotNull TextureAtlasSprite icon) {
        this.icon = icon;
    }

    public @NotNull ResourceLocation getTexture() {
        return texture;
    }

    public void setTexture(@NotNull ResourceLocation textureName) {
        this.texture = textureName;
    }

    public void draw() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        GlStateManager.pushAttrib();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderUtil.bindTexture(texture);
        drawTexturedModalRect(xPosition, yPosition, icon, width, height);

        GlStateManager.popAttrib();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
