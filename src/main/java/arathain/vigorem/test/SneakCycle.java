package arathain.vigorem.test;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.api.IntervalCycle;
import arathain.vigorem.api.init.Animations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class SneakCycle extends IntervalCycle {
	public SneakCycle() {
		super(Animations.SNEAK, 20, new float[] {0, 10, 20}, 0.05f);
	}

	@Override
	public void tick(PlayerEntity entity, MinecraftClient cli) {
		super.tick(entity, cli);;
		float f = (float) (entity.getVelocity().horizontalLength() * 20);
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
