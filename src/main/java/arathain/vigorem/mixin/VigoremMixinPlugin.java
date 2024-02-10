package arathain.vigorem.mixin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.quiltmc.loader.api.MappingResolver;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.objectweb.asm.Opcodes.*;

/**
 * Courtesy of like 98% sschr15
 * @author sschr15
 * */
public class VigoremMixinPlugin implements IMixinConfigPlugin {
	private static final Path generatedMixinPath = QuiltLoader.getConfigDir().resolve("vigorem-mixin-escrow.class");

	static {

		byte[] bytes = null;

		Path existingCheck = QuiltLoader.getConfigDir().resolve("current-hash");

		MessageDigest digest = uncatch(ignored -> MessageDigest.getInstance("SHA-256")).apply(null);
		for (ModContainer m : QuiltLoader.getAllMods()) {
			String s = m.metadata().id() + '@' + m.metadata().version().raw();
			digest.update(s.getBytes());
		}
		byte[] hash = digest.digest();
		if (Files.exists(existingCheck) && !QuiltLoader.isDevelopmentEnvironment()) {
			try {
				byte[] existing = Files.readAllBytes(existingCheck);
				if (MessageDigest.isEqual(existing, hash)) {
					if (Files.exists(generatedMixinPath)) {
						try {
							bytes = Files.readAllBytes(generatedMixinPath);
						} catch (Throwable e) {
							throw rethrow(e);
						}
					}
				}
			} catch (Throwable e) {
				throw rethrow(e);
			}
		}

		if (bytes == null) {
			List<String> namesWithRenderer = new ArrayList<>();
			QuiltLoader.getAllMods().stream()
				.filter(m -> m.getSourceType() != ModContainer.BasicSourceType.BUILTIN && m.getSourceType() != ModContainer.BasicSourceType.OTHER)
				.map(ModContainer::rootPath)
				.flatMap(uncatch(w -> Files.walk(w, FileVisitOption.FOLLOW_LINKS)))
				.filter(p -> p.getFileName().toString().endsWith(".class"))
				.forEach(p -> {
					try {
						ClassNode node = new ClassNode();
						new ClassReader(Files.readAllBytes(p)).accept(node, 0);
						if (node.interfaces == null || node.interfaces.isEmpty() ||
							!node.interfaces.contains("net/fabricmc/fabric/api/client/rendering/v1/ArmorRenderer"))
							return;

						namesWithRenderer.add(node.name);
					} catch (Throwable e) {
						throw rethrow(e);
					}
				});

			ClassNode node = new ClassNode();
			node.visit(V17, ACC_PUBLIC | ACC_SUPER, "arathain/vigorem/mixin/GeneratedMixin", null, "java/lang/Object", new String[0]);
			AnnotationVisitor av = node.visitAnnotation("Lorg/spongepowered/asm/mixin/Mixin;", false);
			AnnotationVisitor arr = av.visitArray("value");
			for (String name : namesWithRenderer) {
				arr.visit(null, Type.getObjectType(name));
			}

			ClassWriter writer = new ClassWriter(0);
			node.accept(writer);
			bytes = writer.toByteArray();
		}

		try {
			Files.write(existingCheck, hash);
			Files.write(generatedMixinPath, bytes);
		} catch (Throwable e) {
			throw rethrow(e);
		}
	}

