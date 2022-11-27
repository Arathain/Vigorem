package arathain.vigorem.api;

import arathain.vigorem.anim.Animation;
import arathain.vigorem.anim.Keyframe;

import java.util.List;
import java.util.Map;

public abstract class ContinuousAnimation extends Animation {
	public final Map<String, List<Keyframe>> endKeyframes;
	private final int endLength;
	protected int stage;
	public ContinuousAnimation(int initLength, Map<String, List<Keyframe>> initKeyframes, int endLength, Map<String, List<Keyframe>> endKeyframes) {
		super(initLength, initKeyframes);
		this.endKeyframes = endKeyframes;
		this.endLength = endLength;
	}
	public boolean shouldRemove() {
		return frame >= endLength && stage == 2;
	}

	public void tick() {
		if(stage == 2 || stage == 0) {
			super.tick();
		}
		if(frame >= getLength()) {
			stage = 1;
		}
		if(stage == 1) {
			if(this.shouldEnd()) {
				stage = 2;
			}
			codeTick();
		}
	}
	protected abstract void codeTick();
	public abstract boolean shouldEnd();
}
