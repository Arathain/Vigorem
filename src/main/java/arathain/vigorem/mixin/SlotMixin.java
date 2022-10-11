package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin {
	@Inject(method = "canTakeItems", at = @At("HEAD"), cancellable = true)
	private void charter$checkSix(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		if(player.getComponent(VigoremComponents.ANIMATION).current != null && player.getComponent(VigoremComponents.ANIMATION).current.lockHeldItem()) {
			cir.setReturnValue(false);
		}
	}
}
