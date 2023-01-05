package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.anim.Animation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	@Final
	private Camera camera;

	@Inject(method = "renderWorld", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V"))
	private void vigorem$preCameraUpdate(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo info) {
		if(client.player != null && !camera.isThirdPerson()) {
			Animation a = client.player.getComponent(VigoremComponents.ANIMATION).current;
			if(a != null) {
				Vec3d offset = a.getCameraOffset(client.player.getYaw(tickDelta), tickDelta);
				matrix.translate(-offset.x, -offset.y, -offset.z);
			}
		}
	}
}
