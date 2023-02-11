package arathain.vigorem.anim;

import net.minecraft.client.model.ModelPart;

import java.util.function.Supplier;

public interface CrackCocaine {
	Supplier<ModelPart> getParent();
	void setParent(Supplier<ModelPart> p);
	void setHead(boolean yea);
	boolean getHead();
}
