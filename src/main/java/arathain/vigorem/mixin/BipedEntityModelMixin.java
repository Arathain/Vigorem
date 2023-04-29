package arathain.vigorem.mixin;

import arathain.vigorem.VigoremClient;
import arathain.vigorem.anim.CrackCocaine;
import arathain.vigorem.anim.Methylenedioxymethamphetamine;
import arathain.vigorem.anim.OffsetModelPart;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends LivingEntity> implements Methylenedioxymethamphetamine {
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
		return false;
	}
	@Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;body:Lnet/minecraft/client/model/ModelPart;", ordinal = 4, shift = At.Shift.AFTER))
	private void vigorem$sneaktwo(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if(this.sneaking && !VigoremClient.fancySneak) {
			this.body.pitch += 0.5f;
			this.head.pitch -= 0.5f/2f;
			this.body.pivotX = 0;
		}
	}
	@Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
	private void vigorem$e(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if(livingEntity instanceof PlayerEntity) {
			this.body.pivotY += 12;
			((OffsetModelPart) (Object) this.body).setOffset(0, -12, 0);
		}
	}
	@Inject(method = "setArmAngle", at = @At("TAIL"))
	private void vigorem$setArmAngle(Arm arm, MatrixStack matrices, CallbackInfo ci) {
		this.body.rotate(matrices);
	}

	@Override
	public boolean shouldTransformHead() {
		return !((CrackCocaine)(Object)this.head).getHead();
	}

	@Override
	public void setHead(boolean yea) {
		((CrackCocaine)(Object)this.head).setHead(!yea);
	}
}
