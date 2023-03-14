package arathain.vigorem.api.anim.entity;

import arathain.vigorem.Vigorem;
import arathain.vigorem.anim.OffsetModelPart;
import arathain.vigorem.api.Keyframe;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public class EntityAnimation<T extends Entity & AnimatedEntity> {
	public final Map<String, List<Keyframe>> keyframes;
	private final int length;
	public int frame = 0;

	public EntityAnimation(int length, Map<String, List<Keyframe>> keyframes) {
		this.length = length;
		this.keyframes = keyframes;
	}

	public boolean shouldRemove() {
		return frame >= length;
	}
	public void serverTick(T entity) {}
	public void clientTick(T entity) {}

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

	public Vector3f getRot(String query, float tickDelta) {
		if(!keyframes.containsKey(query)) {
			return Vigorem.ZERO;
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
		assert lastFrame != null;
		if(nextFrame == null) {
			nextFrame = lastFrame;
		}
		return getRot(lastFrame, nextFrame, tickDelta, bl);
	}
	public Vector3f getOffset(String query, float tickDelta) {
		if(!keyframes.containsKey(query)) {
			return Vigorem.ZERO;
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
		assert lastFrame != null;
		if(nextFrame == null) {
			nextFrame = lastFrame;
		}
		return getOffset(lastFrame, nextFrame, tickDelta, bl);
	}
	public Vector3f getPivot(String query, float tickDelta) {
		if(!keyframes.containsKey(query)) {
			return Vigorem.ZERO;
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
		assert lastFrame != null;
		if(nextFrame == null) {
			nextFrame = lastFrame;
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
			assert lastFrame != null;
			if(nextFrame == null) {
				nextFrame = lastFrame;
			}
			setPartAngles(entity.getPart(part, model), lastFrame, nextFrame, tickDelta, bl);
		}
	}
	protected Vector3f getRot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vector3f(prev.rotation.x,prev.rotation.y, prev.rotation.z);
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
			return new Vector3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.x, next.rotation.x), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.y, next.rotation.y), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.z, next.rotation.z));
		}
	}
	protected Vector3f getPivot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vector3f(prev.translation.x, prev.translation.y, prev.translation.z);
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
			return new Vector3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.x, next.translation.x), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.y, next.translation.y), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.z, next.translation.z));
		}
	}
	protected Vector3f getOffset(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vector3f(prev.offset.x, prev.offset.y, prev.offset.z);
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
			return new Vector3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.x, next.offset.x), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.y, next.offset.y), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.z, next.offset.z));
		}
	}
	protected void setPartAngles(ModelPart part, Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			part.setAngles(prev.rotation.x + (!prev.override ? part.pitch : 0),prev.rotation.y + (!prev.override ? part.yaw : 0), prev.rotation.z + (!prev.override ? part.roll : 0));
			part.translate(new Vector3f(prev.translation.x, prev.translation.y, prev.translation.z));
			part.scaleX = 1 + prev.scale.x;
			part.scaleY = 1 + prev.scale.y;
			part.scaleZ = 1 + prev.scale.z;
			((OffsetModelPart)(Object)part).setOffset(prev.offset.x, prev.offset.y, prev.offset.z);
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
			part.setAngles(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.x + (!prev.override ? part.pitch : 0), next.rotation.x + (!next.override ? part.pitch : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.y + (!prev.override ? part.yaw : 0), next.rotation.y + (!next.override ? part.yaw : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.z + (!prev.override ? part.roll : 0), next.rotation.z + (!next.override ? part.roll : 0)));
			part.translate(new Vector3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.x, next.translation.x), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.y, next.translation.y), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.z, next.translation.z)));
			part.scaleX = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.x, next.scale.x);
			part.scaleY = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.y, next.scale.y);
			part.scaleZ = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.z, next.scale.z);
			((OffsetModelPart)(Object)part).setOffset(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.x, next.offset.x), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.y, next.offset.y), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.z, next.offset.z));
		}
	}
}
