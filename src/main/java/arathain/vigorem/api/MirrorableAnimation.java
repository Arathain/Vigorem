package arathain.vigorem.api;

import arathain.vigorem.anim.Animation;
import arathain.vigorem.anim.Keyframe;
import arathain.vigorem.anim.OffsetModelPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.List;
import java.util.Map;

public class MirrorableAnimation extends Animation {
	protected final boolean mirrored;
	public MirrorableAnimation(int length, Map<String, List<Keyframe>> keyframes, boolean mir) {
		super(length, keyframes);
		this.mirrored = mir;
	}
	@Override
	public void setModelAngles(PlayerEntityModel<AbstractClientPlayerEntity> model, PlayerEntity player, float tickDelta) {
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
			switch(part) {
				case "head" -> {
					setPartAngles(model.head, lastFrame, nextFrame, tickDelta, bl);
				}
				case "body" -> {
					setPartAngles(model.body, lastFrame, nextFrame, tickDelta, bl);
				}
				case "left_arm" -> {
					setPartAngles(mirrored ? model.rightArm : model.leftArm, lastFrame, nextFrame, tickDelta, bl);
				}
				case "right_arm" -> {
					setPartAngles(mirrored ? model.leftArm : model.rightArm, lastFrame, nextFrame, tickDelta, bl);
				}
				case "right_leg" -> {
					setPartAngles(mirrored ? model.leftLeg : model.rightLeg, lastFrame, nextFrame, tickDelta, bl);
				}
				case "left_leg" -> {
					setPartAngles(mirrored ? model.rightLeg : model.leftLeg, lastFrame, nextFrame, tickDelta, bl);
				}
				default -> {}
			}
		}
	}
	public Vec3f getRot(String query, float tickDelta) {
		if(mirrored) {
			switch (query) {
				case "left_arm" -> query = "right_arm";
				case "right_arm" -> query = "left_arm";
				case "right_leg" -> query = "left_leg";
				case "left_leg" -> query = "right_leg";
				case "left_hand" -> query = "right_hand";
				case "right_hand" -> query = "left_hand";
				default -> {}
			}
		}
		return super.getRot(query, tickDelta);
	}
	public Vec3f getPivot(String query, float tickDelta) {
		if(mirrored) {
			switch (query) {
				case "left_arm" -> query = "right_arm";
				case "right_arm" -> query = "left_arm";
				case "right_leg" -> query = "left_leg";
				case "left_leg" -> query = "right_leg";
				case "left_hand" -> query = "right_hand";
				case "right_hand" -> query = "left_hand";
				default -> {}
			}
		}
		return super.getPivot(query, tickDelta);
	}

	@Override
	protected void setPartAngles(ModelPart part, Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(this.mirrored) {
			prev = new Keyframe(prev.easing, prev.translation, new Vec3f(prev.rotation.getX(), -prev.rotation.getY(), -prev.rotation.getZ()), prev.scale, prev.offset, prev.frame, prev.override);
			next = new Keyframe(next.easing, next.translation, new Vec3f(next.rotation.getX(), -next.rotation.getY(), -next.rotation.getZ()), next.scale, next.offset, next.frame, next.override);
		}
		if(same) {
			part.setAngles(prev.rotation.getX() + (!prev.override ? part.pitch : 0),prev.rotation.getY() + (!prev.override ? part.yaw : 0), prev.rotation.getZ() + (!prev.override ? part.roll : 0));
			part.setPivot(part.pivotX + prev.translation.getX(), part.pivotY + prev.translation.getY(), part.pivotZ + prev.translation.getZ());
			part.scaleX = 1 + prev.scale.getX();
			part.scaleY = 1 + prev.scale.getY();
			part.scaleZ = 1 + prev.scale.getZ();
			((OffsetModelPart)(Object)part).setOffset(prev.offset.getX(), prev.offset.getY(), prev.offset.getZ());
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
			part.setAngles(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getX() + (!prev.override ? part.pitch : 0), next.rotation.getX() + (!next.override ? part.pitch : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getY() + (!prev.override ? part.yaw : 0), next.rotation.getY() + (!next.override ? part.yaw : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getZ() + (!prev.override ? part.roll : 0), next.rotation.getZ() + (!next.override ? part.roll : 0)));
			part.setPivot(part.pivotX + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getX(), next.translation.getX()), part.pivotY + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getY(), next.translation.getY()), part.pivotZ + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getZ(), next.translation.getZ()));
			part.scaleX = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getX(), next.scale.getX());
			part.scaleY = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getY(), next.scale.getY());
			part.scaleZ = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getZ(), next.scale.getZ());
			((OffsetModelPart)(Object)part).setOffset(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getX(), next.offset.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getY(), next.offset.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getZ(), next.offset.getZ()));
		}
		if(this.mirrored) {
			((OffsetModelPart)(Object)part).setOffset(-((OffsetModelPart)(Object)part).getOffsetX(), ((OffsetModelPart)(Object)part).getOffsetY(), -((OffsetModelPart)(Object)part).getOffsetZ());
		}
	}
	protected Vec3f getRot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		Vec3f soup = super.getRot(prev, next, tickDelta, same);
		if(mirrored) {
			soup = new Vec3f(soup.getX(), -soup.getY(), -soup.getZ());
		}
		return soup;
	}
	protected Vec3f getPivot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		Vec3f soup = super.getPivot(prev, next, tickDelta, same);
		if(mirrored) {
			soup = new Vec3f(-soup.getX(), soup.getY(), -soup.getZ());
		}
		return soup;
	}
}
