package arathain.vigorem;

import arathain.vigorem.anim.EntityAnimationSyncPacket;
import arathain.vigorem.api.AnimationPacket;
import arathain.vigorem.api.init.Animations;
import arathain.vigorem.test.BrickShithouseHammerItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoel.pehkui.api.ScaleTypes;

public class Vigorem implements ModInitializer {
	public static final String MODID = "vigorem";
	public static final Logger LOGGER = LoggerFactory.getLogger("Vigorem");
	public static boolean firstPersonLoaded = FabricLoader.getInstance().isModLoaded("firstperson");
	public static boolean renderingFirstPerson = false;
	public static boolean pehkuiLoaded = FabricLoader.getInstance().isModLoaded("pehkui");
	private static final Vec3d ONE = new Vec3d(1, 1,1);

	@Override
	public void onInitialize() {
		Animations.init();
		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			UseItemCallback.EVENT.register((player, world, hand) -> {
				if (player.getStackInHand(hand).isOf(Items.ECHO_SHARD) && !world.isClient) {
					player.getComponent(VigoremComponents.ANIMATION).queue(Animations.getAnimation(Vigorem.id("t_pose")));
				}
				return TypedActionResult.pass(player.getStackInHand(hand));
			});
			Registry.register(Registries.ITEM, Vigorem.id("hammah"), new BrickShithouseHammerItem(new Item.Settings().rarity(Rarity.EPIC).maxDamage(2000)));
		}
		PayloadTypeRegistry.playS2C().register(EntityAnimationSyncPacket.PACKET_ID, EntityAnimationSyncPacket.PACKET_CODEC);
		PayloadTypeRegistry.playC2S().register(AnimationPacket.PACKET_ID, AnimationPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(AnimationPacket.PACKET_ID, (payload, context) -> {
			payload.handle(context.server(), context.player());
		});
	}
	public static Vec3d getPlayerScale(PlayerEntity p, float delta) {
		if(pehkuiLoaded) {
			float width = ScaleTypes.WIDTH.getScaleData(p).getScale(delta);
			return new Vec3d(width, ScaleTypes.HEIGHT.getScaleData(p).getScale(delta), width);
		}
		return ONE;
	}
	public static Identifier id(String name) {
		return Identifier.of(MODID, name);
	}
}
