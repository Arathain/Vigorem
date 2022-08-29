package arathain.vigorem.anim;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

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

	public void setModelAngles(PlayerEntityModel<AbstractClientPlayerEntity> model, PlayerEntity player) {

	}

}
