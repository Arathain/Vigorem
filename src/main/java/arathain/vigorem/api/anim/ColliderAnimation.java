package arathain.vigorem.api.anim;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Vector3f;

public interface ColliderAnimation {

	private Vector3f add(Vector3f vec1, Vector3f vec2) {
		Vector3f vec = new Vector3f(vec1);
		vec.add(vec2);
		return vec;
	}
	void damage(Entity entity);

	void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, ClientPlayerEntity player, Camera camera);
}
