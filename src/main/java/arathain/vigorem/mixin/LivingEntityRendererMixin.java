package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.anim.AnimationComponent;
import arathain.vigorem.anim.Methylenedioxymethamphetamine;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
	@Shadow
	public abstract M getModel();

	protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;setAngles(Lnet/minecraft/entity/Entity;FFFFF)V", shift = At.Shift.AFTER))
	private void vigorem$globalRotate(T livingEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if(livingEntity instanceof PlayerEntity p) {
			AnimationComponent comp = p.getComponent(VigoremComponents.ANIMATION);
			matrixStack.translate(0, 0.75, 0);
			boolean b = false;
			if(comp.currentCycle != null) {
				comp.currentCycle.rotateGlobal(matrixStack, tickDelta);
				b = true;
			}
			if(comp.current != null) {
				comp.current.rotateGlobal(matrixStack, tickDelta);
				b = true;
			}

			((Methylenedioxymethamphetamine)this.getModel()).setAnimating(b);

			matrixStack.translate(0, -0.75, 0);
		}
	}
}
