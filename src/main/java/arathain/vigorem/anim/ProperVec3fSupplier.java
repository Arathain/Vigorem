package arathain.vigorem.anim;

import arathain.vigorem.api.Vec3fSupplier;
import org.joml.Vector3f;
import sun.misc.Unsafe;

public class ProperVec3fSupplier extends Vec3fSupplier {
	public static ProperVec3fSupplier ZERO = new ProperVec3fSupplier(0, 0, 0);
	private final float x, y, z;
	public ProperVec3fSupplier(Vector3f contain) {
		this.x = contain.x();
		this.y = contain.y();
		this.z = contain.z();
	}
	public ProperVec3fSupplier(float x, float y, float z) {
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
