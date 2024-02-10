package com.enderio.core.client.gui.button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;

import com.enderio.core.api.client.gui.IGuiScreen;
import com.enderio.core.api.client.render.IWidgetIcon;
import com.enderio.core.client.gui.widget.GuiToolTip;
import com.enderio.core.client.render.EnderWidget;
import org.jetbrains.annotations.NotNull;

public class ToggleButton extends IconButton {

    private boolean selected;
    private final @NotNull IWidgetIcon unselectedIcon;
    private final @NotNull IWidgetIcon selectedIcon;

    private GuiToolTip selectedTooltip, unselectedTooltip;
    private boolean paintSelectionBorder;

    public ToggleButton(@NotNull IGuiScreen gui, int id, int x, int y, @NotNull IWidgetIcon unselectedIcon,
                        @NotNull IWidgetIcon selectedIcon) {
        super(gui, id, x, y, unselectedIcon);
        this.unselectedIcon = unselectedIcon;
        this.selectedIcon = selectedIcon;
        selected = false;
        paintSelectionBorder = true;
    }

    public boolean isSelected() {
        return selected;
    }

    public ToggleButton setSelected(boolean selected) {
        this.selected = selected;
        icon = selected ? selectedIcon : unselectedIcon;
        if (selected && selectedTooltip != null) {
            setToolTip(selectedTooltip);
        } else if (!selected && unselectedTooltip != null) {
            setToolTip(unselectedTooltip);
        }
        return this;
    }

    @Override
    protected @NotNull IWidgetIcon getIconForHoverState(int hoverState) {
        if (!selected || !paintSelectionBorder) {
            return super.getIconForHoverState(hoverState);
        }
        if (hoverState == 0) {
            return EnderWidget.BUTTON_DISABLED;
        }
        if (hoverState == 2) {
            return EnderWidget.BUTTON_DOWN_HIGHLIGHT;
        }
        return EnderWidget.BUTTON_DOWN;
    }

    @Override
    public boolean mousePressedButton(@NotNull Minecraft mc, int mouseX, int mouseY, int button) {
        if (super.checkMousePress(mc, mouseX, mouseY)) {
            if (button == 0) {
                toggleSelected();
                return true;
            }
        }
        return false;
    }

    protected boolean toggleSelected() {
        setSelected(!selected);
        return true;
    }

    public void setSelectedToolTip(String... tt) {
        selectedTooltip = new GuiToolTip(getBounds(), makeCombinedTooltipList(tt));
        setSelected(selected);
    }

    private @NotNull List<String> makeCombinedTooltipList(String... tt) {
        final @NotNull List<String> list = new ArrayList<String>();
        if (toolTipText != null) {
            Collections.addAll(list, toolTipText);
        }
        Collections.addAll(list, tt);
        return list;
    }

    public void setUnselectedToolTip(String... tt) {
        unselectedTooltip = new GuiToolTip(getBounds(), makeCombinedTooltipList(tt));
        setSelected(selected);
    }

    public void setPaintSelectedBorder(boolean b) {
        paintSelectionBorder = b;
    }
}
