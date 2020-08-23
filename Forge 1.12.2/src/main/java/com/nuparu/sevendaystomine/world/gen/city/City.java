package com.nuparu.sevendaystomine.world.gen.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.collect.Maps;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.plot.Plot;

import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class City {

	public BlockPos start;
	public World world;

	private List<Street> streets = new ArrayList<Street>();
	private BlockingQueue<Street> streetsQueue = new LinkedBlockingQueue<Street>();

	protected HashMap<Integer, String> streetNamesX = Maps.newHashMap();
	protected HashMap<Integer, String> streetNamesZ = Maps.newHashMap();

	// 256 takes roughly 43 seconds ; 128 takes around 17 seconds
	public static final int MAX_ROADS = 24;

	public int roads_limit;

	private boolean allStreetsFound = false;
	protected List<String> unclaimedStreetNames = null;

	protected Random rand;

	public String name = "Genericville";
	public int population;

	public City() {

	}

	public City(World world, BlockPos start) {
		this.start = start;

		int height = Utils.getTopSolidGroundHeight(new BlockPos(this.start.getX() + 8, 0, this.start.getZ() + 8),
				world);
		this.start = new BlockPos(this.start.getX() + 8, height, this.start.getZ() + 8);

		this.world = world;
		this.rand = world.rand;
		//this.rand = new Random(0);
		this.unclaimedStreetNames = new ArrayList<String>(CityHelper.streets);
		this.name = CityHelper.getRandomCityName(this.rand);
		this.roads_limit = MathUtils.getIntInRange(rand, 8, MAX_ROADS);
	}

	public void startCityGen() {
		long timeStamp = System.currentTimeMillis();
		if (!areAllStreetsFound()) {
			prepareStreets();
		}
		this.population = this.roads_limit * (MathUtils.getIntInRange(rand, 2048, 8192));
		generate();
		if (!world.isRemote) {
			Utils.getLogger().info(this.name + " generated at " + start.getX() + " " + start.getZ() + " within "
					+ (System.currentTimeMillis() - timeStamp) + "ms with " + streets.size() + " streets");
			Utils.getServer().getPlayerList()
					.sendMessage(new TextComponentString(TextFormatting.GREEN + this.name + " generated at "
							+ start.getX() + " " + start.getZ() + " within " + (System.currentTimeMillis() - timeStamp)
							+ "ms with " + streets.size() + " streets"));
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
		BlockPos bp_start = Utils.getTopSolidGroundBlock(start, world);
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (getStreetsCount() < this.roads_limit) {
				BlockPos blockpos = bp_start.offset(facing, Street.LENGTH - 1);
				Biome biome = world.getBiome(blockpos);
				if (biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN || biome == Biomes.OCEAN)
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
		while (streetsQueue.size() > 0) {
			addIteration();
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

		for (Street street : streets) {
			street.generateSewers();
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
		if (size > 0) {
			while (size > 0) {
				try {
					Street street = streetsQueue.take();
					street.tryToContinueStreets();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				size--;
			}
		}
	}

	public int getStreetsAtCrossingCount(BlockPos pos, int tolerance) {
		Iterable<BlockPos> poses = BlockPos.getAllInBox(
				pos.offset(EnumFacing.NORTH, tolerance).offset(EnumFacing.WEST, tolerance).down(1),
				pos.offset(EnumFacing.SOUTH, tolerance).offset(EnumFacing.EAST, tolerance).up(1));
		Crossing crossing = new Crossing(this);
		int i = 0;
		for (Street street : streets) {

			// System.out.println(pos + " " + street.start + " " + street.end);
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
