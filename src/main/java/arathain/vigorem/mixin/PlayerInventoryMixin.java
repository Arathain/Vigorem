package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
	@Unique
	private int selectedLockedSlot = -1;
	@Shadow
	public int selectedSlot;
	@Shadow
	@Final
	public PlayerEntity player;

	@Inject(method = "getMainHandStack", at = @At("HEAD"))
	private void charter$checkOne(CallbackInfoReturnable<ItemStack> cir) {
		checkLock();
	}
	@Inject(method = "scrollInHotbar(D)V", at = @At("TAIL"))
	private void charter$checkThree(double scrollAmount, CallbackInfo ci) {
		checkLock();
	}
	@Inject(method = "getOccupiedSlotWithRoomForStack(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"))
	private void charter$checkSix(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		checkLock();
	}
	@Inject(method = "addPickBlock(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
	private void charter$checkTwo(ItemStack stack, CallbackInfo ci) {
		checkLock();
	}
	@Inject(method = "getSwappableHotbarSlot()I", at = @At("RETURN"))
	private void charter$checkFour(CallbackInfoReturnable<Integer> cir) {
		checkLock();
	}
	@Inject(method = "dropSelectedItem(Z)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true)
	private void charter$checkFive(boolean bl, CallbackInfoReturnable<ItemStack> cir) {
		if(yeah()) {
			cir.setReturnValue(ItemStack.EMPTY);
		}
	}
	@Unique
	private boolean yeah() {
		if(player.getComponent(VigoremComponents.ANIMATION).current != null && player.getComponent(VigoremComponents.ANIMATION).current.lockHeldItem()) {
			if(selectedLockedSlot == -1) {
				selectedLockedSlot = selectedSlot;
			}
			selectedSlot = selectedLockedSlot;
			return true;
		} else {
			selectedLockedSlot = -1;
			return false;
		}
	}
	@Unique
	private void checkLock() {
		if(player.getComponent(VigoremComponents.ANIMATION).current != null && player.getComponent(VigoremComponents.ANIMATION).current.lockHeldItem()) {
			if(selectedLockedSlot == -1) {
				selectedLockedSlot = selectedSlot;
			}
			selectedSlot = selectedLockedSlot;
		} else {
			selectedLockedSlot = -1;
		}
	}
}
