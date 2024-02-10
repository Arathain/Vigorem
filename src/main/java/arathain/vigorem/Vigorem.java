package arathain.vigorem;

import arathain.vigorem.api.AnimationPacket;
import arathain.vigorem.api.init.Animations;
import arathain.vigorem.test.BrickShithouseHammerItem;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.ArmorRenderingRegistryImpl;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.ArmorRendererProvider;
import virtuoel.pehkui.api.ScaleTypes;

public class Vigorem implements ModInitializer {
	public static final String MODID = "vigorem";
	public static final Logger LOGGER = LoggerFactory.getLogger("Vigorem");
	public static boolean firstPersonLoaded = QuiltLoader.isModLoaded("firstperson");
	public static boolean renderingFirstPerson = false;
	public static boolean pehkuiLoaded = QuiltLoader.isModLoaded("pehkui");
	private static final Vec3d ONE = new Vec3d(1, 1,1);

	@Override
	public void onInitialize(ModContainer mod) {
		Animations.init();
		if(QuiltLoader.isDevelopmentEnvironment()) {
			GeckoLib.initialize();
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
	public static Vec3d getPlayerScale(PlayerEntity p, float delta) {
		if(pehkuiLoaded) {
			float width = ScaleTypes.WIDTH.getScaleData(p).getScale(delta);
			return new Vec3d(width, ScaleTypes.HEIGHT.getScaleData(p).getScale(delta), width);
		}
		return ONE;
	}
	public static Identifier id(String name) {
		return new Identifier(MODID, name);
	}
}
