package arathain.vigorem.api;

import arathain.vigorem.Vigorem;
import arathain.vigorem.VigoremComponents;
import arathain.vigorem.api.init.Animations;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import static net.minecraft.entity.EntityPose.CROUCHING;
import static net.minecraft.entity.EntityPose.STANDING;

public record AnimationPacket(Identifier id) implements CustomPayload {

	public static final Identifier ID = Vigorem.id("animation");
	public static final CustomPayload.Id<AnimationPacket> PACKET_ID = new CustomPayload.Id<>(ID);
	public static final PacketCodec<RegistryByteBuf, AnimationPacket> PACKET_CODEC = PacketCodec.of((val, buf) -> {
		buf.writeIdentifier(val.id);
	}, buf ->  new AnimationPacket(buf.readIdentifier()));

	public void handle(MinecraftServer server, ServerPlayerEntity player) {
		Identifier animId = id;

		server.execute(() -> {
			if(player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof AnimatingWeaponItem && (player.getPose().equals(STANDING) || player.getPose().equals(CROUCHING))) {
				player.getComponent(VigoremComponents.ANIMATION).queue(Animations.getAnimation(animId));
			}
		});
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
