package arathain.vigorem.anim.box;

import arathain.vigorem.mixin.ArrayVoxelShapeInvoker;
import com.google.common.collect.Iterators;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.BitSetVoxelSet;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
/***
 * @author miyo6032
 * Taken from <a href="https://github.com/miyo6032/MultipartEntities/blob/master/src/main/java/io/github/stuff_stuffs/multipart_entities/common/util/CompoundOrientedBox.java">...</a>
 ***/
public final class CompoundOrientedBox extends Box implements Iterable<OrientedBox> {
	private final Collection<OrientedBox> boxes;
	private VoxelShape cached;
	private final @Nullable MutableBox overrideBox;

	public CompoundOrientedBox(final Box bounds, final Collection<OrientedBox> boxes, MutableBox overrideBox) {
		this(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ, boxes, overrideBox);
	}

	public CompoundOrientedBox(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final Collection<OrientedBox> boxes, @Nullable MutableBox overrideBox) {
		super(minX, minY, minZ, maxX, maxY, maxZ);
		this.boxes = boxes;
		this.overrideBox = overrideBox;
	}

	private CompoundOrientedBox(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final Collection<OrientedBox> boxes, final VoxelShape cached, @Nullable MutableBox overrideBox) {
		super(minX, minY, minZ, maxX, maxY, maxZ);
		this.boxes = boxes;
		this.cached = cached;
		this.overrideBox = overrideBox;
	}

	@Override
	public Box expand(final double x, final double y, final double z) {
		final List<OrientedBox> orientedBoxes = new ObjectArrayList<>(boxes.size());
		for (final OrientedBox box : boxes) {
			orientedBoxes.add(box.expand(x,y,z));
		}
		if (cached != null) {
			return new CompoundOrientedBox(minX - x, minY - y, minZ - z, maxX + x, maxY + y, maxZ + z, orientedBoxes, cached.offset(x, y, z), overrideBox);
		}
		return new CompoundOrientedBox(minX - x, minY - y, minZ - z, maxX + x, maxY + y, maxZ + z, orientedBoxes, overrideBox);
	}

	@Override
	public Box offset(final double x, final double y, final double z) {
		final List<OrientedBox> orientedBoxes = new ObjectArrayList<>(boxes.size());
		for (final OrientedBox box : boxes) {
			orientedBoxes.add(box.offset(x, y, z));
		}
		if (cached != null) {
			return new CompoundOrientedBox(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z, orientedBoxes, cached.offset(x, y, z), overrideBox);
		}
		return new CompoundOrientedBox(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z, orientedBoxes, overrideBox);
	}

	@Override
	public Box offset(final BlockPos blockPos) {
		return offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	@Override
	public Optional<Vec3d> raycast(final Vec3d min, final Vec3d max) {
		double t = Double.MAX_VALUE;
		for (final OrientedBox box : boxes) {
			final double tmp = box.raycast(min, max);
			if (tmp != -1) {
				t = Math.min(t, tmp);
			}
		}
		if (t != Double.MAX_VALUE) {
			final double d = max.x - min.x;
			final double e = max.y - min.y;
			final double f = max.z - min.z;
			return Optional.of(min.add(t * d, t * e, t * f));
		}
		return Optional.empty();
	}

	@NotNull
	@Override
	public Iterator<OrientedBox> iterator() {
		return Iterators.unmodifiableIterator(boxes.iterator());
	}

	@Override
	public boolean intersects(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
		return intersects(new Box(minX, minY, minZ, maxX, maxY, maxZ));
	}

	@Override
	public boolean intersects(final Box box) {
		final Vec3d[] vertices = OrientedBox.getVertices(box);
		for (final OrientedBox orientedBox : boxes) {
			if (orientedBox.intersects(vertices)) {
				return true;
			}
		}
		return false;
	}

	public VoxelShape toVoxelShape() {
		if (cached != null) {
			return cached;
		}
		if(overrideBox != null) {
			cached = VoxelShapes.cuboid(overrideBox.getBox());
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

	@Override
	public boolean contains(final double x, final double y, final double z) {
		for (final OrientedBox box : boxes) {
			if (box.contains(x, y, z)) {
				return true;
			}
		}
		return false;
	}

	public CompoundOrientedBox withBounds(final Box bounds) {
		return new CompoundOrientedBox(bounds, new ObjectArrayList<>(boxes), overrideBox);
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
}
