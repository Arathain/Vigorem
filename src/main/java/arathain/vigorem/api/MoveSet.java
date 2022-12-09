package arathain.vigorem.api;

import arathain.vigorem.anim.Animation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MoveSet {
	private final List<Identifier> anims;
	private final Predicate<Pair<PlayerEntity, Boolean>> predicate;
	public MoveSet(Predicate<Pair<PlayerEntity, Boolean>> applicationPredicate, Identifier... animationIds) {
		this.anims = Arrays.stream(animationIds).toList();
		this.predicate = applicationPredicate;
	}
	public boolean shouldApply(PlayerEntity player, boolean rightClick) {
		return predicate.test(new Pair<>(player, rightClick));
	}

	public Identifier getAnimId(Animation current) {
		if(!anims.contains(current.getId()) || anims.size() == 1 || (anims.contains(current.getId()) && anims.indexOf(current.getId()) == anims.size() - 1)) {
			return anims.get(0);
		} else {
			return anims.get(anims.indexOf(current.getId()) + 1);
		}
	}
}
