package arathain.vigorem.mixin;

import arathain.vigorem.anim.box.OrientedBox;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VoxelShapes.class)
public class VoxelShapesMixin {
	@Inject(method = "cuboid(Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
	private static void hook(final Box box, final CallbackInfoReturnable<VoxelShape> cir) {
		if (box instanceof OrientedBox) {
			cir.setReturnValue(((OrientedBox) box).toVoxelShape());
		}
	}
}
