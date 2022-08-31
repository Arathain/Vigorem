package arathain.vigorem.test;

import arathain.vigorem.Vigorem;
import arathain.vigorem.anim.Animation;
import arathain.vigorem.anim.Keyframe;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class TPoseAnimation extends Animation {
	public TPoseAnimation(int length, Map<String, List<Keyframe>> keyframes) {
		super(length, keyframes);
	}

	@Override
	public Identifier getId() {
		return Vigorem.id("t_pose");
	}
}
