package com.enderio.core.common.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

public final class OreDictionaryHelper {

    public static final String INGOT_COPPER = "ingotCopper";
    public static final String INGOT_TIN = "ingotTin";
    public static final String DUST_ENDERPEARL = "dustEnderPearl";
    public static final String INGOT_ENDERIUM = "ingotEnderium";

    public static boolean isRegistered(String name) {
        return !OreDictionary.getOres(name).isEmpty();
    }

    public static boolean hasCopper() {
        return isRegistered(INGOT_COPPER);
    }

    public static boolean hasTin() {
        return isRegistered(INGOT_TIN);
    }

    public static boolean hasEnderPearlDust() {
        return isRegistered(DUST_ENDERPEARL);
    }

    private OreDictionaryHelper() {}

    public static boolean hasEnderium() {
        return isRegistered(INGOT_ENDERIUM);
    }

    public static String[] getOreNames(@NotNull ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        String[] ret = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            ret[i] = OreDictionary.getOreName(ids[i]);
        }
        return ret;
    }

    public static boolean hasName(@NotNull ItemStack stack, String oreName) {
        return ArrayUtils.contains(getOreNames(stack), oreName);
    }
}
