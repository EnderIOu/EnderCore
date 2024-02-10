package com.enderio.core.common.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ArrayInventory implements IInventory {

    protected final @NotNull ItemStack[] items;

    public ArrayInventory(@NotNull ItemStack[] items) {
        this.items = items;
    }

    public ArrayInventory(int size) {
        items = new ItemStack[size];
    }

    @Override
    public int getSizeInventory() {
        return items.length;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        final ItemStack itemStack = items[slot];
        return itemStack != null ? itemStack : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack decrStackSize(int slot, int amount) {
        return Util.decrStackSize(this, slot, amount);
    }

    @Override
    public void setInventorySlotContents(int slot, @NotNull ItemStack stack) {
        items[slot] = stack;
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(@NotNull EntityPlayer var1) {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int i, @NotNull ItemStack itemstack) {
        return true;
    }

    @Override
    public void markDirty() {}

    @Override
    public @NotNull String getName() {
        return "ArrayInventory";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public @NotNull ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }

    @Override
    public @NotNull ItemStack removeStackFromSlot(int index) {
        ItemStack res = items[index];
        items[index] = ItemStack.EMPTY;
        return res != null ? res : ItemStack.EMPTY;
    }

    @Override
    public void openInventory(@NotNull EntityPlayer player) {}

    @Override
    public void closeInventory(@NotNull EntityPlayer player) {}

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        Arrays.fill(items, ItemStack.EMPTY);
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
