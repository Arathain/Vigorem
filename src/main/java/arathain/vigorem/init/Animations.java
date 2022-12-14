package arathain.vigorem.init;

import arathain.vigorem.Vigorem;
import arathain.vigorem.anim.Animation;
import arathain.vigorem.anim.Easing;
import arathain.vigorem.anim.Keyframe;
import arathain.vigorem.test.SmashAnimation;
import arathain.vigorem.test.TPoseAnimation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Animations {
	private static final Map<Identifier, Supplier<Animation>> ANIMATIONS = new HashMap<>();
	private static final Map<String, List<Keyframe>> TPOSE_START = new HashMap<>(), TPOSE_END = new HashMap<>();
	private static final Map<String, List<Keyframe>> SMASH_R = new HashMap<>();
	public static void init() {
		List<Keyframe> cache = new ArrayList<>();
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, 1.57079632679f), Vec3f.ZERO, Vec3f.ZERO, 20));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, 1.57079632679f), Vec3f.ZERO, Vec3f.ZERO, 30));
		TPOSE_START.put("right_arm", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, 1.57079632679f), Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, 0), Vec3f.ZERO, Vec3f.ZERO, 20));
		TPOSE_END.put("right_arm", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, -1.57079632679f), Vec3f.ZERO, Vec3f.ZERO, 20));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, -1.57079632679f), Vec3f.ZERO, Vec3f.ZERO, 30));
		TPOSE_START.put("left_arm", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, -1.57079632679f), Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, 0), Vec3f.ZERO, Vec3f.ZERO, 20));
		TPOSE_END.put("left_arm", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO,  new Vec3f(0, -12, 0), 0));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(-0.1f, 0, 0), Vec3f.ZERO, new Vec3f(0, -12, 0), 10));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(-0.1f, 0, 0), Vec3f.ZERO, new Vec3f(0, -12, 0), 30));
		TPOSE_START.put("body", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(-0.1f, 0, 0), Vec3f.ZERO, new Vec3f(0, -12, 0), 0));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, new Vec3f(0, -12, 0), 20));
		TPOSE_END.put("body", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.CUBIC_IN, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO,  new Vec3f(0, -12, 0), 0));
		cache.add(new Keyframe(Easing.EXPO_OUT, Vec3f.ZERO, new Vec3f(-0.15585f, 0, 0), Vec3f.ZERO,  new Vec3f(0, -12, 0), 3));
		cache.add(new Keyframe(Easing.QUINTIC_IN, Vec3f.ZERO, new Vec3f(-0.305f, 0, 0), Vec3f.ZERO,  new Vec3f(0, -12, 0), 6));
		cache.add(new Keyframe(Easing.SINE_OUT, Vec3f.ZERO, new Vec3f(1.09f, 0, 0.1f), Vec3f.ZERO,  new Vec3f(0, -12, 0), 15));
		cache.add(new Keyframe(Easing.LINEAR, Vec3f.ZERO, new Vec3f(0.9354f, 0, 0.1f), Vec3f.ZERO,  new Vec3f(0, -12, 0), 20));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0.9354f, 0, 0.1f), Vec3f.ZERO,  new Vec3f(0, -12, 0), 25));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0f, 0, 0), Vec3f.ZERO,  new Vec3f(0, -12, 0), 33));
		SMASH_R.put("body", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.CUBIC_IN, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO,  Vec3f.ZERO, 0, false));
		cache.add(new Keyframe(Easing.EXPO_OUT, Vec3f.ZERO, new Vec3f(0.15585f, 0, 0), Vec3f.ZERO,  Vec3f.ZERO, 3, false));
		cache.add(new Keyframe(Easing.QUINTIC_IN, Vec3f.ZERO, new Vec3f(0.305f, 0, 0), Vec3f.ZERO,  Vec3f.ZERO, 6, false));
		cache.add(new Keyframe(Easing.SINE_OUT, Vec3f.ZERO, new Vec3f(-1.09f, 0, 0), Vec3f.ZERO,  Vec3f.ZERO, 15, false));
		cache.add(new Keyframe(Easing.LINEAR, Vec3f.ZERO, new Vec3f(-0.9354f, 0, 0), Vec3f.ZERO,  Vec3f.ZERO, 20, false));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(-0.9354f, 0, 0), Vec3f.ZERO,  Vec3f.ZERO, 25, false));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0f, 0, 0), Vec3f.ZERO,  Vec3f.ZERO, 33, false));
		SMASH_R.put("head", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.CUBIC_IN, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.EXPO_OUT, Vec3f.ZERO, new Vec3f(0, 0.15585f, 0), Vec3f.ZERO, Vec3f.ZERO, 4));
		cache.add(new Keyframe(Easing.EXPO_IN, Vec3f.ZERO, new Vec3f(0, 0.305f, 0), Vec3f.ZERO, Vec3f.ZERO, 8));
		cache.add(new Keyframe(Easing.SINE_OUT, Vec3f.ZERO, new Vec3f(0.6545f, 0.3927f, 0.5236f), Vec3f.ZERO,  Vec3f.ZERO, 15));
		cache.add(new Keyframe(Easing.LINEAR, Vec3f.ZERO, new Vec3f(0.5817f, 0.383f, 0.465f), Vec3f.ZERO,  Vec3f.ZERO, 20));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0.5817f, 0.383f, 0.465f), Vec3f.ZERO, Vec3f.ZERO, 25));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 33));
		SMASH_R.put("left_arm", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.CUBIC_IN, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.EXPO_OUT, Vec3f.ZERO, new Vec3f(-1.204f, 0.09f, 0.09f), Vec3f.ZERO, Vec3f.ZERO, 4));
		cache.add(new Keyframe(Easing.EXPO_IN, Vec3f.ZERO, new Vec3f(-2.456f, 0.175f, 0.175f), Vec3f.ZERO, Vec3f.ZERO, 8));
		cache.add(new Keyframe(Easing.SINE_OUT, Vec3f.ZERO, new Vec3f(-1.702f, 0.09f, 0.47f), Vec3f.ZERO,  Vec3f.ZERO, 15));
		cache.add(new Keyframe(Easing.LINEAR, Vec3f.ZERO, new Vec3f(-1.7745f, -0.06f, 0.40f), Vec3f.ZERO,  Vec3f.ZERO, 20));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(-1.7745f, -0.06f, 0.40f), Vec3f.ZERO, Vec3f.ZERO, 25));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 33));
		SMASH_R.put("right_arm", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.CUBIC_IN, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.EXPO_IN, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 8));
		cache.add(new Keyframe(Easing.SINE_OUT, Vec3f.ZERO, new Vec3f(-0.95f, 0, 0), Vec3f.ZERO,  Vec3f.ZERO, 15));
		cache.add(new Keyframe(Easing.LINEAR, Vec3f.ZERO, new Vec3f(-0.9f, 0, 0), Vec3f.ZERO,  Vec3f.ZERO, 20));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(-0.9f, 0, 0), Vec3f.ZERO, Vec3f.ZERO, 25));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 33));
		SMASH_R.put("right_hand", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.CUBIC_IN, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.EXPO_OUT, Vec3f.ZERO, new Vec3f(0f, -0.0445f, 0f), Vec3f.ZERO, Vec3f.ZERO, 3));
		cache.add(new Keyframe(Easing.CUBIC_IN, Vec3f.ZERO,new Vec3f(0f, -0.087266f, 0f), Vec3f.ZERO, Vec3f.ZERO, 7));
		cache.add(new Keyframe(Easing.SINE_OUT, Vec3f.ZERO, new Vec3f(-0.087266f, 0.087266f, 0f), Vec3f.ZERO,  Vec3f.ZERO, 16));
		cache.add(new Keyframe(Easing.LINEAR, Vec3f.ZERO, new Vec3f(-0.0775f, 0.06789f, 0f), Vec3f.ZERO,  Vec3f.ZERO, 21));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(-0.0775f, 0.06789f, 0f), Vec3f.ZERO, Vec3f.ZERO, 26));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 33));
		SMASH_R.put("right_leg", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.CUBIC_IN, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.EXPO_OUT, Vec3f.ZERO, new Vec3f(0.178f, 0f, 0.0669f), Vec3f.ZERO, new Vec3f(0, 1, 0), 3));
		cache.add(new Keyframe(Easing.CUBIC_IN, Vec3f.ZERO,new Vec3f(0.349066f, 0f, 0.1309f), Vec3f.ZERO, Vec3f.ZERO, 6));
		cache.add(new Keyframe(Easing.QUAD_IN, Vec3f.ZERO, new Vec3f(0.349066f, 0f, 0.1309f), Vec3f.ZERO,  Vec3f.ZERO, 9));
		cache.add(new Keyframe(Easing.CUBIC_OUT, Vec3f.ZERO, new Vec3f(0.05693f, 0f, 0.102f), Vec3f.ZERO,  new Vec3f(0, 1.5f, -0.75f), 12));
		cache.add(new Keyframe(Easing.LINEAR, Vec3f.ZERO, new Vec3f(-0.3103f, 0f, 0.0534f), Vec3f.ZERO,  new Vec3f(0, 0.067f, -2f), 15));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(-0.3103f, 0f, 0.0534f), Vec3f.ZERO,  new Vec3f(0, 0.067f, -2f), 25));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 33));
		SMASH_R.put("left_leg", new ArrayList<>(cache));
		cache.clear();

		put(Vigorem.id("smash_left"), () -> new SmashAnimation(33, SMASH_R, true));
		put(Vigorem.id("smash_right"), () -> new SmashAnimation(33, SMASH_R, false));
		put(Vigorem.id("t_pose"), () -> new TPoseAnimation(30, TPOSE_START, 20, TPOSE_END));
	}
	public static void put(Identifier id, Supplier<Animation> anim) {
		ANIMATIONS.put(id, anim);
	}
	public static Animation getAnimation(Identifier id) {
		return ANIMATIONS.get(id) != null ? ANIMATIONS.get(id).get() : null;
	}
	public static Vec3f deg(float x, float y, float z) {
		return new Vec3f((float) (x * Math.PI/180), (float) (y * Math.PI/180), (float) (z * Math.PI/180));
	}
}
