package arathain.vigorem;

import arathain.vigorem.anim.EntityAnimationSyncPacket;
import eu.midnightdust.lib.config.MidnightConfig;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import static arathain.vigorem.Vigorem.MODID;

public class VigoremClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		ClientPlayNetworking.registerGlobalReceiver(EntityAnimationSyncPacket.ID, (client, handler, buf, responseSender) -> EntityAnimationSyncPacket.handle(client, buf));
		MidnightConfig.init(MODID, VigoremConfig.class);
	}
}
