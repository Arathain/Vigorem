package arathain.vigorem.init;

import arathain.vigorem.Vigorem;
import arathain.vigorem.anim.Animation;
import arathain.vigorem.anim.Easing;
import arathain.vigorem.anim.Keyframe;
import arathain.vigorem.test.TPoseAnimation;
import net.minecraft.block.HoneyBlock;
import net.minecraft.server.network.ServerPlayerEntity;
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
	public static void init() {
		List<Keyframe> rarm = new ArrayList<>();
		rarm.add(new Keyframe(Easing.QUAD_IN_OUT, new Vec3f(-5.0F, 2.0F, 0.0F), Vec3f.ZERO, Vec3f.ZERO, 0));
		rarm.add(new Keyframe(Easing.LINEAR, new Vec3f(-5.0F, 2.0F, 0.0F), new Vec3f(0, 0, 1.57079632679f), Vec3f.ZERO, 20));
		rarm.add(new Keyframe(Easing.QUAD_IN_OUT, new Vec3f(-5.0F, 2.0F, 0.0F), new Vec3f(0, 0, 1.57079632679f), Vec3f.ZERO, 30));
		rarm.add(new Keyframe(Easing.QUAD_IN_OUT, new Vec3f(-5.0F, 2.0F, 0.0F), new Vec3f(0, 0, 0), Vec3f.ZERO, 40));
		TPOSE.put("right_arm", new ArrayList<>(rarm));
		rarm.clear();
		rarm.add(new Keyframe(Easing.QUAD_IN_OUT, new Vec3f(5.0F, 2.0F, 0.0F), Vec3f.ZERO, Vec3f.ZERO, 0));
		rarm.add(new Keyframe(Easing.LINEAR, new Vec3f(5.0F, 2.0F, 0.0F), new Vec3f(0, 0, -1.57079632679f), Vec3f.ZERO, 20));
		rarm.add(new Keyframe(Easing.QUAD_IN_OUT, new Vec3f(5.0F, 2.0F, 0.0F), new Vec3f(0, 0, -1.57079632679f), Vec3f.ZERO, 30));
		rarm.add(new Keyframe(Easing.QUAD_IN_OUT, new Vec3f(5.0F, 2.0F, 0.0F), new Vec3f(0, 0, 0), Vec3f.ZERO, 40));
		TPOSE.put("left_arm", new ArrayList<>(rarm));
		rarm.clear();
		rarm.add(new Keyframe(Easing.QUAD_IN_OUT, new Vec3f(0.0F, 0F, 0.0F), new Vec3f(0, 0, 0), Vec3f.ZERO, 0));
		rarm.add(new Keyframe(Easing.QUAD_IN_OUT, new Vec3f(0.0F, 0F, 0.0F), new Vec3f(0, 0, 0), Vec3f.ZERO, 40));
		TPOSE.put("body", new ArrayList<>(rarm));
		put(Vigorem.id("t_pose"), () -> new TPoseAnimation(40, TPOSE));
	}
	public static void put(Identifier id, Supplier<Animation> anim) {
		ANIMATIONS.put(id, anim);
	}
	public static Animation getAnimation(Identifier id) {
		return ANIMATIONS.get(id).get();
	}
}
