package arathain.vigorem.test;

import arathain.vigorem.anim.box.RotatableEntityPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.math.Vec3d;

public class SecretCreeperPart extends RotatableEntityPart<CreeperEntity> {
	public SecretCreeperPart(CreeperEntity creeper, float width, float height, Vec3d relativePosition, Vec3d relativePivot) {
		super(creeper, width, height);
		this.setRelativePosition(relativePosition);
		this.setPivot(relativePivot);
	}

	public boolean damage(DamageSource source, float amount) {
		return super.damage(source, amount * 100);
	}
}
