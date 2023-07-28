package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import com.mojang.authlib.GameProfile;
import dev.onyxstudios.cca.api.v3.component.ComponentAccess;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	public ClientPlayerEntityMixin(ClientWorld world, GameProfile gameProfile) {
		super(world, gameProfile);
	}

	@Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
	public void vigorem$sneak(CallbackInfoReturnable<Boolean> info) {
		if (this.getComponent(VigoremComponents.ANIMATION).current != null && this.getComponent(VigoremComponents.ANIMATION).current.isBlockingMovement()) {
			info.setReturnValue(false);
		}
	}
}
