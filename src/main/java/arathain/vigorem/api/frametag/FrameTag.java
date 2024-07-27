package arathain.vigorem.api.frametag;

public class FrameTag {
	protected final String id;

	public FrameTag(String type, String namespace, String name, String subname) {
		String add = subname != null && !subname.isEmpty() ? "$" + subname : "";
		this.id = type + "/" + namespace + ":" + name + add;
	}

	public static FrameTag regularTag(String namespace, String name) {
		return new FrameTag("stat", namespace, name, null);
	}
	public static FrameTag customTag(String type, String namespace, String name) {
		return new FrameTag(type, namespace, name, null);
	}


	public String getId() {
		return id;
	}
}
