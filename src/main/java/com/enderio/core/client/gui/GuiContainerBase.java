package com.enderio.core.client.gui;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;

import com.enderio.core.api.client.gui.IGuiOverlay;
import com.enderio.core.api.client.gui.IGuiScreen;
import com.enderio.core.client.gui.ToolTipManager.ToolTipRenderer;
import com.enderio.core.client.gui.button.IButtonAwareButton;
import com.enderio.core.client.gui.button.IPriorityButton;
import com.enderio.core.client.gui.button.IconButton;
import com.enderio.core.client.gui.widget.GhostSlot;
import com.enderio.core.client.gui.widget.GuiToolTip;
import com.enderio.core.client.gui.widget.TextFieldEnder;
import com.enderio.core.client.gui.widget.VScrollbar;
import com.enderio.core.common.util.NNList;

public abstract class GuiContainerBase extends GuiContainer implements ToolTipRenderer, IGuiScreen {

    protected @NotNull ToolTipManager ttMan = new ToolTipManager();
    protected @NotNull NNList<IGuiOverlay> overlays = new NNList<IGuiOverlay>();
    protected @NotNull NNList<TextFieldEnder> textFields = new NNList<TextFieldEnder>();
    protected @NotNull NNList<VScrollbar> scrollbars = new NNList<VScrollbar>();
    protected @NotNull NNList<IDrawingElement> drawingElements = new NNList<IDrawingElement>();
    protected @NotNull GhostSlotHandler ghostSlotHandler = new GhostSlotHandler();

    protected @Nullable VScrollbar draggingScrollbar;

    protected GuiContainerBase(@NotNull Container par1Container) {
        super(par1Container);
    }

    @Override
    public void initGui() {
        super.initGui();
        fixupGuiPosition();
        for (IGuiOverlay overlay : overlays) {
            overlay.init(this);
        }
        for (TextFieldEnder f : textFields) {
            f.init(this);
        }
    }

    protected void fixupGuiPosition() {}

    @Override
    protected void keyTyped(char c, int key) throws IOException {
        TextFieldEnder focused = null;
        for (TextFieldEnder f : textFields) {
            if (f.isFocused()) {
                focused = f;
            }
        }

        // If esc is pressed
        if (key == 1) {
            // If there is a focused text field unfocus it
            if (focused != null) {
                focused.setFocused(false);
                focused = null;
                return;
            } else if (!hideOverlays()) { // Otherwise close overlays/GUI
                this.mc.player.closeScreen();
                return;
            }
        }

        // If the user pressed tab, switch to the next text field, or unfocus if there are none
        if (c == '\t') {
            for (int i = 0; i < textFields.size(); i++) {
                TextFieldEnder f = textFields.get(i);
                if (f.isFocused()) {
                    textFields.get((i + 1) % textFields.size()).setFocused(true);
                    f.setFocused(false);
                    return;
                }
            }
        }

        // If there is a focused text field, attempt to type into it
        if (focused != null) {
            String old = focused.getText();
            if (focused.textboxKeyTyped(c, key)) {
                onTextFieldChanged(focused, old);
                return;
            }
        }

        // More NEI behavior, f key focuses first text field
        if (c == 'f' && focused == null && !textFields.isEmpty()) {
            focused = textFields.get(0);
            focused.setFocused(true);
        }

        // Finally if 'e' was pressed but not captured by a text field, close the overlays/GUI
        if (key == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            if (!hideOverlays()) {
                this.mc.player.closeScreen();
            }
            return;
        }

        // If the key was not captured, let NEI do its thing
        super.keyTyped(c, key);
    }

    protected final void setText(@NotNull TextFieldEnder tf, @NotNull String newText) {
        String old = tf.getText();
        tf.setText(newText);
        onTextFieldChanged(tf, old);
    }

    protected void onTextFieldChanged(@NotNull TextFieldEnder tf, @NotNull String old) {}

    public boolean hideOverlays() {
        for (IGuiOverlay overlay : overlays) {
            if (overlay.isVisible()) {
                overlay.setIsVisible(false);
                return true;
            }
        }
        return false;
    }

