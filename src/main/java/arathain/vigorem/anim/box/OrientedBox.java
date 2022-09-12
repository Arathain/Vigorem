package arathain.vigorem.anim.box;

import arathain.vigorem.mixin.ArrayVoxelShapeInvoker;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.BitSetVoxelSet;
import net.minecraft.util.shape.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Taken from <a href="https://github.com/miyo6032/MultipartEntities/blob/master/src/main/java/io/github/stuff_stuffs/multipart_entities/common/util/OrientedBox.java">...</a>
 * @author miyo6032
 **/
public final class OrientedBox extends Box {
	private final Vec3d center;
	private final Vec3d halfExtents;
	private final DoubleQuat rotation;
	private Box extents;
	private Matrix3d matrix;
	private Matrix3d inverse;
	private Vec3d[] vertices;
	private Vec3d[] basis;

	private VoxelShape cached;

	public OrientedBox(final Box box) {
		super(0, 0, 0, 0, 0, 0);
		center = box.getCenter();
		halfExtents = new Vec3d(box.getXLength() / 2, box.getYLength() / 2, box.getZLength() / 2);
		rotation = DoubleQuat.IDENTITY;
	}

	public OrientedBox(final Vec3d center, final Vec3d halfExtents, final DoubleQuat rotation) {
		super(0, 0, 0, 0, 0, 0);
		this.center = center;
		this.halfExtents = halfExtents;
		this.rotation = rotation;
	}

	public OrientedBox(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final DoubleQuat rotation) {
		super(0, 0, 0, 0, 0, 0);
		center = new Vec3d((minX + maxX) / 2, (minY + maxY) / 2, (minZ + maxZ) / 2);
		halfExtents = new Vec3d((maxX - minX) / 2, (maxY - minY) / 2, (maxZ - minZ) / 2);
		this.rotation = rotation;
	}

	private OrientedBox(final Vec3d center, final Vec3d halfExtents, final DoubleQuat rotation, final Matrix3d matrix, final Matrix3d inverse, final Vec3d[] basis) {
		super(0, 0, 0, 0, 0, 0);
		this.center = center;
		this.halfExtents = halfExtents;
		this.rotation = rotation;
		this.matrix = matrix;
		this.inverse = inverse;
		this.basis = basis;
	}

