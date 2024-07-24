package arathain.vigorem;

import arathain.vigorem.anim.EntityAnimationSyncPacket;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

import static arathain.vigorem.Vigorem.MODID;

public class VigoremClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(EntityAnimationSyncPacket.PACKET_ID, (p, c) -> p.handle(c.client()));
		if(FabricLoader.getInstance().isDevelopmentEnvironment())
			MidnightConfig.init(MODID, VigoremConfig.class);
	}
}
