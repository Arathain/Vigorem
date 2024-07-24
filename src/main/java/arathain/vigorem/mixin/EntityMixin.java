package arathain.vigorem.mixin;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.anim.AnimationComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
	private Vec3d vigorem$modifyMovement(Vec3d value, MovementType movementType) {
		if (movementType == MovementType.SELF) {
			AnimationComponent component = VigoremComponents.ANIMATION.getNullable(this);
			if (component != null && component.current != null) {
				return value.multiply(component.current.getMovementMultiplier(), component.current.isAffectingGravity() ? component.current.getMovementMultiplier() : 1, component.current.getMovementMultiplier());
			}
		}
		return value;
	}

}
