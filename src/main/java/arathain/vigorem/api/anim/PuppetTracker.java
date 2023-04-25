package arathain.vigorem.api.anim;

import arathain.vigorem.Vigorem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.List;

public class PuppetTracker {
	public PuppetTracker() {

	}
	private Vec3d[] pivots;
	private Vec3f[] rotations;

	public PuppetTracker setPivots(Vec3d... pivots) {
		this.pivots = pivots;
		return this;
	}
	public PuppetTracker setRotations(Vec3f... rot) {
		this.rotations = rot;
		return this;
	}
	public Vec3d getOffset(Vec3d initialOffset) {
		if(pivots.length != rotations.length) {
			throw new RuntimeException("Rotation and pivot amounts aren't equal! Get your pivots & rotations in order! Repent!! Dismissed!!!");
		}
		for(int i = 0; i < pivots.length; i++) {
			Vec3f[] rot = new Vec3f[i+1];
			System.arraycopy(rotations, 0, rot, 0, i + 1);
			initialOffset = initialOffset.add(rotatoProper(pivots[i], rot));
		}
		return initialOffset;
	}
	public Vec3d getPlayerOffset(PlayerEntity player, Vec3d initialOffset, float yaw) {
		Vec3d offset = this.getOffset(initialOffset);
		return getOffsetVector(player, offset, yaw);
	}
	public static Vec3d rotateViaQuat(Vec3d rot, Quaternion quat) {
		Quaternion q = quat.copy();
		Quaternion qPrime = new Quaternion(-q.getX(), -q.getY(), -q.getZ(), q.getW());
		q.hamiltonProduct(new Quaternion((float)rot.getX(), (float)rot.getY(), (float)rot.getZ(), 0));
		q.hamiltonProduct(qPrime);
		return new Vec3d(q.getX(), q.getY(), q.getZ());
	}
	public static Vec3d rotatoProper(Vec3d input, Vec3f... rotations) {
		Quaternion q = null;
		for(Vec3f v : rotations) {
			if(q == null) {
				q = Vec3f.POSITIVE_Z.getRadialQuaternion(v.getZ());
			} else {
				q.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(v.getZ()));
			}
			q.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion(v.getY()));
			q.hamiltonProduct(Vec3f.POSITIVE_X.getRadialQuaternion(v.getX()));
			q.normalize();
		}
		assert q != null;
		q.normalize();
		input = rotateViaQuat(input, q);
		return input;
	}
	public static Vec3f rotatoProper(Vec3f... rotations) {
		Quaternion q = null;
		for(Vec3f v : rotations) {
			if(q == null) {
				q = Vec3f.POSITIVE_Z.getRadialQuaternion(v.getZ());
			} else {
				q.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(v.getZ()));
			}
			q.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion(v.getY()));
			q.hamiltonProduct(Vec3f.POSITIVE_X.getRadialQuaternion(v.getX()));
			q.normalize();
		}
		assert q != null;
		q.normalize();
		return q.toEulerXyz();
	}
	public static Vec3d getOffsetVector(PlayerEntity player, Vec3d offset, float yaw) {
		return player.getPos().add(offset.multiply(1, 1, -1).multiply(0.9375F).rotateY((180 - yaw) * 0.017453292F).multiply(Vigorem.getPlayerScale(player, 1)));
	};
}
