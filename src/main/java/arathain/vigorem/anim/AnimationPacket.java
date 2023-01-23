package arathain.vigorem.anim;

import arathain.vigorem.Vigorem;
import arathain.vigorem.VigoremComponents;
import arathain.vigorem.api.AnimatingWeaponItem;
import arathain.vigorem.init.Animations;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import static net.minecraft.entity.EntityPose.CROUCHING;
import static net.minecraft.entity.EntityPose.STANDING;

public class AnimationPacket {
	public static final Identifier ID = Vigorem.id("animation");

	public static void send(Identifier id) {
		PacketByteBuf buf = PacketByteBufs.create();

		if(id != null) {
			buf.writeIdentifier(id);
		}

		ClientPlayNetworking.send(ID, buf);
	}

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		Identifier animId = buf.isReadable() ? buf.readIdentifier() : null;

		server.execute(() -> {
			if(player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof AnimatingWeaponItem && (player.getPose().equals(STANDING) || player.getPose().equals(CROUCHING))) {
				player.getComponent(VigoremComponents.ANIMATION).queue(Animations.getAnimation(animId));
			}
		});
	}
}
