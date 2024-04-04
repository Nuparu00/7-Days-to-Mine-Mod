package nuparu.sevendaystomine.world.gen.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.plot.Plot;

public class City {

	public BlockPos start;
	public World world;

	private List<Street> streets = new ArrayList<Street>();
	private BlockingQueue<Street> streetsQueue = new LinkedBlockingQueue<Street>();

	protected HashMap<Integer, String> streetNamesX = Maps.newHashMap();
	protected HashMap<Integer, String> streetNamesZ = Maps.newHashMap();

	public int roadsLimit;

	private boolean allStreetsFound = false;
	protected List<String> unclaimedStreetNames = null;

	public Random rand;

	public String name = "Genericville";
	public EnumCityType type = EnumCityType.CITY;

	public int population;

	public City() {

	}

	public City(World world, BlockPos start, EnumCityType type, Random rand) {
		this.start = start;
		this.type = type;
		this.rand = rand;
		this.start = new BlockPos(this.start.getX(), 255, this.start.getZ());

		this.world = world;
		this.unclaimedStreetNames = new ArrayList<String>(CityHelper.streets);
		this.name = CityHelper.getRandomCityName(this.rand);
		this.roadsLimit = MathUtils.getIntInRange(rand, 8, Math.max(8, ModConfig.worldGen.maxCitySize));
		roadsLimit = Math.round(roadsLimit * type.populationMultiplier);
	}

	public static City foundCity(World world, ChunkPos pos, Random rand) {
		return foundCity(world, new BlockPos(pos.x * 16 + 8, 0, pos.z * 16 + 8), rand);
	}

	public static City foundCity(World world, BlockPos pos) {
		return foundCity(world, pos, new Random(world.getSeed() + (pos.getX() / 16) - (pos.getZ() / 16)));
	}

	public static City foundCity(World world, BlockPos pos, Random rand) {
		Biome biome = world.getBiome(pos);
		EnumCityType type = EnumCityType.TOWN;
		if (biome.getHeightVariation() <= 0.15 && rand.nextInt(3) == 0) {
			type = EnumCityType.VILLAGE;
		} else if (rand.nextInt(2) == 0) {
			type = EnumCityType.CITY;
		}
		return new City(world, pos, type, rand);
	}

	public void startCityGen() {
		long timeStamp = System.currentTimeMillis();
		if (!areAllStreetsFound()) {
			prepareStreets();
		}
		if (this.name.equals("Caprica City")) {
			this.population = 50298;
		} else {
			this.population = this.roadsLimit
					* (MathUtils.getIntInRange(rand, 128, 1024) * (type == EnumCityType.METROPOLIS ? 32
							: (type == EnumCityType.CITY ? 16 : (type == EnumCityType.TOWN ? 2 : 1))));
		}
		generate();
		CitySavedData.get(world).addCity(this);
		if (!world.isRemote) {
			Utils.getLogger().info(this.name + " generated at " + start.getX() + " " + start.getZ() + " within "
					+ (System.currentTimeMillis() - timeStamp) + "ms with " + streets.size() + " streets");
		}
	}

	public boolean isThereStreet(BlockPos s, BlockPos e) {
		for (Street street : streets) {
			if ((street.start.equals(s) && street.end.equals(e)) || (street.start.equals(e) && street.end.equals(s))) {
				return true;
			}
		}
		return false;
	}

