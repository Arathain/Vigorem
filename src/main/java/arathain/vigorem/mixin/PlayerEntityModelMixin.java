package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import net.minecraft.block.Block;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.FrogEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {
	public PlayerEntityModelMixin(ModelPart modelPart) {
		super(modelPart);
	}
	@Unique
	public ModelPart leftHand;
	@Unique
	public ModelPart rightHand;
	@Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;copyTransform(Lnet/minecraft/client/model/ModelPart;)V", ordinal = 0))
	private void vigorem$angles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if(livingEntity instanceof PlayerEntity plr && plr.getComponent(VigoremComponents.ANIMATION).current != null) {
			plr.getComponent(VigoremComponents.ANIMATION).current.setModelAngles(((PlayerEntityModel)(Object)this), plr, g);
		}
	}
	@Inject(method = "<init>", at = @At("TAIL"))
	private void vigorem$appendCustoms(ModelPart modelPart, boolean bl, CallbackInfo ci) {
		this.leftHand = modelPart.getChild("left_arm").getChild("left_hand");
		this.rightHand = modelPart.getChild("right_arm").getChild("right_hand");
		//.getChild("body")
	}
	@Inject(method = "getTexturedModelData", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void vigorem$fixthemodeldammit(Dilation dilation, boolean slim, CallbackInfoReturnable<ModelData> cir, ModelData modelData, ModelPartData data) {
		data.getChild("right_arm").addChild("right_hand", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 10F, 0.0F));
		data.getChild("left_arm").addChild("left_hand", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 10F, 0.0F));
	}

}
