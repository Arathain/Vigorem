package arathain.vigorem;

import arathain.vigorem.anim.AnimationComponent;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class VigoremComponents implements EntityComponentInitializer {
	public static final ComponentKey<AnimationComponent> ANIMATION = ComponentRegistry.getOrCreate(Vigorem.id("animation"), AnimationComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(ANIMATION, AnimationComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
	}
}
