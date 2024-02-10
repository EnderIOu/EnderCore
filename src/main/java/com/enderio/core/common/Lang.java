package com.enderio.core.common;

import java.util.List;

import net.minecraft.util.text.translation.I18n;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class Lang {

    private static final @NotNull String REGEX = "\\" + '|';
    public static final char CHAR = '|';

    private final @NotNull String prefix;

    public Lang(@NotNull String locKey) {
        this.prefix = locKey.concat(".");
    }

    /**
     * @return The prefix assigned to this Lang object.
     */
    public @NotNull String getPrefix() {
        return this.prefix;
    }

    /**
     * Adds the stored prefix to this string, separating with a period. Using this method returns the string that is
     * used for localizing if you passed this arg
     * into {@link #localize(String, Object...)}.
     *
     * @param suffix
     *               The suffix string
     * @return The full string
     */
    public @NotNull String addPrefix(@NotNull String suffix) {
        return prefix.concat(suffix);
    }

    /**
     * Localizes the string passed, first appending the prefix stored in this instance of the class.
     *
     * @param unloc
     *              The unlocalized string.
     * @param args
     *              The args to format the localized text with.
     *
     * @return A localized string.
     */
    public @NotNull String localize(@NotNull String unloc, @NotNull Object... args) {
        return localizeExact(addPrefix(unloc), args);
    }

    /**
     * Localizes the string passed, first appending the prefix stored in this instance of the class.
     *
     * @param unloc
     *              The unlocalized string.
     *
     * @return A localized string.
     */
    public @NotNull String localize(@NotNull String unloc) {
        return localizeExact(addPrefix(unloc));
    }

    /**
     * Ignores the prefix stored in this instance of the class and localizes the raw string passed.
     *
     * @param unloc
     *              The unlocalized string.
     * @param args
     *              The args to format the localized text with.
     *
     * @return A localized string.
     */
    public @NotNull String localizeExact(@NotNull String unloc, @NotNull Object... args) {
        return I18n.translateToLocalFormatted(unloc, args);
    }

    /**
     * Ignores the prefix stored in this instance of the class and localizes the raw string passed.
     *
     * @param unloc
     *              The unlocalized string.
     *
     * @return A localized string.
     */
    public @NotNull String localizeExact(@NotNull String unloc) {
        return I18n.translateToLocal(unloc);
    }

    /**
     * Splits the localized text on "|" into a String[].
     *
     * @param unloc
     *              The unlocalized string.
     * @param args
     *              The args to format the localized text with.
     * @return A localized list of strings.
     */
    public @NotNull String[] localizeList(@NotNull String unloc, @NotNull String... args) {
        return splitList(localize(unloc, (Object[]) args));
    }

    /**
     * Splits the localized text on "|" into a String[].
     *
     * @param unloc
     *              The unlocalized string.
     * @return A localized list of strings.
     */
    public @NotNull String[] localizeList(@NotNull String unloc) {
        return splitList(localize(unloc));
    }

    /**
     * Splits the localized text on "|" into a String[].
     *
     * @param unloc
     *              The unlocalized string.
     * @return A localized list of strings.
     */
    public @NotNull String[] localizeListExact(@NotNull String unloc) {
        return splitList(localizeExact(unloc));
    }

    /**
     * Localizes all strings in a list, using the prefix.
     *
     * @param unloc
     *              The list of unlocalized strings.
     * @return A list of localized versions of the passed strings.
     */
    public @NotNull List<String> localizeAll(@NotNull List<String> unloc) {
        List<String> ret = Lists.newArrayList();
        for (String s : unloc) {
            final @Nullable String notnulliswear = s;
            ret.add(localize(notnulliswear == null ? "null" : notnulliswear));
        }
        return ret;
    }

    /**
     * Localizes all strings in an array, using the prefix.
     *
     * @param unloc
     *              The array of unlocalized strings.
     * @return An array of localized versions of the passed strings.
     */
    public @NotNull String[] localizeAll(@NotNull Lang lang, @NotNull String... unloc) {
        String[] ret = new String[unloc.length];
        for (int i = 0; i < ret.length; i++) {
            final @Nullable String notnulliswear = unloc[i];
            ret[i] = lang.localize(notnulliswear == null ? "null" : notnulliswear);
        }
        return ret;
    }

    /**
     * Splits a list of strings based on {@value #CHAR}
     *
     * @param list
     *             The list of strings to split
     * @return An array of strings split on {@value #CHAR}
     */
    public @NotNull String[] splitList(@NotNull String list) {
        return list.split(REGEX);
    }

    /**
     * Checks if the passed string (plus the prefix) has a localization mapped.
     *
     * @param unloc
     *              The unlocalized suffix
     * @return True if there is a localization mapped, false otherwise.
     */
    public boolean canLocalize(@NotNull String unloc) {
        return canLocalizeExact(addPrefix(unloc));
    }

    /**
     * Checks if the passed string has a localization mapped. Does not use the prefix.
     *
     * @param unloc
     *              The unlocalized string
     * @return True if there is a localization mapped, false otherwise.
     */
    public boolean canLocalizeExact(@NotNull String unloc) {
        return I18n.canTranslate(unloc);
    }
}
