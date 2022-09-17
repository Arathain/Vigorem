package arathain.vigorem.api;

import arathain.vigorem.anim.Animation;
import arathain.vigorem.anim.Keyframe;
import arathain.vigorem.anim.box.OrientedBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class WeaponAnimation extends Animation {
	public WeaponAnimation(int length, Map<String, List<Keyframe>> keyframes) {
		super(length, keyframes);
	}

	@Override
	public void serverTick(PlayerEntity player) {
		super.serverTick(player);
		if(player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof AnimatingWeaponItem item) {
			Vec3f bodyRot = this.getRot("body", 0);
			Vec3f armRot = this.getRot("right_arm", 0);
			Vec3f handRot = this.getRot("right_hand", 0);
			Vec3d offset = new Vec3d(0, 0.75, 0).add(new Vec3d(-0.3125, (14d / 16d), 0).rotateY(-bodyRot.getY()).rotateX(-bodyRot.getX()).rotateZ(bodyRot.getZ()));
			offset.add(new Vec3d(2/16d, -10/16d, 0)/*.rotateY(-bodyRot.getY()).rotateX(-bodyRot.getX()).rotateZ(bodyRot.getZ())*/);
			Vec3d weaponPos = player.getPos().add(offset.rotateY((float) (player.getYaw()*-Math.PI/180f)));
			bodyRot.add(0, (float) (player.getYaw()*Math.PI/180f), 0);
			OrientedBox box = new OrientedBox(weaponPos, item.getHitbox(), (float) (bodyRot.getY() * -180/Math.PI),(float) (bodyRot.getX() * 180/Math.PI));
			box.updateVertex();
			List<Entity> results = player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(10)).stream().filter(entity -> box.intersects(entity.getBoundingBox())).toList();
			System.out.println(results.size() != 0);
		}
	}
	public abstract void damage(Entity entity);
}
