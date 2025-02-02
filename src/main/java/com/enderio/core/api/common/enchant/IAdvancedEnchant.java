package com.enderio.core.api.common.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.enderio.core.EnderCore;
import com.google.common.base.Predicate;

/**
 * Allows your enchants to have some flavor or description text underneath them
 */
public interface IAdvancedEnchant {

    EnumEnchantmentType ALL = EnumHelper.addEnchantmentType("EC_REALLY_ALL", new Predicate<Item>() {

        @Override
        public boolean apply(@Nullable Item input) {
            return true;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            return super.equals(obj);
        }
    });

    /**
     * Get the detail for this itemstack
     *
     * @return an array of strings to be bulleted under the enchantment
     */
    default @NotNull String[] getTooltipDetails(@NotNull ItemStack stack) {
        final String unloc = "description." + ((Enchantment) this).getName();
        final String loc = EnderCore.lang.localizeExact(unloc);
        return unloc.equals(loc) ? new String[0] : EnderCore.lang.splitList(loc);
    }
}
