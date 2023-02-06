package arathain.vigorem.test;

import arathain.vigorem.Vigorem;
import arathain.vigorem.api.anim.Animation;
import arathain.vigorem.api.AnimatingWeaponItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

public class BrickShithouseHammerItem extends AnimatingWeaponItem {
	public BrickShithouseHammerItem(Settings settings) {
		super(settings);
	}

	@Override
	public Identifier getAnimId(PlayerEntity player, boolean rightClick, Arm arm, Animation current) {
		return Vigorem.id(arm == Arm.RIGHT ? "smash_right" : "smash_left");
	}

}
