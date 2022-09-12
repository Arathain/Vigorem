package arathain.vigorem.anim.box;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.quiltmc.qsl.entity.multipart.api.AbstractEntityPart;
import org.quiltmc.qsl.entity.multipart.api.EntityPart;

import javax.annotation.Nullable;

public class RotatableEntityPart<E extends Entity> extends AbstractEntityPart<E> {
	private boolean changed = true;
	private double offX,offY,offZ;
	private double x, y, z = 0;
	private final Box box;
	private double px, py, pz = 0;
	private DoubleQuat rotation;
	private RotatableEntityPart<E> parent;
	public RotatableEntityPart(E owner, float width, float height) {
		super(owner, width, height);
		float f = width/ 2.0F;
		float g = height;
		box = new Box(x - (double)f, y, z - (double)f, x + (double)f, y + (double)g, z + (double)f);
		rotation = DoubleQuat.IDENTITY;
		setX(0.0f);
		setY(0.0f);
		setZ(0.0f);
	}
	/**
	 * @param parent The name of the parent part, the parent part needs to be built first
	 * @return this
	 */
	public RotatableEntityPart<E> setParent(@Nullable final RotatableEntityPart<E> parent) {
		this.parent = parent;
		return this;
	}
	public void setOffX(double offX) {
		this.offX = offX;
	}

	public void setOffY(double offY) {
		this.offY = offY;
	}

	public void setOffZ(double offZ) {
		this.offZ = offZ;
	}

	/**
	 * @param x X coordinate relative to parent
	 */
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * @param y Y coordinate relative to parent
	 */
	public void setY(final double y) {
		this.y = y;
	}

	/**
	 * @param z Z coordinate relative to parent
	 */
	public void setZ(final double z) {
		this.z = z;
	}


	@Override
	public void rotate(float pitch, float yaw, float roll) {
		rotation = new DoubleQuat(pitch, yaw, roll, true);
	}

	@Override
	public void rotate(Vec3d pivot, float pitch, float yaw, float roll) {
		this.px = pivot.x;
		this.py = pivot.y;
		this.pz = pivot.z;
		rotation = new DoubleQuat(pitch, yaw, roll, true);
	}

	@Override
	public void setRelativePosition(Vec3d position) {
		this.x = position.x+offX;
		this.y = position.y+offY;
		this.z = position.z+offZ;
	}
	public void setOffset(Vec3d position) {
		this.offX = position.x;
		this.offY = position.y;
		this.offZ = position.z;
	}

	@Override
	public void setPivot(Vec3d pivot) {
		this.px = pivot.x;
		this.py = pivot.y;
		this.pz = pivot.z;
	}
	/**
	 * @return Oriented box represented by this EntityPart after all transformations have been applied
	 */
	public OrientedBox getBox() {
		final OrientedBox orientedBox = new OrientedBox(box);
		return transformChild(orientedBox);
	}

	private OrientedBox transformChild(OrientedBox orientedBox) {
		if (parent != null) {
			orientedBox = parent.transformChild(orientedBox);
		}
		return orientedBox.transform(x, y, z, px, py, pz, rotation).offset(offX, offY, offZ);
	}
}
