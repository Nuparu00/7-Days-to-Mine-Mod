package com.nuparu.sevendaystomine.world.gen.city;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.prefab.Prefab;
import com.nuparu.sevendaystomine.world.gen.prefab.PrefabParser;

import net.minecraft.util.ResourceLocation;

public class CityHelper {

	protected static List<String> streets = null;
	protected static List<String> cities = null;
	
	public static List<Prefab> prefabs = new ArrayList<Prefab>();

	public static void getStreetNames() {
		InputStream stream = Utils.getInsideFileStream(SevenDaysToMine.MODID.toLowerCase() + "/data/streets.csv");
		try {
			streets = IOUtils.readLines(stream, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getCityNames() {
		InputStream stream = Utils.getInsideFileStream(SevenDaysToMine.MODID.toLowerCase() + "/data/cities.csv");
		try {
			cities = IOUtils.readLines(stream, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getRandomStreet(Random rand) {
		if (streets == null)
			return "Missing Street";
		return streets.get(rand.nextInt(streets.size()));
	}
	
	public static String getRandomStreetForCity(City city) {
		if (streets == null)
			return "Missing Street";
		int index = city.rand.nextInt(city.unclaimedStreetNames.size());
		String name = city.unclaimedStreetNames.get(index);
		city.unclaimedStreetNames.remove(index);
		return name;
	}
	
	public static String getRandomCityName(Random rand) {
		if (cities == null)
			return "Genericville";
		return cities.get(rand.nextInt(cities.size()));
	}
	
	public static void loadPrefabs() {
		prefabs.add(PrefabParser.INSTANCE.getPrefabFromResource(new ResourceLocation(SevenDaysToMine.MODID,"prefabs/brick_house.prfb")));
		prefabs.add(PrefabParser.INSTANCE.getPrefabFromResource(new ResourceLocation(SevenDaysToMine.MODID,"prefabs/brick_house_large.prfb")));
	}
}
