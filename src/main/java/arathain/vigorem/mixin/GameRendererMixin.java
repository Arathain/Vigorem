package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.anim.CameraAttachment;
import arathain.vigorem.api.anim.Animation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
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
	MinecraftClient client;

	@Shadow
	@Final
	private Camera camera;

	@Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/LightmapTextureManager;update(F)V", shift = At.Shift.BEFORE))
	private void vigorem$preCameraUpdate(RenderTickCounter tickCounter, CallbackInfo ci, @Local(ordinal = 0) float tickDelta) {
		if(client.player != null && !camera.isThirdPerson()) {
			Animation a = client.player.getComponent(VigoremComponents.ANIMATION).current;
			if(a != null) {
				Vec3d offset = a.getCameraOffset(client.player.getYaw(tickDelta), MathHelper.lerp(tickDelta, client.player.prevPitch, client.player.getPitch()), tickDelta);
				((CameraAttachment) camera).vigorem$setOffset(offset.negate());
			}
		}
	}
	@Inject(method = "renderWorld", at = @At("TAIL"))
	private void vigorem$postCameraUpdate(RenderTickCounter tickCounter, CallbackInfo ci) {
		if(client.player != null && !camera.isThirdPerson()) {
			Animation a = client.player.getComponent(VigoremComponents.ANIMATION).current;
			if(a != null) {
				((CameraAttachment) camera).vigorem$setOffset(Vec3d.ZERO);
			}
		}
	}
}
