package com.enderio.core.api.client.gui;

import org.jetbrains.annotations.NotNull;

import com.enderio.core.client.gui.widget.GuiScrollableList;

public interface ListSelectionListener<T> {

    void selectionChanged(@NotNull GuiScrollableList<T> list, int selectedIndex);
}
