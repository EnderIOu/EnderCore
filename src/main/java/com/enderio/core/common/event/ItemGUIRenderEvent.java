package com.enderio.core.common.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

import org.jetbrains.annotations.NotNull;

/**
 * Fired when an item is rendered into the GUI
 *
 */
public class ItemGUIRenderEvent extends Event {

    private final @NotNull ItemStack stack;
    private final int xPosition, yPosition;

    public ItemGUIRenderEvent(@NotNull ItemStack stack, int xPosition, int yPosition) {
        super();
        this.stack = stack;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public @NotNull ItemStack getStack() {
        return stack;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    /**
     * Fired before the item is rendered
     *
     */
    public static class Pre extends ItemGUIRenderEvent {

        public Pre(@NotNull ItemStack stack, int xPosition, int yPosition) {
            super(stack, xPosition, yPosition);
        }
    }

    /**
     * Fired after the item and either the damage bar or the stack size are rendered but before the cooldown overlay is
     * rendered
     *
     */
    public static class Post extends ItemGUIRenderEvent {

        public Post(@NotNull ItemStack stack, int xPosition, int yPosition) {
            super(stack, xPosition, yPosition);
        }
    }
}
