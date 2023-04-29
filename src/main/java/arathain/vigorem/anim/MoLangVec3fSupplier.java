package arathain.vigorem.anim;

import arathain.vigorem.api.Vec3fSupplier;

public class MoLangVec3fSupplier extends Vec3fSupplier {
	private final ValueSupplier x, y, z;
	private EntityQuery q;
	public MoLangVec3fSupplier(ValueSupplier x, ValueSupplier y, ValueSupplier z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void update(EntityQuery in) {
		q = in;
	}
	@Override
	public float getX() {
		return x.get(MathsEngine.get(), q);
	}

	@Override
	public float getY() {
		return y.get(MathsEngine.get(), q);
	}

	@Override
	public float getZ() {
		return z.get(MathsEngine.get(), q);
	}

	@Override
	public boolean isMoLang() {
		return true;
	}
}
