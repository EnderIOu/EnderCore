package com.enderio.core.client.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import com.enderio.core.api.client.render.VertexTransform;
import com.enderio.core.common.util.NNList;
import com.enderio.core.common.vecmath.Vector2f;
import com.enderio.core.common.vecmath.Vector3d;
import com.enderio.core.common.vecmath.Vector3f;
import com.enderio.core.common.vecmath.Vertex;
import org.jetbrains.annotations.NotNull;

public final class BoundingBox extends AxisAlignedBB {

    public static final @NotNull BoundingBox UNIT_CUBE = new BoundingBox(0, 0, 0, 1, 1, 1);

    public BoundingBox(@NotNull BlockPos pos1, @NotNull BlockPos pos2) {
        super(pos1, pos2);
    }

    public BoundingBox(@NotNull AxisAlignedBB bb) {
        super(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
    }

    public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public BoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public BoundingBox(@NotNull Vector3d min, @NotNull Vector3d max) {
        super(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public BoundingBox(@NotNull BlockPos bc) {
        super(bc.getX(), bc.getY(), bc.getZ(), bc.getX() + 1, bc.getY() + 1, bc.getZ() + 1);
    }

    public @NotNull BoundingBox expandBy(@NotNull BoundingBox other) {
        return new BoundingBox(Math.min(minX, other.minX), Math.min(minY, other.minY), Math.min(minZ, other.minZ),
                Math.max(maxX, other.maxX),
                Math.max(maxY, other.maxY), Math.max(maxZ, other.maxZ));
    }

    public boolean contains(@NotNull BoundingBox other) {
        return minX <= other.minX && minY <= other.minY && minZ <= other.minZ && maxX >= other.maxX &&
                maxY >= other.maxY && maxZ >= other.maxZ;
    }

    public boolean contains(@NotNull BlockPos pos) {
        return minX <= pos.getX() && minY <= pos.getY() && minZ <= pos.getZ() && maxX >= pos.getX() &&
                maxY >= pos.getY() && maxZ >= pos.getZ();
    }

    /**
     * Returns <code>true</code> if the given entity's location point is within the bounding box.
     */
    public boolean contains(@NotNull Entity entity) {
        return minX <= entity.posX && minY <= entity.posY && minZ <= entity.posZ && maxX >= entity.posX &&
                maxY >= entity.posY && maxZ >= entity.posZ;
    }

    /**
     * Returns <code>true</code> if the given entity's bounding box intersects with the bounding box.
     * <p>
     * Note that this checks Entity#getEntityBoundingBox.
     */
    public boolean intersects(@NotNull Entity entity) {
        return intersects(entity.getEntityBoundingBox());
    }

    public boolean intersects(@NotNull BoundingBox other) {
        return other.maxX > this.minX && other.minX < this.maxX ? (other.maxY > this.minY && other.minY < this.maxY ?
                other.maxZ > this.minZ && other.minZ < this.maxZ : false) : false;
    }

    public @NotNull BoundingBox scale(float xyz) {
        return scale((double) xyz, (double) xyz, (double) xyz);
    }

    public @NotNull BoundingBox scale(double xyz) {
        return scale(xyz, xyz, xyz);
    }

    public @NotNull BoundingBox scale(float x, float y, float z) {
        return scale((double) x, (double) y, (double) z);
    }

    public @NotNull BoundingBox scale(double x, double y, double z) {
        double w = ((maxX - minX) * (1 - x)) / 2;
        double h = ((maxY - minY) * (1 - y)) / 2;
        double d = ((maxZ - minZ) * (1 - z)) / 2;
        return new BoundingBox(minX + w, minY + h, minZ + d, maxX - w, maxY - h, maxZ - d);
    }

    public @NotNull BoundingBox translate(float x, float y, float z) {
        return new BoundingBox(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z);
    }

    public @NotNull BoundingBox translate(@NotNull Vector3d translation) {
        return translate((float) translation.x, (float) translation.y, (float) translation.z);
    }

    public @NotNull BoundingBox translate(@NotNull Vector3f vec) {
        return translate(vec.x, vec.y, vec.z);
    }

    /**
     * Returns the vertices of the corners for the specified face in counter clockwise order.
     */
    public @NotNull List<Vertex> getCornersWithUvForFace(@NotNull EnumFacing face) {
        return getCornersWithUvForFace(face, 0, 1, 0, 1);
    }

    /**
     * Returns the vertices of the corners for the specified face in counter clockwise order.
     */
    public @NotNull NNList<Vertex> getCornersWithUvForFace(@NotNull EnumFacing face, float minU, float maxU, float minV,
                                                           float maxV) {
        NNList<Vertex> result = new NNList<Vertex>();
        switch (face) {
            case NORTH:
                result.add(
                        new Vertex(new Vector3d(maxX, minY, minZ), new Vector3f(0, 0, -1), new Vector2f(minU, minV)));
                result.add(
                        new Vertex(new Vector3d(minX, minY, minZ), new Vector3f(0, 0, -1), new Vector2f(maxU, minV)));
                result.add(
                        new Vertex(new Vector3d(minX, maxY, minZ), new Vector3f(0, 0, -1), new Vector2f(maxU, maxV)));
                result.add(
                        new Vertex(new Vector3d(maxX, maxY, minZ), new Vector3f(0, 0, -1), new Vector2f(minU, maxV)));
                break;
            case SOUTH:
                result.add(new Vertex(new Vector3d(minX, minY, maxZ), new Vector3f(0, 0, 1), new Vector2f(maxU, minV)));
                result.add(new Vertex(new Vector3d(maxX, minY, maxZ), new Vector3f(0, 0, 1), new Vector2f(minU, minV)));
                result.add(new Vertex(new Vector3d(maxX, maxY, maxZ), new Vector3f(0, 0, 1), new Vector2f(minU, maxV)));
                result.add(new Vertex(new Vector3d(minX, maxY, maxZ), new Vector3f(0, 0, 1), new Vector2f(maxU, maxV)));
                break;
            case EAST:
                result.add(new Vertex(new Vector3d(maxX, maxY, minZ), new Vector3f(1, 0, 0), new Vector2f(maxU, maxV)));
                result.add(new Vertex(new Vector3d(maxX, maxY, maxZ), new Vector3f(1, 0, 0), new Vector2f(minU, maxV)));
                result.add(new Vertex(new Vector3d(maxX, minY, maxZ), new Vector3f(1, 0, 0), new Vector2f(minU, minV)));
                result.add(new Vertex(new Vector3d(maxX, minY, minZ), new Vector3f(1, 0, 0), new Vector2f(maxU, minV)));
                break;
            case WEST:
                result.add(
                        new Vertex(new Vector3d(minX, minY, minZ), new Vector3f(-1, 0, 0), new Vector2f(maxU, minV)));
                result.add(
                        new Vertex(new Vector3d(minX, minY, maxZ), new Vector3f(-1, 0, 0), new Vector2f(minU, minV)));
                result.add(
                        new Vertex(new Vector3d(minX, maxY, maxZ), new Vector3f(-1, 0, 0), new Vector2f(minU, maxV)));
                result.add(
                        new Vertex(new Vector3d(minX, maxY, minZ), new Vector3f(-1, 0, 0), new Vector2f(maxU, maxV)));
                break;
            case UP:
                result.add(new Vertex(new Vector3d(maxX, maxY, maxZ), new Vector3f(0, 1, 0), new Vector2f(minU, minV)));
                result.add(new Vertex(new Vector3d(maxX, maxY, minZ), new Vector3f(0, 1, 0), new Vector2f(minU, maxV)));
                result.add(new Vertex(new Vector3d(minX, maxY, minZ), new Vector3f(0, 1, 0), new Vector2f(maxU, maxV)));
                result.add(new Vertex(new Vector3d(minX, maxY, maxZ), new Vector3f(0, 1, 0), new Vector2f(maxU, minV)));
                break;
            case DOWN:
            default:
                result.add(
                        new Vertex(new Vector3d(minX, minY, minZ), new Vector3f(0, -1, 0), new Vector2f(maxU, maxV)));
                result.add(
                        new Vertex(new Vector3d(maxX, minY, minZ), new Vector3f(0, -1, 0), new Vector2f(minU, maxV)));
                result.add(
                        new Vertex(new Vector3d(maxX, minY, maxZ), new Vector3f(0, -1, 0), new Vector2f(minU, minV)));
                result.add(
                        new Vertex(new Vector3d(minX, minY, maxZ), new Vector3f(0, -1, 0), new Vector2f(maxU, minV)));
                break;
        }
        return result;
    }

    /**
     * Returns the vertices of the corners for the specified face in counter clockwise order, starting with the top
     * left.
     */
    public @NotNull List<Vector3f> getCornersForFace(@NotNull EnumFacing face) {
        List<Vector3f> result = new ArrayList<Vector3f>(4);
        switch (face) {
            case NORTH:
                result.add(new Vector3f(minX, maxY, minZ));
                result.add(new Vector3f(maxX, maxY, minZ));
                result.add(new Vector3f(maxX, minY, minZ));
                result.add(new Vector3f(minX, minY, minZ));
                break;
            case SOUTH:
                result.add(new Vector3f(minX, maxY, maxZ));
                result.add(new Vector3f(minX, minY, maxZ));
                result.add(new Vector3f(maxX, minY, maxZ));
                result.add(new Vector3f(maxX, maxY, maxZ));
                break;
            case EAST:
                result.add(new Vector3f(maxX, minY, maxZ));
                result.add(new Vector3f(maxX, minY, minZ));
                result.add(new Vector3f(maxX, maxY, minZ));
                result.add(new Vector3f(maxX, maxY, maxZ));
                break;
            case WEST:
                result.add(new Vector3f(minX, maxY, maxZ));
                result.add(new Vector3f(minX, maxY, minZ));
                result.add(new Vector3f(minX, minY, minZ));
                result.add(new Vector3f(minX, minY, maxZ));
                break;
            case UP:
                result.add(new Vector3f(maxX, maxY, maxZ));
                result.add(new Vector3f(maxX, maxY, minZ));
                result.add(new Vector3f(minX, maxY, minZ));
                result.add(new Vector3f(minX, maxY, maxZ));
                break;
            case DOWN:
            default:
                result.add(new Vector3f(minX, minY, maxZ));
                result.add(new Vector3f(minX, minY, minZ));
                result.add(new Vector3f(maxX, minY, minZ));
                result.add(new Vector3f(maxX, minY, maxZ));
                break;
        }
        return result;
    }

    /**
     * Returns the vertices of the corners for the specified face in counter clockwise order, starting with the top
     * left.
     */
    public @NotNull List<Vector3d> getCornersForFaceD(@NotNull EnumFacing face) {
        List<Vector3d> result = new ArrayList<Vector3d>(4);
        switch (face) {
            case NORTH:
                result.add(new Vector3d(minX, maxY, minZ));
                result.add(new Vector3d(maxX, maxY, minZ));
                result.add(new Vector3d(maxX, minY, minZ));
                result.add(new Vector3d(minX, minY, minZ));
                break;
            case SOUTH:
                result.add(new Vector3d(minX, maxY, maxZ));
                result.add(new Vector3d(minX, minY, maxZ));
                result.add(new Vector3d(maxX, minY, maxZ));
                result.add(new Vector3d(maxX, maxY, maxZ));
                break;
            case EAST:
                result.add(new Vector3d(maxX, minY, maxZ));
                result.add(new Vector3d(maxX, minY, minZ));
                result.add(new Vector3d(maxX, maxY, minZ));
                result.add(new Vector3d(maxX, maxY, maxZ));
                break;
            case WEST:
                result.add(new Vector3d(minX, maxY, maxZ));
                result.add(new Vector3d(minX, maxY, minZ));
                result.add(new Vector3d(minX, minY, minZ));
                result.add(new Vector3d(minX, minY, maxZ));
                break;
            case UP:
                result.add(new Vector3d(maxX, maxY, maxZ));
                result.add(new Vector3d(maxX, maxY, minZ));
                result.add(new Vector3d(minX, maxY, minZ));
                result.add(new Vector3d(minX, maxY, maxZ));
                break;
            case DOWN:
            default:
                result.add(new Vector3d(minX, minY, maxZ));
                result.add(new Vector3d(minX, minY, minZ));
                result.add(new Vector3d(maxX, minY, minZ));
                result.add(new Vector3d(maxX, minY, maxZ));
                break;
        }
        return result;
    }

    public @NotNull Vector3d getBBCenter() {
        return new Vector3d(minX + (maxX - minX) / 2, minY + (maxY - minY) / 2, minZ + (maxZ - minZ) / 2);
    }

    public double sizeX() {
        return Math.abs(maxX - minX);
    }

    public double sizeY() {
        return Math.abs(maxY - minY);
    }

    public double sizeZ() {
        return Math.abs(maxZ - minZ);
    }

    public @NotNull Vector3d getMin() {
        return new Vector3d(minX, minY, minZ);
    }

    public @NotNull Vector3d getMax() {
        return new Vector3d(maxX, maxY, maxZ);
    }

    public double getArea() {
        return sizeX() * sizeY() * sizeZ();
    }

    public @NotNull BoundingBox fixMinMax() {
        double mnX = minX;
        double mnY = minY;
        double mnZ = minZ;
        double mxX = maxX;
        double mxY = maxY;
        double mxZ = maxZ;
        boolean mod = false;
        if (minX > maxX) {
            mnX = maxX;
            mxX = minX;
            mod = true;
        }
        if (minY > maxY) {
            mnY = maxY;
            mxY = minY;
            mod = true;
        }
        if (minZ > maxZ) {
            mnZ = maxZ;
            mxZ = minZ;
            mod = true;
        }
        if (!mod) {
            return this;
        }
        return new BoundingBox(mnX, mnY, mnZ, mxX, mxY, mxZ);
    }

    public @NotNull BoundingBox transform(@NotNull VertexTransform vertexTransform) {
        Vector3d min = new Vector3d(minX, minY, minZ);
        Vector3d max = new Vector3d(maxX, maxY, maxZ);

        vertexTransform.apply(min);
        vertexTransform.apply(max);

        return new BoundingBox(Math.min(min.x, max.x), Math.min(min.y, max.y), Math.min(min.z, max.z),
                Math.max(min.x, max.x), Math.max(min.y, max.y),
                Math.max(min.z, max.z));
    }

    @Override
    public @NotNull BoundingBox expand(double x, double y, double z) {
        return new BoundingBox(minX - x, minY - y, minZ - z, maxX + x, maxY + y, maxZ + z);
    }

    @Override
    public @NotNull BoundingBox setMaxY(double y2) {
        return new BoundingBox(this.minX, this.minY, this.minZ, this.maxX, y2, this.maxZ);
    }

    public @NotNull BoundingBox expand(double xyz) {
        return new BoundingBox(minX - xyz, minY - xyz, minZ - xyz, maxX + xyz, maxY + xyz, maxZ + xyz);
    }

    @Override
    public @NotNull Vec3d getCenter() {
        return new Vec3d(this.minX + (this.maxX - this.minX) * 0.5D, this.minY + (this.maxY - this.minY) * 0.5D,
                this.minZ + (this.maxZ - this.minZ) * 0.5D);
    }
}
