package com.nuparu.sevendaystomine.world.gen.city;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.nuparu.sevendaystomine.block.BlockCar;
import com.nuparu.sevendaystomine.block.BlockHorizontalBase;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.tileentity.TileEntityBigSignMaster;
import com.nuparu.sevendaystomine.tileentity.TileEntityStreetSign;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.building.Building;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingSupermarket;
import com.nuparu.sevendaystomine.world.gen.city.plot.Plot;
import com.nuparu.sevendaystomine.world.gen.prefab.Prefab;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Street {

	public BlockPos start;
	public BlockPos end;
	public World world;

	public Crossing startCrossing = null;
	public Crossing endCrossing = null;

	public City city;

	public static int thickness = 7;
	public static int pavement = 3; // On each side
	public EnumFacing facing;
	public boolean canBranch = true;
	public boolean tunnel = false;
	public int branchIndex = 0;

	public int streetIndex = 0;

	public String name;
	public List<Plot> plots = new ArrayList<Plot>();

	public static final int LENGTH = 64;
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
			if (f != facing.getOpposite() && city.getStreetsCount() < city.roads_limit) {
				if (canBranch || f == facing) {
					BlockPos blockpos = end.offset(f, Street.LENGTH - 1);
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
		/*
		 * Prefab prefab =
		 * CityHelper.prefabs.get(world.rand.nextInt(CityHelper.prefabs.size()));
		 * EnumFacing f = (facing == EnumFacing.EAST || facing == EnumFacing.WEST) ?
		 * facing.rotateY() : facing.rotateYCCW();
		 * 
		 * int checkA = Utils.getTopSolidGroundHeight(start.offset(facing, (LENGTH / 2)
		 * + (prefab.getWidth() / 2)).offset(f, (thickness / 2) + pavement + (f ==
		 * EnumFacing.NORTH || f == EnumFacing.EAST ? 1 : 0)), world); int checkB =
		 * Utils.getTopSolidGroundHeight(start.offset(facing, (LENGTH / 2) -
		 * (prefab.getWidth() / 2)).offset(f, (thickness / 2) + pavement + (f ==
		 * EnumFacing.NORTH || f == EnumFacing.EAST ? 1 : 0)), world); int checkC =
		 * Utils.getTopSolidGroundHeight(start.offset(facing, (LENGTH / 2) +
		 * (prefab.getWidth() / 2)).offset(f, (thickness / 2) + pavement +
		 * prefab.getLength() + (f == EnumFacing.NORTH || f == EnumFacing.EAST ? 1 :
		 * 0)), world); int checkD = Utils.getTopSolidGroundHeight(start.offset(facing,
		 * (LENGTH / 2) - (prefab.getWidth() / 2)).offset(f, (thickness / 2) + pavement
		 * + prefab.getLength() + (f == EnumFacing.NORTH || f == EnumFacing.EAST ? 1 :
		 * 0)), world);
		 * 
		 * BlockPos pos = start.offset(facing, (LENGTH / 2) + (prefab.getWidth() /
		 * 2)).offset((f == EnumFacing.NORTH || f == EnumFacing.SOUTH ? f.getOpposite()
		 * : f), (thickness / 2) + pavement + (f == EnumFacing.NORTH || f ==
		 * EnumFacing.EAST ? 1 : 0));
		 * 
		 * prefab.generate(world, new BlockPos(pos.getX(), (checkA + checkB + checkC +
		 * checkD) / 4, pos.getZ()), f, true);
		 */
		// if(facing.getAxis() == EnumFacing.Axis.X) {
		/*
		 * CityHelper.buildings.get(world.rand.nextInt(CityHelper.buildings.size())).
		 * generate(world, start.offset(f, (thickness / 2) + pavement + (f ==
		 * EnumFacing.NORTH || f == EnumFacing.EAST ? 1 : 0)), facing);
		 */
		/*
		 * CityHelper.buildings.get(world.rand.nextInt(CityHelper.buildings.size())).
		 * generate(world, start, facing, false); if (canBranch) {
		 * CityHelper.buildings.get(world.rand.nextInt(CityHelper.buildings.size())).
		 * generate(world, start, facing, true); }
		 */
		BlockPos pos = start;
		// if(facing.getAxisDirection()==EnumFacing.AxisDirection.POSITIVE)return;
		// if(facing!=EnumFacing.NORTH)return;
		int i = 0;
		while (i < (canBranch ? LENGTH - 10 : LENGTH)) {
			Plot plot = new Plot(this, 0, CityHelper.getRandomBuilding(world.rand), false, pos);

			if (facing.getAxis() == EnumFacing.Axis.X) {
				i += plot.xSize + 1;
				pos = pos.offset(facing, plot.xSize + 1);

			} else {
				i += plot.zSize + 1;
				pos = pos.offset(facing, plot.zSize + 1);
			}
			if (i > (canBranch ? LENGTH - 10 : LENGTH))
				break;
			if (city.doesPlotIntersect(plot)) {
				continue;
			}
			plots.add(plot);
			plot.generate();

		}
		pos = start;
		i = 0;
		while (i < (canBranch ? LENGTH - 10 : LENGTH)) {
			// HAVE TO FIND IF ANY OTHER BUILDING FITS
			Building building = CityHelper.getRandomBuilding(world.rand);
			if (building instanceof BuildingSupermarket)
				continue;
			Plot plot = new Plot(this, 0, building, true, pos);

			if (facing.getAxis() == EnumFacing.Axis.X) {
				i += plot.xSize + 1;
				pos = pos.offset(facing, plot.xSize + 1);

			} else {
				i += plot.zSize + 1;
				pos = pos.offset(facing, plot.zSize + 1);
			}
			if (i > (canBranch ? LENGTH - 10 : LENGTH))
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

			int r = (int) Math.ceil(thickness / 2);
			for (int i = 1; i < r + pavement + 1; i++) {
				for (int j = 1; j < r + pavement + 1; j++) {
					BlockPos pos = end.offset(facing.rotateYCCW(), i).offset(facing, j);
					IBlockState state = Blocks.STONEBRICK.getDefaultState();
					if (i <= r && j <= r) {
						state = ModBlocks.ASPHALT.getDefaultState();
					}
					Block block2 = world.getBlockState(pos).getBlock();
					if (block2 != ModBlocks.ASPHALT && block2 != Blocks.CONCRETE) {
						world.setBlockState(pos, state);
						BlockPos pos2 = pos.down();
						while (world.getBlockState(pos2).getBlock().isReplaceable(world, pos2)) {
							world.setBlockState(pos2, Blocks.STONE.getDefaultState());
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
		int roofBlocks = 0;

		int startHeight = Utils.getTopSolidGroundHeight(start, world) - 1;
		int endHeight = Utils.getTopSolidGroundHeight(end, world) - 1;

		start = new BlockPos(start.getX(), startHeight, start.getZ());
		end = new BlockPos(end.getX(), endHeight, end.getZ());
		for (int i = 0; i <= LENGTH - 1; i++) {

			int lerpedHeight = (int) Utils.lerp(startHeight, endHeight, i / (float) LENGTH);
			int y = lerpedHeight;

			for (int t = 0; t < thickness + (pavement * 2); t++) {

				int offset = t - (thickness + (pavement * 2) / 2) + 4;
				if (facing == EnumFacing.NORTH || facing == EnumFacing.WEST) {

				}
				BlockPos pos = new BlockPos(start.getX(), 0, start.getZ()).offset(facing, i).offset(facing.rotateY(),
						offset);
				BlockPos road = pos.up(y);

				IBlockState block = ModBlocks.ASPHALT.getDefaultState();

				int cross = 0;
				if (endCrossing == null) {
					cross = city.getStreetsAtCrossingCount(end, 4);
				} else {
					cross = endCrossing.getStreetsCont();
				}

				int tt = t - ((int) Math.ceil(pavement / 2));

				// Makes the blocks more diverse
				if ((t < pavement || t >= thickness + pavement)) {
					if (world.rand.nextInt() < 3) {
						block = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
								BlockStoneBrick.EnumType.CRACKED);
					} else if (world.rand.nextInt() < 2) {
						block = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT,
								BlockStoneSlab.EnumType.SMOOTHBRICK);
					} else {
						Biome biome = world.getBiome(pos);
						if (biome.getTemperature(pos) > 0.5 && world.rand.nextInt() < 2) {
							if (biome.getRainfall() > 0.1) {
								block = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
										BlockStoneBrick.EnumType.MOSSY);
							} else {
								block = ModBlocks.DEAD_MOSSY_BRICK.getDefaultState();
							}
						} else {
							block = Blocks.STONEBRICK.getDefaultState();
						}

					}

				} /*
					 * else if (!canBranch && (i == 5 || i == 6) && (tt == 7 || tt == 5 || tt == 3)
					 * && cross > 1) { block = Blocks.CONCRETE.getDefaultState(); } else if
					 * (canBranch && (i == LENGTH - (thickness - 0) || i == LENGTH - (thickness -
					 * 1)) && (tt == 7 || tt == 5 || tt == 3) && cross > 1) { block =
					 * Blocks.CONCRETE.getDefaultState(); }
					 */

				// Makes room above the road - except when there is a tree
				for (int air = y + 1; air < y + 6; air++) {
					BlockPos pos2 = pos.up(air);
					IBlockState state = world.getBlockState(pos2);
					Block block2 = state.getBlock();
					if (block2 == ModBlocks.ASPHALT) {
						block = ModBlocks.ASPHALT.getDefaultState();
					} else if (block2 == Blocks.CONCRETE) {
						block = Blocks.CONCRETE.getDefaultState();
					}
					if (block2 != Blocks.AIR && !(block2 instanceof BlockLog) && !(block2 instanceof BlockLeaves)) {
						world.setBlockState(pos2, Blocks.AIR.getDefaultState());
						if (air == y + 5) {
							roofBlocks++;
						}
					}

					// Handles fixing of floating trees

					BlockPos pos3 = pos2.up();
					IBlockState state2 = world.getBlockState(pos3);
					Block block3 = state2.getBlock();
					if (block3 instanceof BlockLog && state2.getValue(BlockLog.LOG_AXIS) == BlockLog.EnumAxis.Y) {
						IBlockState state3 = world.getBlockState(pos3.down());
						while (state3.getBlock().isReplaceable(world, pos3.down())) {
							world.setBlockState(pos3.down(), state2);
							pos3 = pos3.down();
							state3 = world.getBlockState(pos3.down());
						}
						break;
					}
				}

				boolean flag = false;

				// Places the road?
				for (int air = y; air > y - 6; air--) {
					BlockPos pos2 = pos.up(air);
					IBlockState state = world.getBlockState(pos2);
					Block block2 = state.getBlock();

					boolean flag2 = false;

					if (block2 == ModBlocks.ASPHALT || block2 == Blocks.CONCRETE || block2 == Blocks.CONCRETE
							|| block2 == Blocks.STONEBRICK || block2 == Blocks.STONE_SLAB
							|| block2 == ModBlocks.DEAD_MOSSY_BRICK) {
						flag2 = true;
					}
					if (flag2) {
						flag = true;
						if (block.getBlock() != Blocks.STONEBRICK && block.getBlock() != Blocks.STONE_SLAB) {
							world.setBlockState(pos2, block);
						}
						break;
					}
				}

				if (i >= 0 && i <= LENGTH && !flag) {

					Block block2 = world.getBlockState(road).getBlock();
					if (block2 != ModBlocks.ASPHALT && block2 != Blocks.CONCRETE || (block == Blocks.CONCRETE)) {
						world.setBlockState(road, block);
						world.setBlockState(road.down(), Blocks.STONE.getDefaultState());
					}
				}

				if ((offset >= -1 && offset <= 1) && ((i % 12 == 0) || (i % 12 == 11) || (i % 12 == 1))) {
					BlockPos pos2 = road.down(2);
					while (world.getBlockState(pos2).getBlock().isReplaceable(world, pos2)) {
						world.setBlockState(pos2, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR,
								EnumDyeColor.SILVER));
						pos2 = pos2.down();
					}

				}
			}
		}
		if (roofBlocks >= ((thickness + 2 * pavement) * LENGTH) / 4) {
			this.tunnel = true;
		}
	}

	/*
	 * Sewers
	 */
	public void generateSewers() {

		for (int i = 0; i <= LENGTH - 1; i++) {

			int lerpedHeight = (int) Utils.lerp(start.getY(), end.getY(), i / (float) LENGTH);
			int y = lerpedHeight;

			for (int t = 0; t < thickness + (pavement * 2); t++) {

				int offset = t - (thickness + (pavement * 2) / 2) + 4;
				if (facing == EnumFacing.NORTH || facing == EnumFacing.WEST) {

				}
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
								&& (i <= 2 || i >= LENGTH - 3))
							continue;
						if ((i > 2 || canBranch) && Math.abs(offset) == SEWERS_WIDTH - 1 || j == SEWERS_HEIGHT - 1
								|| (j == 0 && offset != 0)) {
							if (world.rand.nextInt(7) == 0) {
								state = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
										BlockStoneBrick.EnumType.CRACKED);
							} else if (world.rand.nextInt(5) == 0) {
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
						}
						if (j == 0 && offset == 0) {
							state = Blocks.WATER.getDefaultState();
							world.setBlockState(sewersPos.down(), Blocks.STONEBRICK.getDefaultState());
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
										world.setBlockState(manholePos.offset(f), ModBlocks.ASPHALT.getDefaultState());
									}
								}
							}
							world.setBlockState(manholePos, state);
						}
					}
				}
			}
		}
	}

	/*
	 * Handles generating of street lamps, traffic lights, city limits signs and
	 * cars
	 */
	public void decorate() {

		for (int i = 0; i <= LENGTH - 1; i++) {

			// What should be Y of the road in this position between the start and end
			int lerpedHeight = (int) Utils.lerp(start.getY(), end.getY(), i / (float) LENGTH);
			int y = lerpedHeight;

			for (int t = 0; t < thickness + (pavement * 2); t++) {

				int offset = t - (thickness + (pavement * 2) / 2) + 4;
				BlockPos pos = new BlockPos(start.getX(), 0, start.getZ()).offset(facing, i).offset(facing.rotateY(),
						offset);
				int halfThickness = (int) Math.ceil(thickness / 2);
				if (canBranch) {
					if ((endCrossing != null && endCrossing.getStreets().size() > 2)
							|| city.getStreetsAtCrossingCount(end, 4) > 2) {
						if (i == LENGTH - (thickness - 2) && t == thickness + (pavement * 2) - 2) {
							generateTrafficLight(pos.up(y + 1));
						}
					}

					if (i < LENGTH - (halfThickness + pavement) && (i + 5) % 8 == 0) {
						if (t == 0) {
							generateStreetLamp(pos.up(y + 1), false);
						} else if (t == thickness + (pavement * 2) - 1) {
							generateStreetLamp(pos.up(y + 1), true);

						}
					}

				} else {
					if ((startCrossing != null && startCrossing.getStreets().size() > 2)
							|| city.getStreetsAtCrossingCount(start, 4) > 2) {
						if (i == Math.ceil(thickness / 2) + 1 && t == pavement - 1) {
							generateTrafficLight(pos.up(y + 1));
						}
					}
					if (i > halfThickness + pavement && (i - halfThickness) % 8 == 0) {
						if (t == 0) {
							generateStreetLamp(pos.up(y + 1), false);
						} else if (t == thickness + (pavement * 2) - 1) {
							generateStreetLamp(pos.up(y + 1), true);
						}
					}
				}
				if (connectedStreets.size() == 1 && previousStreet != null) {
					if (i == LENGTH - 2) {
						if (t == 2) {
							generateCityLimitsSign(pos.up(y + 1));
						}
					}
				}

				// Cars generation
				if (Math.abs(t - (thickness / 2 + pavement)) <= 2) {
					if (city.rand.nextInt(50) == 0) {
						BlockPos pos2 = pos.up(y + 1);
						if (world.getBlockState(pos2).getBlock() != Blocks.AIR) {
							pos2 = pos2.up();
						}
						EnumFacing facing2 = facing;
						if (city.rand.nextInt(4) == 0) {
							facing2 = EnumFacing.getHorizontal(city.rand.nextInt(4));
						}
						CityHelper.placeRandomCar(world, pos2, facing2);
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
		IBlockState base2 = world.getBlockState(pos.down().offset(facing.rotateY(), thickness + 1));
		if (base2.getBlock() == Blocks.STONE_SLAB) {
			world.setBlockState(pos.down(), Blocks.STONEBRICK.getDefaultState());
		}

		for (int i = 0; i < 8; i++) {
			world.setBlockState(pos.up(i), Blocks.COBBLESTONE_WALL.getDefaultState());
		}

		for (int i = 0; i < 8; i++) {
			world.setBlockState(pos.up(i).offset(facing.rotateY(), thickness + 1),
					Blocks.COBBLESTONE_WALL.getDefaultState());
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

	public void generateTrafficLight(BlockPos pos) {

		IBlockState base = world.getBlockState(pos.down());
		if (base.getBlock() == Blocks.STONE_SLAB) {
			world.setBlockState(pos.down(), Blocks.STONEBRICK.getDefaultState());
		}
		int height = 5;
		for (int i = 0; i < height; i++) {
			world.setBlockState(pos.up(i), Blocks.COBBLESTONE_WALL.getDefaultState());
		}
		if (world.getBlockState(pos.down()).getBlock() == Blocks.AIR) {
			int i = 1;
			while (world.getBlockState(pos.down(i)).getBlock() == Blocks.AIR) {
				world.setBlockState(pos.down(i), Blocks.COBBLESTONE_WALL.getDefaultState());
				i--;
				if (world.getBlockState(pos.down(i)).getBlock() == Blocks.STONE_SLAB) {
					world.setBlockState(pos.down(i), Blocks.STONEBRICK.getDefaultState());
				}
			}
		}
		world.setBlockState(pos.up(height), Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
				BlockStoneBrick.EnumType.CHISELED));
		if (!canBranch) {
			for (int i = 1; i < 4; i++) {
				world.setBlockState(pos.up(height).offset(facing.rotateY(), i),
						Blocks.COBBLESTONE_WALL.getDefaultState());
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
				world.setBlockState(pos.up(height).offset(facing.rotateY(), -i),
						Blocks.COBBLESTONE_WALL.getDefaultState());
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
		IBlockState base = world.getBlockState(pos.down());
		if (base.getBlock() == Blocks.STONE_SLAB) {
			world.setBlockState(pos.down(), Blocks.STONEBRICK.getDefaultState());
		}
		int height = 5;
		for (int i = 0; i < height; i++) {
			world.setBlockState(pos.up(i), Blocks.COBBLESTONE_WALL.getDefaultState());
		}
		if (world.getBlockState(pos.down()).getBlock() == Blocks.AIR) {
			int i = 1;
			while (world.getBlockState(pos.down(i)).getBlock() == Blocks.AIR) {
				world.setBlockState(pos.down(i), Blocks.COBBLESTONE_WALL.getDefaultState());
				i--;
				if (world.getBlockState(pos.down(i)).getBlock() == Blocks.STONE_SLAB) {
					world.setBlockState(pos.down(i), Blocks.STONEBRICK.getDefaultState());
				}
			}
		}
		BlockPos top = pos.up(height);
		world.setBlockState(top, Blocks.ANVIL.getDefaultState().withProperty(BlockAnvil.FACING, facing.rotateY()));
		world.setBlockState(top.up(), Blocks.STONE_SLAB.getDefaultState());
		if (!end) {
			world.setBlockState(top.offset(facing.rotateY()),
					Blocks.HOPPER.getDefaultState().withProperty(BlockHopper.FACING, facing.rotateY()));
			world.setBlockState(top.offset(facing.rotateY(), 2), Blocks.REDSTONE_LAMP.getDefaultState());

			world.setBlockState(top.offset(facing.rotateY()).up(), Blocks.STONE_SLAB.getDefaultState());
			world.setBlockState(top.offset(facing.rotateY(), 2).up(), Blocks.STONE_SLAB.getDefaultState());
		} else {
			world.setBlockState(top.offset(facing.rotateYCCW()),
					Blocks.HOPPER.getDefaultState().withProperty(BlockHopper.FACING, facing.rotateYCCW()));
			world.setBlockState(top.offset(facing.rotateYCCW(), 2), Blocks.REDSTONE_LAMP.getDefaultState());

			world.setBlockState(top.offset(facing.rotateYCCW()).up(), Blocks.STONE_SLAB.getDefaultState());
			world.setBlockState(top.offset(facing.rotateYCCW(), 2).up(), Blocks.STONE_SLAB.getDefaultState());
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

	}

	public NBTTagCompound writeNBT(NBTTagCompound nbt) {

		nbt.setLong("start", this.start.toLong());
		nbt.setLong("end", this.end.toLong());
		return nbt;
	}
}
