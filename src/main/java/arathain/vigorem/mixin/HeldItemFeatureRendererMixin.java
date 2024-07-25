package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.api.anim.Animation;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithArms> extends FeatureRenderer<T, M> {
	@Shadow
	@Final
	private HeldItemRenderer heldItemRenderer;

	@Unique
	private float tickDelta;

	public HeldItemFeatureRendererMixin(FeatureRendererContext<T, M> featureRendererContext) {
		super(featureRendererContext);
	}

	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/HeldItemFeatureRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;Lnet/minecraft/util/Arm;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",  shift = At.Shift.AFTER))
	private void vigorem$catchDelta(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		this.tickDelta = 1;
	}

	@Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/ModelWithArms;setArmAngle(Lnet/minecraft/util/Arm;Lnet/minecraft/client/util/math/MatrixStack;)V",  shift = At.Shift.AFTER), cancellable = true)
	private void vigorem$rotateItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if(entity instanceof PlayerEntity player) {
			if (player.getComponent(VigoremComponents.ANIMATION).current != null) {
				Animation anim = player.getComponent(VigoremComponents.ANIMATION).current;

				Vector3f rot = anim.getRot(arm == Arm.LEFT ? "left_hand" : "right_hand", tickDelta);
				Vector3f offset = anim.getOffset(arm == Arm.LEFT ? "left_hand" : "right_hand", tickDelta);
				Vector3f pivot = anim.getPivot(arm == Arm.LEFT ? "left_hand" : "right_hand", tickDelta);

				boolean bl = arm == Arm.LEFT;
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
				matrices.translate(((bl ? -1 : 1) / 16.0F), 0.125, -0.625);
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-180.0F));
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
				matrices.translate(pivot.x()/16f, -pivot.y()/16f, pivot.z()/16f);
				matrices.multiply(RotationAxis.POSITIVE_Z.rotation(rot.z()));
				matrices.multiply(RotationAxis.POSITIVE_Y.rotation(rot.y()));
				matrices.multiply(RotationAxis.POSITIVE_X.rotation(rot.x()));
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
				matrices.translate(offset.x()/16f, offset.y()/16f, offset.z()/16f);
				this.heldItemRenderer.renderItem(entity, stack, transformationMode, bl, matrices, vertexConsumers, light);
				matrices.pop();
				ci.cancel();
			}
		}
	}


}
