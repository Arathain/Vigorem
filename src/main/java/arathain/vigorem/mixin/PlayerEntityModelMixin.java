package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.anim.AnimationComponent;
import arathain.vigorem.anim.Methylenedioxymethamphetamine;
import arathain.vigorem.anim.OffsetModelPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
		if (livingEntity instanceof PlayerEntity plr) {
			AnimationComponent comp = plr.getComponent(VigoremComponents.ANIMATION);
			boolean b = comp.currentCycle != null || comp.current != null;
			if(b)
				((OffsetModelPart) (Object) this.body).setOffset(0, -12, 0);
			if(comp.currentCycle != null) {
				comp.currentCycle.setModelAngles(((PlayerEntityModel<AbstractClientPlayerEntity>) (Object) this), plr, g);
				if(((Methylenedioxymethamphetamine) this).shouldTransformHead() != comp.currentCycle.shouldTransformHead()) {
					((Methylenedioxymethamphetamine) this).setHead(comp.currentCycle.shouldTransformHead());
				}
			}
			if(comp.current != null) {
				comp.current.setModelAngles(((PlayerEntityModel<AbstractClientPlayerEntity>) (Object) this), plr, g);
				if(((Methylenedioxymethamphetamine) this).shouldTransformHead() != comp.current.shouldTransformHead()) {
					((Methylenedioxymethamphetamine) this).setHead(comp.current.shouldTransformHead());
				}
			}
			if(b)
				this.body.pivotY += 12;
			if(b) {
				this.hat.copyTransform(this.head);
			}
		}
	}
}
