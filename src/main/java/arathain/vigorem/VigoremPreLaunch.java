package arathain.vigorem;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class VigoremPreLaunch implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch(ModContainer mod) {
		MixinExtrasBootstrap.init();
	}
}
