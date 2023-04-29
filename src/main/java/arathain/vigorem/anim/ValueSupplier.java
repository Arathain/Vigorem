package arathain.vigorem.anim;

import net.minecraft.util.math.Vec3f;

@FunctionalInterface
public interface ValueSupplier {
	float get(MathsEngine math, EntityQuery query);
}
