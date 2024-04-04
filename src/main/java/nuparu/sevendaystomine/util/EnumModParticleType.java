package nuparu.sevendaystomine.util;

public enum EnumModParticleType {

	BLOOD("blood",49),
	VOMIT("vomit",50),
	MUZZLE_FLASH("muzzle_flash",50);
	
	String name;
	private int id;
	EnumModParticleType(String name, int id){
		this.name = name;
		this.id = id;
	}
	public int getId() {
		return id;
	}
}
