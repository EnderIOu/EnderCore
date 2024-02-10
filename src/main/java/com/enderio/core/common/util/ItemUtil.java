package com.enderio.core.common.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.enderio.core.EnderCore;
import com.enderio.core.common.vecmath.Vector3f;
import org.jetbrains.annotations.NotNull;

public class ItemUtil {

    public static void spawnItemInWorldWithRandomMotion(@NotNull World world, @NotNull ItemStack item,
                                                        @NotNull BlockPos pos) {
        spawnItemInWorldWithRandomMotion(world, item, pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Spawns an ItemStack into the world with motion that simulates a normal block drop.
     *
     * @param world
     *              The world object.
     * @param item
     *              The ItemStack to spawn.
     * @param x
     *              X coordinate of the block in which to spawn the entity.
     * @param y
     *              Y coordinate of the block in which to spawn the entity.
     * @param z
     *              Z coordinate of the block in which to spawn the entity.
     */
    public static void spawnItemInWorldWithRandomMotion(@NotNull World world, @NotNull ItemStack item, int x, int y,
                                                        int z) {
        if (!item.isEmpty()) {
            spawnItemInWorldWithRandomMotion(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, item));
        }
    }

    /**
     * Spawns an ItemStack into the world with motion that simulates a normal block drop.
     *
     * @param world
     *              The world object.
     * @param item
     *              The ItemStack to spawn.
     * @param pos
     *              The block location to spawn the item.
     * @param hitX
     *              The location within the block to spawn the item at.
     * @param hitY
     *              The location within the block to spawn the item at.
     * @param hitZ
     *              The location within the block to spawn the item at.
     * @param scale
     *              The factor with which to push the spawn location out.
     */
    public static void spawnItemInWorldWithRandomMotion(@NotNull World world, @NotNull ItemStack item,
                                                        @NotNull BlockPos pos, float hitX, float hitY, float hitZ,
                                                        float scale) {
        Vector3f v = new Vector3f((hitX - .5f), (hitY - .5f), (hitZ - .5f));
        v.normalize();
        v.scale(scale);
        float x = pos.getX() + .5f + v.x;
        float y = pos.getY() + .5f + v.y;
        float z = pos.getZ() + .5f + v.z;
        spawnItemInWorldWithRandomMotion(new EntityItem(world, x, y, z, item));
    }

    /**
     * Spawns an EntityItem into the world with motion that simulates a normal block drop.
     *
     * @param entity
     *               The entity to spawn.
     */
    public static void spawnItemInWorldWithRandomMotion(@NotNull EntityItem entity) {
        entity.setDefaultPickupDelay();
        entity.world.spawnEntity(entity);
    }

    /**
     * Returns true if the given stack has the given Ore Dictionary name applied to it.
     *
     * @param stack
     *                The ItemStack to check.
     * @param oredict
     *                The oredict name.
     * @return True if the ItemStack matches the name passed.
     */
    public static boolean itemStackMatchesOredict(@NotNull ItemStack stack, String oredict) {
        int[] ids = OreDictionary.getOreIDs(stack);
        for (int i : ids) {
            String name = OreDictionary.getOreName(i);
            if (name.equals(oredict)) {
                return true;
            }
        }
        return false;
    }

    public static String getDurabilityString(@NotNull ItemStack item) {
        if (item.isEmpty()) {
            return null;
        }
        return EnderCore.lang.localize("tooltip.durability") + " " + (item.getMaxDamage() - item.getItemDamage()) +
                "/" + item.getMaxDamage();
    }

    /**
     * Gets an NBT tag from an ItemStack, creating it if needed. The tag returned will always be the same as the one on
     * the stack.
     *
     * @param stack
     *              The ItemStack to get the tag from.
     * @return An NBTTagCompound from the stack.
     */
    public static NBTTagCompound getOrCreateNBT(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    public static boolean isStackFull(@NotNull ItemStack contents) {
        return contents.getCount() >= contents.getMaxStackSize();
    }

    /**
     * Checks if items, damage and NBT are equal and the items are stackable.
     *
     * @return True if the two stacks are mergeable, false otherwise.
     */
    public static boolean areStackMergable(@NotNull ItemStack s1, @NotNull ItemStack s2) {
        if (s1.isEmpty() || s2.isEmpty() || !s1.isStackable() || !s2.isStackable()) {
            return false;
        }
        if (!s1.isItemEqual(s2)) {
            return false;
        }
        return ItemStack.areItemStackTagsEqual(s1, s2);
    }

    /**
     * Checks if items, damage and NBT are equal.
     *
     * @return True if the two stacks are equal, false otherwise.
     */
    public static boolean areStacksEqual(@NotNull ItemStack s1, @NotNull ItemStack s2) {
        if (s1.isEmpty() || s2.isEmpty()) {
            return false;
        }
        if (!s1.isItemEqual(s2)) {
            return false;
        }
        return ItemStack.areItemStackTagsEqual(s1, s2);
    }

    /**
     * Checks if items, meta and NBT are equal.
     *
     * @return True if the two stacks are equal, false otherwise.
     */
    public static boolean areStacksEqualIgnoringDamage(@NotNull ItemStack s1, @NotNull ItemStack s2) {
        if (s1.isEmpty() || s2.isEmpty()) {
            return false;
        }
        if (!s1.isItemEqualIgnoreDurability(s2)) {
            return false;
        }
        return ItemStack.areItemStackTagsEqual(s1, s2);
    }

    /**
     * Tries to put an item into the player's inventory as if it was picked up in the world.
     * <p>
     * Note: Needs to be called server-side.
     * 
     * @param player
     *                  The player
     * @param itemstack
     *                  The item to pick up
     * @return The remaining stack. Empty when all was picked up.
     */
    public static @NotNull ItemStack fakeItemPickup(@NotNull EntityPlayer player, @NotNull ItemStack itemstack) {
        if (!player.world.isRemote) {
            EntityItem entityItem = new EntityItem(player.world, player.posX, player.posY, player.posZ, itemstack);
            entityItem.onCollideWithPlayer(player);
            if (entityItem.isDead) {
                return ItemStack.EMPTY;
            } else {
                entityItem.setDead();
                return entityItem.getItem();
            }
        }
        return itemstack;
    }
}
