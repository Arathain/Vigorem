package arathain.vigorem.api;

import arathain.vigorem.Vigorem;
import arathain.vigorem.anim.EntityQuery;
import arathain.vigorem.anim.MoLangVector3fSupplier;
import arathain.vigorem.anim.ProperVector3fSupplier;
import org.joml.Vector3f;

public class Keyframe {
	public final Easing easing;
	public final Vector3fSupplier translation;
	public final Vector3fSupplier rotation;
	public final Vector3fSupplier offset;
	public final Vector3fSupplier scale;
	public final boolean override;

	public final float frame;
	public Keyframe(Easing easing, Vector3f translation, Vector3f rot, Vector3f scale, Vector3f offset, float frame) {
		if(scale == Vigorem.ZERO) {
			scale = new Vector3f(1, 1, 1);
		}
		this.override = true;
		this.easing = easing;
		this.translation = new ProperVector3fSupplier(translation);
		this.rotation = new ProperVector3fSupplier(rot);
		this.scale = new ProperVector3fSupplier(scale);
		this.frame = frame;
		this.offset = new ProperVector3fSupplier(offset);
	}
	public Keyframe(Easing easing, Vector3f translation, Vector3f rot, Vector3f scale, Vector3f offset, float frame, boolean override) {
		if(scale == Vigorem.ZERO) {
			scale = new Vector3f(1, 1, 1);
		}
		this.override = override;
		this.easing = easing;
		this.translation = new ProperVector3fSupplier(translation);
		this.rotation = new ProperVector3fSupplier(rot);
		this.scale = new ProperVector3fSupplier(scale);
		this.frame = frame;
		this.offset = new ProperVector3fSupplier(offset);
	}
	public Keyframe(Easing easing, Vector3fSupplier translation, Vector3fSupplier rot, Vector3fSupplier scale, Vector3fSupplier offset, float frame) {
		if(scale == ProperVector3fSupplier.ZERO) {
			scale = ProperVector3fSupplier.ONE;
		}
		this.override = true;
		this.easing = easing;
		this.translation = translation;
		this.rotation = rot;
		this.scale = scale;
		this.frame = frame;
		this.offset = offset;
	}
	public Keyframe(Easing easing, Vector3fSupplier translation, Vector3fSupplier rot, Vector3fSupplier scale, Vector3fSupplier offset, float frame, boolean override) {
		if(scale == ProperVector3fSupplier.ZERO) {
			scale = ProperVector3fSupplier.ONE;
		}
		this.override = override;
		this.easing = easing;
		this.translation = translation;
		this.rotation = rot;
		this.scale = scale;
		this.frame = frame;
		this.offset = offset;
	}

	public void update(EntityQuery q) {
		if(translation.isMoLang()) {
			((MoLangVector3fSupplier)translation).update(q);
		}
		if(rotation.isMoLang()) {
			((MoLangVector3fSupplier)rotation).update(q);
		}
		if(scale.isMoLang()) {
			((MoLangVector3fSupplier)scale).update(q);
		}
		if(offset.isMoLang()) {
			((MoLangVector3fSupplier)offset).update(q);
		}
	}

}
