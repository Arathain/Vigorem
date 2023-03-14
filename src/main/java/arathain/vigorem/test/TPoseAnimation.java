package arathain.vigorem.test;

import arathain.vigorem.Vigorem;
import arathain.vigorem.api.Keyframe;
import arathain.vigorem.api.anim.ExtendableAnimation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;


import java.util.List;
import java.util.Map;

public class TPoseAnimation extends ExtendableAnimation {
	public TPoseAnimation(int length, Map<String, List<Keyframe>> keyframes, int endLength, Map<String, List<Keyframe>> endKeyframes) {
		super(length, keyframes, endLength, endKeyframes);
	}

	@Override
	public Identifier getId() {
		return Vigorem.id("t_pose");
	}

	@Override
	public float getMovementMultiplier() {
		return MathHelper.clamp(2/(float)this.frame, 0, 1);
	}

	@Override
	public boolean isBlockingMovement() {
		return true;
	}

	@Override
	protected Vector3f getCodeRot(String query, float tickDelta) {
		return getRotSuper(query, -2);
	}

	@Override
	protected Vector3f getCodePivot(String query, float tickDelta) {
		return getPivotSuper(query, 0);
	}

	@Override
	protected Vector3f getCodeOffset(String query, float tickDelta) {
		return getOffsetSuper(query,0);
	}

	@Override
	protected void setCodeModelAngles(PlayerEntityModel<AbstractClientPlayerEntity> model, PlayerEntity player, float tickDelta) {
		this.setModelAnglesSuper(model, player, -2);
	}

	@Override
	protected void codeTick() {

	}

	@Override
	public boolean shouldEnd(PlayerEntity player) {
		return super.shouldEnd(player) && MathHelper.abs(player.getPitch()) > 80;
	}
}
