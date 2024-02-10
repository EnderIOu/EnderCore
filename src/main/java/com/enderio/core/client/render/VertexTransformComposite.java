package com.enderio.core.client.render;

import java.util.Collection;

import com.enderio.core.api.client.render.VertexTransform;
import com.enderio.core.common.vecmath.Vector3d;
import com.enderio.core.common.vecmath.Vector3f;
import com.enderio.core.common.vecmath.Vertex;
import org.jetbrains.annotations.NotNull;

public class VertexTransformComposite implements VertexTransform {

    public final @NotNull VertexTransform[] xforms;

    public VertexTransformComposite(@NotNull VertexTransform... xforms) {
        this.xforms = xforms;
    }

    VertexTransformComposite(@NotNull Collection<VertexTransform> xformsIn) {
        xforms = new VertexTransform[xformsIn.size()];
        int i = 0;
        for (VertexTransform xform : xformsIn) {
            xforms[i] = xform;
            i++;
        }
    }

    @Override
    public void apply(@NotNull Vertex vertex) {
        for (VertexTransform xform : xforms) {
            xform.apply(vertex);
        }
    }

    @Override
    public void apply(@NotNull Vector3d vec) {
        for (VertexTransform xform : xforms) {
            xform.apply(vec);
        }
    }

    @Override
    public void applyToNormal(@NotNull Vector3f vec) {
        for (VertexTransform xform : xforms) {
            xform.applyToNormal(vec);
        }
    }
}
