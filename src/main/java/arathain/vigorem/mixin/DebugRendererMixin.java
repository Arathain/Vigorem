package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.api.ColliderAnimation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Shamelessly stolen from <a href="https://github.com/ZsoltMolnarrr/BetterCombat/blob/1.19.X/common/src/main/java/net/bettercombat/mixin/client/ColliderDebugRenderer.java">BetterCombat</a>
 * Praise the god code \o/
 * @author ZsoltMolnarrr
 */
@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
	@Inject(method = "render", at = @At("TAIL"))
	public void renderDebugWeaponCollider(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		if (!camera.isReady()) {
			return;
		}
		if(MinecraftClient.getInstance().player.getComponent(VigoremComponents.ANIMATION).current instanceof ColliderAnimation anim) {
			anim.render(matrices, vertexConsumers, MinecraftClient.getInstance().player, camera);
		}

	}
}
