package arathain.vigorem.anim.box;


import net.minecraft.util.math.Box;

public final class MutableBox {
	private Box box;

	public MutableBox(Box box) {
		this.box = box;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}
}
