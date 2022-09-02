package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import net.minecraft.client.model.ModelCuboidData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(ModelPartData.class)
public abstract class ModelPartDataMixin {
	@Shadow
	@Final
	private Map<String, ModelPartData> children;

	@Shadow
	public abstract ModelPartData getChild(String name);

//	@Invoker("<init>")
//	static ModelPartData ModelPartData(List<ModelCuboidData> list, ModelTransform modelTransform) {
//		throw new AssertionError();
//	}
//
//	@Inject(method = "addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;", at = @At("HEAD"), cancellable = true)
//	public void vigorem$addChild(String name, ModelPartBuilder builder, ModelTransform rotationData, CallbackInfoReturnable<ModelPartData> cir) {
//		switch (name) {
////			case "head" -> {
////
////			}
//			case "right_arm", "left_arm" -> {
//				if(this.getChild("body") != null) {
//					ModelPartData modelPartData = ModelPartData(builder.build(), rotationData);
//					ModelPartData modelPartData2 = ((ModelPartDataMixin) (Object) this.getChild("body")).children.put(name, modelPartData);
//					if (modelPartData2 != null) {
//						((ModelPartDataMixin) (Object) modelPartData).children.putAll(((ModelPartDataMixin) (Object) modelPartData2).children);
//					}
//
//					cir.setReturnValue(modelPartData);
//				}
//			}
//			default -> {}
//		}
//	}
}
