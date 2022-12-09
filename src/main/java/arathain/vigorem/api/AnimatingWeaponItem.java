package arathain.vigorem.api;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.anim.Animation;
import arathain.vigorem.init.Animations;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AnimatingWeaponItem extends Item {
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if(useRightClick()) {
			Identifier id = getAnimId(user, true, hand == Hand.MAIN_HAND ? user.getMainArm() : user.getMainArm().getOpposite(), user.getComponent(VigoremComponents.ANIMATION).current);
			if (id != null) {
				user.getComponent(VigoremComponents.ANIMATION).queue(Animations.getAnimation(id));
				return TypedActionResult.success(user.getStackInHand(hand), false);
			}
		}
		return super.use(world, user, hand);
	}

	public AnimatingWeaponItem(Settings settings) {
		super(settings);
	}
	private boolean useRightClick() {
		return true;
	}
	public abstract Identifier getAnimId(PlayerEntity player, boolean rightClick, Arm arm, @Nullable Animation current);
}
