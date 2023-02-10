package arathain.vigorem.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;
import java.util.Map;

public abstract class IntervalCycle extends AnimationCycle {
	protected float progressDirection;
	protected final float[] intervals;
	protected final float leniency;
	public IntervalCycle(Map<String, List<Keyframe>> keyframes, int length, float[] intervals, float leniency) {
		super(keyframes, length);
		this.leniency = leniency;
		this.intervals = intervals;
	}

	@Override
	public void tick(PlayerEntity entity, MinecraftClient cli) {
		super.tick(entity, cli);
		//I need to do this sometime soon
	}
}
