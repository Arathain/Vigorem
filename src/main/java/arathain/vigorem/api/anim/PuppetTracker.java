package arathain.vigorem.api.anim;

import arathain.vigorem.Vigorem;
import arathain.vigorem.api.box.OrientedBox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

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
	/**
	 * This one's a bit of a mess -
	 * @param box the size of the collision box in question
	 * @param player the player we're using this on
	 * @param initialOffset the offset (normally 0.75 y, where the body starts)
	 * @param yaw player yaw
	 * @param precision how many tries should the method go through
	 * @param precisionOffset a value between 0 and 1, limiting the scope of the animation. May be useful when movement only happens at a specific point between two ticks
	 * @param rotationProviders the providers for rotation - should take in a single float value as the delta parameter, and return all of your rotations.
	 * **/
	@SafeVarargs
	public final List<Entity> getCollided(Vec3d box, PlayerEntity player, Vec3d initialOffset, float yaw, int precision, float precisionOffset, Function<Float, Vec3f>... rotationProviders) {
		List<Entity> entities = new ArrayList<>();
		OrientedBox OBB = new OrientedBox(Vec3d.ZERO, box, 0, 0, 0);
		for(int i = 1; i <= precision; i++) {
			Vec3f[] rot = new Vec3f[rotationProviders.length];
			for(int l = 0; l < rotationProviders.length; l++) {
				rot[l] = rotationProviders[l].apply((i/precision) * (1-precisionOffset) + precisionOffset);
			}
			setRotations(rot);
			Vec3d offset = getPlayerOffset(player, initialOffset, yaw);
			Vec3f rotation = rotatoProper(rot);
			OBB.offset(offset).setRotation(rotation.getX(), rotation.getY(), rotation.getZ()).updateVertex();
			entities.addAll(player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(160, 160, 160), entity -> OBB.intersects(entity.getBoundingBox()) && !entities.contains(entity)));
			OBB.resetOffset();
		}
		return entities;
	}
	/**
	 * This one's a bit of a mess -
	 * @param box the size of the collision box in question
	 * @param target the target entity - has to be an animated entity
	 * @param initialOffset the offset (normally 0.75 y, where the body starts)
	 * @param yaw entity yaw
	 * @param precision how many tries should the method go through
	 * @param precisionOffset a value between 0 and 1, limiting the scope of the animation. May be useful when movement only happens at a specific point between two ticks
	 * @param rotationProviders the providers for rotation - should take in a single float value as the delta parameter, and return all of your rotations.
	 * **/
	@SafeVarargs
	public final List<Entity> getCollided(Vec3d box, Entity target, Vec3d initialOffset, float yaw, int precision, float precisionOffset, Function<Float, Vec3f>... rotationProviders) {
		List<Entity> entities = new ArrayList<>();
		OrientedBox OBB = new OrientedBox(Vec3d.ZERO, box, 0, 0, 0);
		for(int i = 1; i <= precision; i++) {
			Vec3f[] rot = new Vec3f[rotationProviders.length];
			for(int l = 0; l < rotationProviders.length; l++) {
				rot[l] = rotationProviders[l].apply((i/precision) * (1-precisionOffset) + precisionOffset);
			}
			setRotations(rot);
			Vec3d offset = getEntityOffset(target, initialOffset, yaw);
			Vec3f rotation = rotatoProper(rot);
			OBB.offset(offset).setRotation(rotation.getX(), rotation.getY(), rotation.getZ()).updateVertex();
			entities.addAll(target.getWorld().getOtherEntities(target, target.getBoundingBox().expand(160, 160, 160), entity -> OBB.intersects(entity.getBoundingBox()) && !entities.contains(entity)));
			OBB.resetOffset();
		}
		return entities;
	}
	public Vec3d getPlayerOffset(PlayerEntity player, Vec3d initialOffset, float yaw) {
		Vec3d offset = this.getOffset(initialOffset);
		return getOffsetVector(player, offset, yaw);
	}
	public Vec3d getEntityOffset(Entity entity, Vec3d initialOffset, float yaw) {
		Vec3d offset = this.getOffset(initialOffset);
		return getOffsetVector(entity, offset, yaw);
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
	}
	public static Vec3d getOffsetVector(Entity target, Vec3d offset, float yaw) {
		return target.getPos().add(offset.multiply(1, 1, -1).rotateY((180 - yaw) * 0.017453292F));
	}
}
