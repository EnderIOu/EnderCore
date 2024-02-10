package com.enderio.core.api.client.gui;

import com.enderio.core.client.gui.widget.GuiScrollableList;
import org.jetbrains.annotations.NotNull;

public interface ListSelectionListener<T> {

    void selectionChanged(@NotNull GuiScrollableList<T> list, int selectedIndex);
}
