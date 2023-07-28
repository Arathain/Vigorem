package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.api.anim.Animation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	@Final
	private Camera camera;

	@SuppressWarnings("InvalidInjectorMethodSignature")
	@ModifyArg(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;setupFrustum(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Vec3d;Lorg/joml/Matrix4f;)V"), index = 0)
	private MatrixStack vigorem$preCameraUpdate(MatrixStack matrix, @Local(ordinal = 0) float tickDelta) {
		if(client.player != null && !camera.isThirdPerson()) {
			Animation a = client.player.getComponent(VigoremComponents.ANIMATION).current;
			if(a != null) {
				Vec3d offset = a.getCameraOffset(client.player.getYaw(tickDelta), tickDelta);
				matrix.translate(-offset.x, -offset.y, -offset.z);
			}
		}
		return matrix;
	}
}
