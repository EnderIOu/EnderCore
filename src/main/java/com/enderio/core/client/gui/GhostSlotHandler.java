package com.enderio.core.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

import com.enderio.core.client.gui.widget.GhostSlot;
import com.enderio.core.client.render.RenderUtil;
import com.enderio.core.common.util.ItemUtil;
import com.enderio.core.common.util.NNList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GhostSlotHandler {

    protected final @NotNull NNList<GhostSlot> ghostSlots = new NNList<>();
    protected @Nullable GhostSlot hoverGhostSlot;

    public GhostSlotHandler() {}

    // GhostSlot managing

    public @NotNull NNList<GhostSlot> getGhostSlots() {
        return ghostSlots;
    }

    public void add(GhostSlot slot) {
        ghostSlots.add(slot);
    }

    public GhostSlot getGhostSlotAt(@NotNull GuiContainerBase guiContainerBase, int mouseX, int mouseY) {
        int mX = mouseX - guiContainerBase.getGuiLeft();
        int mY = mouseY - guiContainerBase.getGuiTop();
        for (GhostSlot slot : ghostSlots) {
            if (slot.isVisible() && slot.isMouseOver(mX, mY) && slot.shouldDrawFakeHover()) {
                return slot;
            }
        }
        return null;
    }

    // Slot interaction

    /**
     * Called when a ghost slot is clicked or mouse wheeled.
     *
     * @param gui
     *               The GUI the GhostSlot is in
     * @param slot
     *               The GhostSlot
     * @param x
     *               Mouse position x
     * @param y
     *               Mouse position y
     * @param button
     *               The button used (0=left, 1=right). The mouse wheel is mapped to -1=down and -2=up.
     */
    protected void ghostSlotClicked(@NotNull GuiContainerBase gui, @NotNull GhostSlot slot, int x, int y, int button) {
        ItemStack handStack = Minecraft.getMinecraft().player.inventory.getItemStack();
        ItemStack existingStack = slot.getStack();
        if (button == 0) { // left
            ghostSlotClickedPrimaryMouseButton(slot, handStack, existingStack);
        } else if (button == 1) { // right
            ghostSlotClickedSecondaryMouseButton(slot, handStack, existingStack);
        } else if (button == -2) { // wheel up
            ghostSlotClickedMouseWheelUp(slot, handStack, existingStack);
        } else if (button == -1) { // wheel down
            ghostSlotClickedMouseWheelDown(slot, handStack, existingStack);
        }
    }

    protected void ghostSlotClickedPrimaryMouseButton(@NotNull GhostSlot slot, @NotNull ItemStack handStack,
                                                      @NotNull ItemStack existingStack) {
        if (handStack.isEmpty()) { // empty hand
            slot.putStack(ItemStack.EMPTY, 0);
        } else { // item in hand
            if (existingStack.isEmpty()) { // empty slot
                replaceSlot(slot, handStack);
            } else { // filled slot
                if (ItemUtil.areStackMergable(existingStack, handStack)) { // same item
                    if (existingStack.getCount() < existingStack.getMaxStackSize() &&
                            existingStack.getCount() < slot.getStackSizeLimit()) {
                        increaseSlot(slot, existingStack);
                    }
                } else { // different item
                    replaceSlot(slot, handStack);
                }
            }
        }
    }

    protected void ghostSlotClickedSecondaryMouseButton(@NotNull GhostSlot slot, @NotNull ItemStack handStack,
                                                        @NotNull ItemStack existingStack) {
        if (handStack.isEmpty()) { // empty hand
            slot.putStack(ItemStack.EMPTY, 0);
        } else { // item in hand
            if (existingStack.isEmpty()) { // empty slot
                replaceSlot1Item(slot, handStack);
            } else { // filled slot
                if (ItemUtil.areStackMergable(existingStack, handStack)) { // same item
                    descreaseSlot(slot, existingStack);
                } else { // different item
                    replaceSlot1Item(slot, handStack);
                }
            }
        }
    }

    protected void ghostSlotClickedMouseWheelUp(@NotNull GhostSlot slot, @NotNull ItemStack handStack,
                                                @NotNull ItemStack existingStack) {
        if (!existingStack.isEmpty() && existingStack.getCount() < existingStack.getMaxStackSize() &&
                existingStack.getCount() < slot.getStackSizeLimit()) {
            increaseSlot(slot, existingStack);
        }
    }

    protected void ghostSlotClickedMouseWheelDown(@NotNull GhostSlot slot, @NotNull ItemStack handStack,
                                                  @NotNull ItemStack existingStack) {
        if (!existingStack.isEmpty()) {
            descreaseSlot(slot, existingStack);
        }
    }

    // Slot interaction tools

    protected void descreaseSlot(@NotNull GhostSlot slot, @NotNull ItemStack existingStack) {
        existingStack.shrink(1);
        slot.putStack(existingStack, existingStack.getCount());
    }

    protected void increaseSlot(@NotNull GhostSlot slot, @NotNull ItemStack existingStack) {
        existingStack.grow(1);
        slot.putStack(existingStack, existingStack.getCount());
    }

    protected void replaceSlot1Item(@NotNull GhostSlot slot, @NotNull ItemStack handStack) {
        ItemStack oneItem = handStack.copy();
        oneItem.setCount(1);
        slot.putStack(oneItem, oneItem.getCount());
    }

    protected void replaceSlot(@NotNull GhostSlot slot, @NotNull ItemStack handStack) {
        if (handStack.getCount() <= slot.getStackSizeLimit()) {
            slot.putStack(handStack, handStack.getCount());
        } else {
            ItemStack tmp = handStack.copy();
            tmp.setCount(slot.getStackSizeLimit());
            slot.putStack(tmp, tmp.getCount());
        }
    }

    // Rendering

    protected void startDrawing(@NotNull GuiContainerBase gui) {
        hoverGhostSlot = null;
    }

    protected void drawGhostSlots(@NotNull GuiContainerBase gui, int mouseX, int mouseY) {
        int sx = gui.getGuiLeft();
        int sy = gui.getGuiTop();
        gui.drawFakeItemsStart();
        try {
            hoverGhostSlot = null;
            for (GhostSlot slot : ghostSlots) {
                ItemStack stack = slot.getStack();
                if (slot.isVisible()) {
                    if (!stack.isEmpty()) {
                        gui.drawFakeItemStack(slot.getX() + sx, slot.getY() + sy, stack);
                        if (slot.shouldDisplayStdOverlay()) {
                            gui.drawFakeItemStackStdOverlay(slot.getX() + sx, slot.getY() + sy, stack);
                        }
                        if (slot.shouldGrayOut()) {
                            drawGhostSlotGrayout(gui, slot);
                        }
                    }
                    if (slot.isMouseOver(mouseX - sx, mouseY - sy)) {
                        hoverGhostSlot = slot;
                    }
                }
            }
            final GhostSlot hoverGhostSlot2 = hoverGhostSlot;
            if (hoverGhostSlot2 != null && hoverGhostSlot2.shouldDrawFakeHover()) {
                // draw hover last to prevent it from affecting rendering of other slots ...
                gui.drawFakeItemHover(hoverGhostSlot2.getX() + sx, hoverGhostSlot2.getY() + sy);
            }
        } finally {
            gui.drawFakeItemsEnd();
        }
    }

    /**
     * Gray out the item that was just painted into a GhostSlot by over-painting it with 50% transparent background.
     * This gives the illusion that the item was
     * painted with 50% transparency. (100%*a ° 100%*b ° 50%*a == 100%*a ° 50%*b)
     */
    protected void drawGhostSlotGrayout(@NotNull GuiContainerBase gui, @NotNull GhostSlot slot) {
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, slot.getGrayOutLevel());
        RenderUtil.bindTexture(gui.getGuiTexture());
        gui.drawTexturedModalRect(gui.getGuiLeft() + slot.getX(), gui.getGuiTop() + slot.getY(), slot.getX(),
                slot.getY(), 16, 16);
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
    }

    protected boolean drawGhostSlotToolTip(@NotNull GuiContainerBase gui, int mouseX, int mouseY) {
        final GhostSlot hoverGhostSlot2 = hoverGhostSlot;
        if (hoverGhostSlot2 != null) {
            return hoverGhostSlot2.drawGhostSlotToolTip(gui, mouseX, mouseY);
        }
        return false;
    }
}