	@Override
	public Box offset(final BlockPos blockPos) {
		return offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	@Override
	public Optional<Vec3d> raycast(final Vec3d min, final Vec3d max) {
		double t = Double.MAX_VALUE;
		final double tmp = raycast1(min, max);
		if (tmp != -1) {
			t = Math.min(t, tmp);
		}
		if (t != Double.MAX_VALUE) {
			final double d = max.x - min.x;
			final double e = max.y - min.y;
			final double f = max.z - min.z;
			return Optional.of(min.add(t * d, t * e, t * f));
		}
		return Optional.empty();
	}

	@Override
	public boolean intersects(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
		return intersects(new Box(minX, minY, minZ, maxX, maxY, maxZ));
	}

	public VoxelShape toVoxelShape() {
		if (cached != null) {
			return cached;
		}
		final double minX = getMin(Direction.Axis.X) + 0.0001;
		final double minY = getMin(Direction.Axis.Y) + 0.0001;
		final double minZ = getMin(Direction.Axis.Z) + 0.0001;

		final double deltaX = getMax(Direction.Axis.X) - minX;
		final double deltaY = getMax(Direction.Axis.Y) - minY;
		final double deltaZ = getMax(Direction.Axis.Z) - minZ;
		double resolution = 4.0;
		final int xResolution = (int) Math.ceil(deltaX * resolution + 0.0001);
		final int yResolution = (int) Math.ceil(deltaY * resolution + 0.0001);
		final int zResolution = (int) Math.ceil(deltaZ * resolution + 0.0001);

		final BitSetVoxelSet bitSet = new BitSetVoxelSet(xResolution, yResolution, zResolution);
		for (int i = 0; i < xResolution; i++) {
			final double x = minX + i / resolution;
			for (int j = 0; j < zResolution; j++) {
				final double z = minZ + j / resolution;
				for (int k = 0; k < yResolution; k++) {
					final double y = minY + k / resolution;
					final Box box = new Box(x, y, z, x + 0.9999 / xResolution, y + 0.9999 / yResolution, z + 0.9999 / zResolution);
					if (intersects(box)) {
						bitSet.set(i, k, j);
					}
				}
			}
		}
		final DoubleList xPoints = new DoubleArrayList(xResolution + 1);
		for (int i = 0; i < xResolution + 1; i++) {
			xPoints.add(minX + i / resolution);
		}
		final DoubleList yPoints = new DoubleArrayList(yResolution + 1);
		for (int i = 0; i < yResolution + 1; i++) {
			yPoints.add(minY + i / resolution);
		}
		final DoubleList zPoints = new DoubleArrayList(zResolution + 1);
		for (int i = 0; i < zResolution + 1; i++) {
			zPoints.add(minZ + i / resolution);
		}
		return cached = ArrayVoxelShapeInvoker.init(bitSet, xPoints, yPoints, zPoints);
	}

	public double calculateMaxDistance(final Direction.Axis axis, final VoxelShape voxelShape, double maxDist) {
		for (final Box boundingBox : toVoxelShape().getBoundingBoxes()) {
			maxDist = voxelShape.calculateMaxDistance(axis, boundingBox, maxDist);
			if (Math.abs(maxDist) < 0.0001) {
				return 0;
			}
		}
		return maxDist;
	}

	public Matrix3d getMatrix() {
		if (matrix == null) {
			matrix = new Matrix3d(rotation);
		}
		return matrix;
	}

	public Matrix3d getInverse() {
		if (inverse == null) {
			inverse = getMatrix().invert();
		}
		return inverse;
	}

	public Box getExtents() {
		if (extents == null) {
			extents = new Box(halfExtents.multiply(-1), halfExtents);
		}
		return extents;
	}

	public Vec3d[] getBasis() {
		if (basis == null) {
			basis = matrix.getBasis();
		}
		return basis;
	}

	public OrientedBox setRotation(final DoubleQuat quat) {
		if (DoubleQuat.IDENTITY.equals(quat)) {
			return this;
		}
		return new OrientedBox(center, halfExtents, quat);
	}
	public OrientedBox setPivot(final double x, final double y, final double z, final double pivotX, final double pivotY, final double pivotZ) {
		final Vec3d vec = getMatrix().transform(x - pivotX, y - pivotY, z - pivotZ);

		return new OrientedBox(center.add(vec), halfExtents, rotation).translate(pivotX, pivotY, pivotZ);
	}

	public OrientedBox rotate(final DoubleQuat quaternion) {
		if (DoubleQuat.IDENTITY.equals(quaternion)) {
			return this;
		}
		return new OrientedBox(center, halfExtents, rotation.hamiltonProduct(quaternion));
	}

	public OrientedBox translate(final double x, final double y, final double z) {
		if (x == 0 && y == 0 && z == 0) {
			return this;
		}
		final Matrix3d matrix = getMatrix();
		final double transX = matrix.transformX(x, y, z);
		final double transY = matrix.transformY(x, y, z);
		final double transZ = matrix.transformZ(x, y, z);
		return new OrientedBox(center.add(transX, transY, transZ), halfExtents, rotation, matrix, inverse, basis);
	}

	public OrientedBox transform(final double x, final double y, final double z, final double pivotX, final double pivotY, final double pivotZ, final DoubleQuat quaternion) {
		final Vec3d vec = getMatrix().transform(x - pivotX, y - pivotY, z - pivotZ);
		final boolean bl = quaternion.equals(DoubleQuat.IDENTITY);
		return new OrientedBox(center.add(vec), halfExtents, rotation.hamiltonProduct(quaternion), bl ? matrix : null, bl ? inverse : null, bl ? basis : null).translate(pivotX, pivotY, pivotZ);
	}

	public DoubleQuat getRotation() {
		return rotation;
	}

	@Override
	public Vec3d getCenter() {
		return center;
	}

	public Vec3d getHalfExtents() {
		return halfExtents;
	}
	@Override
	public OrientedBox offset(final double x, final double y, final double z) {
		return new OrientedBox(center.add(x, y, z), halfExtents, rotation, matrix, inverse, basis);
	}

	private void computeVertices() {
		final Box box = getExtents();
		final Vec3d[] vertices = getVertices(box);
		this.vertices = new Vec3d[8];
		final Matrix3d matrix = getMatrix();
		for (int i = 0; i < vertices.length; i++) {
			this.vertices[i] = matrix.transform(vertices[i]).add(center);
		}
	}

	public static Vec3d[] getVertices(final Box box) {
		final Vec3d[] vertices = new Vec3d[8];
		int index = 0;
		final Direction.AxisDirection[] axisDirections = Direction.AxisDirection.values();
		for (final Direction.AxisDirection x : axisDirections) {
			for (final Direction.AxisDirection y : axisDirections) {
				for (final Direction.AxisDirection z : axisDirections) {
					vertices[index++] = new Vec3d(getPoint(box, x, Direction.Axis.X), getPoint(box, y, Direction.Axis.Y), getPoint(box, z, Direction.Axis.Z));
				}
			}
		}
		return vertices;
	}

	private static double getPoint(final Box box, final Direction.AxisDirection direction, final Direction.Axis axis) {
		if (direction == Direction.AxisDirection.NEGATIVE) {
			return box.getMin(axis);
		} else {
			return box.getMax(axis);
		}
	}
	@Override
	public boolean intersects(final Box other) {
		return intersects(getVertices(other));
	}
	public boolean intersects(final Vec3d[] otherVertices) {
		if (vertices == null) {
			computeVertices();
		}
		final Vec3d[] vertices1 = vertices;
		final Vec3d[] normals1 = getBasis();
		for (final Vec3d normal : normals1) {
			if (!sat(normal, vertices1, otherVertices)) {
				return false;
			}
		}
		final Vec3d[] normals2 = Matrix3d.IDENTITY_BASIS;
		for (final Vec3d normal : normals2) {
			if (!sat(normal, vertices1, otherVertices)) {
				return false;
			}
		}
		for (int i = 0; i < normals1.length; i++) {
			for (int j = i; j < normals2.length; j++) {
				final Vec3d normal = cross(normals1[i], normals2[j]);
				if (!sat(normal, vertices1, otherVertices)) {
					return false;
				}
			}
		}
		return true;
	}


	private static Vec3d cross(final Vec3d first, final Vec3d second) {
		return new Vec3d(first.y * second.z - first.z * second.y, first.z * second.x - first.x * second.z, first.x * second.y - first.y * second.x);
	}

	private static boolean sat(final Vec3d normal, final Vec3d[] vertices1, final Vec3d[] vertices2) {
		double min1 = Double.MAX_VALUE;
		double max1 = -Double.MAX_VALUE;
		for (final Vec3d d : vertices1) {
			final double v = d.dotProduct(normal);
			min1 = Math.min(min1, v);
			max1 = Math.max(max1, v);
		}
		double min2 = Double.MAX_VALUE;
		double max2 = -Double.MAX_VALUE;
		for (final Vec3d vec3d : vertices2) {
			final double v = vec3d.dotProduct(normal);
			min2 = Math.min(min2, v);
			max2 = Math.max(max2, v);
		}
		return min1 <= min2 && min2 <= max1 || min2 <= min1 && min1 <= max2;
	}

	public double raycast1(final Vec3d start, final Vec3d end) {
		final Matrix3d inverse = getInverse();
		final Vec3d d = inverse.transform(start.x - center.x, start.y - center.y, start.z - center.z);
		final Vec3d e = inverse.transform(end.x - center.x, end.y - center.y, end.z - center.z);
		return raycast0(d, e);
	}

	private double raycast0(final Vec3d start, final Vec3d end) {
		final double d = end.x - start.x;
		final double e = end.y - start.y;
		final double f = end.z - start.z;
		final double[] t = new double[]{1};
		final Direction direction = traceCollisionSide(getExtents(), start, t, d, e, f);
		if (direction != null) {
			return t[0];
		}
		return -1;
	}

	@Nullable
	private static Direction traceCollisionSide(final Box box, final Vec3d intersectingVector, final double[] traceDistanceResult, final double xDelta, final double yDelta, final double zDelta) {
		Direction approachDirection = null;
		if (xDelta > 1.0E-7D) {
			approachDirection = traceCollisionSide(traceDistanceResult, approachDirection, xDelta, yDelta, zDelta, box.minX, box.minY, box.maxY, box.minZ, box.maxZ, Direction.WEST, intersectingVector.x, intersectingVector.y, intersectingVector.z);
		} else if (xDelta < -1.0E-7D) {
			approachDirection = traceCollisionSide(traceDistanceResult, approachDirection, xDelta, yDelta, zDelta, box.maxX, box.minY, box.maxY, box.minZ, box.maxZ, Direction.EAST, intersectingVector.x, intersectingVector.y, intersectingVector.z);
		}

		if (yDelta > 1.0E-7D) {
			approachDirection = traceCollisionSide(traceDistanceResult, approachDirection, yDelta, zDelta, xDelta, box.minY, box.minZ, box.maxZ, box.minX, box.maxX, Direction.DOWN, intersectingVector.y, intersectingVector.z, intersectingVector.x);
		} else if (yDelta < -1.0E-7D) {
			approachDirection = traceCollisionSide(traceDistanceResult, approachDirection, yDelta, zDelta, xDelta, box.maxY, box.minZ, box.maxZ, box.minX, box.maxX, Direction.UP, intersectingVector.y, intersectingVector.z, intersectingVector.x);
		}

		if (zDelta > 1.0E-7D) {
			approachDirection = traceCollisionSide(traceDistanceResult, approachDirection, zDelta, xDelta, yDelta, box.minZ, box.minX, box.maxX, box.minY, box.maxY, Direction.NORTH, intersectingVector.z, intersectingVector.x, intersectingVector.y);
		} else if (zDelta < -1.0E-7D) {
			approachDirection = traceCollisionSide(traceDistanceResult, approachDirection, zDelta, xDelta, yDelta, box.maxZ, box.minX, box.maxX, box.minY, box.maxY, Direction.SOUTH, intersectingVector.z, intersectingVector.x, intersectingVector.y);
		}

		return approachDirection;
	}

	@Nullable
	private static Direction traceCollisionSide(final double[] traceDistanceResult, final Direction approachDirection, final double xDelta, final double yDelta, final double zDelta, final double begin, final double minX, final double maxX, final double minZ, final double maxZ, final Direction resultDirection, final double startX, final double startY, final double startZ) {
		final double d = (begin - startX) / xDelta;
		final double e = startY + d * yDelta;
		final double f = startZ + d * zDelta;
		if (0.0D < d && d < traceDistanceResult[0] && minX - 1.0E-7D < e && e < maxX + 1.0E-7D && minZ - 1.0E-7D < f && f < maxZ + 1.0E-7D) {
			traceDistanceResult[0] = d;
			return resultDirection;
		} else {
			return approachDirection;
		}
	}

	@Override
	public boolean contains(double x, double y, double z) {
		x -= center.x;
		y -= center.y;
		z -= center.z;
		final double transX = getMatrix().transformX(x, y, z);
		final double transY = getMatrix().transformY(x, y, z);
		final double transZ = getMatrix().transformZ(x, y, z);
		return getExtents().contains(transX, transY, transZ);
	}

	public double getMax(final Direction.Axis axis) {
		final Matrix3d matrix = getMatrix();
		switch (axis) {
			case X:
				return Math.max(matrix.m00, Math.max(matrix.m01, matrix.m02)) * halfExtents.x + center.x;
			case Y:
				return Math.max(matrix.m10, Math.max(matrix.m11, matrix.m12)) * halfExtents.y + center.y;
			case Z:
				return Math.max(matrix.m20, Math.max(matrix.m21, matrix.m22)) * halfExtents.z + center.z;
		}
		throw new NullPointerException();
	}

	public double getMin(final Direction.Axis axis) {
		final Matrix3d matrix = getMatrix();
		switch (axis) {
			case X:
				return Math.min(matrix.m00, Math.min(matrix.m01, matrix.m02)) * halfExtents.x + center.x;
			case Y:
				return Math.min(matrix.m10, Math.min(matrix.m11, matrix.m12)) * halfExtents.y + center.y;
			case Z:
				return Math.min(matrix.m20, Math.min(matrix.m21, matrix.m22)) * halfExtents.z + center.z;
		}
		throw new NullPointerException();
	}

	public OrientedBox expand(double x, double y, double z) {
		if(x==0 && y==0 && z==0) {
			return this;
		}
		return new OrientedBox(center, halfExtents.add(x/2,y/2,z/2), rotation, matrix, inverse, basis);
	}
}
