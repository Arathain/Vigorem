package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.anim.box.OrientedBox;
import arathain.vigorem.api.AnimatingWeaponItem;
import arathain.vigorem.api.WeaponAnimation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

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
		if(MinecraftClient.getInstance().player.getComponent(VigoremComponents.ANIMATION).current instanceof WeaponAnimation anim) {
			PlayerEntity player = MinecraftClient.getInstance().player;
			float delta = MinecraftClient.getInstance().getTickDelta();
			if(player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof AnimatingWeaponItem item) {
				Vec3f bodyRot = anim.getRot("body", delta);
				Vec3f armRot = anim.getRot("right_arm", delta);
				Vec3f handRot = anim.getRot("right_hand", delta);
				Vec3d offset = new Vec3d(0, 0.75, 0).add(new Vec3d(-0.3125, (14d / 16d), 0).rotateY(-bodyRot.getY()).rotateX(-bodyRot.getX()).rotateZ(bodyRot.getZ()))
						.add(new Vec3d(-2/16d, -10/16d, 0).rotateY(-add(bodyRot, armRot).getY()).rotateX(-add(bodyRot, armRot).getX()).rotateZ(add(bodyRot, armRot).getZ()));
				Vec3d weaponPos = player.getPos().add(offset.rotateY((float) (player.getYaw()*-Math.PI/180f)));
				bodyRot.add(0, (float) (player.getYaw(delta)*Math.PI/180f), 0);
				OrientedBox box = new OrientedBox(weaponPos, item.getHitbox(), (float) (bodyRot.getY() * -180/Math.PI),(float) (bodyRot.getX() * 180/Math.PI));
				box.updateVertex();
				OrientedBox newBox = box.copy().offset(camera.getPos().negate()).updateVertex();
				drawOutline(newBox, new ArrayList<>());
			}
		}

	}
	private Vec3f add(Vec3f vec1, Vec3f vec2) {
		Vec3f vec = vec1.copy();
		vec.add(vec2);
		return vec;
	}
	@Unique
	private void drawOutline(OrientedBox obb, List<OrientedBox> otherObbs) {
		RenderSystem.enableDepthTest();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBufferBuilder();
		RenderSystem.disableTexture();
		RenderSystem.disableBlend();
		RenderSystem.lineWidth(1.0f);
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

		outlineOBB(obb, bufferBuilder,
				0, 1, 0,
				1, 1, 0,0.5F);
		look(obb, bufferBuilder, 0.5F);

		for(OrientedBox otherObb: otherObbs){
			outlineOBB(otherObb, bufferBuilder,
					1, 0, 0,
					1, 0, 0,0.5F);
		}

		tessellator.draw();

		RenderSystem.lineWidth(1.0f);
		RenderSystem.enableBlend();
		RenderSystem.enableTexture();
	}
	private void look(OrientedBox box, BufferBuilder buffer, float alpha) {
		buffer.vertex(box.center.x, box.center.y, box.center.z).color(0, 0, 0, alpha).next();

		buffer.vertex(box.center.x, box.center.y, box.center.z).color(1, 0, 0, alpha).next();
		buffer.vertex(box.axisZ.x, box.axisZ.y, box.axisZ.z).color(1, 0, 0, alpha).next();
		buffer.vertex(box.center.x, box.center.y, box.center.z).color(1, 0, 0, alpha).next();

		buffer.vertex(box.center.x, box.center.y, box.center.z).color(0, 1, 0, alpha).next();
		buffer.vertex(box.axisY.x, box.axisY.y, box.axisY.z).color(0, 1, 0, alpha).next();
		buffer.vertex(box.center.x, box.center.y, box.center.z).color(0, 1, 0, alpha).next();

		buffer.vertex(box.center.x, box.center.y, box.center.z).color(0, 0, 1, alpha).next();
		buffer.vertex(box.axisX.x, box.axisX.y, box.axisX.z).color(0, 0, 1, alpha).next();
		buffer.vertex(box.center.x, box.center.y, box.center.z).color(0, 0, 1, alpha).next();

		buffer.vertex(box.center.x, box.center.y, box.center.z).color(0, 0, 0, alpha).next();
	}

	private void outlineOBB(OrientedBox box, BufferBuilder buffer,
							float red1, float green1, float blue1,
							float red2, float green2, float blue2,
							float alpha) {
		buffer.vertex(box.vertex1.x, box.vertex1.y, box.vertex1.z).color(0, 0, 0, 0).next();

		buffer.vertex(box.vertex1.x, box.vertex1.y, box.vertex1.z).color(red1, green1, blue1, alpha).next();
		buffer.vertex(box.vertex2.x, box.vertex2.y, box.vertex2.z).color(red1, green1, blue1, alpha).next();
		buffer.vertex(box.vertex3.x, box.vertex3.y, box.vertex3.z).color(red1, green1, blue1, alpha).next();
		buffer.vertex(box.vertex4.x, box.vertex4.y, box.vertex4.z).color(red1, green1, blue1, alpha).next();
		buffer.vertex(box.vertex1.x, box.vertex1.y, box.vertex1.z).color(red1, green1, blue1, alpha).next();
		buffer.vertex(box.vertex5.x, box.vertex5.y, box.vertex5.z).color(red2, green2, blue2, alpha).next();
		buffer.vertex(box.vertex6.x, box.vertex6.y, box.vertex6.z).color(red2, green2, blue2, alpha).next();
		buffer.vertex(box.vertex2.x, box.vertex2.y, box.vertex2.z).color(red1, green1, blue1, alpha).next();
		buffer.vertex(box.vertex6.x, box.vertex6.y, box.vertex6.z).color(red2, green2, blue2, alpha).next();
		buffer.vertex(box.vertex7.x, box.vertex7.y, box.vertex7.z).color(red2, green2, blue2, alpha).next();
		buffer.vertex(box.vertex3.x, box.vertex3.y, box.vertex3.z).color(red1, green1, blue1, alpha).next();
		buffer.vertex(box.vertex7.x, box.vertex7.y, box.vertex7.z).color(red2, green2, blue2, alpha).next();
		buffer.vertex(box.vertex8.x, box.vertex8.y, box.vertex8.z).color(red2, green2, blue2, alpha).next();
		buffer.vertex(box.vertex4.x, box.vertex4.y, box.vertex4.z).color(red1, green1, blue1, alpha).next();
		buffer.vertex(box.vertex8.x, box.vertex8.y, box.vertex8.z).color(red2, green2, blue2, alpha).next();
		buffer.vertex(box.vertex5.x, box.vertex5.y, box.vertex5.z).color(red2, green2, blue2, alpha).next();

		buffer.vertex(box.vertex5.x, box.vertex5.y, box.vertex5.z).color(0, 0, 0, 0).next();
		buffer.vertex(box.center.x, box.center.y, box.center.z).color(0, 0, 0, 0).next();
	}
}
