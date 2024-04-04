package nuparu.sevendaystomine.world.gen.city;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;

import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockCar;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityCarMaster;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.building.Building;
import nuparu.sevendaystomine.world.gen.city.building.BuildingApartmentBig;
import nuparu.sevendaystomine.world.gen.city.building.BuildingApartmentDark;
import nuparu.sevendaystomine.world.gen.city.building.BuildingBrickHouse;
import nuparu.sevendaystomine.world.gen.city.building.BuildingCarRepair;
import nuparu.sevendaystomine.world.gen.city.building.BuildingCemetery;
import nuparu.sevendaystomine.world.gen.city.building.BuildingChurch;
import nuparu.sevendaystomine.world.gen.city.building.BuildingConstructionSite;
import nuparu.sevendaystomine.world.gen.city.building.BuildingFarm;
import nuparu.sevendaystomine.world.gen.city.building.BuildingGasStation;
import nuparu.sevendaystomine.world.gen.city.building.BuildingGrayHouse;
import nuparu.sevendaystomine.world.gen.city.building.BuildingHospital;
import nuparu.sevendaystomine.world.gen.city.building.BuildingHotel;
import nuparu.sevendaystomine.world.gen.city.building.BuildingOffice;
import nuparu.sevendaystomine.world.gen.city.building.BuildingOvergrownHouse;
import nuparu.sevendaystomine.world.gen.city.building.BuildingPoliceStation;
import nuparu.sevendaystomine.world.gen.city.building.BuildingSchool;
import nuparu.sevendaystomine.world.gen.city.building.BuildingSupermarket;

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
		try {
			stream.close();
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
		try {
			stream.close();
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

	public static BlockCar getRandomCar(Random rand, boolean special) {
		BlockCar car = null;
		do {
			car = cars.get(rand.nextInt(CityHelper.cars.size()));
		} while (car == null || (!special && car.special) || (car.special && rand.nextInt(8) != 0));
		return car;
	}

	public static void placeRandomCar(World world, BlockPos pos, EnumFacing facing, boolean special, Random rand) {
		BlockCar car = getRandomCar(world.rand, special);
		if (car.canBePlaced(world, pos, facing)) {
			car.generate(world, pos, facing, true, null);
		}
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityCarMaster) {
			TileEntityCarMaster master = (TileEntityCarMaster) te;
			master.setLootTable(car.lootTable, rand.nextLong());
			master.fillWithLoot(null);
		}
	}

	public static void placeRandomCar(World world, BlockPos pos, EnumFacing facing, Random rand) {
		placeRandomCar(world, pos, facing, false, rand);
	}

	public static void loadBuildings() {
		// buildings.add(new BuildingOvergrownHouse(new
		// ResourceLocation(SevenDaysToMine.MODID,"overgrown_house")));

		buildings.add(new BuildingBrickHouse(new ResourceLocation(SevenDaysToMine.MODID, "brick_house"), 40, -2));

		buildings.add(new BuildingBrickHouse(
				new ResourceLocation(SevenDaysToMine.MODID, "brick_house_damaged_inverted"), 40, -2));
		buildings.add(new BuildingBrickHouse(new ResourceLocation(SevenDaysToMine.MODID, "brick_house_dead"), 40, -2));
		
		buildings.add(new BuildingGrayHouse(new ResourceLocation(SevenDaysToMine.MODID, "gray_house"), 20, -1));
		buildings.add(new BuildingGasStation(new ResourceLocation(SevenDaysToMine.MODID, "gas_station_1"), 10, -1));
		buildings.add(
				new BuildingOvergrownHouse(new ResourceLocation(SevenDaysToMine.MODID, "overgrown_house"), 20, -1));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "water_tower"), 1)
				.setAllowedCityTypes(EnumCityType.TOWN, EnumCityType.VILLAGE));
		buildings.add(new BuildingFarm(new ResourceLocation(SevenDaysToMine.MODID, "farm"), 30, 0,
				Blocks.STONE.getDefaultState()).setAllowedCityTypes(EnumCityType.TOWN, EnumCityType.VILLAGE));
		buildings.add(new BuildingChurch(new ResourceLocation(SevenDaysToMine.MODID, "church_front"), 5, -1));

		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "menu_house"), 20, -4)
				.setAllowedCityTypes(EnumCityType.TOWN, EnumCityType.VILLAGE));
		buildings
				.add(new BuildingSupermarket(new ResourceLocation(SevenDaysToMine.MODID, "supermarket_right"), 30, -1));
		buildings.add(new BuildingPoliceStation(30, 0));
		buildings.add(new BuildingCarRepair(20));
		buildings.add(new BuildingSchool(10));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "book_store"), 10, -1)
				.setAllowedCityTypes(EnumCityType.CITY).setCanBeMirrored(false));
		buildings.add(new BuildingConstructionSite(40));
		buildings.add(new BuildingCemetery(18, 0));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "condo"), 20, -1)
				.setAllowedCityTypes(EnumCityType.CITY, EnumCityType.TOWN));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "parking_lot"), 10, 0) {
			@Override
			public BlockPos getDimensions(World world, EnumFacing facing) {
				return super.getDimensions(world, facing).add(2, 0, 2);
			}
		}.setAllowedCityTypes(EnumCityType.CITY).setPedestal(ModBlocks.STRUCTURE_STONE.getDefaultState()));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "fire_station"), 18, -1)
				.setAllowedCityTypes(EnumCityType.CITY, EnumCityType.TOWN)
				.setPedestal(ModBlocks.STRUCTURE_STONE.getDefaultState()));

		if (!ModConfig.worldGen.smallStructuresOnly) {
			buildings.add(new BuildingApartmentDark(new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_bottom"),
					50, -1));
			buildings.add(
					new BuildingHospital(new ResourceLocation(SevenDaysToMine.MODID, "hospital_front_left"), 6, -1));
			buildings.add(
					new BuildingOffice(new ResourceLocation(SevenDaysToMine.MODID, "office_building_right"), 10, -1));
			buildings.add(
					new BuildingApartmentBig(new ResourceLocation(SevenDaysToMine.MODID, "apartments_big_undamaged_0"),
							new ResourceLocation(SevenDaysToMine.MODID, "apartments_big_undamaged_1"), 20, -1));
			buildings
					.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "apartments_ruins_overgrown"), 20, -1)
							.setAllowedCityTypes(EnumCityType.CITY));
			buildings.add(
					new BuildingBrickHouse(new ResourceLocation(SevenDaysToMine.MODID, "brick_house_damaged"), 40, -2));
			buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "apartment"), 40, -1, Blocks.STONE
					.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH))
							.setAllowedCityTypes(EnumCityType.CITY));
			buildings.add(new BuildingApartmentBig(new ResourceLocation(SevenDaysToMine.MODID, "apartments_big_0"),
					new ResourceLocation(SevenDaysToMine.MODID, "apartments_big_1"), 20, -1));
			buildings.add(new BuildingHotel(18, -2));
		}
	}

	public int getBuildingsCount() {
		return buildings.size();
	}

	public int getBuildingIndex(Building building) {
		return buildings.indexOf(building);
	}

	public Building getBuildingByIndex(int i) {
		return buildings.get(MathHelper.clamp(i, 0, buildings.size()));
	}

	public static Building getRandomBuilding(Random rand) {
		int total = 0;
		for (Building building : buildings) {
			total += building.weight;
		}
		int i = rand.nextInt(total);
		for (Building building : buildings) {
			i -= building.weight;
			if (i <= 0) {
				return building;
			}
		}
		// Failsafe
		return buildings.get(rand.nextInt(buildings.size()));
	}

	public static List<Building> getBuildings() {
		List<Building> list = new ArrayList<Building>(buildings);
		return list;
	}
}
