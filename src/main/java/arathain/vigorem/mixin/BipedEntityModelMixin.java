package arathain.vigorem.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ZombieVillagerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin {
//	@WrapWithCondition(method = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;getModelData(Lnet/minecraft/client/model/Dilation;F)Lnet/minecraft/client/model/ModelData;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;", ordinal = 0)
//	)
//	private static boolean vigorem$head(ModelPartData instance, String string, ModelPartBuilder build, ModelTransform transform) {
//		return false;
//	}
//	@WrapWithCondition(method = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;getModelData(Lnet/minecraft/client/model/Dilation;F)Lnet/minecraft/client/model/ModelData;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;", ordinal = 1)
//	)
//	private static boolean vigorem$hat(ModelPartData instance, String string, ModelPartBuilder build, ModelTransform transform) {
//		return false;
//	}
	@ModifyArg(method = "getModelData", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;", ordinal = 2), index = 2)
	private static ModelTransform vigorem$body(ModelTransform rotationData) {
		return ModelTransform.pivot(rotationData.pivotX, rotationData.pivotY - 12f, rotationData.pivotZ);
	}
//	@WrapWithCondition(method = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;getModelData(Lnet/minecraft/client/model/Dilation;F)Lnet/minecraft/client/model/ModelData;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;", ordinal = 3)
//	)
//	private static boolean vigorem$rArm(ModelPartData instance, String string, ModelPartBuilder build, ModelTransform transform) {
//		return false;
//	}
//	@WrapWithCondition(method = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;getModelData(Lnet/minecraft/client/model/Dilation;F)Lnet/minecraft/client/model/ModelData;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;", ordinal = 4)
//	)
//	private static boolean vigorem$lArm(ModelPartData instance, String string, ModelPartBuilder build, ModelTransform transform) {
//		return false;
//	}
//	@ModifyReceiver(
//			method = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;<init>(Lnet/minecraft/client/model/ModelPart;Ljava/util/function/Function;)V",
//			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;getChild(Ljava/lang/String;)Lnet/minecraft/client/model/ModelPart;")
//	)
//	private static ModelPart vigorem$correction(ModelPart receiver, String yeah) {
//		switch(yeah) {
//			case "head", "hat", "right_arm", "left_arm" -> {
//				return receiver.getChild("body");
//			}
//			default -> {
//				return receiver;
//			}
//		}
//	}/

//	@Inject(method = "getModelData", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
//	private static void vigorem$fixthemodeldammit(Dilation dilation, float pivotOffsetY, CallbackInfoReturnable<ModelData> cir, ModelData modelData, ModelPartData data) {
//		data.getChild("body").addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation), ModelTransform.pivot(0.0F, -12.0F + pivotOffsetY, 0.0F));
//		data.getChild("body").addChild("hat", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.5F)), ModelTransform.pivot(0.0F, -12.0F + pivotOffsetY, 0.0F));
//		data.getChild("body").addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-5.0F, -10.0F + pivotOffsetY, 0.0F));
//		data.getChild("body").addChild("left_arm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, -10.0F + pivotOffsetY, 0.0F));
//	}

}
