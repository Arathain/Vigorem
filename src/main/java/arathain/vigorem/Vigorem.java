package arathain.vigorem;

import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vigorem implements ModInitializer {
	public static final String MODID = "vigorem";
	public static final Logger LOGGER = LoggerFactory.getLogger("Vigorem");

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
	}
	public static Identifier id(String name) {
		return new Identifier(MODID, name);
	}
}
