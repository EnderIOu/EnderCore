package com.enderio.core.common.inventory;

import org.jetbrains.annotations.NotNull;

public interface Callback<T> {

    void onChange(@NotNull T oldStack, @NotNull T newStack);
}
