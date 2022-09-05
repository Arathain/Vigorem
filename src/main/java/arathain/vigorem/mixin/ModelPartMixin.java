package arathain.vigorem.mixin;

import arathain.vigorem.anim.OffsetModelPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ModelPart.class)
public class ModelPartMixin implements OffsetModelPart {
	@Unique
	public float offsetX = 0;
	@Unique
	public float offsetY = 0;
	@Unique
	public float offsetZ = 0;
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
	} //

	@Override
	public boolean isChild(ModelPart part) {
		return this.children.containsValue(part);
	}

	//	@Inject(method = "getChild", at = @At("HEAD"), cancellable = true)
//	public void vigorem$getChild(String name, CallbackInfoReturnable<ModelPart> cir) {
//		switch (name) {
////			case "head" -> {
////
////			}
//			case "right_arm", "left_arm" -> {
//				if(this.children.get("body") != null && this.children.get("body").getChild(name) != null)
//					cir.setReturnValue(this.children.get("body").getChild(name));
//			}
//			default -> {}
//		}
//	}
	@Inject(method = "rotate(Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("TAIL"))
	public void vigorem$rotato(MatrixStack matrix, CallbackInfo ci) {
		if (this.offsetX != 0F || this.offsetY != 0F || this.offsetZ != 0F) {
			matrix.translate(this.offsetX/16F, this.offsetY/16F, this.offsetZ/16F);
		}
	}
	@Inject(method = "copyTransform", at = @At("TAIL"))
	public void vigorem$copyTrans(ModelPart part, CallbackInfo ci) {
		this.offsetX = ((OffsetModelPart)(Object)part).getOffsetX();
		this.offsetY = ((OffsetModelPart)(Object)part).getOffsetY();
		this.offsetZ = ((OffsetModelPart)(Object)part).getOffsetZ();
	}
}
