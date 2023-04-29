package arathain.vigorem.anim;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class MathsEngine {
	public final float pi = MathHelper.PI;
	private static final Random random = new Random();
	private static MathsEngine INSTANCE;

	public static MathsEngine get() {
		if(INSTANCE == null) {
			INSTANCE = new MathsEngine();
		}
		return INSTANCE;
	}

	public float abs(float in) {
		return MathHelper.abs(in);
	}
	public float ceil(float in) {
		return MathHelper.ceil(in);
	}
	public float floor(float in) {
		return MathHelper.floor(in);
	}
	public float sqrt(float in) {
		return MathHelper.sqrt(in);
	}
	public float lerp(float delta, float first, float second) {
		return MathHelper.lerp(delta, first, second);
	}
	public float lerprotate(float delta, float first, float second) {
		return MathHelper.lerpAngleDegrees(delta, first, second);
	}
	public float acos(float in) {
		return (float) Math.acos(in);
	}
	public float asin(float in) {
		return (float) Math.asin(in);
	}
	public float atan(float in) {
		return (float) Math.atan(in);
	}
	public float sin(float in) {
		return MathHelper.sin(in);
	}
	public float cos(float in) {
		return MathHelper.cos(in);
	}
	public float tan(float in) {
		return (float) Math.tan(in);
	}
	public float atan2(float y, float x) {
		return (float) MathHelper.atan2(y, x);
	}
	public float clamp(float in, float min, float max) {
		return MathHelper.clamp(in, min, max);
	}
	public float ln(float in) {
		return (float) Math.log(in);
	}
	public float min(float first, float second) {
		return Math.min(first, second);
	}
	public float max(float first, float second) {
		return Math.max(first, second);
	}
	public float pow(float in, float pow) {
		return (float) Math.pow(in, pow);
	}
	public float random(float low, float high) {
		return low + random.nextFloat()*(high-low);
	}
	public float die_roll(int num, float low, float high) {
		float sum = 0;
		for(int i = 0; i < num; i++) {
			sum += random(low, high);
		}
		return sum;
	}
	public int random_integer(int low, int high) {
		return low + random.nextInt(high-low);
	}
	public float die_roll_integer(int num, int low, int high) {
		float sum = 0;
		for(int i = 0; i < num; i++) {
			sum += random_integer(low, high);
		}
		return sum;
	}
	public float hermite_blend(float in) {
		return 3*in*in-2*pow(in, 3);
	}
	public float round(float in) {
		return Math.round(in);
	}
	public float trunc(float in) {
		return in > 0 ? MathHelper.floor(in) : MathHelper.ceil(in);
	}
	public float exp(float in) {
		return (float) Math.exp(in);
	}
	public float mod(float in, float denominator) {
		return in % denominator;
	}
	public float min_angle(float in) {
		return MathHelper.clamp(in, -180, 180);
	}
}
