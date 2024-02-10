package com.enderio.core.api.client.render;

import com.enderio.core.common.vecmath.Vector3d;
import com.enderio.core.common.vecmath.Vector3f;
import com.enderio.core.common.vecmath.Vertex;
import org.jetbrains.annotations.NotNull;

public interface VertexTransform {

    void apply(@NotNull Vertex vertex);

    void apply(@NotNull Vector3d vec);

    void applyToNormal(@NotNull Vector3f vec);
}
