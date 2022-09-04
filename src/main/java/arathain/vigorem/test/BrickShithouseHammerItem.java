package arathain.vigorem.test;

import arathain.vigorem.Vigorem;
import arathain.vigorem.api.AnimatingWeaponItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class BrickShithouseHammerItem extends AnimatingWeaponItem {
	public BrickShithouseHammerItem(Settings settings) {
		super(settings);
	}

	@Override
	public Identifier getAnimId(AttackType type, boolean rightClick, Arm arm) {
		return Vigorem.id(arm == Arm.RIGHT ? "smash_right" : "smash_left");
	}
}
