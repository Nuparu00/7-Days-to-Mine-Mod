package nuparu.sevendaystomine.world.gen.city;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import nuparu.sevendaystomine.block.BlockAsphalt;
import nuparu.sevendaystomine.block.BlockBackpack;
import nuparu.sevendaystomine.block.BlockFakeAnvil;
import nuparu.sevendaystomine.block.BlockHorizontalBase;
import nuparu.sevendaystomine.block.BlockPaper;
import nuparu.sevendaystomine.block.BlockSandLayer;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.tileentity.TileEntityBackpack;
import nuparu.sevendaystomine.tileentity.TileEntityBigSignMaster;
import nuparu.sevendaystomine.tileentity.TileEntityStreetSign;
import nuparu.sevendaystomine.tileentity.TileEntityTrashCan;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.building.Building;
import nuparu.sevendaystomine.world.gen.city.plot.Plot;

public class Street {

	public BlockPos start;
	public BlockPos end;
	public World world;

	public Crossing startCrossing = null;
	public Crossing endCrossing = null;

	public City city;

	public EnumFacing facing;
	public boolean canBranch = true;
	public boolean tunnel = false;
	public int branchIndex = 0;

	public int streetIndex = 0;

	public String name;
	public List<Plot> plots = new ArrayList<Plot>();

	public static final int MAX_SLOPE = 24;

	public static final int SEWERS_HEIGHT = 5;
	public static final int SEWERS_WIDTH = 3;

	private Street previousStreet;
	private List<Street> connectedStreets = new ArrayList<Street>();

	public Street(World world, BlockPos start, BlockPos end, EnumFacing facing, City city) {
		this.world = world;
		this.start = start;
		this.end = end;
		this.facing = facing;
		this.city = city;
		this.name = "missing.street";
	}

	public void tryToContinueStreets() {
		for (EnumFacing f : EnumFacing.HORIZONTALS) {
			if (f != facing.getOpposite() && city.getStreetsCount() < city.roadsLimit) {
				if (canBranch || f == facing) {
					BlockPos blockpos = end.offset(f, city.type.roadLength - 1);
					Biome biome = world.getBiome(blockpos);
					if (biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN || biome == Biomes.OCEAN
							|| biome.getHeightVariation() > 0.15f)
						continue;
					int height = Utils.getTopSolidGroundHeight(blockpos, world);
					int deltaHeight = Math.abs(end.getY() - height);
					if (deltaHeight <= Street.MAX_SLOPE) {
						Street street = new Street(world, end,
								new BlockPos(blockpos.getX(), height - 1, blockpos.getZ()), f, this.city);
						street.canBranch = !canBranch;

						if (f == EnumFacing.WEST || facing == EnumFacing.EAST) {
							if (city.streetNamesZ.containsKey(end.getZ())) {
								street.name = city.streetNamesZ.get(end.getZ());
							} else {
								String name = CityHelper.getRandomStreetForCity(city);
								city.streetNamesZ.put(end.getZ(), name);
								street.name = name;
							}
						}
						if (f == EnumFacing.SOUTH || facing == EnumFacing.NORTH) {
							if (city.streetNamesX.containsKey(end.getX())) {
								street.name = city.streetNamesX.get(end.getX());
							} else {
								String name = CityHelper.getRandomStreetForCity(city);
								city.streetNamesX.put(end.getX(), name);
								street.name = name;
							}
						}

						if (!city.isThereStreet(street.start, street.end)) {
							city.addStreet(street);
							connectedStreets.add(street);
							street.addPreviousStreet(this);
						}
					}
				}
			}
		}
	}

	public void generate() {
		generateAsphaltAndSidewalk();
	}

