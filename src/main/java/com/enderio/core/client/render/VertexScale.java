package com.enderio.core.client.render;

import com.enderio.core.api.client.render.VertexTransform;
import com.enderio.core.common.vecmath.Vector3d;
import com.enderio.core.common.vecmath.Vector3f;
import com.enderio.core.common.vecmath.Vertex;
import org.jetbrains.annotations.NotNull;

public class VertexScale implements VertexTransform {

    private final Vector3d center;
    private final double x;
    private final double y;
    private final double z;

    public VertexScale(double x, double y, double z, Vector3d center) {
        this.center = new Vector3d(center);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public VertexScale(float x, float y, float z, Vector3d center) {
        this.center = new Vector3d(center);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public VertexScale(Vector3d scale, Vector3d center) {
        this(scale.x, scale.y, scale.z, center);
    }

    public VertexScale(Vector3f scale, Vector3d center) {
        this(scale.x, scale.y, scale.z, center);
    }

    @Override
    public void apply(@NotNull Vertex vertex) {
        apply(vertex.xyz);
    }

    @Override
    public void apply(@NotNull Vector3d vec) {
        vec.sub(center);
        vec.x *= x;
        vec.y *= y;
        vec.z *= z;
        vec.add(center);
    }

    @Override
    public void applyToNormal(@NotNull Vector3f vec) {}
}
