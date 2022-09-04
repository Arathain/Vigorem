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
	private static final Map<String, List<Keyframe>> TPOSE = new HashMap<>();
	private static final Map<String, List<Keyframe>> SMASH_L = new HashMap<>();
	private static final Map<String, List<Keyframe>> SMASH_R = new HashMap<>();
	public static void init() {
		List<Keyframe> cache = new ArrayList<>();
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, 1.57079632679f), Vec3f.ZERO, Vec3f.ZERO, 20));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, 1.57079632679f), Vec3f.ZERO, Vec3f.ZERO, 30));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, 0), Vec3f.ZERO, Vec3f.ZERO, 40));
		TPOSE.put("right_arm", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, -1.57079632679f), Vec3f.ZERO, Vec3f.ZERO, 20));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, -1.57079632679f), Vec3f.ZERO, Vec3f.ZERO, 30));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0, 0, 0), Vec3f.ZERO, Vec3f.ZERO, 40));
		TPOSE.put("left_arm", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO,  new Vec3f(0, -12, 0), 0));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(-0.1f, 0, 0), Vec3f.ZERO, new Vec3f(0, -12, 0), 10));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(-0.1f, 0, 0), Vec3f.ZERO, new Vec3f(0, -12, 0), 20));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, new Vec3f(0, -12, 0), 40));
		TPOSE.put("body", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.CUBIC_IN, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO,  new Vec3f(0, -12, 0), 0));
		cache.add(new Keyframe(Easing.EXPO_OUT, Vec3f.ZERO, new Vec3f(-0.15585f, 0, 0), Vec3f.ZERO,  new Vec3f(0, -12, 0), 3));
		cache.add(new Keyframe(Easing.QUINTIC_IN, Vec3f.ZERO, new Vec3f(-0.305f, 0, 0), Vec3f.ZERO,  new Vec3f(0, -12, 0), 6));
		cache.add(new Keyframe(Easing.SINE_OUT, Vec3f.ZERO, new Vec3f(1.09f, 0, 0), Vec3f.ZERO,  new Vec3f(0, -12, 0), 15));
		cache.add(new Keyframe(Easing.LINEAR, Vec3f.ZERO, new Vec3f(0.9354f, 0, 0), Vec3f.ZERO,  new Vec3f(0, -12, 0), 20));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0.9354f, 0, 0), Vec3f.ZERO,  new Vec3f(0, -12, 0), 25));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0f, 0, 0), Vec3f.ZERO,  new Vec3f(0, -12, 0), 33));
		SMASH_L.put("body", new ArrayList<>(cache));
		SMASH_R.put("body", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.CUBIC_IN, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.EXPO_OUT, Vec3f.ZERO, new Vec3f(0, 0.15585f, 0), Vec3f.ZERO, Vec3f.ZERO, 4));
		cache.add(new Keyframe(Easing.EXPO_IN, Vec3f.ZERO, new Vec3f(0, 0.305f, 0), Vec3f.ZERO, Vec3f.ZERO, 8));
		cache.add(new Keyframe(Easing.SINE_OUT, Vec3f.ZERO, new Vec3f(0.6545f, 0.3927f, 0.5236f), Vec3f.ZERO,  Vec3f.ZERO, 15));
		cache.add(new Keyframe(Easing.LINEAR, Vec3f.ZERO, new Vec3f(0.5817f, 0.383f, 0.465f), Vec3f.ZERO,  Vec3f.ZERO, 20));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(0.5817f, 0.383f, 0.465f), Vec3f.ZERO, Vec3f.ZERO, 25));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 33));
		SMASH_L.put("right_arm", new ArrayList<>(cache));
		SMASH_R.put("left_arm", new ArrayList<>(cache));
		cache.clear();
		cache.add(new Keyframe(Easing.CUBIC_IN, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 0));
		cache.add(new Keyframe(Easing.EXPO_OUT, Vec3f.ZERO, new Vec3f(-1.204f, 0.09f, 0.09f), Vec3f.ZERO, Vec3f.ZERO, 4));
		cache.add(new Keyframe(Easing.EXPO_IN, Vec3f.ZERO, new Vec3f(2.356f, 0.175f, 0.175f), Vec3f.ZERO, Vec3f.ZERO, 8));
		cache.add(new Keyframe(Easing.SINE_OUT, Vec3f.ZERO, new Vec3f(-1.702f, 0.09f, 0.5236f), Vec3f.ZERO,  Vec3f.ZERO, 15));
		cache.add(new Keyframe(Easing.LINEAR, Vec3f.ZERO, new Vec3f(-1.7745f, -0.06f, 0.4849f), Vec3f.ZERO,  Vec3f.ZERO, 20));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, new Vec3f(-1.7745f, -0.06f, 0.4849f), Vec3f.ZERO, Vec3f.ZERO, 25));
		cache.add(new Keyframe(Easing.QUAD_IN_OUT, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, Vec3f.ZERO, 33));
		SMASH_L.put("left_arm", new ArrayList<>(cache));
		SMASH_R.put("right_arm", new ArrayList<>(cache));
		cache.clear();

		put(Vigorem.id("smash_left"), () -> new SmashAnimation(33, SMASH_L, false));
		put(Vigorem.id("smash_right"), () -> new SmashAnimation(33, SMASH_R, true));
		put(Vigorem.id("t_pose"), () -> new TPoseAnimation(40, TPOSE));
	}
	public static void put(Identifier id, Supplier<Animation> anim) {
		ANIMATIONS.put(id, anim);
	}
	public static Animation getAnimation(Identifier id) {
		return ANIMATIONS.get(id) != null ? ANIMATIONS.get(id).get() : null;
	}
}
