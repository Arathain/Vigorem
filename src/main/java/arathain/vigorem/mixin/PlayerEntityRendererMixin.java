package arathain.vigorem.mixin;

import arathain.vigorem.Vigorem;
import arathain.vigorem.VigoremComponents;
import arathain.vigorem.anim.OffsetModelPart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public PlayerEntityRendererMixin(EntityRendererFactory.Context context, PlayerEntityModel<AbstractClientPlayerEntity> entityModel, float f) {
		super(context, entityModel, f);
	}
	@Inject(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/PlayerEntityModel;setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", shift = At.Shift.AFTER))
	private void vigorem$renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
		if(player.getComponent(VigoremComponents.ANIMATION).current != null) {
			player.getComponent(VigoremComponents.ANIMATION).current.setModelAngles(this.model, player, MinecraftClient.getInstance().getTickDelta());
		}
	}
	@Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", shift = At.Shift.BEFORE))
	private void vigorem$render(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo callbackInfo) {
		if (player == MinecraftClient.getInstance().player && Vigorem.renderingFirstPerson) {
			this.model.head.visible = false;
			this.model.body.visible = false;
			this.model.leftLeg.visible = false;
			this.model.rightLeg.visible = false;
			this.model.hat.visible = false;
			this.model.leftSleeve.visible = false;
			this.model.rightSleeve.visible = false;
			this.model.leftPants.visible = false;
			this.model.rightPants.visible = false;
			this.model.jacket.visible = false;
			//this.model.rightArm.visible = this.model.leftArm.visible =  true;
			this.model.rightSleeve.visible = player.isPartVisible(PlayerModelPart.RIGHT_SLEEVE);
			this.model.leftSleeve.visible = player.isPartVisible(PlayerModelPart.LEFT_SLEEVE);
		}
	}
	@Inject(method = "getPositionOffset(Lnet/minecraft/client/network/AbstractClientPlayerEntity;F)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
	private void vigorem$getPosition(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, CallbackInfoReturnable<Vec3d> cir) {
		cir.setReturnValue(super.getPositionOffset(abstractClientPlayerEntity, f));
	}
}
