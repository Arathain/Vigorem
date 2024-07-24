package arathain.vigorem.mixin;

import arathain.vigorem.anim.CrackCocaine;
import arathain.vigorem.anim.OffsetModelPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(ModelPart.class)
public class ModelPartMixin implements OffsetModelPart, CrackCocaine {
	@Unique
	public float offsetX = 0;
	@Unique
	public float offsetY = 0;
	@Unique
	public float offsetZ = 0;
	private boolean vigorem$headHijinks;
	@Unique
	private Supplier<ModelPart> parent = () -> null;

	@Shadow
	@Final
	private Map<String, ModelPart> children;

	@Shadow
	public float pitch;

	@Shadow
	public float yaw;

	@Shadow
	public float roll;

	@Override
	public float getOffsetX() {
		return offsetX;
	}

	@Override
	public float getOffsetY() {
		return offsetY;
	}

	@Override
	public float getOffsetZ() {
		return offsetZ;
	}

	@Override
	public void setOffset(float x, float y, float z) {
		this.offsetX = x;
		this.offsetY = y;
		this.offsetZ = z;
	}

	@Override
	public void addOffset(float x, float y, float z) {
		this.offsetX += x;
		this.offsetY += y;
		this.offsetZ += z;
	}

	@Override
	public boolean isChild(ModelPart part) {
		return this.children.containsValue(part);
	}

	@Inject(method = "rotate(Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("TAIL"))
	public void vigorem$rotato(MatrixStack matrix, CallbackInfo ci) {
		if (this.offsetX != 0F || this.offsetY != 0F || this.offsetZ != 0F) {
			matrix.translate(this.offsetX/16F, this.offsetY/16F, this.offsetZ/16F);
		}
	}
	@Inject(method = "setAngles", at = @At("TAIL"))
	public void vigorem$modulus(float pitch, float yaw, float roll, CallbackInfo ci) {
		this.pitch = pitch % MathHelper.TAU;
		this.yaw = yaw % MathHelper.TAU;
		this.roll = roll % MathHelper.TAU;
	}
	@Inject(method = "rotate(Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("HEAD"))
	public void vigorem$protato(MatrixStack matrix, CallbackInfo ci) {
		if(this.parent.get() != null) {
			act(parent.get(), matrix);
		}
	}
	@Inject(method = "copyTransform", at = @At("TAIL"))
	public void vigorem$copyTrans(ModelPart part, CallbackInfo ci) {
		this.vigorem$headHijinks = ((ModelPartMixin)(Object)part).vigorem$headHijinks;
		this.offsetX = ((OffsetModelPart)(Object)part).getOffsetX();
		this.offsetY = ((OffsetModelPart)(Object)part).getOffsetY();
		this.offsetZ = ((OffsetModelPart)(Object)part).getOffsetZ();
	}

	@Override
	public Supplier<ModelPart> getParent() {
		return parent;
	}

	@Override
	public void setParent(Supplier<ModelPart> parent) {
		this.parent = parent;
	}

	@Override
	public void setHead(boolean yea) {
		this.vigorem$headHijinks = yea;
	}

	@Override
	public boolean getHead() {
		return this.vigorem$headHijinks;
	}

	@Unique
	private void act(ModelPart part, MatrixStack matrix) {
		matrix.translate((double) (part.pivotX / 16.0F), (double) (part.pivotY / 16.0F), (double) (part.pivotZ / 16.0F));
		if (part.roll != 0.0F) {
			matrix.multiply(RotationAxis.POSITIVE_Z.rotation(part.roll));
		}

		if (part.yaw != 0.0F) {
			matrix.multiply(RotationAxis.POSITIVE_Y.rotation(part.yaw));
		}

		if (part.pitch != 0.0F) {
			matrix.multiply(RotationAxis.POSITIVE_X.rotation(part.pitch));
		}

		if (part.xScale != 1.0F || part.yScale != 1.0F || part.zScale != 1.0F) {
			matrix.scale(part.xScale, part.yScale, part.zScale);
		}

		if (((OffsetModelPart) (Object) part).getOffsetX() != 0F || ((OffsetModelPart) (Object) part).getOffsetY() != 0F || ((OffsetModelPart) (Object) part).getOffsetZ() != 0F) {
			matrix.translate((((OffsetModelPart) (Object) part).getOffsetX() / 16F), (((OffsetModelPart) (Object) part).getOffsetY() / 16F), (((OffsetModelPart) (Object) part).getOffsetZ() / 16F));
		}

		if (getHead()) {
			if (part.pitch != 0.0F) {
				matrix.multiply(RotationAxis.NEGATIVE_X.rotation(part.pitch));
			}
			if (part.yaw != 0.0F) {
				matrix.multiply(RotationAxis.NEGATIVE_Y.rotation(part.yaw));
			}
			if (part.roll != 0.0F) {
				matrix.multiply(RotationAxis.NEGATIVE_Z.rotation(part.roll));
			}
		}

	}
}
