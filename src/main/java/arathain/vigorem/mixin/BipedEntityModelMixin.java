package arathain.vigorem.mixin;

import arathain.vigorem.anim.OffsetModelPart;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends LivingEntity> {
	@Shadow
	@Final
	public ModelPart head;

	@Shadow
	@Final
	public ModelPart body;

	@Shadow
	public boolean sneaking;

	@Shadow
	@Final
	public ModelPart rightLeg;

	@Shadow
	@Final
	public ModelPart leftLeg;

	@ModifyExpressionValue(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;sneaking:Z"))
	private boolean vigorem$cancelSneak(boolean original) {
		if(original) {
			this.body.pitch += 0.5f;
			this.head.pitch -= 0.5f;
			this.body.pivotX = 3.2F;
		}
		return false;
	}
	@Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;body:Lnet/minecraft/client/model/ModelPart;", ordinal = 4, shift = At.Shift.AFTER))
	private void vigorem$sneaktwo(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if(this.sneaking) {
			this.body.pitch += 0.5f;
			this.head.pitch -= 0.5f;
			this.body.pivotX = 3.2F;
			this.rightLeg.pivotX = -1.9f;
			this.leftLeg.pivotX = 1.9f;
		}
	}
	@Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
	private void vigorem$e(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if(livingEntity instanceof PlayerEntity) {
			this.body.setPivot(0, 12, 0);
			((OffsetModelPart) (Object) this.body).setOffset(0, -12, 0);
		}
	}
	@Inject(method = "setArmAngle", at = @At("TAIL"))
	private void vigorem$setArmAngle(Arm arm, MatrixStack matrices, CallbackInfo ci) {
		this.body.rotate(matrices);
	}

}
