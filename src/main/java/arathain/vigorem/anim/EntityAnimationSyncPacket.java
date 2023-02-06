package arathain.vigorem.anim;

import arathain.vigorem.Vigorem;
import arathain.vigorem.api.anim.entity.AnimatedEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.Collection;

public class EntityAnimationSyncPacket {
	public static final Identifier ID = Vigorem.id("entity_animation_sync");

	public static void send(Entity entity, NbtCompound animNbt, Collection<ServerPlayerEntity> e) {
		PacketByteBuf buf = PacketByteBufs.create();

		if(entity instanceof AnimatedEntity) {
			buf.writeInt(entity.getId());
			buf.writeNbt(animNbt);
		}

		ServerPlayNetworking.send(e, ID, buf);
	}

	public static void handle(MinecraftClient cli, PacketByteBuf buf) {
		int entityId = buf.isReadable() ? buf.readInt() : -1;
		NbtCompound toRead = buf.isReadable() ? buf.readNbt() : null;
		cli.execute(() -> {
			if(cli.world.getEntityById(entityId) instanceof AnimatedEntity e) {
				e.readAnimNbt(toRead);
			}
		});
	}
}
