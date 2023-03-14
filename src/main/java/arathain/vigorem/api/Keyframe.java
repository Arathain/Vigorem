package arathain.vigorem.api;

import org.joml.Vector3f;

public final class Keyframe {
	public final Easing easing;
	public final Vector3f translation;
	public final Vector3f rotation;
	public final Vector3f offset;
	public final Vector3f scale;
	public final boolean override;

	public final float frame;
	public Keyframe(Easing easing, Vector3f translation, Vector3f rot, Vector3f scale, Vector3f offset, float frame) {
		this.override = true;
		this.easing = easing;
		this.translation = translation;
		this.rotation = rot;
		this.scale = scale;
		this.frame = frame;
		this.offset = offset;
	}
	public Keyframe(Easing easing, Vector3f translation, Vector3f rot, Vector3f scale, Vector3f offset, float frame, boolean override) {
		this.override = override;
		this.easing = easing;
		this.translation = translation;
		this.rotation = rot;
		this.scale = scale;
		this.frame = frame;
		this.offset = offset;
	}

}
