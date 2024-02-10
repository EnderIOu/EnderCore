package com.enderio.core.client.gui;

import java.util.List;
import java.util.Set;

import net.minecraft.client.gui.FontRenderer;

import com.enderio.core.client.gui.widget.GuiToolTip;
import com.enderio.core.common.util.NNList;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

public class ToolTipManager {

    public static interface ToolTipRenderer {

        int getGuiRootLeft();

        int getGuiRootTop();

        int getGuiXSize();

        @NotNull
        FontRenderer getFontRenderer();

        void drawHoveringToolTipText(@NotNull List<String> par1List, int par2, int par3, @NotNull FontRenderer font);
    }

    private final @NotNull Set<GuiToolTip> toolTips = Sets.newHashSet();

    public void addToolTip(@NotNull GuiToolTip toolTip) {
        toolTips.add(toolTip);
    }

    public boolean removeToolTip(@NotNull GuiToolTip toolTip) {
        return toolTips.remove(toolTip);
    }

    public void clearToolTips() {
        toolTips.clear();
    }

    protected final void drawTooltips(@NotNull ToolTipRenderer renderer, int mouseX, int mouseY) {
        for (GuiToolTip toolTip : toolTips) {
            toolTip.onTick(mouseX - renderer.getGuiRootLeft(), mouseY - renderer.getGuiRootTop());
            if (toolTip.shouldDraw()) {
                drawTooltip(toolTip, mouseX, mouseY, renderer);
            }
        }
    }

    protected void drawTooltip(@NotNull GuiToolTip toolTip, int mouseX, int mouseY, @NotNull ToolTipRenderer renderer) {
        List<String> list = toolTip.getToolTipText();
        if (list.isEmpty()) {
            return;
        }

        NNList<String> formatted = new NNList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                formatted.add("§f" + list.get(i));
            } else {
                formatted.add("§7" + list.get(i));
            }
        }

        renderer.drawHoveringToolTipText(formatted, mouseX, mouseY, renderer.getFontRenderer());
    }
}
