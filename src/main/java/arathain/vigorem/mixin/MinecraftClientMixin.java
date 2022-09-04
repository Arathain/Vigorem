package arathain.vigorem.mixin;

import arathain.vigorem.anim.AnimationPacket;
import arathain.vigorem.api.AnimatingWeaponItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBind;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import javax.swing.text.JTextComponent;

import static arathain.vigorem.api.AnimatingWeaponItem.AttackType.*;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Shadow @Nullable
	public ClientPlayerEntity player;

	@Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
	private void pre_doAttack(CallbackInfoReturnable<Boolean> info) {
		MinecraftClient client = (MinecraftClient)((Object)this);
		if (client.player.getMainHandStack().getItem() instanceof AnimatingWeaponItem anim) {
			AnimationPacket.send(anim.getAnimId(client.player.isSneaking() ? NORMAL : SHIFT, false, client.player.getMainArm()));
			info.setReturnValue(false);
			info.cancel();
		}
	}

}