	public void generateBuildings() {
		if (tunnel || world.isRemote)
			return;
		EnumFacing f = (facing == EnumFacing.EAST || facing == EnumFacing.WEST) ? facing.rotateY()
				: facing.rotateYCCW();

		BlockPos pos = start;
		Biome biome = world.getBiome(pos);
		int i = 0;
		while (i < (canBranch ? city.type.roadLength - 10 : city.type.roadLength)) {
			Building building = CityHelper.getRandomBuilding(city.rand);
			if (building.allowedCityTypes != null && !building.allowedCityTypes.contains(city.type))
				continue;
			if (building.allowedBiomes != null && !building.allowedBiomes.isEmpty()
					&& !building.allowedBiomes.contains(biome))
				continue;
			Plot plot = new Plot(this, 0, building, false, pos);

			if (facing.getAxis() == EnumFacing.Axis.X) {
				i += plot.xSize + 1;
				pos = pos.offset(facing, plot.xSize + 1);

			} else {
				i += plot.zSize + 1;
				pos = pos.offset(facing, plot.zSize + 1);
			}
			if (i > (canBranch ? city.type.roadLength - 10 : city.type.roadLength))
				break;
			if (city.doesPlotIntersect(plot)) {
				continue;
			}
			plots.add(plot);
			plot.generate();

		}
		pos = start;
		i = 0;
		while (i < (canBranch ? city.type.roadLength - 10 : city.type.roadLength)) {
			Building building = CityHelper.getRandomBuilding(city.rand);
			if (building.allowedCityTypes != null && !building.allowedCityTypes.contains(city.type))
				continue;
			if (building.allowedBiomes != null && !building.allowedBiomes.isEmpty()
					&& !building.allowedBiomes.contains(biome))
				continue;
			if (!building.canBeMirrored) {
				continue;
			}
			Plot plot = new Plot(this, 0, building, true, pos);

			if (facing.getAxis() == EnumFacing.Axis.X) {
				i += plot.xSize + 1;
				pos = pos.offset(facing, plot.xSize + 1);

			} else {
				i += plot.zSize + 1;
				pos = pos.offset(facing, plot.zSize + 1);
			}
			if (i > (canBranch ? city.type.roadLength - 10 : city.type.roadLength))
				break;
			if (city.doesPlotIntersect(plot)) {
				continue;
			}
			plots.add(plot);
			plot.generate();

		}

	}

	public void checkEnding() {
		int cross = 0;
		if (endCrossing == null) {
			cross = city.getStreetsAtCrossingCount(end, 4);
		} else {
			cross = endCrossing.getStreetsCont();
		}
		if (cross > 2)
			return;

		if (cross == 2) {
			for (Street street : endCrossing.getStreets()) {
				if (street != this && street.facing == facing) {
					return;
				}
			}

			int r = (int) Math.ceil(city.type.getRoadWidth() / 2);
			for (int i = 1; i < r + city.type.getPavementWidth() + 1; i++) {
				for (int j = 1; j < r + city.type.getPavementWidth() + 1; j++) {
					BlockPos pos = end.offset(facing.rotateYCCW(), i).offset(facing, j);
					IBlockState state = city.type.pavementBlock == null ? Blocks.STONEBRICK.getDefaultState()
							: city.type.pavementBlock;
					if (i <= r && j <= r) {
						state = city.type.roadBlock;
					}
					Block block2 = world.getBlockState(pos).getBlock();
					if (block2 != city.type.roadBlock.getBlock() && block2 != Blocks.CONCRETE) {
						world.setBlockState(pos, state);
						BlockPos pos2 = pos.down();
						while (world.getBlockState(pos2).getBlock().isReplaceable(world, pos2)) {
							world.setBlockState(pos2, ModBlocks.STRUCTURE_STONE.getDefaultState());
							pos2 = pos2.down();
						}
					} else {
						break;
					}
				}
			}
		}

	}

	// To-do: Clean this, fix the random stonebrick near crossing
	public void generateAsphaltAndSidewalk() {
		int cross = city.getStreetsAtCrossingCount(end,
				(city.type.getRoadWidth() + city.type.getPavementWidth() + 2) / 2);

		int roofBlocks = 0;

		int startHeight = Utils.getTopSolidGroundHeight(start, world) - 1;
		int endHeight = Utils.getTopSolidGroundHeight(end, world) - 1;

		start = new BlockPos(start.getX(), startHeight, start.getZ());
		end = new BlockPos(end.getX(), endHeight, end.getZ());

		int width = city.type.getRoadWidth() + (city.type.getPavementWidth() * 2);

		for (int i = 0; i <= city.type.roadLength - 1; i++) {

			int lerpedHeight = (int) Utils.lerp(startHeight, endHeight, i / (float) city.type.roadLength);
			int y = lerpedHeight;

			for (int t = 0; t < width; t++) {

				int offset = t - Math.round(width / 2f) + 1;

				BlockPos pos = new BlockPos(start.getX(), 0, start.getZ()).offset(facing, i)
						.offset(facing.rotateY(), offset).up(y);
				IBlockState originalState = world.getBlockState(pos);
				Block originalBLock = originalState.getBlock();

				IBlockState stateUnder = world.getBlockState(pos.down());
				Block blockUnder = stateUnder.getBlock();

				if (originalBLock == Blocks.CONCRETE || blockUnder == Blocks.CONCRETE
						|| stateUnder == city.type.roadBlock)
					continue;

				// Columns
				if ((offset >= -1 && offset <= 1) && ((i % 12 == 0) || (i % 12 == 11) || (i % 12 == 1))) {
					BlockPos pos2 = pos.down(2);
					while (world.getBlockState(pos2).getBlock().isReplaceable(world, pos2)) {
						world.setBlockState(pos2, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR,
								EnumDyeColor.SILVER));
						pos2 = pos2.down();
					}

				}

				IBlockState stateToPlace = city.type.roadBlock;

				// Zebras
				if (cross > 1 && (i == 6 || i == 7) && t % 2 == 0) {
					stateToPlace = Blocks.CONCRETE.getDefaultState();
				}

				// Sidewalk
				if ((t < city.type.getPavementWidth() || t >= city.type.getRoadWidth() + city.type.getPavementWidth())
						&& originalState != city.type.roadBlock) {
					if (city.type.pavementBlock == null) {
						stateToPlace = Blocks.STONEBRICK.getDefaultState();
						// Diversify
						if (city.rand.nextInt() < 3) {
							stateToPlace = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
									BlockStoneBrick.EnumType.CRACKED);
						} else if (city.rand.nextInt() < 2) {
							stateToPlace = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT,
									BlockStoneSlab.EnumType.SMOOTHBRICK);
						} else {
							Biome biome = world.getBiome(pos);
							if (biome.getTemperature(pos) > 0.5 && city.rand.nextInt() < 2) {
								if (biome.getRainfall() > 0.1) {
									stateToPlace = Blocks.STONEBRICK.getDefaultState()
											.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
								} else {
									stateToPlace = ModBlocks.DEAD_MOSSY_BRICK.getDefaultState();
								}
							} else {
								stateToPlace = Blocks.STONEBRICK.getDefaultState();
							}

						}
					} else {
						stateToPlace = city.type.pavementBlock;
						if (city.rand.nextInt(7) == 0) {
							stateToPlace = originalState;
						}
					}
				}

