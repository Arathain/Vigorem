package arathain.vigorem.api.anim;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3f;

public interface ColliderAnimation {

	private Vec3f add(Vec3f vec1, Vec3f vec2) {
		Vec3f vec = vec1.copy();
		vec.add(vec2);
		return vec;
	}
	void damage(Entity entity);

	void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, ClientPlayerEntity player, Camera camera);
}
