package arathain.vigorem.anim;

import arathain.vigorem.Vigorem;
import arathain.vigorem.VigoremComponents;
import arathain.vigorem.api.AnimatingWeaponItem;
import arathain.vigorem.init.Animations;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import org.quiltmc.qsl.networking.impl.client.ClientNetworkingImpl;

import javax.annotation.Nullable;

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
			if(player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof AnimatingWeaponItem) {
				System.out.println(Animations.getAnimation(animId));
				player.getComponent(VigoremComponents.ANIMATION).queue(Animations.getAnimation(animId));
			}
		});
	}
}
