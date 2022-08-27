package arathain.vigorem.anim;

import net.minecraft.client.model.ModelPart;

import java.util.List;
import java.util.Map;

public class Animation {
	public final Map<ModelPart, List<Keyframe>> keyframes;
	private final int length;
	public int frame = 0;
	public Animation(int length, Map<ModelPart, List<Keyframe>> keyframes) {
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
}
