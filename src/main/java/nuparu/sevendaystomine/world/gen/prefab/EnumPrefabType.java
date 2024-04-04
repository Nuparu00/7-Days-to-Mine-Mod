package nuparu.sevendaystomine.world.gen.prefab;

public enum EnumPrefabType {
	NONE("none"),
	CITY_BUILDING("city"),
	RANDOM_BUILDING("random");
	
	String name;

	EnumPrefabType(String name) {
		this.name = name;
	}

	public static EnumPrefabType fromString(String text) {
		for (EnumPrefabType e : EnumPrefabType.values()) {
			if (e.name.equalsIgnoreCase(text)) {
				return e;
			}
		}
		return null;
	}
}
