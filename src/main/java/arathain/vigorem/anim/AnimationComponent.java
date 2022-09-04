package arathain.vigorem.anim;

import arathain.vigorem.VigoremComponents;
import arathain.vigorem.init.Animations;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

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
			this.current.serverTick(obj);
			if(this.current.shouldRemove() || this.current.canCancel()) {
				this.current = this.queued;
				this.queued = null;
			}
			sync();
		} else {
			if(this.queued != null) {
				this.current = this.queued;
				this.queued = null;
				sync();
			}
		}
	}
	public void clientTick() {
		if(current != null) {
			this.current.tick();
			this.current.clientTick(obj);
			if(this.current.shouldRemove()) {
				this.current = null;
			}
		}
	}
	@Override
	public void readFromNbt(NbtCompound tag) {
		if(tag.contains("namespace") && Animations.getAnimation(new Identifier(tag.getString("namespace"), tag.getString("path"))) != null) {
			this.current = Animations.getAnimation(new Identifier(tag.getString("namespace"), tag.getString("path")));
			this.current.setFrame(tag.getInt("time"));
		}
	}
	public void queue(Animation anim) {
		if(anim != null) {
			this.queued = anim;
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		if(current != null) {
			tag.putString("namespace", current.getId().getNamespace());
			tag.putString("path", current.getId().getPath());
			tag.putInt("time", current.frame);
		}
	}
	public void sync() {
		VigoremComponents.ANIMATION.sync(obj);
	}
}
