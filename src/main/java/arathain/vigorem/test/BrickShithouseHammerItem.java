package arathain.vigorem.test;

import arathain.vigorem.Vigorem;
import arathain.vigorem.api.AnimatingWeaponItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class BrickShithouseHammerItem extends AnimatingWeaponItem {
	public BrickShithouseHammerItem(Settings settings) {
		super(settings);
	}

	@Override
	public Identifier getAnimId(AttackType type, boolean rightClick, Arm arm) {
		return Vigorem.id(arm == Arm.RIGHT ? "smash_right" : "smash_left");
	}

	@Override
	public Vec3d getHitbox() {
		return new Vec3d(1, 0.4, 0.4).multiply(2);
	}

	@Override
	public Vec3d getHitboxOffset() {
		return new Vec3d(0, 2, 0);
	}
}
