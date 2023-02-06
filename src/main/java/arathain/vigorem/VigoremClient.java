package arathain.vigorem;

import arathain.vigorem.anim.EntityAnimationSyncPacket;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class VigoremClient implements ClientModInitializer {

	@Override
	public void onInitializeClient(ModContainer mod) {
		ClientPlayNetworking.registerGlobalReceiver(EntityAnimationSyncPacket.ID, (client, handler, buf, responseSender) -> EntityAnimationSyncPacket.handle(client, buf));
	}
}
