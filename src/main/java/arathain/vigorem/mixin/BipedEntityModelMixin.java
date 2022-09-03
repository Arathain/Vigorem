package arathain.vigorem.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.FrogEntityRenderer;
import net.minecraft.client.render.entity.animation.FrogEntityAnimations;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.client.render.entity.model.ZombieVillagerEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends LivingEntity> {
//	@ModifyArg(method = "getModelData", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;", ordinal = 2), index = 2)
//	private static ModelTransform vigorem$body(ModelTransform rotationData) {
//		return ModelTransform.pivot(rotationData.pivotX, -12f, rotationData.pivotZ);
//	}
//	@Inject(method = "setAttributes", at = @At("TAIL"))
//	private void vigorem$playerModelFix(BipedEntityModel<T> model, CallbackInfo ci) {
//
//	}

}
