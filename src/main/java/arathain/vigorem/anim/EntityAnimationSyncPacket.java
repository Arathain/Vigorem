package arathain.vigorem.anim;

import arathain.vigorem.Vigorem;
import arathain.vigorem.api.anim.entity.AnimatedEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record EntityAnimationSyncPacket(int entityID, NbtCompound animnbt) implements CustomPayload {

	public static final Identifier ID = Vigorem.id("entity_animation_sync");
	public static final CustomPayload.Id<EntityAnimationSyncPacket> PACKET_ID = new CustomPayload.Id<>(ID);
	public static final PacketCodec<RegistryByteBuf, EntityAnimationSyncPacket> PACKET_CODEC = PacketCodec.of((val, buf) -> {
		buf.writeInt(val.entityID);
		buf.writeNbt(val.animnbt);
	}, buf -> new EntityAnimationSyncPacket(buf.readInt(), buf.readNbt()));

	public void handle(MinecraftClient cli) {
		cli.execute(() -> {
			if(cli.world != null && cli.world.getEntityById(entityID) instanceof AnimatedEntity e) {
				e.readAnimNbt(animnbt);
			}
		});
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
