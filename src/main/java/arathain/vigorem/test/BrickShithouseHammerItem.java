package arathain.vigorem.test;

import arathain.vigorem.Vigorem;
import arathain.vigorem.api.AnimatingWeaponItem;
import net.minecraft.client.render.entity.model.EndermanEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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
	public Identifier getAnimId(PlayerEntity player, AttackType type, boolean rightClick, Arm arm) {
		return Vigorem.id(arm == Arm.RIGHT ? "smash_right" : "smash_left");
	}

	public Vec3d getHitbox() {
		return new Vec3d(0.3, 0.3, 0.75);
	}

	public Vec3d getHitboxOffset() {
		return new Vec3d(0, 1, 0);
	}
}
