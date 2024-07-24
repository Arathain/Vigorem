package arathain.vigorem.api.anim;

import arathain.vigorem.Vigorem;
import arathain.vigorem.anim.EntityQuery;
import arathain.vigorem.anim.OffsetModelPart;
import arathain.vigorem.api.Keyframe;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public abstract class Animation {
	public final Map<String, List<Keyframe>> keyframes;
	private final int length;
	public int frame = 0;
	protected final EntityQuery entityQuery = new EntityQuery();

	public Animation(int length, Map<String, List<Keyframe>> keyframes) {
		this.length = length;
		this.keyframes = keyframes;
	}

	public boolean shouldTransformHead() {
		return true;
	};

	public boolean shouldRemove() {
		return frame >= length;
	}
	public void serverTick(PlayerEntity player) {
		entityQuery.update(player, this.frame, player.getWorld());
	}
	public void clientTick(PlayerEntity player) {
		entityQuery.update(player, this.frame, player.getWorld());
	}

	public void tick() {
		this.frame++;
	}

	public int getLength() {
		return length;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public boolean canInterrupt() {
		return false;
	}
	public boolean canCancel(Animation queued) {
		return false;
	}
	public Identifier getId() {
		return Vigorem.id("balls");
	}

	public float getMovementMultiplier() {
		return 1;
	}
	public boolean isAffectingGravity() {
		return false;
	}
	public boolean isBlockingMovement() {
		return false;
	}
	public boolean lockHeldItem(){
		return false;
	}

	public boolean isIFrame() {return false;}

	public void rotateGlobal(MatrixStack matrices, float tickDelta) {
		String part = "global";
		if(keyframes.containsKey(part)) {
			Keyframe lastFrame = null;
			Keyframe nextFrame = null;
			boolean bl = false;
			for(Keyframe frame : keyframes.get(part)) {
				if(frame.frame == this.frame + tickDelta) {
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
			setMatrixTransform(matrices, lastFrame, nextFrame, tickDelta, bl);
		}
	}

	public Vector3f getRot(String query, float tickDelta) {
		if(!keyframes.containsKey(query)) {
			return Vigorem.ZERO;
		}
		Keyframe lastFrame = null;
		Keyframe nextFrame = null;
		boolean bl = false;
		for(Keyframe frame : keyframes.get(query)) {
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
	public Vector3f getOffset(String query, float tickDelta) {
		if(!keyframes.containsKey(query)) {
			return Vigorem.ZERO;
		}
		Keyframe lastFrame = null;
		Keyframe nextFrame = null;
		boolean bl = false;
		for(Keyframe frame : keyframes.get(query)) {
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
	public Vector3f getPivot(String query, float tickDelta) {
		if(!keyframes.containsKey(query)) {
			return Vigorem.ZERO;
		}
		Keyframe lastFrame = null;
		Keyframe nextFrame = null;
		boolean bl = false;
		for(Keyframe frame : keyframes.get(query)) {
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
	public void writeNbt(NbtCompound nbt) {
		nbt.putInt("time", this.frame);
	}
	public void readNbt(NbtCompound nbt) {
		this.frame = nbt.getInt("time");
	}

	public void setModelAngles(PlayerEntityModel<AbstractClientPlayerEntity> model, PlayerEntity player, float tickDelta) {
		entityQuery.updateTime(this.frame+tickDelta, player);
		entityQuery.pitch = MathHelper.lerp(tickDelta, player.prevPitch, player.getPitch());
		for(String part : keyframes.keySet()) {
			Keyframe lastFrame = null;
			Keyframe nextFrame = null;
			boolean bl = false;
			for(Keyframe frame : keyframes.get(part)) {
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
				case "head" -> setPartAngles(model.head, lastFrame, nextFrame, tickDelta, bl);
				case "body" -> setPartAngles(model.body, lastFrame, nextFrame, tickDelta, bl);
				case "right_arm" -> setPartAngles(model.rightArm, lastFrame, nextFrame, tickDelta, bl);
				case "left_arm" -> setPartAngles(model.leftArm, lastFrame, nextFrame, tickDelta, bl);
				case "left_leg" -> setPartAngles(model.leftLeg, lastFrame, nextFrame, tickDelta, bl);
				case "right_leg" -> setPartAngles(model.rightLeg, lastFrame, nextFrame, tickDelta, bl);
				default -> {}
			}
		}
	}
	protected Vector3f getRot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vector3f(prev.rotation.getX(),prev.rotation.getY(), prev.rotation.getZ());
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / (next.frame - prev.frame);
			return new Vector3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getX(), next.rotation.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getY(), next.rotation.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getZ(), next.rotation.getZ()));
		}
	}
	protected Vector3f getPivot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vector3f(prev.translation.getX(), prev.translation.getY(), prev.translation.getZ());
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / (next.frame - prev.frame);
			return new Vector3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getX(), next.translation.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getY(), next.translation.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getZ(), next.translation.getZ()));
		}
	}
	protected Vector3f getOffset(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			return new Vector3f(prev.offset.getX(), prev.offset.getY(), prev.offset.getZ());
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / (next.frame - prev.frame);
			return new Vector3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getX(), next.offset.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getY(), next.offset.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getZ(), next.offset.getZ()));
		}
	}
	protected void setPartAngles(ModelPart part, Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			part.setAngles(prev.rotation.getX() + (!prev.override ? part.pitch : 0),prev.rotation.getY() + (!prev.override ? part.yaw : 0), prev.rotation.getZ() + (!prev.override ? part.roll : 0));
			part.translate(new Vector3f(prev.translation.getX(), prev.translation.getY(), prev.translation.getZ()));
			part.xScale = prev.scale.getX();
			part.yScale = prev.scale.getY();
			part.zScale = prev.scale.getZ();
			((OffsetModelPart)(Object)part).setOffset(prev.offset.getX(), prev.offset.getY(), prev.offset.getZ());
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / (next.frame - prev.frame);
			part.setAngles(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getX() + (!prev.override ? part.pitch : 0), next.rotation.getX() + (!next.override ? part.pitch : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getY() + (!prev.override ? part.yaw : 0), next.rotation.getY() + (!next.override ? part.yaw : 0)), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getZ() + (!prev.override ? part.roll : 0), next.rotation.getZ() + (!next.override ? part.roll : 0)));
			part.translate(new Vector3f(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getX(), next.translation.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getY(), next.translation.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getZ(), next.translation.getZ())));
			part.xScale = MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getX(), next.scale.getX());
			part.yScale = MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getY(), next.scale.getY());
			part.zScale = MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getZ(), next.scale.getZ());
			if(prev.override) {
				((OffsetModelPart) (Object) part).setOffset(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getX(), next.offset.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getY(), next.offset.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getZ(), next.offset.getZ()));
			} else {
				((OffsetModelPart) (Object) part).addOffset(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getX(), next.offset.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getY(), next.offset.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getZ(), next.offset.getZ()));
			}

		}
	}
	protected void setMatrixTransform(MatrixStack s, Keyframe prev, Keyframe next, float tickDelta, boolean same) {
		if(same) {
			s.translate(prev.translation.getX(), prev.translation.getY(), prev.translation.getZ());
			s.multiply(RotationAxis.POSITIVE_X.rotation(prev.rotation.getX()));
			s.multiply(RotationAxis.POSITIVE_Y.rotation(prev.rotation.getY()));
			s.multiply(RotationAxis.POSITIVE_Z.rotation(prev.rotation.getZ()));
			s.scale(prev.scale.getX(), prev.scale.getY(), prev.scale.getZ());
			s.translate(prev.offset.getX(), prev.offset.getY(), prev.offset.getZ());
		} else {
			float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
			s.translate(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getX(), next.translation.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getY(), next.translation.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.getZ(), next.translation.getZ()));
			s.multiply(RotationAxis.POSITIVE_X.rotation(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getX(), next.rotation.getX())));
			s.multiply(RotationAxis.POSITIVE_Y.rotation(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getY(), next.rotation.getY())));
			s.multiply(RotationAxis.POSITIVE_Z.rotation(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.getZ(), next.rotation.getZ())));
			s.scale(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getX(), next.scale.getX()),
					MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getY(), next.scale.getY()),
					MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.getZ(), next.scale.getZ()));
			s.translate(MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getX(), next.offset.getX()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getY(), next.offset.getY()), MathHelper.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.getZ(), next.offset.getZ()));
		}
	}

	public Vec3d getCameraOffset(float yaw, float pitch, float tickDelta) {
		entityQuery.pitch = pitch;
		Vector3f bodyRot = this.getRot("body", tickDelta);
		Vector3f headRot = this.getRot("head", tickDelta);
		headRot.add(bodyRot);
		return new Vec3d(0, -1, 0).add(new Vec3d(0, (12d / 16d), 0).rotateY(-bodyRot.y()).rotateX(-bodyRot.x()).rotateZ(bodyRot.z())).add(new Vec3d(0, 4/16f, 0).rotateY(-headRot.y)).rotateX(-headRot.x()).rotateZ(-headRot.z()).rotateY((float) (yaw * -Math.PI / 180f));
	}
}
