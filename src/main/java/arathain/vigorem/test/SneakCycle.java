package arathain.vigorem.test;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.api.AnimationCycle;
import arathain.vigorem.api.init.Animations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class SneakCycle extends AnimationCycle {
	public SneakCycle() {
		super(Animations.SNEAK, 20);
	}

	@Override
	public void tick(PlayerEntity entity, MinecraftClient cli) {
		super.tick(entity, cli);
		float f = (float) (entity.getVelocity().horizontalLength() * 20);
		if(entity.forwardSpeed < -0.05) {
			f = -f;
		}
		progress += f;
	}

	@Override
	public boolean shouldRemove(PlayerEntity p) {
		return !p.isSneaking() || p.getComponent(VigoremComponents.ANIMATION).current != null;
	}

	@Override
	public boolean shouldTransformHead() {
		return false;
	}
}
