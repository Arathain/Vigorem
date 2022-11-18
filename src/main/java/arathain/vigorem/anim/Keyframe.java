package arathain.vigorem.anim;

import net.minecraft.client.model.ModelPart;
import net.minecraft.util.math.Vec3f;

public final class Keyframe {
	public final Easing easing;
	public final Vec3f translation;
	public final Vec3f rotation;
	public final Vec3f offset;
	public final Vec3f scale;
	public final boolean override;

	public final float frame;
	public Keyframe(Easing easing, Vec3f translation, Vec3f rot, Vec3f scale, Vec3f offset, float frame) {
		this.override = true;
		this.easing = easing;
		this.translation = translation;
		this.rotation = rot;
		this.scale = scale;
		this.frame = frame;
		this.offset = offset;
	}
	public Keyframe(Easing easing, Vec3f translation, Vec3f rot, Vec3f scale, Vec3f offset, float frame, boolean override) {
		this.override = override;
		this.easing = easing;
		this.translation = translation;
		this.rotation = rot;
		this.scale = scale;
		this.frame = frame;
		this.offset = offset;
	}

}
