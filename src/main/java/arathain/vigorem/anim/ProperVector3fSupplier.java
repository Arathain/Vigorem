package arathain.vigorem.anim;

import arathain.vigorem.api.Vector3fSupplier;
import org.joml.Vector3f;

public class ProperVector3fSupplier extends Vector3fSupplier {
	public static ProperVector3fSupplier ZERO = new ProperVector3fSupplier(0, 0, 0);
	public static ProperVector3fSupplier ONE = new ProperVector3fSupplier(1, 1, 1);
	private final float x, y, z;
	public ProperVector3fSupplier(Vector3f contain) {
		this.x = contain.x();
		this.y = contain.y();
		this.z = contain.z();
	}
	public ProperVector3fSupplier(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return z;
	}

	@Override
	public boolean isMoLang() {
		return false;
	}
}
