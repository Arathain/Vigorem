package arathain.vigorem.mixin;

import arathain.vigorem.anim.CrackCocaine;
import arathain.vigorem.anim.OffsetModelPart;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3f;
import org.quiltmc.qsl.entity.multipart.api.AbstractEntityPart;
import org.quiltmc.qsl.entity.multipart.api.MultipartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(AnimalModel.class)
public class AnimalModelMixin {
	@Unique
	Supplier<MatrixStack> matrices;
	@Unique
	VertexConsumer vertices;
	@Unique
	int light;
	@Unique
	int overlay;
	@Unique
	float red;
	@Unique
	float green;
	@Unique
	float blue;
	@Unique
	float alpha;

	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V", at = @At("HEAD"))
	private void vigorem$cursedhijackery(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo ci) {
		this.matrices = () -> matrices;
		this.vertices = vertices;
		this.light = light;
		this.overlay = overlay;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	@ModifyArgs(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V", at = @At(value = "INVOKE", target = "Ljava/lang/Iterable;forEach(Ljava/util/function/Consumer;)V"))
	private void vigorem$doublethecursed(Args args) {
		if(((AnimalModel)(Object)this) instanceof BipedEntityModel<?> biped) {
			args.set(0, ((Consumer<ModelPart>)(headPart) -> {
				MatrixStack temp = matrices.get();
				temp.push();
				boolean b = headPart.equals(biped.leftArm) || headPart.equals(biped.head) || headPart.equals(biped.rightArm) || headPart.equals(biped.hat) || ((OffsetModelPart)(Object)biped.rightArm).isChild(headPart) || ((OffsetModelPart)(Object)biped.head).isChild(headPart) || ((OffsetModelPart)(Object)biped.leftArm).isChild(headPart);
				if(biped instanceof PlayerEntityModel<?> p && !b) {
					b = headPart.equals(p.leftSleeve) || headPart.equals(p.rightSleeve);
				}
				if(b) {
					((CrackCocaine)(Object)headPart).setParent(() -> biped.body, headPart.equals(biped.head) || headPart.equals(biped.hat) || ((OffsetModelPart)(Object)biped.head).isChild(headPart));
				}
				headPart.render(temp, vertices, light, overlay, red, green, blue, alpha);
				temp.pop();
			}));
		}
	}

}
