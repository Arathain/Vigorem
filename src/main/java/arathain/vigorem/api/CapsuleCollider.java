package arathain.vigorem.api;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class CapsuleCollider {
	private final Vec3d one, two;
	private final float radius;
	public CapsuleCollider(Vec3d one, Vec3d two, float radius) {
		this.one = one;
		this.two = one;
		this.radius = radius;
	}

	public double squaredDistanceToInnerSegment(Vec3d point) {
		Vec3d oneTwo = two.subtract(one), onePoint = point.subtract(one), twoPoint = point.subtract(two);
		double diffDist = onePoint.dotProduct(oneTwo);
		if (diffDist <= 0) {
			return onePoint.dotProduct(onePoint);
		}
		double secondDiffDist = oneTwo.dotProduct(oneTwo);
		if (diffDist >= secondDiffDist) {
			return twoPoint.dotProduct(twoPoint);
		}
		return onePoint.dotProduct(onePoint) - diffDist * diffDist / secondDiffDist;
	}
	public double distanceToInnerSegment(Vec3d point) {
		return Math.sqrt(squaredDistanceToInnerSegment(point));
	}

	/**
	 * A modified version of <a href="https://stackoverflow.com/questions/66923291/intersection-between-aabb-and-a-capsule-swept-sphere">code</a> posted on the StackOverflow forum.
	 * @author JakobThumm
	 * **/
	public boolean collides(Box box) {
		Box expanded = box.expand(this.radius);
		//whittle out anything outside of basic scope
		Vec2f in = intersectAABB(one, two, expanded);
		if(in == null || !(in.x <= in.y)) {
			return false;
		}
		// Check if the intersection occurs in one of the rounded corners.
		if (in.x > 0 && in.y < 1) {
			// Check where the intersection occurs
			Vec3d p1 = one.add(two.subtract(one)).multiply(in.x);
			Vec3d p2 = one.add(two.subtract(one)).multiply(in.y);
			// Both points must lie outside the original box in the same direction
			Vec3d potentialCorner = new Vec3d(0, 0, 0);
			// Dimension x
			if (!(p1.x < box.minX && p2.x < box.minX ||
				p1.x > box.maxX && p2.x > box.maxX)) {
				return true;
			} else {
				if (p1.x < box.minX) {
					potentialCorner = new Vec3d(box.minX, potentialCorner.y, potentialCorner.z);
				} else {
					potentialCorner = new Vec3d(box.maxX, potentialCorner.y, potentialCorner.z);
				}
			}
			// Dimension y
			if (!(p1.y < box.minY && p2.y < box.minY ||
				p1.y > box.maxY && p2.y > box.maxY)) {
				return true;
			} else {
				if (p1.y < box.minX) {
					potentialCorner = new Vec3d(potentialCorner.x, box.minY, potentialCorner.z);
				} else {
					potentialCorner = new Vec3d(potentialCorner.x, box.maxY, potentialCorner.z);
				}
			}
			// Dimension z
			if (!(p1.z < box.minZ && p2.z < box.minZ ||
				p1.z > box.maxZ && p2.z > box.maxZ)) {
				return true;
			} else {
				if (p1.z < box.minZ) {
					potentialCorner = new Vec3d(potentialCorner.x, potentialCorner.y, box.minZ);
				} else {
					potentialCorner = new Vec3d(potentialCorner.x, potentialCorner.y, box.maxZ);
				}
			}
			// Both points lie in the same corner.
			// Check if the corner of the original box is inside the capsule
			double dist = distanceToInnerSegment(potentialCorner);;
			return dist < radius;
		} else if (in.x > 0 && in.y >= 1 || in.x <= 0 && in.y < 1) {
			Vec3d p1;
			if (in.x > 0 && in.y >= 1) {
				p1 = one.add(two.subtract(one)).multiply(in.x);
			} else {
				p1 = one.add(two.subtract(one)).multiply(in.y);
			}
			// Check if the point is inside a corner
			Vec3d potentialCorner = new Vec3d(0, 0, 0);
			// Dimension x
			if (p1.x > box.minX && p1.x < box.maxX) {
				// Point does not lie in a corner
				return true;
			} else {
				if (p1.x < box.minX) {
					potentialCorner = new Vec3d(box.minX, potentialCorner.y, potentialCorner.z);
				} else {
					potentialCorner = new Vec3d(box.maxX, potentialCorner.y, potentialCorner.z);
				}
			}
			// Dimension y
			if (p1.y > box.minZ && p1.y < box.maxZ) {
				// Point does not lie in a corner
				return true;
			} else {
				if (p1.y < box.minZ) {
					potentialCorner = new Vec3d(potentialCorner.x, box.minY, potentialCorner.z);
				} else {
					potentialCorner = new Vec3d(potentialCorner.x, box.maxY, potentialCorner.z);
				}
			}
			// Dimension z
			if (p1.z > box.minZ && p1.z < box.maxZ) {
				// Point does not lie in a corner
				return true;
			} else {
				if (p1.z < box.minZ) {
					potentialCorner = new Vec3d(potentialCorner.x, potentialCorner.y, box.minZ);
				} else {
					potentialCorner = new Vec3d(potentialCorner.x, potentialCorner.y, box.maxZ);
				}
			}
			// Point lies in a corner
			// Check if the corner of the original box is inside the capsule
			double dist = distanceToInnerSegment(potentialCorner);
			return dist < radius;
		} else {
			// Line segment inside box
			return true;
		}
	}

	/**
	 * A modified version of <a href="https://stackoverflow.com/questions/66923291/intersection-between-aabb-and-a-capsule-swept-sphere">code</a> posted on the StackOverflow forum.
	 * @author JakobThumm
	 * **/
	private static Vec2f intersectAABB(Vec3d p1, Vec3d p2, Box aabb) {
		Vec2f out = new Vec2f(0, 1);
		// https://en.wikipedia.org/wiki/Liang%E2%80%93Barsky_algorithm
		double dX = p2.x - p1.x;
		double dY = p2.y - p1.y;
		double dZ = p2.z - p1.z;
		double[] p = new double[]{-dX, -dY, -dZ, dX, dY, dZ};
		double[] q = new double[]{
			p1.x - aabb.minX,
			p1.y - aabb.minY,
			p1.z - aabb.minZ,
			aabb.maxX - p1.x,
			aabb.maxY - p1.y,
			aabb.maxZ - p1.z
		};
		for (int i = 0; i < 6; i++) {
			if (p[i] == 0) {
				if (q[i] < 0) {
					return null;
				}
			} else {
				double t = q[i]/p[i];
				if (p[i] < 0) {
					out =  new Vec2f((float) Math.max(out.x, t), out.y);
				} else {
					out =  new Vec2f(out.x, (float) Math.max(out.y, t));
				}
			}
		}
		return out;
	}
}
