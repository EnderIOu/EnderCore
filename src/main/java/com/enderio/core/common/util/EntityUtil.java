package com.enderio.core.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.enderio.core.EnderCore;
import com.enderio.core.common.vecmath.Vector3d;
import org.jetbrains.annotations.NotNull;

public class EntityUtil {

    private static final Random rand = new Random();

    public static void setEntityVelocity(Entity entity, double velX, double velY, double velZ) {
        entity.motionX = velX;
        entity.motionY = velY;
        entity.motionZ = velZ;
    }

    public static @NotNull EntityFireworkRocket getRandomFirework(@NotNull World world) {
        return getRandomFirework(world, new BlockPos(0, 0, 0));
    }

    public static @NotNull EntityFireworkRocket getRandomFirework(@NotNull World world, @NotNull BlockPos pos) {
        ItemStack firework = new ItemStack(Items.FIREWORKS);
        firework.setTagCompound(new NBTTagCompound());
        NBTTagCompound expl = new NBTTagCompound();
        expl.setBoolean("Flicker", true);
        expl.setBoolean("Trail", true);

        int[] colors = new int[rand.nextInt(8) + 1];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = ItemDye.DYE_COLORS[rand.nextInt(16)];
        }
        expl.setIntArray("Colors", colors);
        byte type = (byte) (rand.nextInt(3) + 1);
        type = type == 3 ? 4 : type;
        expl.setByte("Type", type);

        NBTTagList explosions = new NBTTagList();
        explosions.appendTag(expl);

        NBTTagCompound fireworkTag = new NBTTagCompound();
        fireworkTag.setTag("Explosions", explosions);
        fireworkTag.setByte("Flight", (byte) 1);
        firework.setTagInfo("Fireworks", fireworkTag);

        return new EntityFireworkRocket(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, firework);
    }

    public static void spawnFirework(@NotNull BlockPos block, int dimID) {
        spawnFirework(block, dimID, 0);
    }

    public static void spawnFirework(@NotNull BlockPos pos, int dimID, int range) {
        World world = DimensionManager.getWorld(dimID);
        BlockPos spawnPos = pos;

        // don't bother if there's no randomness at all
        if (range > 0) {
            spawnPos = new BlockPos(moveRandomly(spawnPos.getX(), range), spawnPos.getY(),
                    moveRandomly(spawnPos.getZ(), range));
            IBlockState bs = world.getBlockState(spawnPos);

            int tries = -1;
            while (!world.isAirBlock(new BlockPos(spawnPos)) && !bs.getBlock().isReplaceable(world, spawnPos)) {
                tries++;
                if (tries > 100) {
                    return;
                }
            }
        }

        world.spawnEntity(getRandomFirework(world, spawnPos));
    }

    private static double moveRandomly(double base, double range) {
        return base + 0.5 + rand.nextDouble() * range - (range / 2);
    }

    public static @NotNull String getDisplayNameForEntity(@NotNull String mobName) {
        return EnderCore.lang.localizeExact("entity." + mobName + ".name");
    }

    public static @NotNull NNList<ResourceLocation> getAllRegisteredMobNames() {
        NNList<ResourceLocation> result = new NNList<>();
        for (ResourceLocation entityName : EntityList.getEntityNameList()) {
            final Class<? extends Entity> clazz = EntityList
                    .getClass(NullHelper.notnullF(entityName, "EntityList.getEntityNameList()"));
            if (clazz != null && EntityLiving.class.isAssignableFrom(clazz)) {
                result.add(entityName);
            }
        }
        return result;
    }

    public static boolean isRegisteredMob(ResourceLocation entityName) {
        if (entityName != null) {
            final Class<? extends Entity> clazz = EntityList.getClass(entityName);
            return clazz != null && EntityLiving.class.isAssignableFrom(clazz);
        }
        return false;
    }

    private EntityUtil() {}

    public static Vector3d getEntityPosition(@NotNull Entity ent) {
        return new Vector3d(ent.posX, ent.posY, ent.posZ);
    }

    public static List<AxisAlignedBB> getCollidingBlockGeometry(@NotNull World world, @NotNull Entity entity) {
        AxisAlignedBB entityBounds = entity.getEntityBoundingBox();
        ArrayList<AxisAlignedBB> collidingBoundingBoxes = new ArrayList<>();
        int minX = MathHelper.floor(entityBounds.minX);
        int minY = MathHelper.floor(entityBounds.minY);
        int minZ = MathHelper.floor(entityBounds.minZ);
        int maxX = MathHelper.floor(entityBounds.maxX + 1.0D);
        int maxY = MathHelper.floor(entityBounds.maxY + 1.0D);
        int maxZ = MathHelper.floor(entityBounds.maxZ + 1.0D);
        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                for (int y = minY; y < maxY; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    world.getBlockState(pos).addCollisionBoxToList(world, pos, entityBounds, collidingBoundingBoxes,
                            entity, false);
                }
            }
        }
        return collidingBoundingBoxes;
    }

    public static void spawnItemInWorldWithRandomMotion(@NotNull World world, @NotNull ItemStack item, int x, int y,
                                                        int z) {
        if (!item.isEmpty()) {
            spawnItemInWorldWithRandomMotion(world, item, x + 0.5, y + 0.5, z + 0.5);
        }
    }

    public static void spawnItemInWorldWithRandomMotion(@NotNull World world, @NotNull ItemStack item, double x,
                                                        double y, double z) {
        if (!item.isEmpty()) {
            spawnItemInWorldWithRandomMotion(new EntityItem(world, x, y, z, item));
        }
    }

    public static void spawnItemInWorldWithRandomMotion(@NotNull EntityItem entity) {
        entity.setDefaultPickupDelay();

        float f = (entity.world.rand.nextFloat() * 0.1f) - 0.05f;
        float f1 = (entity.world.rand.nextFloat() * 0.1f) - 0.05f;
        float f2 = (entity.world.rand.nextFloat() * 0.1f) - 0.05f;

        entity.motionX += f;
        entity.motionY += f1;
        entity.motionZ += f2;

        entity.world.spawnEntity(entity);
    }
}
