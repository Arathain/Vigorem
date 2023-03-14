package arathain.vigorem.api;

import arathain.vigorem.anim.OffsetModelPart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.List;
import java.util.Map;

public abstract class AnimationCycle {
	public final Map<String, List<Keyframe>> keyframes;
	private final int length;
	protected float progress, prevProgress;

	public AnimationCycle(Map<String, List<Keyframe>> keyframes, int length) {
		this.keyframes = keyframes;
		this.length = length;
	}

	public boolean shouldTransformHead() {
		return true;
	}
	public boolean shouldRemove(PlayerEntity p) {
		return false;
	}

	public void tick(PlayerEntity entity, MinecraftClient cli) {
		this.prevProgress = progress;
	}

	public void writeNbt(NbtCompound nbt) {
		nbt.putFloat("time", this.progress);
	}
	public void readNbt(NbtCompound nbt) {
		this.progress = nbt.getInt("time");
	}

	public float getProgress(float tickDelta) {
		if(progress > length) {
			progress = 0;
			prevProgress = 0;
		}
		if(progress < 0) {
			progress = length;
			prevProgress = length;
		}
		return MathHelper.lerp(tickDelta, prevProgress, progress);
	}
	public void tick() {
		this.prevProgress = progress;
	}
	public int getLength() {
		return length;
	}

	public void setModelAngles(PlayerEntityModel<AbstractClientPlayerEntity> model, PlayerEntity player, float tickDelta) {
		for(String part : keyframes.keySet()) {
			Keyframe lastFrame = null;
			Keyframe nextFrame = null;
			boolean bl = false;
			for(Keyframe frame : keyframes.get(part)) {
				if(frame.frame == (getProgress(tickDelta))) {
					lastFrame = frame;
					nextFrame = frame;
					bl = true;
				}
				if(lastFrame == null && frame.frame < (getProgress(tickDelta))) {
					lastFrame = frame;
				} else {
					if(lastFrame != null && frame.frame > lastFrame.frame && frame.frame < (getProgress(tickDelta))) {
						lastFrame = frame;
					}
				}
				if(nextFrame == null && frame.frame > (getProgress(tickDelta))) {
					nextFrame = frame;
				} else {
					if(nextFrame != null && frame.frame < nextFrame.frame && frame.frame > (getProgress(tickDelta))) {
						nextFrame = frame;
					}
				}
			}
			assert lastFrame != null;
			if(nextFrame == null) {
				nextFrame = lastFrame;
			}
			switch(part) {
				case "head" -> setPartAngles(model.head, lastFrame, nextFrame, tickDelta, bl);
				case "body" -> setPartAngles(model.body, lastFrame, nextFrame, tickDelta, bl);
				case "right_arm" -> setPartAngles(model.rightArm, lastFrame, nextFrame, tickDelta, bl);
				case "left_arm" -> setPartAngles(model.leftArm, lastFrame, nextFrame, tickDelta, bl);
				case "left_leg" -> setPartAngles(model.leftLeg, lastFrame, nextFrame, tickDelta, bl);
				case "right_leg" -> setPartAngles(model.rightLeg, lastFrame, nextFrame, tickDelta, bl);
				default -> {}
			}
		}
	}
	protected Vector3f getRot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vector3f(prev.rotation.x,prev.rotation.y, prev.rotation.z);
		} else {
			float percentage = (getProgress(tickDelta) - prev.frame) / (next.frame - prev.frame);
			return new Vector3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.x, next.rotation.x), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.y, next.rotation.y), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.z, next.rotation.z));
		}
	}
	protected Vector3f getPivot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vector3f(prev.translation.x, prev.translation.y, prev.translation.z);
		} else {
			float percentage = (getProgress(tickDelta) - prev.frame) / (next.frame - prev.frame);
			return new Vector3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.x, next.translation.x), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.y, next.translation.y), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.z, next.translation.z));
		}
	}
	protected Vector3f getOffset(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vector3f(prev.offset.x, prev.offset.y, prev.offset.z);
		} else {
			float percentage = (getProgress(tickDelta) - prev.frame) / (next.frame - prev.frame);
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
			float percentage = (getProgress(tickDelta) - prev.frame) / ((float) next.frame - prev.frame);
			part.setAngles(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.x + (!prev.override ? part.pitch : 0), next.rotation.x + (!next.override ? part.pitch : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.y + (!prev.override ? part.yaw : 0), next.rotation.y + (!next.override ? part.yaw : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.z + (!prev.override ? part.roll : 0), next.rotation.z + (!next.override ? part.roll : 0)));
			part.translate(new Vector3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.x, next.translation.x), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.y, next.translation.y), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.z, next.translation.z)));
			part.scaleX = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.x, next.scale.x);
			part.scaleY = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.y, next.scale.y);
			part.scaleZ = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.z, next.scale.z);
			((OffsetModelPart)(Object)part).setOffset(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.x, next.offset.x), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.y, next.offset.y), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.z, next.offset.z));
		}
	}
}
