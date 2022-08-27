package arathain.vigorem;

import arathain.vigorem.anim.AnimationComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

public class VigoremComponents implements EntityComponentInitializer {
	public static final ComponentKey<AnimationComponent> ANIMATION = ComponentRegistry.getOrCreate(Vigorem.id("player"), AnimationComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(ANIMATION, AnimationComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
	}
}
