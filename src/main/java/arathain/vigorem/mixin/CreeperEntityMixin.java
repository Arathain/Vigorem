package arathain.vigorem.mixin;

import arathain.vigorem.anim.box.DoubleQuat;
import arathain.vigorem.anim.box.OrientedBox;
import arathain.vigorem.anim.box.RotatableEntityPart;
import org.quiltmc.qsl.entity.multipart.api.EntityPart;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import org.quiltmc.qsl.entity.multipart.api.EntityPart;
import org.quiltmc.qsl.entity.multipart.api.MultipartEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity implements MultipartEntity {
	private final RotatableEntityPart<CreeperEntity> secretHitboxParent = new RotatableEntityPart<>((CreeperEntity) (Object) this, 0f, 0f);
	private final RotatableEntityPart<CreeperEntity> secretHitbox = new RotatableEntityPart<>((CreeperEntity) (Object) this, 0.65f, 0.65f).setParent(secretHitboxParent);

	protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public EntityPart<?>[] getEntityParts() {
		return new EntityPart[] {this.secretHitbox, this.secretHitboxParent};
	}

	@Override
	public void tickMovement() {
		super.tickMovement();
		var cycle = 0.5f + (age % 100) / 100f;
		this.secretHitbox.scale(cycle);
		this.secretHitbox.setOffset(new Vec3d(0.0d, 1.1d, 0d));
		this.secretHitboxParent.rotate(0, -this.getHeadYaw(), 0.0f);
		this.secretHitbox.rotate(this.getPitch(), 0, 0.0f);
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		this.secretHitbox.setId(1 + packet.getId());
		this.secretHitboxParent.setId(2 + packet.getId());
	}
}
