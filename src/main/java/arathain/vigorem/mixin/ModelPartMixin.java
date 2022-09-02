package arathain.vigorem.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ModelPart.class)
public class ModelPartMixin {
	@Shadow
	@Final
	private Map<String, ModelPart> children;

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
}
