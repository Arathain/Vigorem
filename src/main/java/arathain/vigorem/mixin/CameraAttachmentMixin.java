package arathain.vigorem.mixin;

import arathain.vigorem.anim.CameraAttachment;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraAttachmentMixin implements CameraAttachment {
	@Unique
	private Vec3d vigorem$offs = Vec3d.ZERO;

	@Override
	public void vigorem$setOffset(Vec3d offset) {
		this.vigorem$offs = offset;
	}

	@ModifyReturnValue(method = "getPos", at = @At("RETURN"))
	private Vec3d vigorem$posOffs(Vec3d original) {
		if(!vigorem$offs.equals(Vec3d.ZERO)) {
			original.add(vigorem$offs);
		}
		return original;
	}
}
