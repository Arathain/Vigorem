package arathain.vigorem.anim;

@FunctionalInterface
public interface ValueSupplier {
	float get(MathsEngine math, EntityQuery query);
}
