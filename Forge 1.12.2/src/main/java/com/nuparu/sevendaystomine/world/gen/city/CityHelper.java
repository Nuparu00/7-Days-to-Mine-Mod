package com.nuparu.sevendaystomine.world.gen.city;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockCar;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.building.Building;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingApartment;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingApartmentDark;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingBrickHouse;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingChurch;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingGasStation;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingOvergrownHouse;
import com.nuparu.sevendaystomine.world.gen.prefab.Prefab;
import com.nuparu.sevendaystomine.world.gen.prefab.PrefabParser;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CityHelper {

	protected static List<String> streets = null;
	protected static List<String> cities = null;

	public static List<BlockCar> cars = new ArrayList<BlockCar>();
	public static List<Building> buildings = new ArrayList<Building>();

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

	public static BlockCar getRandomCar(Random rand) {
		return cars.get(rand.nextInt(CityHelper.cars.size()));
	}

	public static void placeRandomCar(World world, BlockPos pos, EnumFacing facing) {
		BlockCar car = getRandomCar(world.rand);
		if (car.canBePlaced(world, pos, facing)) {
			car.generate(world, pos, facing, true, null);
		}
	}

	public static void loadBuildings() {
		// buildings.add(new BuildingOvergrownHouse(new
		// ResourceLocation(SevenDaysToMine.MODID,"overgrown_house")));
		
		buildings.add(new BuildingApartment(new ResourceLocation(SevenDaysToMine.MODID, "apartment"),50));
		buildings.add(new BuildingBrickHouse(new ResourceLocation(SevenDaysToMine.MODID, "brick_house"),45));
		buildings.add(new BuildingApartmentDark(new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_bottom"),50));
		buildings.add(new BuildingBrickHouse(new ResourceLocation(SevenDaysToMine.MODID, "gray_house"),20));
		buildings.add(new BuildingGasStation(new ResourceLocation(SevenDaysToMine.MODID, "gas_station_0"),2));
		buildings.add(new BuildingBrickHouse(new ResourceLocation(SevenDaysToMine.MODID, "overgrown_house"),20));
		buildings.add(new BuildingBrickHouse(new ResourceLocation(SevenDaysToMine.MODID, "water_tower"),1));
		buildings.add(new BuildingChurch(new ResourceLocation(SevenDaysToMine.MODID, "church_front"),20));
	}

	public static Building getRandomBuilding(Random rand) {
		int total = 0;
		for(Building building : buildings) {
			total+=building.weight;
		}
		int i = rand.nextInt(total);
		for(Building building : buildings) {
			i-=building.weight;
			if(i <= 0) {
				return building;
			}
		}
		//Failsafe
		return buildings.get(rand.nextInt(buildings.size()));
	}

	public static List<Building> getBuildings() {
		List<Building> list = new ArrayList<Building>(buildings);
		return list;
	}
}
