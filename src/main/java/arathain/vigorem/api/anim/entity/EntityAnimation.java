package arathain.vigorem.api.anim.entity;

import arathain.vigorem.Vigorem;
import arathain.vigorem.anim.EntityQuery;
import arathain.vigorem.anim.OffsetModelPart;
import arathain.vigorem.api.Keyframe;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.List;
import java.util.Map;

public class EntityAnimation<T extends Entity & AnimatedEntity> {
	public final Map<String, List<Keyframe>> keyframes;
	private final int length;
	public int frame = 0;

	protected final EntityQuery entityQuery = new EntityQuery();

	public EntityAnimation(int length, Map<String, List<Keyframe>> keyframes) {
		this.length = length;
		this.keyframes = keyframes;
	}

	public boolean shouldRemove() {
		return frame >= length;
	}
	public void serverTick(T entity) {entityQuery.update(entity, this.frame, entity.getWorld());}
	public void clientTick(T entity) {entityQuery.update(entity, this.frame, entity.getWorld());}

	public void tick() {
		this.frame++;
	}

	public int getLength() {
		return length;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public boolean canInterrupt() {
		return false;
	}
	public boolean canCancel() {
		return false;
	}
	public Identifier getId() {
		return Vigorem.id("balls");
	}

	public float getMovementMultiplier() {
		return 1;
	}
	public boolean isAffectingGravity() {
		return false;
	}
	public boolean isBlockingMovement() {
		return false;
	}
	public boolean lockHeldItem(){
		return false;
	}

	public Vec3f getRot(String query, float tickDelta) {
		if(!keyframes.containsKey(query)) {
			return Vec3f.ZERO;
		}
		Keyframe lastFrame = null;
		Keyframe nextFrame = null;
		boolean bl = false;
		for(Keyframe frame : keyframes.get(query)) {
			if(frame.frame == (this.frame + tickDelta)) {
				lastFrame = frame;
				nextFrame = frame;
				bl = true;
			}
			if(lastFrame == null && frame.frame < (this.frame + tickDelta)) {
				lastFrame = frame;
			} else {
				if(lastFrame != null && frame.frame > lastFrame.frame && frame.frame < (this.frame + tickDelta)) {
					lastFrame = frame;
				}
			}
			if(nextFrame == null && frame.frame > (this.frame + tickDelta)) {
				nextFrame = frame;
			} else {
				if(nextFrame != null && frame.frame < nextFrame.frame && frame.frame > (this.frame + tickDelta)) {
					nextFrame = frame;
				}
			}
		}
		if(lastFrame == null) {
			lastFrame = nextFrame;
			bl = true;
		}
		if(nextFrame == null) {
			nextFrame = lastFrame;
			bl = true;
		}
		return getRot(lastFrame, nextFrame, tickDelta, bl);
	}
	public Vec3f getOffset(String query, float tickDelta) {
		if(!keyframes.containsKey(query)) {
			return Vec3f.ZERO;
		}
		Keyframe lastFrame = null;
		Keyframe nextFrame = null;
		boolean bl = false;
		for(Keyframe frame : keyframes.get(query)) {
			if(frame.frame == (this.frame + tickDelta)) {
				lastFrame = frame;
				nextFrame = frame;
				bl = true;
			}
			if(lastFrame == null && frame.frame < (this.frame + tickDelta)) {
				lastFrame = frame;
			} else {
				if(lastFrame != null && frame.frame > lastFrame.frame && frame.frame < (this.frame + tickDelta)) {
					lastFrame = frame;
				}
			}
			if(nextFrame == null && frame.frame > (this.frame + tickDelta)) {
				nextFrame = frame;
			} else {
				if(nextFrame != null && frame.frame < nextFrame.frame && frame.frame > (this.frame + tickDelta)) {
					nextFrame = frame;
				}
			}
		}
		if(lastFrame == null) {
			lastFrame = nextFrame;
			bl = true;
		}
		if(nextFrame == null) {
			nextFrame = lastFrame;
			bl = true;
		}
		return getOffset(lastFrame, nextFrame, tickDelta, bl);
	}
	public Vec3f getPivot(String query, float tickDelta) {
		if(!keyframes.containsKey(query)) {
			return Vec3f.ZERO;
		}
		Keyframe lastFrame = null;
		Keyframe nextFrame = null;
		boolean bl = false;
		for(Keyframe frame : keyframes.get(query)) {
			if(frame.frame == (this.frame + tickDelta)) {
				lastFrame = frame;
				nextFrame = frame;
				bl = true;
			}
			if(lastFrame == null && frame.frame < (this.frame + tickDelta)) {
				lastFrame = frame;
			} else {
				if(lastFrame != null && frame.frame > lastFrame.frame && frame.frame < (this.frame + tickDelta)) {
					lastFrame = frame;
				}
			}
			if(nextFrame == null && frame.frame > (this.frame + tickDelta)) {
				nextFrame = frame;
			} else {
				if(nextFrame != null && frame.frame < nextFrame.frame && frame.frame > (this.frame + tickDelta)) {
					nextFrame = frame;
				}
			}
		}
		if(lastFrame == null) {
			lastFrame = nextFrame;
			bl = true;
		}
		if(nextFrame == null) {
			nextFrame = lastFrame;
			bl = true;
		}
		return getPivot(lastFrame, nextFrame, tickDelta, bl);
	}
	public void writeNbt(NbtCompound nbt) {
		nbt.putInt("time", this.frame);
	}
	public void readNbt(NbtCompound nbt) {
		this.frame = nbt.getInt("time");
	}

	public void setModelAngles(EntityModel<T> model, T entity, float tickDelta) {
		entityQuery.updateTime(this.frame + tickDelta);
		for(String part : keyframes.keySet()) {
			Keyframe lastFrame = null;
			Keyframe nextFrame = null;
			boolean bl = false;
			for(Keyframe frame : keyframes.get(part)) {
				if(frame.frame == (this.frame + tickDelta)) {
					lastFrame = frame;
					nextFrame = frame;
					bl = true;
				}
				if(lastFrame == null && frame.frame < (this.frame + tickDelta)) {
					lastFrame = frame;
				} else {
					if(lastFrame != null && frame.frame > lastFrame.frame && frame.frame < (this.frame + tickDelta)) {
						lastFrame = frame;
					}
				}
				if(nextFrame == null && frame.frame > (this.frame + tickDelta)) {
					nextFrame = frame;
				} else {
					if(nextFrame != null && frame.frame < nextFrame.frame && frame.frame > (this.frame + tickDelta)) {
						nextFrame = frame;
					}
				}
			}
			if(lastFrame == null) {
				lastFrame = nextFrame;
				bl = true;
			}
			if(nextFrame == null) {
				nextFrame = lastFrame;
				bl = true;
			}
			lastFrame.update(entityQuery);
			nextFrame.update(entityQuery);
			setPartAngles(entity.getPart(part, model), lastFrame, nextFrame, tickDelta, bl);
		}
	}
	protected Vec3f getRot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vec3f(prev.rotation.getX(),prev.rotation.getY(), prev.rotation.getZ());
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
			return new Vec3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getX(), next.rotation.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getY(), next.rotation.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getZ(), next.rotation.getZ()));
		}
	}
	protected Vec3f getPivot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vec3f(prev.translation.getX(), prev.translation.getY(), prev.translation.getZ());
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
			return new Vec3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getX(), next.translation.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getY(), next.translation.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getZ(), next.translation.getZ()));
		}
	}
	protected Vec3f getOffset(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vec3f(prev.offset.getX(), prev.offset.getY(), prev.offset.getZ());
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
			return new Vec3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getX(), next.offset.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getY(), next.offset.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getZ(), next.offset.getZ()));
		}
	}
	protected void setPartAngles(ModelPart part, Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			part.setAngles(prev.rotation.getX() + (!prev.override ? part.pitch : 0),prev.rotation.getY() + (!prev.override ? part.yaw : 0), prev.rotation.getZ() + (!prev.override ? part.roll : 0));
			part.translate(new Vec3f(prev.translation.getX(), prev.translation.getY(), prev.translation.getZ()));
			part.scaleX = 1 + prev.scale.getX();
			part.scaleY = 1 + prev.scale.getY();
			part.scaleZ = 1 + prev.scale.getZ();
			((OffsetModelPart)(Object)part).setOffset(prev.offset.getX(), prev.offset.getY(), prev.offset.getZ());
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
			part.setAngles(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getX() + (!prev.override ? part.pitch : 0), next.rotation.getX() + (!next.override ? part.pitch : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getY() + (!prev.override ? part.yaw : 0), next.rotation.getY() + (!next.override ? part.yaw : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getZ() + (!prev.override ? part.roll : 0), next.rotation.getZ() + (!next.override ? part.roll : 0)));
			part.translate(new Vec3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getX(), next.translation.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getY(), next.translation.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getZ(), next.translation.getZ())));
			part.scaleX = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getX(), next.scale.getX());
			part.scaleY = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getY(), next.scale.getY());
			part.scaleZ = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getZ(), next.scale.getZ());
			((OffsetModelPart)(Object)part).setOffset(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getX(), next.offset.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getY(), next.offset.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getZ(), next.offset.getZ()));
		}
	}
}
