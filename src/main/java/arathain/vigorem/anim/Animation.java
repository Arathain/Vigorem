package arathain.vigorem.anim;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Map;

public class Animation {
	public final Map<String, List<Keyframe>> keyframes;
	private final int length;
	public int frame = 0;

	public Animation(int length, Map<String, List<Keyframe>> keyframes) {
		this.length = length;
		this.keyframes = keyframes;
	}

	public boolean shouldRemove() {
		return frame >= length;
	}

	public void tick() {
		this.frame++;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public void setModelAngles(PlayerEntityModel<AbstractClientPlayerEntity> model, PlayerEntity player, float tickDelta) {
		for(String part : keyframes.keySet()) {
			Keyframe lastFrame = null;
			Keyframe nextFrame = null;
			for(Keyframe frame : keyframes.get(part)) {
				if(frame.frame == this.frame) {
					lastFrame = frame;
					nextFrame = frame;
				}
				if(lastFrame == null && frame.frame < this.frame) {
					lastFrame = frame;
				} else {
					if(lastFrame != null && frame.frame > lastFrame.frame && frame.frame < this.frame) {
						lastFrame = frame;
					}
				}
				if(nextFrame == null && frame.frame > this.frame) {
					nextFrame = frame;
				} else {
					if(nextFrame != null && frame.frame < nextFrame.frame && frame.frame > this.frame) {
						nextFrame = frame;
					}
				}
			}
			assert lastFrame != null;
			assert nextFrame != null;
			switch(part) {
				case "head" -> {
					setPartAngles(model.head, lastFrame, nextFrame, tickDelta);
				}
				case "body" -> {
					setPartAngles(model.body, lastFrame, nextFrame, tickDelta);
				}
				case "right_arm" -> {
					setPartAngles(model.rightArm, lastFrame, nextFrame, tickDelta);
				}
				case "left_arm" -> {
					setPartAngles(model.leftArm, lastFrame, nextFrame, tickDelta);
				}
				case "left_leg" -> {
					setPartAngles(model.leftLeg, lastFrame, nextFrame, tickDelta);
				}
				case "right_leg" -> {
					setPartAngles(model.rightLeg, lastFrame, nextFrame, tickDelta);
				}
				default -> {}
			}
		}
	}
	private void setPartAngles(ModelPart part, Keyframe prev, Keyframe next, float tickDelta) {
		float percentage = (this.frame + tickDelta - prev.frame) / (float)next.frame;
		part.setAngles(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getX(), next.rotation.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getY(), next.rotation.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getY(), next.rotation.getY()));
		part.setPivot(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.pivot.getX(), next.pivot.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.pivot.getY(), next.pivot.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.pivot.getZ(), next.pivot.getZ()));
	}

}
