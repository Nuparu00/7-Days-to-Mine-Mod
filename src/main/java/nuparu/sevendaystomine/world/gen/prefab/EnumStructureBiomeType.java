package nuparu.sevendaystomine.world.gen.prefab;

public enum EnumStructureBiomeType {
	NONE("none"), ALL("all"), FOREST("forest"), PLAINS("plains"), OVERGROWN("overgrown"), COLD("cold"), HOT("hot"),
	WASTELAND("wasteland"), WATER("water");

	String name;

	EnumStructureBiomeType(String name) {
		this.name = name;
	}

	public static EnumStructureBiomeType fromString(String text) {
		for (EnumStructureBiomeType e : EnumStructureBiomeType.values()) {
			if (e.name.equalsIgnoreCase(text)) {
				return e;
			}
		}
		return null;
	}
}