    @Override
    public void addToolTip(@NotNull GuiToolTip toolTip) {
        ttMan.addToolTip(toolTip);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        for (GuiTextField f : textFields) {
            f.updateCursorCounter();
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int b = Mouse.getEventButton();
        for (IGuiOverlay overlay : overlays) {
            if (overlay != null && overlay.isVisible() && overlay.handleMouseInput(x, y, b)) {
                return;
            }
        }
        int delta = Mouse.getEventDWheel();
        if (delta != 0) {
            mouseWheel(x, y, delta);
        }
        super.handleMouseInput();
    }

    @Override
    protected boolean isPointInRegion(int p_146978_1_, int p_146978_2_, int p_146978_3_, int p_146978_4_,
                                      int p_146978_5_, int p_146978_6_) {
        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        for (IGuiOverlay overlay : overlays) {
            if (overlay != null && overlay.isVisible() && overlay.isMouseInBounds(x, y)) {
                return false;
            }
        }
        return super.isPointInRegion(p_146978_1_, p_146978_2_, p_146978_3_, p_146978_4_, p_146978_5_, p_146978_6_);
    }

    @Override
    public @NotNull GhostSlotHandler getGhostSlotHandler() {
        return ghostSlotHandler;
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        for (GuiButton guibutton : buttonList) {
            if (guibutton instanceof IPriorityButton && ((IPriorityButton) guibutton).isTopmost() &&
                    doHandleButtonClick(x, y, button, guibutton)) {
                return;
            }
        }
        for (GuiTextField f : textFields) {
            f.mouseClicked(x, y, button);
        }
        if (!scrollbars.isEmpty()) {
            if (draggingScrollbar != null) {
                draggingScrollbar.mouseClicked(x, y, button);
                return;
            }
            for (VScrollbar vs : scrollbars) {
                if (vs.mouseClicked(x, y, button)) {
                    draggingScrollbar = vs;
                    return;
                }
            }
        }
        if (!getGhostSlotHandler().getGhostSlots().isEmpty()) {
            GhostSlot slot = getGhostSlotHandler().getGhostSlotAt(this, x, y);
            if (slot != null) {
                getGhostSlotHandler().ghostSlotClicked(this, slot, x, y, button);
                return;
            }
        }
        // Right click field clearing
        if (button == 1) {
            for (TextFieldEnder tf : textFields) {
                if (tf.contains(x, y)) {
                    setText(tf, "");
                }
            }
        }
        // Button events for (a) non-left-clicks and (b) for buttons that don't want their click to be propagated to the
        // rest of the gui
        for (GuiButton guibutton : buttonList) {
            if (doHandleButtonClick(x, y, button, guibutton)) {
                return;
            }
        }
        List<GuiButton> temp = buttonList; // we don't want the buttons to be tested twice, that'd be a waste
        try {
            buttonList = Collections.emptyList();
            super.mouseClicked(x, y, button);
        } finally {
            buttonList = temp;
        }
    }

    private boolean doHandleButtonClick(int x, int y, int button, GuiButton guibutton) throws IOException {
        if (guibutton instanceof IButtonAwareButton ?
                ((IButtonAwareButton) guibutton).mousePressedButton(mc, x, y, button) :
                guibutton.mousePressed(mc, x, y)) {
            GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre(this, guibutton,
                    buttonList);
            if (!MinecraftForge.EVENT_BUS.post(event)) {
                guibutton = event.getButton();
                selectedButton = guibutton;
                guibutton.playPressSound(mc.getSoundHandler());
                actionPerformed(guibutton);
                if (this.equals(mc.currentScreen)) {
                    MinecraftForge.EVENT_BUS
                            .post(new GuiScreenEvent.ActionPerformedEvent.Post(this, event.getButton(), buttonList));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected void mouseReleased(int x, int y, int button) {
        if (draggingScrollbar != null) {
            draggingScrollbar.mouseMovedOrUp(x, y, button);
            draggingScrollbar = null;
        }
        super.mouseReleased(x, y, button);
    }

    @Override
    protected void mouseClickMove(int x, int y, int button, long time) {
        if (draggingScrollbar != null) {
            draggingScrollbar.mouseClickMove(x, y, button, time);
            return;
        }
        super.mouseClickMove(x, y, button, time);
    }

    protected void mouseWheel(int x, int y, int delta) {
        if (!scrollbars.isEmpty()) {
            for (VScrollbar vs : scrollbars) {
                vs.mouseWheel(x, y, delta);
            }
        }
        if (!getGhostSlotHandler().getGhostSlots().isEmpty()) {
            GhostSlot slot = getGhostSlotHandler().getGhostSlotAt(this, x, y);
            if (slot != null) {
                getGhostSlotHandler().ghostSlotClicked(this, slot, x, y, delta < 0 ? -1 : -2);
            }
        }
    }

    protected void actionPerformedButton(@NotNull IconButton btn, int mouseButton) throws IOException {
        actionPerformed(btn);
    }

    public void addOverlay(@NotNull IGuiOverlay overlay) {
        overlays.add(overlay);
    }

    public void removeOverlay(@NotNull IGuiOverlay overlay) {
        overlays.remove(overlay);
    }

    public void addScrollbar(@NotNull VScrollbar vs) {
        scrollbars.add(vs);
        vs.adjustPosition();
    }

    public void removeScrollbar(@NotNull VScrollbar vs) {
        scrollbars.remove(vs);
        if (draggingScrollbar == vs) {
            draggingScrollbar = null;
        }
    }

    public void addDrawingElement(@NotNull IDrawingElement element) {
        drawingElements.add(element);
        GuiToolTip tooltip = element.getTooltip();
        if (tooltip != null) {
            addToolTip(tooltip);
        }
    }

    public void removeDrawingElement(@NotNull IDrawingElement element) {
        drawingElements.remove(element);
        GuiToolTip tooltip = element.getTooltip();
        if (tooltip != null) {
            removeToolTip(tooltip);
        }
    }

    private int realMx, realMy;

    @Override
    protected final void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawForegroundImpl(mouseX, mouseY);

        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableDepth();
        zLevel = 300.0F;
        itemRender.zLevel = 300.0F;
        for (IGuiOverlay overlay : overlays) {
            if (overlay != null && overlay.isVisible()) {
                overlay.draw(realMx, realMy, mc.getRenderPartialTicks());
            }
        }
        zLevel = 0F;
        itemRender.zLevel = 0F;
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int mouseX, int mouseY) {
        for (IDrawingElement drawingElement : drawingElements) {
            drawingElement.drawGuiContainerBackgroundLayer(par1, mouseX, mouseY);
        }
        for (GuiTextField f : textFields) {
            f.drawTextBox();
        }
        if (!scrollbars.isEmpty()) {
            for (VScrollbar vs : scrollbars) {
                vs.drawScrollbar(mouseX, mouseY);
            }
        }
        if (!ghostSlotHandler.getGhostSlots().isEmpty()) {
            getGhostSlotHandler().drawGhostSlots(this, mouseX, mouseY);
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        int mx = realMx = par1;
        int my = realMy = par2;

        this.drawDefaultBackground();
        super.drawScreen(mx, my, par3);

        // try to only draw one tooltip...
        if (draggingScrollbar == null) {
            if (!renderHoveredToolTip2(mx, my)) {
                if (!ghostSlotHandler.drawGhostSlotToolTip(this, par1, par2)) {
                    ttMan.drawTooltips(this, par1, par2);
                }
            }
        }
    }

    /**
     * See {@link #renderHoveredToolTip(int, int)} but with a feedback return value
     */
    protected boolean renderHoveredToolTip2(int p_191948_1_, int p_191948_2_) {
        if (mc.player.inventory.getItemStack().isEmpty()) {
            final Slot slotUnderMouse = getSlotUnderMouse();
            if (slotUnderMouse != null && slotUnderMouse.getHasStack()) {
                renderToolTip(slotUnderMouse.getStack(), p_191948_1_, p_191948_2_);
                return true;
            }
        }
        return false;
    }

    // copied from super with hate
    protected void drawItemStack(@NotNull ItemStack stack, int mouseX, int mouseY, String str) {
        if (stack.isEmpty()) {
            return;
        }

        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        zLevel = 200.0F;
        itemRender.zLevel = 200.0F;
        FontRenderer font = null;
        font = stack.getItem().getFontRenderer(stack);
        if (font == null) {
            font = fontRenderer;
        }
        itemRender.renderItemIntoGUI(stack, mouseX, mouseY);
        itemRender.renderItemOverlayIntoGUI(font, stack, mouseX, mouseY, str);
        zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
    }

    protected void drawFakeItemsStart() {
        zLevel = 100.0F;
        itemRender.zLevel = 100.0F;

        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
    }

    public void drawFakeItemStack(int x, int y, @NotNull ItemStack stack) {
        itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        GlStateManager.enableAlpha();
    }

    public void drawFakeItemStackStdOverlay(int x, int y, @NotNull ItemStack stack) {
        itemRender.renderItemOverlayIntoGUI(fontRenderer, stack, x, y, null);
    }

    protected void drawFakeItemHover(int x, int y) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.colorMask(true, true, true, false);
        drawGradientRect(x, y, x + 16, y + 16, 0x80FFFFFF, 0x80FFFFFF);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.enableDepth();

        GlStateManager.enableLighting();
    }

    protected void drawFakeItemsEnd() {
        itemRender.zLevel = 0.0F;
        zLevel = 0.0F;
    }

    @Override
    public void renderToolTip(@NotNull ItemStack p_146285_1_, int p_146285_2_, int p_146285_3_) {
        super.renderToolTip(p_146285_1_, p_146285_2_, p_146285_3_);
    }

    /**
     * Return the current texture to allow GhostSlots to gray out by over-painting the slot background.
     */
    protected abstract @NotNull ResourceLocation getGuiTexture();

    @Override
    public boolean removeToolTip(@NotNull GuiToolTip toolTip) {
        return ttMan.removeToolTip(toolTip);
    }

    protected void drawForegroundImpl(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    public void drawHoveringToolTipText(@NotNull List<String> par1List, int par2, int par3,
                                        @NotNull FontRenderer font) {
        super.drawHoveringText(par1List, par2, par3, font);
    }

    public float getZlevel() {
        return zLevel;
    }

    @Override
    public int getGuiLeft() {
        return guiLeft;
    }

    @Override
    public int getGuiTop() {
        return guiTop;
    }

    @Override
    public int getXSize() {
        return xSize;
    }

    @Override
    public int getYSize() {
        return ySize;
    }

    public void setGuiLeft(int i) {
        guiLeft = i;
    }

    public void setGuiTop(int i) {
        guiTop = i;
    }

    public void setXSize(int i) {
        xSize = i;
    }

    public void setYSize(int i) {
        ySize = i;
    }

    @Override
    public @NotNull FontRenderer getFontRenderer() {
        return Minecraft.getMinecraft().fontRenderer;
    }

    @Override
    public @NotNull <T extends GuiButton> T addButton(@NotNull T button) {
        if (!buttonList.contains(button)) {
            buttonList.add(button);
        }
        return button;
    }

    @Override
    public void removeButton(@NotNull GuiButton button) {
        buttonList.remove(button);
    }

    @Override
    public int getOverlayOffsetXLeft() {
        return 0;
    }

    @Override
    public int getOverlayOffsetXRight() {
        return 0;
    }

    @Override
    public void doActionPerformed(@NotNull GuiButton guiButton) throws IOException {
        actionPerformed(guiButton);
    }

    @Override
    public void clearToolTips() {}

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (IGuiOverlay overlay : overlays) {
            overlay.guiClosed();
        }
    }

    @Override
    public final int getGuiRootLeft() {
        return getGuiLeft();
    }

    @Override
    public final int getGuiRootTop() {
        return getGuiTop();
    }

    @Override
    public final int getGuiXSize() {
        return getXSize();
    }

    @Override
    public final int getGuiYSize() {
        return getYSize();
    }

    @Override
    public final <T extends GuiButton> @NotNull T addGuiButton(@NotNull T button) {
        return addButton(button);
    }
}
