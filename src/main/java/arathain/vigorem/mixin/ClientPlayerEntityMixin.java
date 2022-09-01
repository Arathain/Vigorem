package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import dev.onyxstudios.cca.api.v3.component.ComponentAccess;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	@Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
	public void vigorem$sneak(CallbackInfoReturnable<Boolean> info) {
		if (((Entity)(Object)this) instanceof PlayerEntity && ((ComponentAccess) this).getComponent(VigoremComponents.ANIMATION).current != null && ((ComponentAccess) this).getComponent(VigoremComponents.ANIMATION).current.isBlockingMovement()) {
			info.setReturnValue(false);
		}
	}
}
