package com.enderio.core.client.render;

import org.jetbrains.annotations.NotNull;

import com.enderio.core.api.client.render.VertexTransform;
import com.enderio.core.common.vecmath.Quat4d;
import com.enderio.core.common.vecmath.Vector3d;
import com.enderio.core.common.vecmath.Vector3f;
import com.enderio.core.common.vecmath.Vertex;

public class VertexRotation implements VertexTransform {

    private final @NotNull Vector3d center;
    private @NotNull Quat4d quat;
    private double angle;
    private final @NotNull Vector3d axis;

    public VertexRotation(double angle, @NotNull Vector3d axis, @NotNull Vector3d center) {
        this.center = new Vector3d(center);
        this.axis = new Vector3d(axis);
        this.angle = angle;
        quat = Quat4d.makeRotate(angle, axis);
    }

    @Override
    public void apply(@NotNull Vertex vertex) {
        apply(vertex.xyz);
    }

    @Override
    public void apply(@NotNull Vector3d vec) {
        vec.sub(center);
        quat.rotate(vec);
        vec.add(center);
    }

    public void setAngle(double angle) {
        this.angle = angle;
        quat = Quat4d.makeRotate(angle, axis);
    }

    public double getAngle() {
        return angle;
    }

    public void setAxis(@NotNull Vector3d axis) {
        this.axis.set(axis);
        quat = Quat4d.makeRotate(angle, axis);
    }

    public void setCenter(@NotNull Vector3d cen) {
        center.set(cen);
    }

    @Override
    public void applyToNormal(@NotNull Vector3f vec) {
        quat.rotate(vec);
    }
}
