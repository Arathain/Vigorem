package arathain.vigorem.api.anim.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Supplier;

public interface AnimatedEntity {
	<T extends Entity & AnimatedEntity> Map<Identifier, Supplier<? extends EntityAnimation<T>>> getAnimations();
	<T extends Entity & AnimatedEntity> ModelPart getPart(String string, EntityModel<T> model);

	NbtCompound writeAnimNbt(NbtCompound nbt);
	void readAnimNbt(NbtCompound nbt);
}
