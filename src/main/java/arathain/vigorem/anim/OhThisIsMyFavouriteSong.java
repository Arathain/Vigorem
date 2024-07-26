package arathain.vigorem.anim;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class OhThisIsMyFavouriteSong {
	//Don't worry about how this gets run.
	public static void setVisible(Model armourModel, BipedEntityModel<?> playerModel) {
		if(armourModel instanceof BipedEntityModel<?> bipedModel) {
			if (!playerModel.body.visible) {
				bipedModel.body.visible = false;
			}
			if (!playerModel.leftLeg.visible) {
				bipedModel.leftLeg.visible = false;
			}
			if (!playerModel.rightLeg.visible) {
				bipedModel.rightLeg.visible = false;
			}
			if (!playerModel.head.visible) {
				bipedModel.head.visible = false;
			}
		}
	}
}
