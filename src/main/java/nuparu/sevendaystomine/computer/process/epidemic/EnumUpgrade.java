package nuparu.sevendaystomine.computer.process.epidemic;

public enum EnumUpgrade {
	COUGHING("Coughing", 1.05, 1, 1.08, 10), SNEEZING("Sneezing", 1.05, 1, 1.08, 20),
	VOMITING("Vomiting", 1.05, 1, 1.08, 30), SEIZURES("Seizures", 1.05, 1.10, 1.25, 10),
	NECROSIS("Necrosis", 1.05, 1.5, 1.08, 20), ORGAN_FAILURE("Organ Failure", 1.05, 1.75, 1.08, 30);

	public String name;
	public double infectivityModifer;
	public double lethalityModifer;
	public double detectibilityModifer;
	public int price;

	EnumUpgrade(String name, double infectivityModifer, double lethalityModifer, double detectibilityModifer,
			int price) {
		this.name = name;
		this.infectivityModifer = infectivityModifer;
		this.lethalityModifer = lethalityModifer;
		this.detectibilityModifer = detectibilityModifer;
		this.price = price;
	}

	public static EnumUpgrade getByName(String name) {
		for (EnumUpgrade state : EnumUpgrade.values()) {
			if (state.name.equals(name))
				return state;
		}
		return null;
	}

	public String getName() {
		return name;
	}

}