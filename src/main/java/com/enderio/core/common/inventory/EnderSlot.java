package com.enderio.core.common.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnderSlot extends SlotItemHandler {

    protected final @NotNull EnderInventory.Type type;

    public EnderSlot(@NotNull EnderInventory.Type type, @NotNull InventorySlot itemHandler, int xPosition,
                     int yPosition) {
        super(itemHandler, 0, xPosition, yPosition);
        this.type = type;
    }

    public EnderSlot(@NotNull EnderInventory.Type type, @NotNull EnderInventory enderInventory, @NotNull String ident,
                     int xPosition, int yPosition) {
        super(enderInventory.getSlot(ident), 0, xPosition, yPosition);
        this.type = type;
    }

    public EnderSlot(@NotNull EnderInventory.View enderInventory, @NotNull String ident, int xPosition, int yPosition) {
        this(enderInventory.getType(), enderInventory.getParent(), ident, xPosition, yPosition);
    }

    public EnderSlot(@NotNull EnderInventory.Type type, @NotNull EnderInventory enderInventory, @NotNull Enum<?> ident,
                     int xPosition, int yPosition) {
        super(enderInventory.getSlot(ident), 0, xPosition, yPosition);
        this.type = type;
    }

    public EnderSlot(@NotNull EnderInventory.View enderInventory, @NotNull Enum<?> ident, int xPosition,
                     int yPosition) {
        this(enderInventory.getType(), enderInventory.getParent(), ident, xPosition, yPosition);
    }

    public static List<EnderSlot> create(@NotNull EnderInventory enderInventory, @NotNull EnderInventory.Type type,
                                         int xPosition, int yPosition, int cols,
                                         int rows) {
        return create(enderInventory, type, xPosition, yPosition, 18, 18, cols, rows);
    }

    public static List<EnderSlot> create(@NotNull EnderInventory enderInventory, @NotNull EnderInventory.Type type,
                                         int xPosition, int yPosition, int xOffset,
                                         int yOffset, int cols, int rows) {
        List<EnderSlot> result = new ArrayList<>();
        int x = 0, y = 0;
        EnderInventory.View view = enderInventory.getView(type);
        for (int i = 0; i < view.getSlots(); i++) {
            InventorySlot slot = view.getSlot(i);
            if (slot != null) {
                result.add(new EnderSlot(view.getType(), slot, xPosition + x * xOffset, yPosition + y * yOffset));
                x++;
                if (x >= cols) {
                    y++;
                    x = 0;
                    if (y >= rows) {
                        return result;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void putStack(@NotNull ItemStack stack) {
        ((InventorySlot) getItemHandler()).set(stack);
        this.onSlotChanged();
    }

    @Override
    public boolean isItemValid(@NotNull ItemStack stack) {
        return ((InventorySlot) getItemHandler()).isItemValidForSlot(stack);
    }

    @Override
    public int getItemStackLimit(@NotNull ItemStack stack) {
        return getSlotStackLimit();
    }

    @Override
    public int getSlotStackLimit() {
        return ((InventorySlot) getItemHandler()).getMaxStackSize();
    }

    @Override
    public boolean isSameInventory(Slot other) {
        return other instanceof EnderSlot && ((InventorySlot) ((EnderSlot) other).getItemHandler()).getOwner() ==
                ((InventorySlot) getItemHandler()).getOwner();
    }

    public @NotNull EnderInventory.Type getType() {
        return type;
    }

    public boolean is(@NotNull EnderInventory.Type typeIn) {
        return this.type == typeIn;
    }

    public static boolean is(@Nullable Slot slot, @NotNull EnderInventory.Type typeIn) {
        return slot instanceof EnderSlot && ((EnderSlot) slot).is(typeIn);
    }
}
