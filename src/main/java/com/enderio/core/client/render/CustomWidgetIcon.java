package com.enderio.core.client.render;

import com.enderio.core.api.client.render.IWidgetIcon;
import com.enderio.core.api.client.render.IWidgetMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomWidgetIcon implements IWidgetIcon {

    private final int x, y, width, height;
    private final @NotNull IWidgetMap map;

    private final @Nullable IWidgetIcon overlay;

    public CustomWidgetIcon(int x, int y, int width, int height, @NotNull IWidgetMap map,
                            @Nullable IWidgetIcon overlay) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.map = map;
        this.overlay = overlay;
    }

    public CustomWidgetIcon(int x, int y, int width, int height, @NotNull IWidgetMap map) {
        this(x, y, width, height, map, null);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public @NotNull IWidgetMap getMap() {
        return map;
    }

    @Override
    public @Nullable IWidgetIcon getOverlay() {
        return overlay;
    }
}
