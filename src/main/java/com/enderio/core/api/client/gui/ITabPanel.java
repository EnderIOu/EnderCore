package com.enderio.core.api.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import com.enderio.core.api.client.render.IWidgetIcon;
import org.jetbrains.annotations.NotNull;

public interface ITabPanel {

    void onGuiInit(int x, int y, int width, int height);

    void deactivate();

    @NotNull
    IWidgetIcon getIcon();

    void render(float par1, int par2, int par3);

    void actionPerformed(@NotNull GuiButton guiButton);

    void mouseClicked(int x, int y, int par3);

    void keyTyped(char par1, int par2);

    void updateScreen();

    /**
     * @return The location of the texture used for slot backgrounds, etc.
     */
    @NotNull
    ResourceLocation getTexture();
}
