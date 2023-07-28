package arathain.vigorem.api.anim;

import arathain.vigorem.Vigorem;
import arathain.vigorem.api.box.OrientedBox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PuppetTracker {
	public PuppetTracker() {

	}
	private Vec3d[] pivots;
	private Vector3f[] rotations;

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
	/**
	 * This one's a bit of a mess -
	 * @param box the size of the box in question
	 * @param player the player we're using this on
	 * @param initialOffset the offset (normally 0.75 y, where the body starts)
	 * @param yaw player yaw
	 * @param precision how many tries should the method go through
	 * @param precisionOffset a value between 0 and 1, limiting the scope of the animation. May be useful when movement only happens at a specific point between two ticks
	 * @param rotationProviders the providers for rotation - should take in a single float value as the delta parameter, and return all of your rotations.
	 * **/
	@SafeVarargs
	public final List<Entity> getCollided(Vec3d box, PlayerEntity player, Vec3d initialOffset, float yaw, int precision, float precisionOffset, Function<Float, Vector3f>... rotationProviders) {
		List<Entity> entities = new ArrayList<>();
		OrientedBox OBB = new OrientedBox(Vec3d.ZERO, box, 0, 0, 0);
		for(int i = 1; i <= precision; i++) {
			Vector3f[] rot = new Vector3f[rotationProviders.length];
			for(int l = 0; l < rotationProviders.length; l++) {
				rot[l] = rotationProviders[l].apply((i/precision) * (1-precisionOffset) + precisionOffset);
			}
			setRotations(rot);
			Vec3d offset = getPlayerOffset(player, initialOffset, yaw);
			Vector3f rotation = rotatoProper(rot);
			OBB.offset(offset).setRotation(rotation.x(), rotation.y(), rotation.z()).updateVertex();
			entities.addAll(player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(160, 160, 160), entity -> OBB.intersects(entity.getBoundingBox()) && !entities.contains(entity)));
			OBB.resetOffset();
		}
		return entities;
	}
	public Vec3d getPlayerOffset(PlayerEntity player, Vec3d initialOffset, float yaw) {
		Vec3d offset = this.getOffset(initialOffset);
		return getOffsetVector(player, offset, yaw);
	}
	public static Vec3d rotateViaQuat(Vec3d rot, Quaternionf quat) {
		Quaterniond yeah = new Quaterniond(quat);
		Vector3d vec = new Vector3d(rot.x, rot.y, rot.z);
		vec.rotate(yeah);
		return new Vec3d(vec.x(), vec.y(), vec.z());
	}
	public static Vec3d rotatoProper(Vec3d input, Vector3f... rotations) {
		Quaternionf q = null;
		for(Vector3f v : rotations) {
			if(q == null) {
				q = Axis.Z_POSITIVE.rotation(v.z());
			} else {
				q.mul(Axis.Z_POSITIVE.rotation(v.z()));
			}
			q.mul(Axis.Y_POSITIVE.rotation(v.y()));
			q.mul(Axis.X_POSITIVE.rotation(v.z()));
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
				q = Axis.Z_POSITIVE.rotation(v.z());
			} else {
				q.mul(Axis.Z_POSITIVE.rotation(v.z()));
			}
			q.mul(Axis.Y_POSITIVE.rotation(v.y()));
			q.mul(Axis.X_POSITIVE.rotation(v.z()));
			q.normalize();
		}
		assert q != null;
		q.normalize();
		return q.getEulerAnglesZYX(new Vector3f());
	}
	public static Vec3d getOffsetVector(PlayerEntity player, Vec3d offset, float yaw) {
		return player.getPos().add(offset.multiply(1, 1, -1).multiply(0.9375F).rotateY((180 - yaw) * 0.017453292F).multiply(Vigorem.getPlayerScale(player, 1)));
	};
}
