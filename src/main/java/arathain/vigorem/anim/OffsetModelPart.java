package arathain.vigorem.anim;

import net.minecraft.client.model.ModelPart;

public interface OffsetModelPart {
	float getOffsetX();
	float getOffsetY();
	float getOffsetZ();
	void setOffset(float x, float y, float z);
	boolean isChild(ModelPart part);
}
