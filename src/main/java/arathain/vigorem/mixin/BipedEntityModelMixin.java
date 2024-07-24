package arathain.vigorem.mixin;

import arathain.vigorem.anim.CrackCocaine;
import arathain.vigorem.anim.Methylenedioxymethamphetamine;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends LivingEntity> implements Methylenedioxymethamphetamine {

	@Unique
	private boolean anim;

	@Shadow
	@Final
	public ModelPart head;

	@Shadow
	@Final
	public ModelPart body;

	@Shadow
	public boolean sneaking;

	@Shadow
	@Final
	public ModelPart rightLeg;

	@Shadow
	@Final
	public ModelPart leftLeg;

	@ModifyExpressionValue(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;sneaking:Z"))
	private boolean vigorem$cancelSneak(boolean original) {
		return original && !this.isAnimating();
	}
	@Inject(method = "setArmAngle", at = @At("TAIL"))
	private void vigorem$setArmAngle(Arm arm, MatrixStack matrices, CallbackInfo ci) {
		if(this.isAnimating())
			this.body.rotate(matrices);
	}
	@Inject(method = "copyBipedStateTo", at = @At("TAIL"))
	private void vigorem$setAttrs(BipedEntityModel<T> model, CallbackInfo ci) {
		((Methylenedioxymethamphetamine)model).setAnimating(this.isAnimating());
	}

	@Override
	public void setAnimating(boolean animating) {
		this.anim = animating;
	}

	@Override
	public boolean isAnimating() {
		return anim;
	}

	@Override
	public boolean shouldTransformHead() {
		return !((CrackCocaine)(Object)this.head).getHead();
	}

	@Override
	public void setHead(boolean yea) {
		((CrackCocaine)(Object)this.head).setHead(!yea);
	}
}
