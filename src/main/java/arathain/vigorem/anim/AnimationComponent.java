package arathain.vigorem.anim;

import arathain.vigorem.VigoremClient;
import arathain.vigorem.VigoremComponents;
import arathain.vigorem.api.AnimationCycle;
import arathain.vigorem.api.anim.Animation;
import arathain.vigorem.api.anim.ExtendableAnimation;
import arathain.vigorem.api.init.Animations;
import arathain.vigorem.test.SneakCycle;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;


public class AnimationComponent implements AutoSyncedComponent {
	@Nullable
	public Animation current;
	@Nullable
	public Animation queued;

	@ClientOnly
	@Nullable
	public AnimationCycle currentCycle;
	private final PlayerEntity obj;

	public AnimationComponent(PlayerEntity player) {
		this.obj = player;
	}
	public void serverTick() {
		if(current != null) {
			this.current.tick();
			this.current.serverTick(obj);
			if(this.current instanceof ExtendableAnimation c && c.shouldEnd(obj)) {
				c.update();
			}
			if(this.current.shouldRemove() || (this.queued != null && this.current.canCancel(this.queued))) {
				this.current = this.queued;
				this.queued = null;
			}
			sync();
		}
		if(current == null) {
			if(this.queued != null) {
				this.current = this.queued;
				this.queued = null;
				sync();
			}
		}
	}
	public void clientTick() {
		if (VigoremClient.fancySneak && obj.isSneaking()) {
			if(!(currentCycle instanceof SneakCycle)) {
				currentCycle = new SneakCycle();
			}
		}
		if(currentCycle != null) {
			currentCycle.tick(this.obj, MinecraftClient.getInstance());
			if(currentCycle.shouldRemove(this.obj)) {
				currentCycle = null;
			}
		}
		if(current != null) {
			this.current.tick();
			this.current.clientTick(obj);
			if(this.current instanceof ExtendableAnimation c && c.shouldEnd(obj)) {
				c.update();
			}
			if(this.current.shouldRemove()  || (this.queued != null && this.current.canCancel(this.queued))) {
				this.current = this.queued;
				this.queued = null;
			}
		}
		if(current == null) {
			if(this.queued != null) {
				this.current = this.queued;
				this.queued = null;
			}
		}
	}
	@Override
	public void readFromNbt(NbtCompound tag) {
		if(tag.contains("namespace") && Animations.getAnimation(new Identifier(tag.getString("namespace"), tag.getString("path"))) != null) {
			this.current = Animations.getAnimation(new Identifier(tag.getString("namespace"), tag.getString("path")));
			assert this.current != null;
			this.current.readNbt(tag);
		} else {
			this.current = null;
		}
		if(tag.contains("Quu") && Animations.getAnimation(new Identifier(tag.getString("Quu"))) != null) {
			this.queued = Animations.getAnimation(new Identifier(tag.getString("Quu")));
		} else {
			this.queued = null;
		}
	}
	public void queue(Animation anim) {
		if(current == null || ((current.getLength() - current.frame) <= Math.max(15, current.getLength()/4))) {
			queueUnconditional(anim);
		}
	}
	public void queueUnconditional(Animation anim) {
		if(anim != null) {
			this.queued = anim;
			sync();
		}
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if(current != null) {
			tag.putString("namespace", current.getId().getNamespace());
			tag.putString("path", current.getId().getPath());
			current.writeNbt(tag);
		}
		if(queued != null) {
			tag.putString("Quu", queued.getId().toString());
		}
	}
	public void sync() {
		VigoremComponents.ANIMATION.sync(obj);
	}
}
