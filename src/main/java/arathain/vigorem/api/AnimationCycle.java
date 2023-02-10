package arathain.vigorem.api;

import arathain.vigorem.anim.OffsetModelPart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
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
	protected Vec3f getRot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vec3f(prev.rotation.getX(),prev.rotation.getY(), prev.rotation.getZ());
		} else {
			float percentage = (getProgress(tickDelta) - prev.frame) / (next.frame - prev.frame);
			return new Vec3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getX(), next.rotation.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getY(), next.rotation.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getZ(), next.rotation.getZ()));
		}
	}
	protected Vec3f getPivot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vec3f(prev.translation.getX(), prev.translation.getY(), prev.translation.getZ());
		} else {
			float percentage = (getProgress(tickDelta) - prev.frame) / (next.frame - prev.frame);
			return new Vec3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getX(), next.translation.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getY(), next.translation.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getZ(), next.translation.getZ()));
		}
	}
	protected Vec3f getOffset(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vec3f(prev.offset.getX(), prev.offset.getY(), prev.offset.getZ());
		} else {
			float percentage = (getProgress(tickDelta) - prev.frame) / (next.frame - prev.frame);
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
			float percentage = (getProgress(tickDelta) - prev.frame) / ((float) next.frame - prev.frame);
			part.setAngles(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getX() + (!prev.override ? part.pitch : 0), next.rotation.getX() + (!next.override ? part.pitch : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getY() + (!prev.override ? part.yaw : 0), next.rotation.getY() + (!next.override ? part.yaw : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getZ() + (!prev.override ? part.roll : 0), next.rotation.getZ() + (!next.override ? part.roll : 0)));
			part.translate(new Vec3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getX(), next.translation.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getY(), next.translation.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getZ(), next.translation.getZ())));
			part.scaleX = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getX(), next.scale.getX());
			part.scaleY = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getY(), next.scale.getY());
			part.scaleZ = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getZ(), next.scale.getZ());
			((OffsetModelPart)(Object)part).setOffset(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getX(), next.offset.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getY(), next.offset.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getZ(), next.offset.getZ()));
		}
	}
}
