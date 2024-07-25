package arathain.vigorem.api.anim;

import arathain.vigorem.Vigorem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PuppetTracker {
	public PuppetTracker() {

	}
	protected Vec3d[] pivots;
	protected Vector3f[] rotations;

	public PuppetTracker setPivots(Vec3d... pivots) {
		this.pivots = pivots;
		return this;
	}
	public PuppetTracker setRotations(Vector3f... rot) {
		this.rotations = rot;
		return this;
	}
	public Vec3d getOffset(Vec3d initialOffset) {
		if(pivots.length != rotations.length) {
			throw new RuntimeException("Rotation and pivot amounts aren't equal! Get your pivots & rotations in order! Repent!! Dismissed!!!");
		}
		for(int i = 0; i < pivots.length; i++) {
			Vector3f[] rot = new Vector3f[i+1];
			System.arraycopy(rotations, 0, rot, 0, i + 1);
			initialOffset = initialOffset.add(rotatoProper(pivots[i], rot));
		}
		return initialOffset;
	}

	public Vec3d getPlayerOffset(PlayerEntity player, Vec3d initialOffset, float yaw) {
		Vec3d offset = this.getOffset(initialOffset);
		return getOffsetVector(player, offset, yaw);
	}
	public Vec3d getEntityOffset(Entity entity, Vec3d initialOffset, float yaw) {
		Vec3d offset = this.getOffset(initialOffset);
		return getOffsetVector(entity, offset, yaw);
	}
	public static Vec3d rotateViaQuat(Vec3d rot, Quaternionf quat) {
		Quaternionf q = new Quaternionf(quat);
		Quaternionf qPrime = new Quaternionf(-q.x(), -q.y(), -q.z(), q.w());
		q.mul(new Quaternionf((float)rot.getX(), (float)rot.getY(), (float)rot.getZ(), 0));
		q.mul(qPrime);
		return new Vec3d(q.x(), q.y(), q.z());
	}
	public static Vec3d rotatoProper(Vec3d input, Vector3f... rotations) {
		Quaternionf q = null;
		for(Vector3f v : rotations) {
			if(q == null) {
				q = RotationAxis.POSITIVE_Z.rotation(v.z());
			} else {
				q.mul(RotationAxis.POSITIVE_Z.rotation(v.z()));
			}
			q.mul(RotationAxis.POSITIVE_Y.rotation(v.y()));
			q.mul(RotationAxis.POSITIVE_X.rotation(v.x()));
			q.normalize();
		}
		assert q != null;
		q.normalize();
		input = rotateViaQuat(input, q);
		return input;
	}
	public static Vector3f rotatoProper(Vector3f... rotations) {
		Quaternionf q = null;
		for(Vector3f v : rotations) {
			if(q == null) {
				q = RotationAxis.POSITIVE_Z.rotation(v.z());
			} else {
				q.mul(RotationAxis.POSITIVE_Z.rotation(v.z()));
			}
			q.mul(RotationAxis.POSITIVE_Y.rotation(v.y()));
			q.mul(RotationAxis.POSITIVE_X.rotation(v.x()));
			q.normalize();
		}
		assert q != null;
		q.normalize();
		return q.getEulerAnglesXYZ(new Vector3f());
	}
	public static Vec3d getOffsetVector(PlayerEntity player, Vec3d offset, float yaw) {
		return player.getPos().add(offset.multiply(1, 1, -1).multiply(0.9375F).rotateY((180 - yaw) * 0.017453292F).multiply(Vigorem.getPlayerScale(player, 1)));
	}
	public static Vec3d getOffsetVector(Entity target, Vec3d offset, float yaw) {
		return target.getPos().add(offset.multiply(1, 1, -1).rotateY((180 - yaw) * 0.017453292F));
	}
}
