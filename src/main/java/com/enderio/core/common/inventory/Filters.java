package com.enderio.core.common.inventory;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class Filters {

    public static final @NotNull Callback<ItemStack> NO_CALLBACK = (oldStack, newStack) -> {};

    public static final @NotNull Predicate<ItemStack> ALWAYS_TRUE = Predicates.alwaysTrue();

    public static final @NotNull Predicate<ItemStack> ALWAYS_FALSE = Predicates.alwaysFalse();

    public static final @NotNull Predicate<ItemStack> ONLY_STACKABLE = new PredicateItemStack() {

        @Override
        public boolean doApply(@NotNull ItemStack input) {
            return input.isStackable();
        }
    };

    public static @NotNull Predicate<ItemStack> and(final @NotNull Predicate<ItemStack> a,
                                                    final @NotNull Predicate<ItemStack> b) {
        return new PredicateItemStack() {

            @Override
            public boolean doApply(@NotNull ItemStack input) {
                return a.apply(input) && b.apply(input);
            }
        };
    }

    public static @NotNull Predicate<ItemStack> or(final @NotNull Predicate<ItemStack> a,
                                                   final @NotNull Predicate<ItemStack> b) {
        return new PredicateItemStack() {

            @Override
            public boolean doApply(@NotNull ItemStack input) {
                return a.apply(input) || b.apply(input);
            }
        };
    }

    public static @NotNull Predicate<ItemStack> not(final @NotNull Predicate<ItemStack> a) {
        return new PredicateItemStack() {

            @Override
            public boolean doApply(@NotNull ItemStack input) {
                return !a.apply(input);
            }
        };
    }

    // ///////////////////////////////////////////////////////////////////

    private Filters() {}

    public static abstract class PredicateItemStack implements Predicate<ItemStack> {

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            return super.equals(obj);
        }

        @Override
        public boolean apply(@Nullable ItemStack input) {
            Preconditions.checkNotNull(input);
            return !input.isEmpty() && doApply(input);
        }

        public abstract boolean doApply(@NotNull ItemStack input);
    }
}
