package arathain.vigorem.test;

import arathain.vigorem.Vigorem;
import arathain.vigorem.anim.Animation;
import arathain.vigorem.anim.Keyframe;
import net.minecraft.block.AnvilBlock;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.explosion.Explosion;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SmashAnimation extends Animation {
	private final boolean right;
	public SmashAnimation(int length, Map<String, List<Keyframe>> keyframes, boolean right) {
		super(length, keyframes);
		this.right = right;
	}
	@Override
	public Identifier getId() {
		return Vigorem.id(right ? "smash_right" : "smash_left");
	}

	@Override
	public float getMovementMultiplier() {
		if(this.frame > 9 && this.frame < 24) {
			return 0f;
		}
		return 0.02f;
	}

	@Override
	public boolean isBlockingMovement() {
		return true;
	}

	@Override
	public void clientTick(PlayerEntity player) {
		super.clientTick(player);
		if(this.frame > 10 && this.frame < 22) {
			player.setBodyYaw(player.headYaw);
		}
		if(this.frame == 15) {
			Vec3d vec = new Vec3d(player.getRotationVecClient().x, 0, player.getRotationVecClient().z).multiply(2);
			player.getWorld().playSound(player.getX() + vec.x, player.getY(), player.getZ() + vec.z, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 0.4f, 0.8f + player.getRandom().nextFloat() * 0.2f, false);
		}
	}

	@Override
	public void serverTick(PlayerEntity player) {
		super.serverTick(player);
		if(this.frame > 10 && this.frame < 22) {
			player.setBodyYaw(player.headYaw);
		}
		if(this.frame == 15) {
			Vec3d vec = new Vec3d(player.getRotationVector().x, 0, player.getRotationVector().z).multiply(2);
			player.getWorld().getEntitiesByClass(LivingEntity.class, player.getBoundingBox().offset(vec).expand(0.5, 0.4, 0.5), livingEntity -> livingEntity.isAlive() && !livingEntity.isInvulnerable()).forEach(entity -> {
				entity.damage(DamageSource.mob(player), (float) (9.0f + player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)));
				entity.addVelocity((player.getRandom().nextFloat() - 0.5f) * 0.2f, 0.3, (player.getRandom().nextFloat() - 0.5f) * 0.2f);
				if(!player.getWorld().getBlockState(player.getBlockPos().add(MathHelper.floor(vec.x), MathHelper.floor(vec.y), MathHelper.floor(vec.z))).isAir()) {
					player.getWorld().setBlockBreakingInfo(player.getId(), player.getBlockPos().add(MathHelper.floor(vec.x), MathHelper.floor(vec.y), MathHelper.floor(vec.z)), (int) ((float) 0.6f * 10.0F));
				}
				if(!player.getWorld().getBlockState(player.getBlockPos().add(MathHelper.floor(vec.x), MathHelper.floor(vec.y - 1), MathHelper.floor(vec.z))).isAir()) {
					player.getWorld().setBlockBreakingInfo(player.getId(), player.getBlockPos().add(MathHelper.floor(vec.x), MathHelper.floor(vec.y), MathHelper.floor(vec.z)), (int) ((float) 0.6f * 10.0F));
				}
			});
		}
	}
}
