package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
	public void vigorem$sneak(CallbackInfoReturnable<Boolean> info) {
		if (this.getComponent(VigoremComponents.ANIMATION).current != null && this.getComponent(VigoremComponents.ANIMATION).current.isBlockingMovement()) {
			info.setReturnValue(false);
		}
	}
}
