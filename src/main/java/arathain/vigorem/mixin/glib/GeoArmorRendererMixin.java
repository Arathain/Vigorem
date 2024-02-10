package arathain.vigorem.mixin.glib;

import arathain.vigorem.anim.Methylenedioxymethamphetamine;
import arathain.vigorem.anim.OffsetModelPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.*;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.GeoUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Mixin(GeoArmorRenderer.class)
public abstract class GeoArmorRendererMixin<T extends ArmorItem & IAnimatable> {
	@Shadow
	public String bodyBone;

	@Shadow
	private AnimatedGeoModel<T> modelProvider;

	@Shadow
	protected BipedEntityModel baseModel;

	@Shadow
	public String headBone;

	@Shadow
	public String leftArmBone;

	@Shadow
	public String rightArmBone;

	@Shadow
	public String leftLegBone;

	@Shadow
	public String rightLegBone;

	@Shadow
	public String rightBootBone;

	@Shadow
	public String leftBootBone;
	@Unique
	private static final String[] boneNames = new String[]{
		"bipedRightArm", "bipedLeftArm", "bipedHead"
	};
	@Unique
	private GeoModel pass = null;

	@ModifyArg(remap = false, method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", index = 0, at = @At(value = "INVOKE", ordinal = 0, target = "Lsoftware/bernie/geckolib3/renderers/geo/GeoArmorRenderer;render(Lsoftware/bernie/geckolib3/geo/render/built/GeoModel;Ljava/lang/Object;FLnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
	public GeoModel vigorem$glibWrapModel(GeoModel par1) {
		if(((Methylenedioxymethamphetamine) baseModel).isAnimating()) {
			if(pass == null) {
				pass = new GeoModel();
				pass.properties = par1.properties;
				pass.topLevelBones = new ArrayList<>(par1.topLevelBones);
				GeoBone biped = vigorem$copyBone(pass.getBone("bipedBody").get());
				pass.topLevelBones.removeIf(geo -> geo.name.equals("bipedBody"));
				vigorem$copyModel(biped, baseModel.body);
				for (String str : boneNames) {
					GeoBone g = vigorem$copyBone(pass.getBone(str).get());
					g.parent = biped;
					biped.childBones.add(g);
				}
				pass.topLevelBones.add(biped);
				vigorem$offsetCubes(pass.getBone("armorBody").get(), new Vec3f(0, 12, 0));

				pass.topLevelBones.removeIf(h -> Arrays.stream(boneNames).anyMatch(b -> Objects.equals(b, h.name)));
			}
//			vigorem$copyModel(pass.getBone("bipedBody").get(), baseModel.body);
//			vigorem$copyModel(pass.getBone("bipedRightArm").get(), baseModel.rightArm);
//			vigorem$copyModel(pass.getBone("bipedLeftArm").get(), baseModel.leftArm);
//			vigorem$copyModel(pass.getBone("bipedHead").get(), baseModel.head);
			return pass;
		}
		return par1;
	}
	@Unique
	private static GeoBone vigorem$copyBone(GeoBone g) {
		GeoBone n = new GeoBone();

		n.setHidden(g.isHidden);
		n.setCubesHidden(g.areCubesHidden);
		n.mirror = g.mirror;
		n.dontRender = g.dontRender;
		n.setModelRendererName(g.name);
		n.setModelSpaceXform(g.getModelSpaceXform());
		n.setLocalSpaceXform(g.getLocalSpaceXform());
		n.setWorldSpaceXform(g.getWorldSpaceXform());

		n.childBones = new ArrayList<>(g.childBones.stream().map(h -> {
			GeoBone l = vigorem$copyBone(h);
			l.parent = n;
			return l;
		}).toList());
		n.childCubes = g.childCubes;

		n.setPivotX(g.getPivotX());
		n.setPivotY(g.getPivotY());
		n.setPivotZ(g.getPivotZ());

		n.setScale(g.getScale());
		n.setRotation(g.getRotation());

		n.setPositionX(g.getPositionX());
		n.setPositionY(g.getPositionY());
		n.setPositionZ(g.getPositionZ());
		return n;
	}
	@Unique
	private static void vigorem$copyModel(GeoBone bodyBone, ModelPart m) {
		OffsetModelPart p = ((OffsetModelPart) (Object)m);

		bodyBone.setPivotX(m.pivotX);
		bodyBone.setPivotY(m.pivotZ);
		bodyBone.setPivotZ(m.pivotZ);

		bodyBone.setHidden(!m.visible);

		GeoUtils.copyRotations(m, bodyBone);

		bodyBone.addPositionX(p.getOffsetX());
		bodyBone.addPositionY(p.getOffsetY());
		bodyBone.addPositionZ(p.getOffsetZ());
	}
	@Unique
	private static void vigorem$offsetCubes(GeoBone bodyBone, Vec3f add) {
		for(GeoCube g : bodyBone.childCubes) {

		}
	}
}
