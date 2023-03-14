package arathain.vigorem.api.anim;

import arathain.vigorem.anim.OffsetModelPart;
import arathain.vigorem.api.Keyframe;
import arathain.vigorem.api.anim.Animation;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

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
	public Vector3f getRot(String query, float tickDelta) {
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
	public Vector3f getPivot(String query, float tickDelta) {
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
	public Vector3f getOffset(String query, float tickDelta) {
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
		return super.getOffset(query, tickDelta);
	}

	@Override
	protected void setPartAngles(ModelPart part, Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(this.mirrored) {
			prev = new Keyframe(prev.easing, prev.translation, new Vector3f(prev.rotation.x, -prev.rotation.y, -prev.rotation.z), prev.scale, prev.offset, prev.frame, prev.override);
			next = new Keyframe(next.easing, next.translation, new Vector3f(next.rotation.x, -next.rotation.y, -next.rotation.z), next.scale, next.offset, next.frame, next.override);
		}
		if(same) {
			part.setAngles(prev.rotation.x + (!prev.override ? part.pitch : 0),prev.rotation.y + (!prev.override ? part.yaw : 0), prev.rotation.z + (!prev.override ? part.roll : 0));
			part.setPivot(part.pivotX + prev.translation.x, part.pivotY + prev.translation.y, part.pivotZ + prev.translation.z);
			part.scaleX = 1 + prev.scale.x;
			part.scaleY = 1 + prev.scale.y;
			part.scaleZ = 1 + prev.scale.z;
			((OffsetModelPart)(Object)part).setOffset(prev.offset.x, prev.offset.y, prev.offset.z);
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
			part.setAngles(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.x + (!prev.override ? part.pitch : 0), next.rotation.x + (!next.override ? part.pitch : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.y + (!prev.override ? part.yaw : 0), next.rotation.y + (!next.override ? part.yaw : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.z + (!prev.override ? part.roll : 0), next.rotation.z + (!next.override ? part.roll : 0)));
			part.setPivot(part.pivotX + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.x, next.translation.x), part.pivotY + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.y, next.translation.y), part.pivotZ + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.z, next.translation.z));
			part.scaleX = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.x, next.scale.x);
			part.scaleY = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.y, next.scale.y);
			part.scaleZ = 1 + MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.z, next.scale.z);
			((OffsetModelPart)(Object)part).setOffset(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.x, next.offset.x), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.y, next.offset.y), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.z, next.offset.z));
		}
		if(this.mirrored) {
			((OffsetModelPart)(Object)part).setOffset(-((OffsetModelPart)(Object)part).getOffsetX(), ((OffsetModelPart)(Object)part).getOffsetY(), -((OffsetModelPart)(Object)part).getOffsetZ());
		}
	}
	protected Vector3f getRot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		Vector3f soup = super.getRot(prev, next, tickDelta, same);
		if(mirrored) {
			soup = new Vector3f(soup.x, -soup.y, -soup.z);
		}
		return soup;
	}
	protected Vector3f getPivot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		Vector3f soup = super.getPivot(prev, next, tickDelta, same);
		if(mirrored) {
			soup = new Vector3f(-soup.x, soup.y, -soup.z);
		}
		return soup;
	}
	protected Vector3f getOffset(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		Vector3f soup = super.getOffset(prev, next, tickDelta, same);
		if(mirrored) {
			soup = new Vector3f(-soup.x, soup.y, -soup.z);
		}
		return soup;
	}
}
