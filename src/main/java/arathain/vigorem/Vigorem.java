package arathain.vigorem;

import arathain.vigorem.anim.AnimationPacket;
import arathain.vigorem.init.Animations;
import arathain.vigorem.test.BrickShithouseHammerItem;
import arathain.vigorem.test.TPoseAnimation;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vigorem implements ModInitializer {
	public static final String MODID = "vigorem";
	public static final Logger LOGGER = LoggerFactory.getLogger("Vigorem");

	@Override
	public void onInitialize(ModContainer mod) {
		Animations.init();
		if(QuiltLoader.isDevelopmentEnvironment()) {
			UseItemCallback.EVENT.register((player, world, hand) -> {
				if (player.getStackInHand(hand).isOf(Items.ECHO_SHARD) && !world.isClient) {
					player.getComponent(VigoremComponents.ANIMATION).queue(Animations.getAnimation(Vigorem.id("t_pose")));
				}
				return TypedActionResult.pass(player.getStackInHand(hand));
			});
			Registry.register(Registry.ITEM, Vigorem.id("hammah"), new BrickShithouseHammerItem(new QuiltItemSettings().rarity(Rarity.EPIC).maxDamage(2000)));
		}
		ServerPlayNetworking.registerGlobalReceiver(AnimationPacket.ID, AnimationPacket::handle);
	}
	public static Identifier id(String name) {
		return new Identifier(MODID, name);
	}
}
