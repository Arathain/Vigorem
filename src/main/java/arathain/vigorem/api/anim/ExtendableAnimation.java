package arathain.vigorem.api.anim;

import arathain.vigorem.api.Keyframe;
import arathain.vigorem.api.anim.Animation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.List;
import java.util.Map;

public abstract class ExtendableAnimation extends Animation {
	public final Map<String, List<Keyframe>> endKeyframes;
	private final int endLength;
	protected int stage;
	public ExtendableAnimation(int initLength, Map<String, List<Keyframe>> initKeyframes, int endLength, Map<String, List<Keyframe>> endKeyframes) {
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
		if(frame >= getLength() && stage == 0) {
			stage = 1;
		}
		if(stage == 1) {
			codeTick();
		}
	}

	@Override
	public Vec3f getRot(String query, float tickDelta) {
		if(!keyframes.containsKey(query)) {
			return Vec3f.ZERO;
		}
		if(stage == 1) {
			return this.getCodeRot(query, tickDelta);
		} else if(stage == 2) {
			Keyframe lastFrame = null;
			Keyframe nextFrame = null;
			boolean bl = false;
			for(Keyframe frame : endKeyframes.get(query)) {
				if(frame.frame == (this.frame + tickDelta)) {
					lastFrame = frame;
					nextFrame = frame;
					bl = true;
				}
				if(lastFrame == null && frame.frame < (this.frame + tickDelta)) {
					lastFrame = frame;
				} else {
					if(lastFrame != null && frame.frame > lastFrame.frame && frame.frame < (this.frame + tickDelta)) {
						lastFrame = frame;
					}
				}
				if(nextFrame == null && frame.frame > (this.frame + tickDelta)) {
					nextFrame = frame;
				} else {
					if(nextFrame != null && frame.frame < nextFrame.frame && frame.frame > (this.frame + tickDelta)) {
						nextFrame = frame;
					}
				}
			}
			if(nextFrame == null) {
				nextFrame = lastFrame;
			}
			if(lastFrame == null) {
				lastFrame = nextFrame;
			}
			lastFrame.update(entityQuery);
			nextFrame.update(entityQuery);
			return getRot(lastFrame, nextFrame, tickDelta, bl);
		}
		return super.getRot(query, tickDelta);
	}
	protected abstract Vec3f getCodeRot(String query, float tickDelta);

	@Override
	public Vec3f getPivot(String query, float tickDelta) {
		if(!keyframes.containsKey(query)) {
			return Vec3f.ZERO;
		}
		if(stage == 1) {
			return this.getCodePivot(query, tickDelta);
		} else if(stage == 2) {
			Keyframe lastFrame = null;
			Keyframe nextFrame = null;
			boolean bl = false;
			for(Keyframe frame : endKeyframes.get(query)) {
				if(frame.frame == (this.frame + tickDelta)) {
					lastFrame = frame;
					nextFrame = frame;
					bl = true;
				}
				if(lastFrame == null && frame.frame < (this.frame + tickDelta)) {
					lastFrame = frame;
				} else {
					if(lastFrame != null && frame.frame > lastFrame.frame && frame.frame < (this.frame + tickDelta)) {
						lastFrame = frame;
					}
				}
				if(nextFrame == null && frame.frame > (this.frame + tickDelta)) {
					nextFrame = frame;
				} else {
					if(nextFrame != null && frame.frame < nextFrame.frame && frame.frame > (this.frame + tickDelta)) {
						nextFrame = frame;
					}
				}
			}
			if(nextFrame == null) {
				nextFrame = lastFrame;
			}
			if(lastFrame == null) {
				lastFrame = nextFrame;
			}
			lastFrame.update(entityQuery);
			nextFrame.update(entityQuery);
			return getPivot(lastFrame, nextFrame, tickDelta, bl);
		}
		return super.getPivot(query, tickDelta);
	}

	protected abstract Vec3f getCodePivot(String query, float tickDelta);

	public Vec3f getOffsetSuper(String query, float tickDelta) {
		return super.getOffset(query, tickDelta);
	}
	public Vec3f getPivotSuper(String query, float tickDelta) {
		return super.getPivot(query, tickDelta);
	}
	public Vec3f getRotSuper(String query, float tickDelta) {
		return super.getRot(query, tickDelta);
	}

	@Override
	public Vec3d getCameraOffset(float yaw, float tickDelta) {
		Vec3f bodyRot = this.getRot("body", tickDelta);
		Vec3f headRot = this.getRot("head", tickDelta);
		return new Vec3d(0, -1, 0).add(new Vec3d(0, (12d / 16d), 0).rotateY(-bodyRot.getY()).rotateX(-bodyRot.getX()).rotateZ(bodyRot.getZ())).add(new Vec3d(0, 4/16f, 0).rotateY(-headRot.getY()).rotateX(-headRot.getX()).rotateZ(-headRot.getZ())).rotateY((float) (yaw * -Math.PI / 180f));
	}

