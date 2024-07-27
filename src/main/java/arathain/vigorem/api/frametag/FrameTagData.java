package arathain.vigorem.api.frametag;

public class FrameTagData {
	protected final FrameTag frameTag;
	protected final float[] frametimes;

	public FrameTagData(FrameTag frameTag, float[] frametimes) {
		this.frameTag = frameTag;
		this.frametimes = frametimes;
	}

	public boolean isActive(float frame) {
		int i = 1;
		for(float f : frametimes) {
			if(f < frame ) {
				i++;
			} else {
				break;
			}
		}
		return i % 2 == 0;
	}

	public FrameTag getFrameTag() {
		return frameTag;
	}
}
