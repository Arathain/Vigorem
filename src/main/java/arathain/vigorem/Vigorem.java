package arathain.vigorem;

import arathain.vigorem.init.Animations;
import arathain.vigorem.test.TPoseAnimation;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vigorem implements ModInitializer {
	public static final String MODID = "vigorem";
	public static final Logger LOGGER = LoggerFactory.getLogger("Vigorem");

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
		Animations.init();
		if(QuiltLoader.isDevelopmentEnvironment()) {
			UseItemCallback.EVENT.register((player, world, hand) -> {
				if (player.getStackInHand(hand).isOf(Items.ECHO_SHARD) && !world.isClient) {
					player.getComponent(VigoremComponents.ANIMATION).queue(Animations.getAnimation(Vigorem.id("t_pose")));
				}
				return TypedActionResult.success(player.getStackInHand(hand));
			});
		}
	}
	public static Identifier id(String name) {
		return new Identifier(MODID, name);
	}
}
