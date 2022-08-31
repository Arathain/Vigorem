package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {
	public PlayerEntityModelMixin(ModelPart modelPart) {
		super(modelPart);
	}
	@Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;copyTransform(Lnet/minecraft/client/model/ModelPart;)V", ordinal = 0))
	private void setEmote(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if(livingEntity instanceof PlayerEntity plr && plr.getComponent(VigoremComponents.ANIMATION).current != null) {
			plr.getComponent(VigoremComponents.ANIMATION).current.setModelAngles(((PlayerEntityModel)(Object)this), plr, g);
		}
	}
}
