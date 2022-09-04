package arathain.vigorem.api;

import arathain.vigorem.Vigorem;
import net.minecraft.item.Item;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public abstract class AnimatingWeaponItem extends Item {
	public AnimatingWeaponItem(Settings settings) {
		super(settings);
	}
	public abstract Identifier getAnimId(AttackType type, boolean rightClick, Arm arm);
	public enum AttackType {
		NORMAL,
		SHIFT,
		SPRINTING,
		JUMP
	}
}
