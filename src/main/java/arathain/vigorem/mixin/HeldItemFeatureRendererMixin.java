package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.api.anim.Animation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithArms> extends FeatureRenderer<T, M> {
	@Shadow
	@Final
	private HeldItemRenderer heldItemRenderer;

	public HeldItemFeatureRendererMixin(FeatureRendererContext<T, M> featureRendererContext) {
		super(featureRendererContext);
	}

	@Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/ModelWithArms;setArmAngle(Lnet/minecraft/util/Arm;Lnet/minecraft/client/util/math/MatrixStack;)V",  shift = At.Shift.AFTER), cancellable = true)
	private void vigorem$rotateItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if(entity instanceof PlayerEntity player) {
			if (player.getComponent(VigoremComponents.ANIMATION).current != null) {
				Animation anim = player.getComponent(VigoremComponents.ANIMATION).current;
				float tickDelta = MinecraftClient.getInstance().getTickDelta();

				Vector3f rot = anim.getRot(arm == Arm.LEFT ? "left_hand" : "right_hand", tickDelta);
				Vector3f offset = anim.getOffset(arm == Arm.LEFT ? "left_hand" : "right_hand", tickDelta);
				Vector3f pivot = anim.getPivot(arm == Arm.LEFT ? "left_hand" : "right_hand", tickDelta);

				boolean bl = arm == Arm.LEFT;
				matrices.multiply(new Quaternionf().rotateX((float) Math.toRadians(-90.0F)));
				matrices.multiply(new Quaternionf().rotateY((float) Math.toRadians(180.0F)));
				matrices.translate(((bl ? -1 : 1) / 16.0F), 0.125, -0.625);
				matrices.multiply(new Quaternionf().rotateY((float) Math.toRadians(-180.0F)));
				matrices.multiply(new Quaternionf().rotateX((float) Math.toRadians(90.0F)));
				matrices.translate(pivot.x/16f, -pivot.y/16f, pivot.z/16f);
				matrices.multiply(new Quaternionf().rotateZYX(rot.z, rot.y, rot.x));
				matrices.multiply(new Quaternionf().rotateX((float) Math.toRadians(-90.0F)));
				matrices.multiply(new Quaternionf().rotateY((float) Math.toRadians(180.0F)));
				matrices.translate(offset.x/16f, offset.y/16f, offset.z/16f);
				this.heldItemRenderer.renderItem(entity, stack, transformationMode, bl, matrices, vertexConsumers, light);
				matrices.pop();
				ci.cancel();
			}
		}
	}


}
