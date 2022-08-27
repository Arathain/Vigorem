package arathain.vigorem.anim;

import net.minecraft.client.model.ModelPart;
import net.minecraft.util.math.Vec3f;

public class Keyframe {
	public final Easing easing;
	public final Vec3f pivot;
	public final Vec3f rotation;
	public final Vec3f scale;

	public final int frame;
	public Keyframe(Easing easing, Vec3f pivot, Vec3f rot, Vec3f scale, int frame) {
		this.easing = easing;
		this.pivot = pivot;
		this.rotation = rot;
		this.scale = scale;
		this.frame = frame;
	}

}
