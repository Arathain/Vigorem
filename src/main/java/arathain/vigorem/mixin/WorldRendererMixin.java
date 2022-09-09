package arathain.vigorem.mixin;

import arathain.vigorem.Vigorem;
import arathain.vigorem.VigoremComponents;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@ModifyExpressionValue(method = "render", at = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;isThirdPerson()Z"))
	private boolean vigorem$cyaegha(boolean bl) {
		if (Vigorem.firstPersonLoaded || MinecraftClient.getInstance().player.isSleeping() || MinecraftClient.getInstance().player.getComponent(VigoremComponents.ANIMATION).current == null) {
			return bl;
		}
		return true;
	}
	@Inject(method = "renderEntity", at = @At("HEAD"))
	private void vigorem$intercept(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
		if (Vigorem.firstPersonLoaded) {
			return;
		}
		if(entity instanceof PlayerEntity player) {
			if (player.isSleeping()) {
				return;
			}
			Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();

			if (entity == camera.getFocusedEntity() && !camera.isThirdPerson() && player.getComponent(VigoremComponents.ANIMATION).current != null) {
				Vigorem.renderingFirstPerson = true;
			}
		}
	}

	@Inject(method = "renderEntity", at = @At("TAIL"))
	private void vigorem$no(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		if (entity == camera.getFocusedEntity()) {
			Vigorem.renderingFirstPerson = false;
		}
	}
}
