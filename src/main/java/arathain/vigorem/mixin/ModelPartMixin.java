package arathain.vigorem.mixin;

import arathain.vigorem.Vigorem;
import arathain.vigorem.anim.CrackCocaine;
import arathain.vigorem.anim.OffsetModelPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
	public boolean isChild(ModelPart part) {
		return this.children.containsValue(part);
	}

	@Inject(method = "rotate(Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("TAIL"))
	public void vigorem$rotato(MatrixStack matrix, CallbackInfo ci) {
		if (this.offsetX != 0F || this.offsetY != 0F || this.offsetZ != 0F) {
			matrix.translate(this.offsetX/16F, this.offsetY/16F, this.offsetZ/16F);
		}
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
		matrix.multiply(new Quaternionf().rotationZYX(part.roll, part.yaw, part.pitch));

		if (part.scaleX != 1.0F || part.scaleY != 1.0F || part.scaleZ != 1.0F) {
			matrix.scale(part.scaleX, part.scaleY, part.scaleZ);
		}

		if (((OffsetModelPart) (Object) part).getOffsetX() != 0F || ((OffsetModelPart) (Object) part).getOffsetY() != 0F || ((OffsetModelPart) (Object) part).getOffsetZ() != 0F) {
			matrix.translate((((OffsetModelPart) (Object) part).getOffsetX() / 16F), (((OffsetModelPart) (Object) part).getOffsetY() / 16F), (((OffsetModelPart) (Object) part).getOffsetZ() / 16F));
		}

		if (getHead()) {
			matrix.multiply(new Quaternionf().rotationZYX(-part.roll, -part.yaw, -part.pitch));
		}

	}
}
