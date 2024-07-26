package arathain.vigorem.mixin;

import arathain.vigorem.Vigorem;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.objectweb.asm.Opcodes.*;

/**
 * Courtesy of like 98% sschr15
 * @author sschr15
 * */
public class VigoremMixinPlugin implements IMixinConfigPlugin {
	private static final FabricLoader fabric = FabricLoader.getInstance();
	private static final Path vigoremTemp = fabric.getGameDir().resolve(".vigoremcache");
	private static final Path generatedMixinJarPath = vigoremTemp.resolve("vigorem-mixin-escrow.jar");

	static {
		try {
			Files.createDirectories(vigoremTemp);

			byte[] bytes = null;

			Path existingCheck = FabricLoader.getInstance().getConfigDir().resolve("current-hash");
			Path generatedMixinPath = vigoremTemp.resolve("vigorem-mixin-escrow.class");

			MessageDigest digest = uncatch(ignored -> MessageDigest.getInstance("SHA-256")).apply(null);
			for (ModContainer m : FabricLoader.getInstance().getAllMods()) {
				String s = m.getMetadata().getId() + '@' + m.getMetadata().getVersion().getFriendlyString();
				digest.update(s.getBytes());
			}
			byte[] hash = digest.digest();
			if (Files.exists(existingCheck) && !fabric.isDevelopmentEnvironment()) {
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
			}

			if (bytes == null) {
				List<String> namesWithRenderer = new ArrayList<>();
				fabric.getAllMods().stream()
					.flatMap(m -> m.getRootPaths().stream())
					.flatMap(uncatch(w -> Files.walk(w, FileVisitOption.FOLLOW_LINKS)))
					.filter(p -> !p.startsWith("/software/bernie/geckolib3") && p.getFileName().toString().endsWith(".class"))
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

			Files.write(existingCheck, hash);
			Files.write(generatedMixinPath, bytes);

			Files.deleteIfExists(generatedMixinJarPath);
			try (var jar = FileSystems.newFileSystem(generatedMixinJarPath, Map.of("create", "true"))) {
				Path dir = jar.getPath("/arathain/vigorem/mixin");
				Files.createDirectories(dir);
				Files.copy(generatedMixinPath, dir.resolve("GeneratedMixin.class"));
			}
		} catch (Throwable e) {
			throw rethrow(e);
		}
	}

	@Override
	public void onLoad(String mixinPackage) {
		try {
			Class<?> KnotClassLoaderInterface = Class.forName("org.quiltmc.loader.impl.launch.knot.KnotClassLoaderInterface");
			Method addURL = KnotClassLoaderInterface.getMethod("addURL", URL.class);
			addURL.setAccessible(true);
			addURL.invoke(getClass().getClassLoader(), generatedMixinJarPath.toUri().toURL());
		} catch (ClassNotFoundException e) {
			try {
				Class<?> KnotClassDelegateClassLoaderAccess = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate.ClassLoaderAccess");
				Method addURL = KnotClassDelegateClassLoaderAccess.getMethod("addUrlFwd", URL.class);
				addURL.setAccessible(true);
				addURL.invoke(getClass().getClassLoader(), generatedMixinJarPath.toUri().toURL());
			} catch (Throwable t) {
				throw rethrow(t);
			}
		} catch (Throwable t) {
			throw rethrow(t);
		}
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		if (Files.notExists(generatedMixinJarPath)) {
			Vigorem.LOGGER.warn("Generated mixin jar does not exist");
			return List.of();
		}

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

	private static String map(String name) {
		return fabric.getMappingResolver().mapClassName("intermediary", name).replace('.', '/');
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if(mixinClassName.contains("GeneratedMixin")) {
			String matrixStack = map( "net.minecraft.class_4587");
			String vertexConsumerProvider = map( "net.minecraft.class_4597");
			String itemStack = map( "net.minecraft.class_1799");
			String livingEntity = map( "net.minecraft.class_1309");
			String equipmentSlot = map( "net.minecraft.class_1304");
			String bipedEntityModel = map( "net.minecraft.class_572");
			String model = map( "net.minecraft.class_3879");
			String identifier = map( "net.minecraft.class_2960");

			String desc = "(" +
				'L' + matrixStack + ';' +
				'L' + vertexConsumerProvider + ';' +
				'L' + itemStack + ';' +
				'L' + livingEntity + ';' +
				'L' + equipmentSlot + ';' +
				'I' + // int
				'L' + bipedEntityModel + ';' +
				")V";

			MethodNode target = targetClass.methods.stream()
				.filter(m -> m.name.equals("render") && m.desc.equals(desc))
				.findFirst().orElseThrow();

			String lookForClass = "net/fabricmc/fabric/api/client/rendering/v1/ArmorRenderer";
			String lookForName = "renderPart";
			String lookForDesc = "(" +
				'L' + matrixStack + ';' +
				'L' + vertexConsumerProvider + ';' +
				'I' + // int
				'L' + itemStack + ';' +
				'L' + model + ';' +
				'L' + identifier + ';' +
				")V";

			String injectClass = "arathain/vigorem/anim/OhThisIsMyFavouriteSong";
			String injectName = "setVisible";
			String injectDesc = "(" +
				'L' + model + ';' +
				'L' + bipedEntityModel + ';' +
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
