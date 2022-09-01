package arathain.vigorem.test;

import arathain.vigorem.Vigorem;
import arathain.vigorem.anim.Animation;
import arathain.vigorem.anim.Keyframe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class TPoseAnimation extends Animation {
	public TPoseAnimation(int length, Map<String, List<Keyframe>> keyframes) {
		super(length, keyframes);
	}

	@Override
	public Identifier getId() {
		return Vigorem.id("t_pose");
	}

	@Override
	public float getMovementMultiplier() {
		return 0.05f;
	}

	@Override
	public boolean isBlockingMovement() {
		return true;
	}
}
