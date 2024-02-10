package com.enderio.core.api.common.enchant;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

import com.enderio.core.EnderCore;
import com.google.common.base.Predicate;

/**
 * Allows your enchants to have some flavor or description text underneath them
 */
public interface IAdvancedEnchant {

    public static final EnumEnchantmentType ALL = EnumHelper.addEnchantmentType("EC_REALLY_ALL", new Predicate<Item>() {

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
     * @param stack
     * @return a list of <code>String</code>s to be bulleted under the enchantment
     */
    default @Nonnull String[] getTooltipDetails(@Nonnull ItemStack stack) {
        final String unloc = "description." + ((Enchantment) this).getName();
        final String loc = EnderCore.lang.localizeExact(unloc);
        return unloc.equals(loc) ? new String[0] : EnderCore.lang.splitList(loc);
    }
}
