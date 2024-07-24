package arathain.vigorem.anim;

import arathain.vigorem.api.Vector3fSupplier;
import net.minecraft.util.math.MathHelper;

public class MoLangVector3fSupplier extends Vector3fSupplier {
	private final ValueSupplier x, y, z;
	private EntityQuery q;
	private final boolean deg;
	public MoLangVector3fSupplier(ValueSupplier x, ValueSupplier y, ValueSupplier z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.deg = false;
	}
	public MoLangVector3fSupplier(ValueSupplier x, ValueSupplier y, ValueSupplier z, boolean degrees) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.deg = degrees;
	}
	public void update(EntityQuery in) {
		q = in;
	}
	@Override
	public float getX() {
		return x.get(MathsEngine.get(), q) * (deg ? MathHelper.PI/180 : 1);
	}

	@Override
	public float getY() {
		return y.get(MathsEngine.get(), q) * (deg ? MathHelper.PI/180 : 1);
	}

	@Override
	public float getZ() {
		return z.get(MathsEngine.get(), q) * (deg ? MathHelper.PI/180 : 1);
	}

	@Override
	public boolean isMoLang() {
		return true;
	}
}
