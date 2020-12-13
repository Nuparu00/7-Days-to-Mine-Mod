package com.nuparu.sevendaystomine.util;

import com.nuparu.sevendaystomine.util.damagesource.EntityDamageShot;
import com.nuparu.sevendaystomine.util.damagesource.EntityDamageVehicle;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

public class DamageSources {
	public static DamageSource thirst = new DamageSource("thirst").setDifficultyScaled().setDamageIsAbsolute()
			.setDamageBypassesArmor();
	public static DamageSource sharpGlass = new DamageSource("sharpGlass").setDamageIsAbsolute();
	public static DamageSource alcoholPoison = new DamageSource("alcoholPoison").setDifficultyScaled().setDamageIsAbsolute()
			.setDamageBypassesArmor();
	public static DamageSource mercuryPoison = new DamageSource("mercury_poisoning").setDifficultyScaled().setDamageIsAbsolute()
			.setDamageBypassesArmor();
	public static DamageSource bleeding = new DamageSource("bleeding").setDifficultyScaled().setDamageIsAbsolute().setDamageBypassesArmor();
	public static DamageSource blade = new DamageSource("blade").setDifficultyScaled().setDamageIsAbsolute();
	public static DamageSource infection = new DamageSource("infection").setDifficultyScaled().setDamageIsAbsolute();
	public static DamageSource chlorinePoison = new DamageSource("chlorine_poisoning").setDifficultyScaled().setDamageIsAbsolute()
			.setDamageBypassesArmor();
	
	public static EntityDamageShot causeShotDamage(Entity source, Entity transmitter) {
	    return new EntityDamageShot("shot.entity", transmitter, source);
	}
	public static EntityDamageVehicle causeVehicleDamage(Entity source, Entity transmitter) {
	    return new EntityDamageVehicle("vehicle", transmitter, source);
	}
}
