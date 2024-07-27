package arathain.vigorem.api.init;

import arathain.vigorem.api.anim.Animation;
import arathain.vigorem.api.frametag.FrameTag;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public interface FrameTags {
	Map<String, FrameTag> FRAME_TAGS = new HashMap<>();

	FrameTag INVULNERABILITY = put(FrameTag.regularTag("vigorem", "iframe"));

	static <T extends FrameTag> T put(T tag) {
		FRAME_TAGS.put(tag.getId(), tag);
		return tag;
	}
}