				// Removed blocks above the road -- tunnels
				for (int air = 1; air < 6; air++) {
					BlockPos pos2 = pos.up(air);
					IBlockState state = world.getBlockState(pos2);
					Block block2 = state.getBlock();
					// Keeps trees in
					if (block2 instanceof BlockLog || block2 instanceof BlockLeaves || block2 instanceof BlockAir)
						continue;
					world.setBlockToAir(pos2);
					if (BlockAsphalt.isCityAsphalt(state) || block2 == Blocks.CONCRETE) {
						stateToPlace = state;
					}
					IBlockState aboveState = world.getBlockState(pos2.up());
					if (aboveState.getBlock() instanceof BlockLog
							&& aboveState.getValue(BlockLog.LOG_AXIS) == BlockLog.EnumAxis.Y) {
						BlockPos pos3 = pos2;
						IBlockState state3 = world.getBlockState(pos3.down());
						while (state3.getBlock().isReplaceable(world, pos3)) {
							world.setBlockState(pos3, aboveState);
							pos3 = pos3.down();
							state3 = world.getBlockState(pos3);
						}
					} else if (air == 5 && aboveState.getBlock() != Blocks.AIR) {
						roofBlocks++;
					}

				}

				world.setBlockState(pos, stateToPlace);
				world.setBlockState(pos.down(), ModBlocks.STRUCTURE_STONE.getDefaultState());
			}
		}
		if (roofBlocks >= ((city.type.getRoadWidth() + 2 * city.type.getPavementWidth()) * city.type.roadLength) / 4) {
			this.tunnel = true;
		}
	}

	/*
	 * Sewers
	 */
	public void generateSewers() {

		boolean flag = false;

		int width = city.type.getRoadWidth() + (city.type.getPavementWidth() * 2);

		for (int i = 0; i <= city.type.roadLength - 1; i++) {

			int lerpedHeight = (int) Utils.lerp(start.getY(), end.getY(), i / (float) city.type.roadLength);
			int y = lerpedHeight;

			for (int t = 0; t < width; t++) {

				int offset = t - Math.round(width / 2f) + 1;
				BlockPos pos = new BlockPos(start.getX(), 0, start.getZ()).offset(facing, i).offset(facing.rotateY(),
						offset);

				Chunk chunk = world.getChunkFromBlockCoords(pos);

				BlockPos road = pos.up(y);

				if (lerpedHeight >= 9 && Math.abs(offset) < SEWERS_WIDTH) {
					BlockPos sewersFloot = road.down(7);

					for (int j = 0; j < SEWERS_HEIGHT; j++) { // height of the sewers ceiling
						BlockPos sewersPos = sewersFloot.up(j);
						// state to place
						IBlockState state = Blocks.AIR.getDefaultState();
						// state to replace
						IBlockState replaceState = world.getBlockState(sewersPos);
						Block replaceBlock = replaceState.getBlock();
						if ((replaceState.getMaterial() == Material.WATER || replaceState.getMaterial() == Material.AIR)
								&& (i <= 2 || i >= city.type.roadLength - 3))
							continue;
						if ((i > 2 || canBranch) && Math.abs(offset) == SEWERS_WIDTH - 1 || j == SEWERS_HEIGHT - 1
								|| (j == 0 && offset != 0)) {
							if (city.rand.nextInt(7) == 0) {
								state = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
										BlockStoneBrick.EnumType.CRACKED);
							} else if (city.rand.nextInt(5) == 0) {
								state = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
										BlockStoneBrick.EnumType.MOSSY);
							} else {

								state = Blocks.STONEBRICK.getDefaultState();
							}
						}
						if (i > 2 && Math.abs(offset) == 1 && j == 3) {
							state = Blocks.STONE_BRICK_STAIRS.getDefaultState()
									.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(
											BlockStairs.FACING, offset > 0 ? facing.rotateY() : facing.rotateYCCW());
						} else if (j == 0 && offset == 0) {
							state = Blocks.WATER.getDefaultState();
							world.setBlockState(sewersPos.down(), Blocks.STONEBRICK.getDefaultState());
						} else if (i != 0 && i % 8 == 0 && (int) Math.abs(offset) == 1 && (j == 1 || j == 2)) {
							state = Blocks.IRON_BARS.getDefaultState();
						} else if (city.rand.nextInt(128) == 0 && (int) Math.abs(offset) == 1 && j == 1) {
							world.setBlockState(sewersPos, ModBlocks.BACKPACK_NORMAL.getDefaultState().withProperty(
									BlockBackpack.FACING, EnumFacing.getHorizontal(city.rand.nextInt(4))));
							TileEntity te = world.getTileEntity(sewersPos);
							if (te != null && te instanceof TileEntityBackpack) {
								TileEntityBackpack backpack = (TileEntityBackpack) te;
								backpack.setLootTable(ModLootTables.BACKPACK, city.rand.nextLong());
								backpack.fillWithLoot(null);
							}
							continue;
						}

						world.setBlockState(sewersPos, state);
					}
					if (i == 0 && offset == 0) {
						for (int j = 0; j < 8 - SEWERS_HEIGHT + 1; j++) {
							IBlockState state = ModBlocks.METAL_LADDER.getDefaultState();
							BlockPos manholePos = sewersFloot.up(SEWERS_HEIGHT - 1 + j);
							if (j == 8 - SEWERS_HEIGHT) {
								state = Blocks.TRAPDOOR.getDefaultState().withProperty(BlockTrapDoor.HALF,
										BlockTrapDoor.DoorHalf.TOP);
								for (EnumFacing f : EnumFacing.HORIZONTALS) {
									if (world.getBlockState(manholePos.offset(f)).getMaterial().isReplaceable()) {
										world.setBlockState(manholePos.offset(f), city.type.roadBlock);
									}
								}
							}
							world.setBlockState(manholePos, state);
						}
					}
				}
			}
			if (i > 8 && i < 20 && city.rand.nextInt(20) == 0 && !flag) {
				IBlockState entranceState = Blocks.GRAVEL.getDefaultState();
				if (city.rand.nextInt(3) == 0) {
					entranceState = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
							BlockStoneBrick.EnumType.CRACKED);
				} else if (city.rand.nextInt(3) == 0) {
					entranceState = Blocks.IRON_BARS.getDefaultState();
				} else if (city.rand.nextInt(3) == 0) {
					entranceState = Blocks.COBBLESTONE.getDefaultState();
				} else if (city.rand.nextInt(3) == 0) {
					entranceState = Blocks.AIR.getDefaultState();
				}
				BlockPos entrance = new BlockPos(start.getX(), 0, start.getZ()).offset(facing, i)
						.offset(facing.rotateY(), (int) Math.ceil(SEWERS_WIDTH / 2) + 1).up(y).down(7).up(1);
				world.setBlockState(entrance, entranceState);
				world.setBlockState(entrance.up(), entranceState);
				for (int xx = -4; xx < 4; xx++) {
					for (int yy = -1; yy < 4; yy++) {
						for (int zz = 0; zz < 6; zz++) {
							BlockPos pos2 = entrance.offset(facing, xx).up(yy).offset(facing.rotateY(), 1 + zz);
							IBlockState state = Blocks.AIR.getDefaultState();
							if (yy == -1 || yy == 3 || zz == 5 || xx == -4 || xx == 3) {
								state = Blocks.STONEBRICK.getDefaultState();
								if (city.rand.nextInt(7) == 0) {
									state = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
											BlockStoneBrick.EnumType.CRACKED);
								} else if (city.rand.nextInt(5) == 0) {
									state = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
											BlockStoneBrick.EnumType.MOSSY);
								}
							} else if (yy == 0) {
								if (xx == 2 && zz == 0) {
									state = Blocks.CRAFTING_TABLE.getDefaultState();
								} else if (xx == 0 && zz == 2) {
									state = ModBlocks.CAMPFIRE.getDefaultState();
								} else if (xx == -3 && zz == 2) {
									state = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, facing);
								} else if (city.rand.nextInt(4) == 0) {
									state = Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR,
											EnumDyeColor.BROWN);
								}
							}
							world.setBlockState(pos2, state);

							TileEntity te = world.getTileEntity(pos2);
							if (te != null) {
								if (te instanceof TileEntityChest) {
									TileEntityChest chest = (TileEntityChest) te;
									chest.setLootTable(ModLootTables.SUPPLY_CHEST, city.rand.nextLong());
									chest.fillWithLoot(null);
								}
							}

						}
					}
				}
				flag = true;
			}
		}
	}

	/*
	 * Handles generating of street lamps, traffic lights, city limits signs and
	 * cars
	 */
	public void decorate() {

		int width = city.type.getRoadWidth() + (city.type.getPavementWidth() * 2);
		int halfThickness = (int) Math.ceil(city.type.getRoadWidth() / 2);

		for (int i = 0; i <= city.type.roadLength - 1; i++) {

			// What should be Y of the road in this position between the start and end
			int lerpedHeight = (int) Utils.lerp(start.getY(), end.getY(), i / (float) city.type.roadLength);
			int y = lerpedHeight;

			for (int t = 0; t < width; t++) {

				int offset = t - Math.round(width / 2f) + 1;
				BlockPos pos = new BlockPos(start.getX(), 0, start.getZ()).offset(facing, i).offset(facing.rotateY(),
						offset);

				Biome biome = world.getBiome(pos);
				if (!tunnel) {
					if (canBranch) {
						if ((endCrossing != null && endCrossing.getStreets().size() > 2)
								|| city.getStreetsAtCrossingCount(end, 4) > 2) {
							if (i == city.type.roadLength
									- (city.type.getRoadWidth() - (2 - (city.type.getRoadWidth() - 7)))
									&& t == city.type.getRoadWidth() + (city.type.getPavementWidth() * 2) - 2) {
								generateTrafficLight(pos.up(y + 1));
							}
						}

						if (i < city.type.roadLength - (halfThickness + city.type.getPavementWidth())
								&& (i + 5) % 8 == 0) {
							if ((i + 5) % 16 == 0 || city.type == EnumCityType.CITY) {
								if (t == 0) {
									generateStreetLamp(pos.up(y + 1), false);
								} else if (t == city.type.getRoadWidth() + (city.type.getPavementWidth() * 2) - 1) {
									generateStreetLamp(pos.up(y + 1), true);

								}
							} else if (city.rand.nextInt(10) == 0) {
								if (t == 0 || t == city.type.getRoadWidth() + (city.type.getPavementWidth() * 2) - 1) {
									world.setBlockState(pos.up(y + 1), ModBlocks.TRASH_CAN.getDefaultState());
									TileEntityTrashCan te = (TileEntityTrashCan) world.getTileEntity(pos.up(y + 1));
									if (te != null) {
										te.setLootTable(ModLootTables.TRASH, city.rand.nextLong());
										te.fillWithLoot(null);
									}
								}
							}
						}

					} else {
						if ((startCrossing != null && startCrossing.getStreets().size() > 2)
								|| city.getStreetsAtCrossingCount(start, 4) > 2) {
							if (i == Math.ceil(city.type.getRoadWidth() / 2) + (1 - (city.type.getRoadWidth() - 7))
									&& t == city.type.getPavementWidth() - 1) {
								generateTrafficLight(pos.up(y + 1));
							}
						}
						if (i > halfThickness + city.type.getPavementWidth() && (i - halfThickness) % 8 == 0) {
							if ((i - halfThickness) % 16 == 0 || city.type == EnumCityType.CITY) {
								if (t == 0) {
									generateStreetLamp(pos.up(y + 1), false);
								} else if (t == city.type.getRoadWidth() + (city.type.getPavementWidth() * 2) - 1) {
									generateStreetLamp(pos.up(y + 1), true);
								}
							} else if (city.rand.nextInt(10) == 0) {
								if (t == 0 || t == city.type.getRoadWidth() + (city.type.getPavementWidth() * 2) - 1) {
									world.setBlockState(pos.up(y + 1), ModBlocks.TRASH_CAN.getDefaultState());
									TileEntityTrashCan te = (TileEntityTrashCan) world.getTileEntity(pos.up(y + 1));
									if (te != null) {
										te.setLootTable(ModLootTables.TRASH, city.rand.nextLong());
										te.fillWithLoot(null);
									}
								}
							}
						}
					}
				}
				if (connectedStreets.size() == 1 && previousStreet != null) {
					if (i == city.type.roadLength - 2) {
						if (offset == -4) {
							generateCityLimitsSign(pos.up(y + 1));
						}
					}
				}

				BlockPos pos2 = pos.up(y + 1);
				if (world.getBlockState(pos2).getBlock() != Blocks.AIR) {
					pos2 = pos2.up();
				}
				if (world.getBlockState(pos2).getBlock() == Blocks.AIR
						&& world.getBlockState(pos2.down()).isSideSolid(world, pos.down(), EnumFacing.UP)) {
					// Cars generation
					if (Math.abs(t - (city.type.getRoadWidth() / 2 + city.type.getPavementWidth())) <= 2
							&& city.rand.nextInt(50) == 0) {

						EnumFacing facing2 = facing;
						if (city.rand.nextInt(4) == 0) {
							facing2 = EnumFacing.getHorizontal(city.rand.nextInt(4));
						}
						CityHelper.placeRandomCar(world, pos2, facing2, true, city.rand);
					} else if (!tunnel && city.rand.nextInt(1) == 0
							&& BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) && biome.topBlock.getBlock() == Blocks.SAND) {
						IBlockState sand = ModBlocks.SAND_LAYER.getDefaultState();
						if(biome.topBlock.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND) {
							sand = ModBlocks.RED_SAND_LAYER.getDefaultState();
						}
						world.setBlockState(pos2, sand.withProperty(BlockSandLayer.LAYERS, 1 + city.rand.nextInt(2)));

					} else if (this.city.type == EnumCityType.CITY && city.rand.nextInt(28) == 0) {

						world.setBlockState(pos2, ModBlocks.PAPER.getDefaultState().withProperty(BlockPaper.FACING,
								EnumFacing.getHorizontal(city.rand.nextInt(4))));

					}
				}

			}

		}
	}

	public void generateCityLimitsSign(BlockPos pos) {
		IBlockState base = world.getBlockState(pos.down());
		if (base.getBlock() == Blocks.STONE_SLAB) {
			world.setBlockState(pos.down(), Blocks.STONEBRICK.getDefaultState());
		}
		IBlockState base2 = world.getBlockState(pos.down().offset(facing.rotateY(), city.type.getRoadWidth() + 1));
		if (base2.getBlock() == Blocks.STONE_SLAB) {
			world.setBlockState(pos.down(), Blocks.STONEBRICK.getDefaultState());
		}

		for (int i = 0; i < 8; i++) {
			world.setBlockState(pos.up(i), getRandomWall(city.rand));
			world.setBlockState(pos.up(i).offset(facing.rotateY(), 8), getRandomWall(city.rand));
		}

		for (int i = 0; i < 9; i++) {
			world.setBlockState(pos.up(7).offset(facing.rotateY(), i), Blocks.STONE_SLAB.getDefaultState());
		}

		for (int i = 1; i < 8; i++) {
			for (int j = 0; j < 3; j++) {
				world.setBlockState(pos.up(4 + j).offset(facing.rotateY(), i), Blocks.STAINED_HARDENED_CLAY
						.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GREEN));
			}
		}

		BlockPos signPos = pos.up(4).offset(facing.rotateY(), 4).offset(facing);
		world.setBlockState(signPos,
				ModBlocks.BIG_SIGN_MASTER.getDefaultState().withProperty(BlockWallSign.FACING, facing));
		TileEntity te = world.getTileEntity(signPos);
		if (te != null && te instanceof TileEntityBigSignMaster) {
			TileEntityBigSignMaster sign = (TileEntityBigSignMaster) te;
			sign.setTextColor(TextFormatting.WHITE);
			sign.signText[0] = new TextComponentString(city.name);
			sign.signText[1] = new TextComponentString("City Limits");
			sign.signText[2] = new TextComponentString("Pop. " + city.population);
		}
	}

	public IBlockState getRandomWall(Random rand) {
		return rand.nextInt(8) == 0
				? Blocks.COBBLESTONE_WALL.getDefaultState().withProperty(BlockWall.VARIANT, BlockWall.EnumType.MOSSY)
				: Blocks.COBBLESTONE_WALL.getDefaultState();
	}

	public void generateTrafficLight(BlockPos pos) {

		IBlockState poleState = getRandomWall(city.rand);
		switch (city.type) {
		default:
		case CITY:
		case METROPOLIS:
			poleState = getRandomWall(city.rand);
			break;
		case VILLAGE:
		case TOWN:
			poleState = Blocks.OAK_FENCE.getDefaultState();
			break;
		}

		IBlockState base = world.getBlockState(pos.down());
		if (base.getBlock() == Blocks.STONE_SLAB) {
			world.setBlockState(pos.down(), Blocks.STONEBRICK.getDefaultState());
		}
		int height = 5;
		for (int i = 0; i < height; i++) {
			world.setBlockState(pos.up(i), poleState);
		}
		if (world.getBlockState(pos.down()).getBlock() == Blocks.AIR) {
			int i = 1;
			while (world.getBlockState(pos.down(i)).getBlock() == Blocks.AIR) {
				world.setBlockState(pos.down(i), poleState);
				i--;
				if (world.getBlockState(pos.down(i)).getBlock() == Blocks.STONE_SLAB) {
					world.setBlockState(pos.down(i), poleState);
				}
			}
		}
		world.setBlockState(pos.up(height), Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
				BlockStoneBrick.EnumType.CHISELED));
		if (!canBranch) {
			for (int i = 1; i < 4; i++) {
				world.setBlockState(pos.up(height).offset(facing.rotateY(), i), poleState);
				world.setBlockState(pos.up(height).offset(facing.rotateY(), i).offset(facing, 1),
						ModBlocks.TRAFFIC_LIGHT.getDefaultState().withProperty(BlockHorizontalBase.FACING, facing));
			}

			world.setBlockState(pos.up(2).offset(facing.rotateY(), 1), ModBlocks.TRAFFIC_LIGHT_PEDESTRIAN
					.getDefaultState().withProperty(BlockHorizontalBase.FACING, facing.rotateY()));
			world.setBlockState(pos.up(2).offset(facing, -1), ModBlocks.TRAFFIC_LIGHT_PEDESTRIAN.getDefaultState()
					.withProperty(BlockHorizontalBase.FACING, facing.getOpposite()));

			world.setBlockState(pos.up(2).offset(facing, 1),
					ModBlocks.STREET_SIGN_WALL.getDefaultState().withProperty(BlockWallSign.FACING, facing), 11);
			String otherName = "";
			if (startCrossing != null && startCrossing.getStreets() != null) {
				for (Street street : startCrossing.getStreets()) {
					if (street.facing != this.facing && street.facing != this.facing.getOpposite()) {
						otherName = street.name;
						break;
					}
				}
			}

			tryToSetSignText(world, pos.up(2).offset(facing, 1), otherName);
			world.setBlockState(pos.up(2).offset(facing.rotateYCCW(), 1), ModBlocks.STREET_SIGN_WALL.getDefaultState()
					.withProperty(BlockWallSign.FACING, facing.rotateYCCW()), 11);
			tryToSetSignText(world, pos.up(2).offset(facing.rotateYCCW(), 1), name);
		} else {
			for (int i = 1; i < 4; i++) {
				world.setBlockState(pos.up(height).offset(facing.rotateY(), -i), getRandomWall(city.rand));
				world.setBlockState(pos.up(height).offset(facing.rotateY(), -i).offset(facing, -1),
						ModBlocks.TRAFFIC_LIGHT.getDefaultState().withProperty(BlockHorizontalBase.FACING,
								facing.getOpposite()));
			}

			world.setBlockState(pos.up(2).offset(facing.rotateY(), -1), ModBlocks.TRAFFIC_LIGHT_PEDESTRIAN
					.getDefaultState().withProperty(BlockHorizontalBase.FACING, facing.rotateY().getOpposite()));
			world.setBlockState(pos.up(2).offset(facing, 1), ModBlocks.TRAFFIC_LIGHT_PEDESTRIAN.getDefaultState()
					.withProperty(BlockHorizontalBase.FACING, facing));

			world.setBlockState(pos.up(2).offset(facing, -1), ModBlocks.STREET_SIGN_WALL.getDefaultState()
					.withProperty(BlockWallSign.FACING, facing.getOpposite()), 11);
			String otherName = "";
			if (endCrossing != null) {
				for (Street street : endCrossing.getStreets()) {
					if (street.facing != this.facing && street.facing != this.facing.getOpposite()) {
						otherName = street.name;
						break;
					}
				}
			}

			tryToSetSignText(world, pos.up(2).offset(facing, -1), otherName);
			world.setBlockState(pos.up(2).offset(facing.rotateYCCW(), -1), ModBlocks.STREET_SIGN_WALL.getDefaultState()
					.withProperty(BlockWallSign.FACING, facing.rotateYCCW().getOpposite()), 11);
			tryToSetSignText(world, pos.up(2).offset(facing.rotateYCCW(), -1), name);
		}
	}

	public void tryToSetSignText(World world, BlockPos pos, String text) {
		TileEntity te = world.getTileEntity(pos);
		if (te == null || !(te instanceof TileEntityStreetSign))
			return;
		TileEntityStreetSign sign = (TileEntityStreetSign) te;
		Iterable<String> result = Splitter.fixedLength(15).split(text);
		String[] parts = Iterables.toArray(result, String.class);
		for (int i = 0; i < 4; i++) {
			if (i + 1 > parts.length) {
				break;
			}
			sign.setTextColor(TextFormatting.WHITE);
			sign.signText[i] = new TextComponentString(parts[i]);
		}
	}

	public void generateStreetLamp(BlockPos pos, boolean end) {
		switch (city.type) {
		default:
		case CITY:
		case METROPOLIS:
			generateCityStreetLamp(pos, end);
			break;
		case VILLAGE:
		case TOWN:
			generateVillageStreetLamp(pos, end);
			break;
		}
	}

	public void generateCityStreetLamp(BlockPos pos, boolean end) {
		IBlockState base = world.getBlockState(pos.down());
		if (base.getBlock() == Blocks.STONE_SLAB) {
			world.setBlockState(pos.down(), Blocks.STONEBRICK.getDefaultState());
		}
		int height = 5;
		for (int i = 0; i < height; i++) {
			world.setBlockState(pos.up(i), getRandomWall(city.rand));
		}
		if (world.getBlockState(pos.down()).getBlock() == Blocks.AIR) {
			int i = 1;
			while (world.getBlockState(pos.down(i)).getBlock() == Blocks.AIR) {
				world.setBlockState(pos.down(i), getRandomWall(city.rand));
				i--;
				if (world.getBlockState(pos.down(i)).getBlock() == Blocks.STONE_SLAB) {
					world.setBlockState(pos.down(i), Blocks.STONEBRICK.getDefaultState());
				}
			}
		}
		BlockPos top = pos.up(height);
		world.setBlockState(top,
				ModBlocks.FAKE_ANVIL.getDefaultState().withProperty(BlockFakeAnvil.FACING, facing.rotateY()));
		world.setBlockState(top.up(), Blocks.STONE_SLAB.getDefaultState());
		
		BlockPos lamp = null;
		
		if (!end) {
			world.setBlockState(top.offset(facing.rotateY()),
					Blocks.HOPPER.getDefaultState().withProperty(BlockHopper.FACING, facing.rotateY()));
			lamp = top.offset(facing.rotateY(), 2);
			
			world.setBlockState(lamp, ModBlocks.REDSTONE_LAMP_BROKEN.getDefaultState());

			world.setBlockState(top.offset(facing.rotateY()).up(), Blocks.STONE_SLAB.getDefaultState());
			world.setBlockState(lamp.up(), Blocks.STONE_SLAB.getDefaultState());
		} else {
			world.setBlockState(top.offset(facing.rotateYCCW()),
					Blocks.HOPPER.getDefaultState().withProperty(BlockHopper.FACING, facing.rotateYCCW()));
			lamp = top.offset(facing.rotateYCCW(), 2);
			world.setBlockState(lamp, ModBlocks.REDSTONE_LAMP_BROKEN.getDefaultState());

			world.setBlockState(top.offset(facing.rotateYCCW()).up(), Blocks.STONE_SLAB.getDefaultState());
			world.setBlockState(lamp.up(), Blocks.STONE_SLAB.getDefaultState());
		}
		for(EnumFacing facing : EnumFacing.HORIZONTALS) {
			BlockPos vineStart = lamp.offset(facing);
			IBlockState toReplace = world.getBlockState(vineStart);
			IBlockState vineState = Blocks.VINE.getDefaultState().withProperty(BlockVine.getPropertyFor(facing.getOpposite()), true);
			while(city.rand.nextBoolean() && toReplace.getBlock() == Blocks.AIR && vineStart.getY() > 0) {
				world.setBlockState(vineStart, vineState);
				vineStart=vineStart.down();
				toReplace = world.getBlockState(vineStart);
			}
		}
	}

	public void generateVillageStreetLamp(BlockPos pos, boolean end) {
		IBlockState base = world.getBlockState(pos.down());
		if (base.getBlock() == Blocks.STONE_SLAB) {
			world.setBlockState(pos.down(), Blocks.STONEBRICK.getDefaultState());
		}
		int height = 6;
		for (int i = 0; i < height; i++) {
			if (i == height - 1) {
				world.setBlockState(pos.up(i), Blocks.STONE_SLAB.getDefaultState());
			} else if (i == height - 2) {
				world.setBlockState(pos.up(i), ModBlocks.REDSTONE_LAMP_BROKEN.getDefaultState());
			} else {
				world.setBlockState(pos.up(i), Blocks.OAK_FENCE.getDefaultState());
			}
		}
		if (world.getBlockState(pos.down()).getBlock() == Blocks.AIR) {
			int i = 1;
			while (world.getBlockState(pos.down(i)).getBlock() == Blocks.AIR) {
				world.setBlockState(pos.down(i), Blocks.OAK_FENCE.getDefaultState());
				i--;
				if (world.getBlockState(pos.down(i)).getBlock() == Blocks.STONE_SLAB) {
					world.setBlockState(pos.down(i), Blocks.STONEBRICK.getDefaultState());
				}
			}
		}
	}

	public void addConnectedStreet(Street street) {
		connectedStreets.add(street);
	}

	public void addPreviousStreet(Street street) {
		this.previousStreet = street;
		addConnectedStreet(street);
	}

	public boolean isConnectedTo(Street street) {
		return connectedStreets.contains(street);
	}

	public void readNBT(NBTTagCompound nbt) {
		this.start = BlockPos.fromLong(nbt.getLong("start"));
		this.end = BlockPos.fromLong(nbt.getLong("end"));
	}

	public NBTTagCompound writeNBT(NBTTagCompound nbt) {

		nbt.setLong("start", this.start.toLong());
		nbt.setLong("end", this.end.toLong());
		return nbt;
	}
}
