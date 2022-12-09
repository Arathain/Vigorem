package arathain.vigorem.api;

import arathain.vigorem.anim.Animation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class MovesetWeaponItem extends AnimatingWeaponItem {
	private final List<MoveSet> moveset;

	public MovesetWeaponItem(Settings settings, List<MoveSet> moveset) {
		super(settings);
		this.moveset = moveset;
	}

	@Override
	public Identifier getAnimId(PlayerEntity player, boolean rightClick, Arm arm, @Nullable Animation current) {
		for(MoveSet moveset : moveset) {
			if(moveset.shouldApply(player, rightClick)) {
				return moveset.getAnimId(current);
			}
		}
		return null;
	}
}