	@Override
	public void onLoad(String mixinPackage) {
		try {
			Class<?> ClassLoaderAccess = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate.ClassLoaderAccess");
			Method addUrlFwd = ClassLoaderAccess.getMethod("addUrlFwd", URL.class);
			addUrlFwd.invoke(getClass().getClassLoader(), generatedMixinPath.toUri().toURL());
		} catch (ClassNotFoundException e) {
			// Possibly on Quilt
			try {
				Class<?> KnotClassLoader = Class.forName("org.quiltmc.loader.impl.launch.knot.KnotClassLoader");
				Class<?> QuiltClassPath = Class.forName("org.quiltmc.loader.impl.filesystem.QuiltClassPath");
				Field pathsField = KnotClassLoader.getDeclaredField("paths");
				pathsField.setAccessible(true);
				Object paths = pathsField.get(getClass().getClassLoader());
				Method putQuickFile = QuiltClassPath.getDeclaredMethod("putQuickFile", String.class, Path.class);
				putQuickFile.setAccessible(true);
				putQuickFile.invoke(paths, "/arathain/vigorem/mixin/GeneratedMixin.class", generatedMixinPath);
			} catch (Throwable t) {
				throw rethrow(t);
			}
		} catch (Throwable e) {
			throw rethrow(e);
		}
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if(mixinClassName.contains("glib")) {
			return false;//QuiltLoader.isModLoaded("geckolib3");
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return List.of("GeneratedMixin");
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	private static <E extends Throwable> Error rethrow(Throwable t) throws E {
		throw (E) t;
	}

	private interface ThrowingFunction<T, R> {
		R apply(T t) throws Throwable;
	}

	private static <T, R> Function<T, R> uncatch(ThrowingFunction<T, R> function) {
		return t -> {
			try {
				return function.apply(t);
			} catch (Throwable e) {
				throw rethrow(e);
			}
		};
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if(mixinClassName.contains("GeneratedMixin")) {
			MappingResolver resolver = QuiltLoader.getMappingResolver();
			String matrixStack = resolver.mapClassName("intermediary", "net.minecraft.class_4587");
			String vertexConsumerProvider = resolver.mapClassName("intermediary", "net.minecraft.class_4597");
			String itemStack = resolver.mapClassName("intermediary", "net.minecraft.class_1799");
			String livingEntity = resolver.mapClassName("intermediary", "net.minecraft.class_1309");
			String equipmentSlot = resolver.mapClassName("intermediary", "net.minecraft.class_1304");
			String bipedEntityModel = resolver.mapClassName("intermediary", "net.minecraft.class_572");
			String model = resolver.mapClassName("intermediary", "net.minecraft.class_3879");
			String identifier = resolver.mapClassName("intermediary", "net.minecraft.class_2960");

			String desc = "(" +
				'L' + matrixStack.replace('.', '/') + ';' +
				'L' + vertexConsumerProvider.replace('.', '/') + ';' +
				'L' + itemStack.replace('.', '/') + ';' +
				'L' + livingEntity.replace('.', '/') + ';' +
				'L' + equipmentSlot.replace('.', '/') + ';' +
				'I' +
				'L' + bipedEntityModel.replace('.', '/') + ';' +
				")V";

			MethodNode target = targetClass.methods.stream()
				.filter(m -> m.name.equals("render") && m.desc.equals(desc))
				.findFirst().orElseThrow();

			String lookForClass = "net/fabricmc/fabric/api/client/rendering/v1/ArmorRenderer";
			String lookForName = "renderPart";
			String lookForDesc = "(" +
				'L' + matrixStack.replace('.', '/') + ';' +
				'L' + vertexConsumerProvider.replace('.', '/') + ';' +
				'I' +
				'L' + itemStack.replace('.', '/') + ';' +
				'L' + model.replace('.', '/') + ';' +
				'L' + identifier.replace('.', '/') + ';' +
				")V";

			String injectClass = "arathain/vigorem/anim/OhThisIsMyFavouriteSong";
			String injectName = "setVisible";
			String injectDesc = "(" +
				'L' + model.replace('.', '/') + ';' +
				'L' + bipedEntityModel.replace('.', '/') + ';' +
				")V";

			InsnList insns = target.instructions;
			for (AbstractInsnNode insn = insns.getFirst(); insn != null; insn = insn.getNext()) {
				if (insn instanceof MethodInsnNode method) {
					if (method.owner.equals(lookForClass) && method.name.equals(lookForName) && method.desc.equals(lookForDesc)) {
						// Stack: MatrixStack, VertexConsumerProvider, int, ItemStack, Model, Identifier
						// short: [...], Model, Idenifier
						List.of(
							new InsnNode(SWAP), // [...], Identifier, Model
							new InsnNode(DUP_X1), // [...], Model, Identifier, Model
							new VarInsnNode(ALOAD, 7), // [...], Model, Identifier, Model, BipedEntityModel
							new MethodInsnNode(INVOKESTATIC, injectClass, injectName, injectDesc, false) // [...], Model, Identifier
						).forEach(i -> insns.insertBefore(method, i));
					}
				}
			}
		}
	}
}
