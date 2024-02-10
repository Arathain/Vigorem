package arathain.vigorem.mixin;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import org.objectweb.asm.tree.ClassNode;
import org.reflections.Reflections;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class VigoremMixinPlugin implements IMixinConfigPlugin {
	Set<Class<? extends ArmorRenderer>> classes;
	@Override
	public void onLoad(String mixinPackage) {
		Reflections reflections = new Reflections("arathain.vigorem.reflections");
		classes = reflections.getSubTypesOf(ArmorRenderer.class);
		for(Class c : classes) {
			System.out.println(c.getPackageName());
		}
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return false;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}