	@Override
	public Vec3f getOffset(String query, float tickDelta) {
		if(!endKeyframes.containsKey(query)) {
			return Vec3f.ZERO;
		}
		if(stage == 1) {
			return this.getCodeOffset(query, tickDelta);
		} else if(stage == 2) {
			Keyframe lastFrame = null;
			Keyframe nextFrame = null;
			boolean bl = false;
			for(Keyframe frame : endKeyframes.get(query)) {
				if(frame.frame == (this.frame + tickDelta)) {
					lastFrame = frame;
					nextFrame = frame;
					bl = true;
				}
				if(lastFrame == null && frame.frame < (this.frame + tickDelta)) {
					lastFrame = frame;
				} else {
					if(lastFrame != null && frame.frame > lastFrame.frame && frame.frame < (this.frame + tickDelta)) {
						lastFrame = frame;
					}
				}
				if(nextFrame == null && frame.frame > (this.frame + tickDelta)) {
					nextFrame = frame;
				} else {
					if(nextFrame != null && frame.frame < nextFrame.frame && frame.frame > (this.frame + tickDelta)) {
						nextFrame = frame;
					}
				}
			}
			if(nextFrame == null) {
				nextFrame = lastFrame;
			}
			if(lastFrame == null) {
				lastFrame = nextFrame;
			}
			lastFrame.update(entityQuery);
			nextFrame.update(entityQuery);
			return getOffset(lastFrame, nextFrame, tickDelta, bl);
		}
		return super.getOffset(query, tickDelta);
	}

	protected abstract Vec3f getCodeOffset(String query, float tickDelta);

	protected void setModelAnglesSuper(PlayerEntityModel<AbstractClientPlayerEntity> model, PlayerEntity player, float tickDelta) {
		super.setModelAngles(model, player, tickDelta);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.stage = nbt.getInt("stage");
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("stage", this.stage);
	}

	@Override
	public void setModelAngles(PlayerEntityModel<AbstractClientPlayerEntity> model, PlayerEntity player, float tickDelta) {
		entityQuery.updateTime(this.frame+tickDelta);
		switch(this.stage) {
			case 0 -> super.setModelAngles(model, player, tickDelta);
			case 1 -> this.setCodeModelAngles(model, player, tickDelta);
			case 2 -> {
				for(String part : endKeyframes.keySet()) {
					Keyframe lastFrame = null;
					Keyframe nextFrame = null;
					boolean bl = false;
					for(Keyframe frame : endKeyframes.get(part)) {
						if(frame.frame == (this.frame + tickDelta)) {
							lastFrame = frame;
							nextFrame = frame;
							bl = true;
						}
						if(lastFrame == null && frame.frame < (this.frame + tickDelta)) {
							lastFrame = frame;
						} else {
							if(lastFrame != null && frame.frame > lastFrame.frame && frame.frame < (this.frame + tickDelta)) {
								lastFrame = frame;
							}
						}
						if(nextFrame == null && frame.frame > (this.frame + tickDelta)) {
							nextFrame = frame;
						} else {
							if(nextFrame != null && frame.frame < nextFrame.frame && frame.frame > (this.frame + tickDelta)) {
								nextFrame = frame;
							}
						}
					}
					if(nextFrame == null) {
						nextFrame = lastFrame;
					}
					if(lastFrame == null) {
						lastFrame = nextFrame;
					}
					lastFrame.update(entityQuery);
					nextFrame.update(entityQuery);
					switch(part) {
						case "head" -> {
							setPartAngles(model.head, lastFrame, nextFrame, tickDelta, bl);
						}
						case "body" -> {
							setPartAngles(model.body, lastFrame, nextFrame, tickDelta, bl);
						}
						case "right_arm" -> {
							setPartAngles(model.rightArm, lastFrame, nextFrame, tickDelta, bl);
						}
						case "left_arm" -> {
							setPartAngles(model.leftArm, lastFrame, nextFrame, tickDelta, bl);
						}
						case "left_leg" -> {
							setPartAngles(model.leftLeg, lastFrame, nextFrame, tickDelta, bl);
						}
						case "right_leg" -> {
							setPartAngles(model.rightLeg, lastFrame, nextFrame, tickDelta, bl);
						}
						default -> {}
					}
				}
			}
		}
	}
	public void update() {
		this.stage = 2;
		this.frame = 0;
	}
	protected abstract void setCodeModelAngles(PlayerEntityModel<AbstractClientPlayerEntity> model, PlayerEntity player, float tickDelta);

	protected abstract void codeTick();
	public boolean shouldEnd(PlayerEntity player) {
		return stage == 1;
	}
}
