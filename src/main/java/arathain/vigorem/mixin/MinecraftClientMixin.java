package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.api.AnimatingWeaponItem;
import arathain.vigorem.api.AnimationPacket;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.entity.EntityPose.CROUCHING;
import static net.minecraft.entity.EntityPose.STANDING;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
	private void vigorem$doAttack(CallbackInfoReturnable<Boolean> info) {
		MinecraftClient client = (MinecraftClient)((Object)this);
		if (client.player.getMainHandStack().getItem() instanceof AnimatingWeaponItem anim && (player.getPose().equals(STANDING) || player.getPose().equals(CROUCHING))) {
			Identifier id = anim.getAnimId(client.player, false, client.player.getMainArm(), client.player.getComponent(VigoremComponents.ANIMATION).current);
			if(id != null) {
				AnimationPacket.send(id);
				info.setReturnValue(false);
				info.cancel();
			}
		}
	}

	@WrapWithCondition(
			method = "handleInputEvents",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V")
	)
	private boolean vigorem$hand(ClientPlayNetworkHandler instance, Packet packet) {
		return player != null && !(player.getComponent(VigoremComponents.ANIMATION).current != null && player.getComponent(VigoremComponents.ANIMATION).current.lockHeldItem());
	}

}
