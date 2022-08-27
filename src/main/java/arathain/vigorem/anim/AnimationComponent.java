package arathain.vigorem.anim;

import arathain.vigorem.VigoremComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import javax.annotation.Nullable;

public class AnimationComponent implements AutoSyncedComponent {
	@Nullable
	public Animation current;
	@Nullable
	private Animation queued;
	private final PlayerEntity obj;

	public AnimationComponent(PlayerEntity player) {
		this.obj = player;
	}
	public void serverTick() {
		if(current != null) {
			this.current.tick();
			if(this.current.shouldRemove()) {
				this.current = this.queued;
			}
			sync();
		}
	}
	@Override
	public void readFromNbt(NbtCompound tag) {

	}

	@Override
	public void writeToNbt(NbtCompound tag) {

	}
	public void sync() {
		VigoremComponents.ANIMATION.sync(obj);
	}
}
