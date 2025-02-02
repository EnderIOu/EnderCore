package com.enderio.core.common.handlers;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.enderio.core.common.Handlers.Handler;
import com.enderio.core.common.enchant.EnchantAutoSmelt;
import com.enderio.core.common.util.NullHelper;

@Handler
public class AutoSmeltHandler {

    @SubscribeEvent
    public void handleBlockBreak(BlockEvent.HarvestDropsEvent event) {
        final World world = NullHelper.notnullF(event.getWorld(), "BlockEvent.HarvestDropsEvent.getWorld()");
        final EntityPlayer harvester = event.getHarvester();
        final EnchantAutoSmelt enchantment = EnchantAutoSmelt.instance();
        if (!world.isRemote && harvester != null && !event.isSilkTouching() && enchantment != null) {

            ItemStack held = harvester.getHeldItemMainhand();
            if (!held.isEmpty()) {
                int level = EnchantmentHelper.getEnchantmentLevel(enchantment, held);

                if (level > 0) {
                    for (int i = 0; i < event.getDrops().size(); i++) {
                        ItemStack stack = NullHelper.notnullF(event.getDrops().get(i),
                                "BlockEvent.HarvestDropsEvent.getDrops()");
                        if (!stack.isEmpty()) {
                            final ItemStack smeltingResult = FurnaceRecipes.instance()
                                    .getSmeltingResult(stack);
                            if (!smeltingResult.isEmpty()) {
                                ItemStack furnaceStack = smeltingResult.copy();

                                event.getDrops().set(i, furnaceStack);

                                // adapted vanilla code, see net.minecraft.inventory.SlotFurnaceOutput.onCrafting()

                                furnaceStack.onCrafting(world, harvester, furnaceStack.getCount());

                                if (!(event.getHarvester() instanceof FakePlayer)) {
                                    int xp = furnaceStack.getCount();
                                    float f = FurnaceRecipes.instance().getSmeltingExperience(furnaceStack);

                                    if (f == 0.0F) {
                                        xp = 0;
                                    } else if (f < 1.0F) {
                                        int j = MathHelper.floor(xp * f);

                                        if (j < MathHelper.ceil(xp * f) && (float) Math.random() < xp * f - j) {
                                            ++j;
                                        }

                                        xp = j;
                                    }

                                    while (xp > 0) {
                                        int k = EntityXPOrb.getXPSplit(xp);
                                        xp -= k;
                                        world.spawnEntity(new EntityXPOrb(world, event.getPos().getX(),
                                                event.getPos().getY() + 0.5, event.getPos().getZ(), k));
                                    }
                                }

                                FMLCommonHandler.instance().firePlayerSmeltedEvent(harvester, furnaceStack);
                            }
                        }
                    }
                }
            }
        }
    }
}
