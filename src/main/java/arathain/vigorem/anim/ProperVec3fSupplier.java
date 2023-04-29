package arathain.vigorem.anim;

import arathain.vigorem.api.Vec3fSupplier;
import net.minecraft.util.math.Vec3f;
import sun.misc.Unsafe;

public class ProperVec3fSupplier extends Vec3fSupplier {
	private final float x, y, z;
	public ProperVec3fSupplier(Vec3f contain) {
		this.x = contain.getX();
		this.y = contain.getZ();
		this.z = contain.getZ();
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
