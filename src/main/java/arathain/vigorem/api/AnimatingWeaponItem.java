package arathain.vigorem.api;

import arathain.vigorem.Vigorem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public abstract class AnimatingWeaponItem extends Item {
	public AnimatingWeaponItem(Settings settings) {
		super(settings);
	}
	public abstract Identifier getAnimId(PlayerEntity player, AttackType type, boolean rightClick, Arm arm);
	public enum AttackType {
		NORMAL,
		SHIFT,
		SPRINTING,
		JUMP
	}
}
