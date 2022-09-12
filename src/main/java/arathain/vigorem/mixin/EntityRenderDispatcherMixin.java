package arathain.vigorem.mixin;

import arathain.vigorem.Vigorem;
import arathain.vigorem.anim.box.OrientedBox;
import arathain.vigorem.anim.box.RotatableEntityPart;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.WorldView;
import org.quiltmc.qsl.entity.multipart.api.EntityPart;
import org.quiltmc.qsl.entity.multipart.api.MultipartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	@Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true)
	private static void vigorem$shadowShadow(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, float opacity, float tickDelta, WorldView world, float radius, CallbackInfo ci) {
		if (Vigorem.renderingFirstPerson) {
			ci.cancel();
		}
	}
	@Inject(
			method = "renderHitbox",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/render/WorldRenderer;drawBox(Lnet/minecraft/client/util/math/MatrixStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/util/math/Box;FFFF)V",
					ordinal = 0,
					shift = At.Shift.AFTER
			)
	)
	private static void renderMultipartHitboxes(MatrixStack matrix, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci) {
		if (entity instanceof MultipartEntity multipartEntity) {
			for (EntityPart<?> part : multipartEntity.getEntityParts()) {
				if(part instanceof RotatableEntityPart<?> r) {
					matrix.push();
					final Vec3d center = r.getBox().getCenter();
					matrix.translate(center.x, center.y, center.z);
					matrix.multiply(((OrientedBox)r.getBoundingBox()).getRotation().toFloatQuat());
					WorldRenderer.drawBox(matrix, vertices, ((OrientedBox)r.getBoundingBox()).getExtents(), 0.1f, 0.5f, 1, 1);
					matrix.pop();
				}
			}
		}
	}
}