	public void prepareStreets() {
		BlockPos bp_start = Utils.getTopGroundBlock(start, world, true);
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (getStreetsCount() < this.roadsLimit) {
				BlockPos blockpos = bp_start.offset(facing, type.roadLength - 1);
				Biome biome = world.getBiome(blockpos);
				if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN))
					continue;
				int height = Utils.getTopSolidGroundHeight(blockpos, world);
				int deltaHeight = bp_start.getY() - height;
				if (deltaHeight <= Street.MAX_SLOPE) {
					Street street = new Street(world, bp_start, new BlockPos(blockpos.getX(), height, blockpos.getZ()),
							facing, this);
					street.canBranch = false;

					if (facing == EnumFacing.WEST || facing == EnumFacing.EAST) {
						if (streetNamesZ.containsKey(bp_start.getZ())) {
							street.name = streetNamesZ.get(bp_start.getZ());
						} else {
							String name = CityHelper.getRandomStreetForCity(this);
							streetNamesZ.put(bp_start.getZ(), name);
							street.name = name;
						}
					}
					if (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH) {
						if (streetNamesX.containsKey(bp_start.getX())) {
							street.name = streetNamesX.get(bp_start.getX());
						} else {
							String name = CityHelper.getRandomStreetForCity(this);
							streetNamesX.put(bp_start.getX(), name);
							street.name = name;
						}
					}

					if (!isThereStreet(street.start, street.end)) {
						addStreet(street);
					}
				}
			}
		}
		int attempts = 0;
		while (streetsQueue.size() > 0 && attempts < 8192) {
			addIteration();
			attempts++;
		}
		setAllStreetsFound(true);
	}

	public void generate() {
		for (Street street : streets) {
			street.generate();
		}
		for (Street street : streets) {
			street.checkEnding();
		}
		if (type.sewers) {
			for (Street street : streets) {
				street.generateSewers();
			}
		}
		for (Street street : streets) {
			street.decorate();
		}
		for (Street street : streets) {
			street.generateBuildings();
		}
	}

	public void addStreet(Street street) {
		for (Street st : streets) {
			if (Utils.compareBlockPos(st.end, street.end)) {
				if (!st.isConnectedTo(street)) {
					st.addConnectedStreet(street);
				}
				if (!street.isConnectedTo(st)) {
					street.addConnectedStreet(st);
				}
			}
		}

		streets.add(street);
		street.streetIndex = streets.size();
		try {
			streetsQueue.put(street);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addIteration() {
		int size = streetsQueue.size();
		int attempts = 0;
		if (size > 0) {
			while (size > 0 && attempts < 16) {
				try {
					Street street = streetsQueue.take();
					street.tryToContinueStreets();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				attempts++;
				size--;
			}
		}
	}

	public int getStreetsAtCrossingCount(BlockPos pos, int tolerance) {
		Iterable<BlockPos> poses = BlockPos.getAllInBox(
				pos.offset(EnumFacing.NORTH, tolerance).offset(EnumFacing.WEST, tolerance).down(5),
				pos.offset(EnumFacing.SOUTH, tolerance).offset(EnumFacing.EAST, tolerance).up(5));
		Crossing crossing = new Crossing(this);
		int i = 0;
		for (Street street : streets) {
			for (BlockPos pos2 : poses) {
				if (pos2.getX() == street.start.getX() && pos2.getY() == street.start.getY()
						&& pos2.getZ() == street.start.getZ()) {
					crossing.addStreet(street);
					street.startCrossing = crossing;
					i++;
				} else if (pos2.getX() == street.end.getX() && pos2.getY() == street.end.getY()
						&& pos2.getZ() == street.end.getZ()) {
					crossing.addStreet(street);
					street.endCrossing = crossing;
					i++;
				}
				if (i == 4) {
					break;
				}
			}

			if (i == 4) {
				break;
			}
		}

		return i;

	}

	public boolean areAllStreetsFound() {
		return allStreetsFound;
	}

	public void setAllStreetsFound(boolean state) {
		allStreetsFound = state;
	}

	public int getStreetsCount() {
		return this.streets.size();
	}

	public void readNBT(NBTTagCompound nbt) {
		this.start = BlockPos.fromLong(nbt.getLong("start"));
		this.population = nbt.getInteger("population");
	}

	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setLong("start", this.start.toLong());
		nbt.setInteger("population", population);
		return nbt;
	}

	public boolean doesPlotIntersect(Plot plot) {
		for (Street street : streets) {
			for (Plot plot2 : street.plots) {
				if (plot2.intersects(plot))
					return true;
			}
		}
		return false;
	}

}
