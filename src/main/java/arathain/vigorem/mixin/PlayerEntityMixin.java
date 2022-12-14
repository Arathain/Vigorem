package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void vigorem$tick(CallbackInfo info) {
		if(!world.isClient) {
			this.getComponent(VigoremComponents.ANIMATION).serverTick();
		} else {
			this.getComponent(VigoremComponents.ANIMATION).clientTick();
		}
	}
	@Inject(method = "jump", at = @At("HEAD"), cancellable = true)
	public void vigorem$jump(CallbackInfo info) {
		if (this.getComponent(VigoremComponents.ANIMATION).current != null && this.getComponent(VigoremComponents.ANIMATION).current.isBlockingMovement()) {
			info.cancel();
		}
	}

}
