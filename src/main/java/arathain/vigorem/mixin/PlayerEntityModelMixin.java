package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.anim.OffsetModelPart;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {
	@Shadow
	protected abstract Iterable<ModelPart> getBodyParts();

	public PlayerEntityModelMixin(ModelPart modelPart) {
		super(modelPart);
	}
	@Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;copyTransform(Lnet/minecraft/client/model/ModelPart;)V", ordinal = 0))
	private void vigorem$angles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if (livingEntity instanceof PlayerEntity plr && plr.getComponent(VigoremComponents.ANIMATION).current != null) {
			this.rightLeg.pivotX = -1.9f;
			this.leftLeg.pivotX = 1.9f;
			plr.getComponent(VigoremComponents.ANIMATION).current.setModelAngles(((PlayerEntityModel<AbstractClientPlayerEntity>) (Object) this), plr, g);
			this.hat.copyTransform(this.head);
			if (plr.getComponent(VigoremComponents.ANIMATION).current.shouldRemove()) {
				this.getBodyParts().forEach(part -> ((OffsetModelPart) (Object) part).setOffset(0, 0, 0));
				this.getHeadParts().forEach(part -> ((OffsetModelPart) (Object) part).setOffset(0, 0, 0));
			}
		}
	}
}
