package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.anim.AnimationComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentAccess;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
	private Vec3d vigorem$modifyMovement(Vec3d value, MovementType movementType) {
		if (movementType == MovementType.SELF) {
			AnimationComponent component = VigoremComponents.ANIMATION.getNullable(this);
			if (component != null && component.current != null) {
				return value.multiply(component.current.getMovementMultiplier(), 1, component.current.getMovementMultiplier());
			}
		}
		return value;
	}
}
