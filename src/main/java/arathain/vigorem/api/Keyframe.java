package arathain.vigorem.api;

import arathain.vigorem.anim.EntityQuery;
import arathain.vigorem.anim.MoLangVec3fSupplier;
import arathain.vigorem.anim.ProperVec3fSupplier;
import net.minecraft.util.math.Vec3f;

public class Keyframe {
	public final Easing easing;
	public final Vec3fSupplier translation;
	public final Vec3fSupplier rotation;
	public final Vec3fSupplier offset;
	public final Vec3fSupplier scale;
	public final boolean override;

	public final float frame;
	public Keyframe(Easing easing, Vec3f translation, Vec3f rot, Vec3f scale, Vec3f offset, float frame) {
		this.override = true;
		this.easing = easing;
		this.translation = new ProperVec3fSupplier(translation);
		this.rotation = new ProperVec3fSupplier(rot);
		this.scale = new ProperVec3fSupplier(scale);
		this.frame = frame;
		this.offset = new ProperVec3fSupplier(offset);
	}
	public Keyframe(Easing easing, Vec3f translation, Vec3f rot, Vec3f scale, Vec3f offset, float frame, boolean override) {
		this.override = override;
		this.easing = easing;
		this.translation = new ProperVec3fSupplier(translation);
		this.rotation = new ProperVec3fSupplier(rot);
		this.scale = new ProperVec3fSupplier(scale);
		this.frame = frame;
		this.offset = new ProperVec3fSupplier(offset);
	}
	public Keyframe(Easing easing, Vec3fSupplier translation, Vec3fSupplier rot, Vec3fSupplier scale, Vec3fSupplier offset, float frame) {
		this.override = true;
		this.easing = easing;
		this.translation = translation;
		this.rotation = rot;
		this.scale = scale;
		this.frame = frame;
		this.offset = offset;
	}
	public Keyframe(Easing easing, Vec3fSupplier translation, Vec3fSupplier rot, Vec3fSupplier scale, Vec3fSupplier offset, float frame, boolean override) {
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
			((MoLangVec3fSupplier)translation).update(q);
		}
		if(rotation.isMoLang()) {
			((MoLangVec3fSupplier)rotation).update(q);
		}
		if(scale.isMoLang()) {
			((MoLangVec3fSupplier)scale).update(q);
		}
		if(offset.isMoLang()) {
			((MoLangVec3fSupplier)offset).update(q);
		}
	}

}
