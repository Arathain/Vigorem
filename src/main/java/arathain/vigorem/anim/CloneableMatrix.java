package arathain.vigorem.anim;

import net.minecraft.client.util.math.MatrixStack;

public interface CloneableMatrix extends Cloneable {
	MatrixStack cloneStack();
}
