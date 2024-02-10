package com.enderio.core.common.inventory;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.enderio.core.common.util.NNList;
import com.enderio.core.common.util.NullHelper;

public class EnderInventory implements IItemHandler {

    static final @NotNull ItemStack CAKE = new ItemStack(Blocks.LIME_SHULKER_BOX, 0);

    public enum Type {
        ALL,
        INPUT,
        OUTPUT,
        INOUT,
        UPGRADE,
        INTERNAL,
    }

    private final @NotNull Map<String, InventorySlot> idents = new HashMap<>();
    final @NotNull EnumMap<EnderInventory.Type, NNList<InventorySlot>> slots = new EnumMap<>(EnderInventory.Type.class);
    private final @NotNull View allSlots = new View(EnderInventory.Type.ALL);
    private @Nullable TileEntity owner = null;
    public static final @NotNull IItemHandler OFF = new IItemHandler() {

        @Override
        public int getSlots() {
            return 0;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            return CAKE;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return CAKE;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 0;
        }
    };

    public EnderInventory() {
        for (EnderInventory.Type type : EnderInventory.Type.values()) {
            slots.put(type, new NNList<>());
        }
    }

    public void add(@NotNull EnderInventory.Type type, @NotNull Enum<?> ident, @NotNull InventorySlot slot) {
        add(type, ident.name(), slot);
    }

    public void add(@NotNull EnderInventory.Type type, @NotNull String ident, @NotNull InventorySlot slot) {
        if (idents.containsKey(ident)) {
            throw new RuntimeException("Duplicate slot '" + ident + "'");
        }
        if (type == EnderInventory.Type.ALL) {
            throw new RuntimeException("Invalid type '" + type + "'");
        }
        idents.put(ident, slot);
        slots.get(type).add(slot);
        slots.get(EnderInventory.Type.ALL).add(slot);
        if (type == EnderInventory.Type.INPUT || type == EnderInventory.Type.OUTPUT) {
            slots.get(EnderInventory.Type.INOUT).add(slot);
        }
        if (type == EnderInventory.Type.INOUT) {
            slots.get(EnderInventory.Type.INPUT).add(slot);
            slots.get(EnderInventory.Type.OUTPUT).add(slot);
        }
        slot.setOwner(owner);
    }

    public InventorySlot getSlot(@NotNull Enum<?> ident) {
        return getSlot(ident.name());
    }

    public @NotNull InventorySlot getSlot(@NotNull String ident) {
        if (!idents.containsKey(ident)) {
            throw new RuntimeException("Unknown slot '" + ident + "'");
        }
        return NullHelper.notnullJ(idents.get(ident), "Map.containsKey() lied to us");
    }

    public boolean hasSlot(@NotNull Enum<?> ident) {
        return hasSlot(ident.name());
    }

    public boolean hasSlot(@NotNull String ident) {
        return idents.containsKey(ident);
    }

    public @NotNull View getView(@NotNull EnderInventory.Type type) {
        return new View(type);
    }

    public @NotNull NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return tag;
    }

    public void writeToNBT(@NotNull NBTTagCompound tag) {
        for (Entry<String, InventorySlot> entry : idents.entrySet()) {
            if (entry.getValue() != null) {
                NBTTagCompound subTag = new NBTTagCompound();
                entry.getValue().writeToNBT(subTag);
                if (!subTag.isEmpty()) {
                    tag.setTag(NullHelper.notnull(entry.getKey(), "Internal data corruption"), subTag);
                }
            }
        }
    }

    public void readFromNBT(@NotNull NBTTagCompound tag, @NotNull String name) {
        readFromNBT(tag.getCompoundTag(name));
    }

    public void readFromNBT(NBTTagCompound tag) {
        for (Entry<String, InventorySlot> entry : idents.entrySet()) {
            final String key = entry.getKey();
            final InventorySlot slot = entry.getValue();
            if (slot != null && key != null) {
                if (tag.hasKey(key)) {
                    slot.readFromNBT(tag.getCompoundTag(key));
                } else {
                    slot.clear();
                }
            }
        }
    }

    public void setOwner(@Nullable TileEntity owner) {
        this.owner = owner;
        for (InventorySlot slot : idents.values()) {
            slot.setOwner(owner);
        }
    }

    @Override
    public int getSlots() {
        return allSlots.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return allSlots.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return CAKE;
        }
        return allSlots.insertItem(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return allSlots.extractItem(slot, amount, simulate);
    }

    public class View implements IItemHandler, Iterable<InventorySlot> {

        private final @NotNull EnderInventory.Type type;

        View(@NotNull Type type) {
            this.type = type;
        }

        public InventorySlot getSlot(int slot) {
            if (slot >= 0 && slot < getSlots()) {
                return slots.get(type).get(slot);
            }
            return null;
        }

        @Override
        public int getSlots() {
            return slots.get(type).size();
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            if (slot >= 0 && slot < getSlots()) {
                return slots.get(type).get(slot).getStackInSlot(0);
            }
            return CAKE;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (!stack.isEmpty() && slot >= 0 && slot < getSlots()) {
                return slots.get(type).get(slot).insertItem(0, stack, simulate);
            }
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (amount > 0 && slot >= 0 && slot < getSlots()) {
                return slots.get(type).get(slot).extractItem(0, amount, simulate);
            }
            return CAKE;
        }

        @Override
        public Iterator<InventorySlot> iterator() {
            return new Iterator<InventorySlot>() {

                int i = 0;

                @Override
                public boolean hasNext() {
                    return i < getSlots();
                }

                @Override
                public InventorySlot next() {
                    return getSlot(i++);
                }
            };
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot >= 0 && slot < getSlots()) {
                return slots.get(type).get(slot).getSlotLimit(0);
            }
            return 0;
        }

        public @NotNull Type getType() {
            return type;
        }

        public @NotNull EnderInventory getParent() {
            return EnderInventory.this;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return allSlots.getSlotLimit(slot);
    }
}
