package nuparu.sevendaystomine.world.gen.prefab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.prefab.buffered.BufferedBlock;
import nuparu.sevendaystomine.world.gen.prefab.buffered.BufferedEntity;

@Deprecated
public class PrefabLegacy implements Runnable {

	private ArrayList<BufferedBlock> default_state_blocks = new ArrayList<BufferedBlock>();
	private ArrayList<BufferedEntity> default_entities = new ArrayList<BufferedEntity>();
	@SuppressWarnings("unused")
	private final EnumFacing default_facing = EnumFacing.SOUTH;
	public boolean underground = false;
	public float neededRoom = 0.75f;
	public float neededFlatness = 0.55f;
	public boolean shouldContainAir = true;
	public int lowestY = 255;
	private HashMap<EnumFacing, ArrayList<BufferedBlock>> blockRotationsMap = new HashMap<EnumFacing, ArrayList<BufferedBlock>>();
	private HashMap<EnumFacing, ArrayList<BufferedEntity>> entityRotationsMap = new HashMap<EnumFacing, ArrayList<BufferedEntity>>();
	protected String name;
	private InputStream fis;

	volatile public boolean finished = false;

	public PrefabLegacy(String name) {
		this.name = name;
		this.underground = false;
	}

	public PrefabLegacy(String name, boolean underground) {
		this.name = name;
		this.underground = underground;
	}

	public String getName() {
		return this.name;
	}

	public void setInputStream(InputStream fis) {
		this.fis = fis;
	}

	public void run() {
		Utils.getLogger().info("Loading varinats of " + name);
		if (fis == null) {
			fis = getClass().getResourceAsStream(
					new StringBuilder().append("/assets/minecraft/structures/").append(name).toString());
		}
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finished = true;
	}

	public void load() throws IOException {
		InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null) {
			String[] words = line.split(" ");

			if (words.length < 4) {
				continue;
			}
			if (words[0].equals("Block")) {
				Block block = Block.getBlockFromName(words[4]);
				if (block == null) {
					System.out.print("Unable to find " + words[4]);
				}
				if (block == Blocks.AIR && !shouldContainAir) {
					continue;
				}
				int i = Integer.parseInt(words[1]);
				int j = Integer.parseInt(words[2]);
				int k = Integer.parseInt(words[3]);
				NBTTagCompound nbt = null;
				if (words.length >= 7 && words[6] != null) {
					try {
						nbt = JsonToNBT.getTagFromJson(words[6]);
					} catch (NBTException e) {
						e.printStackTrace();
					}
				}
				String lootTable = null;
				if (words.length == 8 && words[7] != null) {
					lootTable = words[7];
				}
				int meta = Integer.parseInt(words[5]);
				default_state_blocks.add(new BufferedBlock(i, j, k, block, meta, nbt, lootTable));
				if (j < lowestY && block.getDefaultState().getMaterial().isSolid()) {
					lowestY = j;
				}
			} else if (words[0].equals("Entity")) {
				double i = Double.parseDouble(words[1]);
				double j = Double.parseDouble(words[2]);
				double k = Double.parseDouble(words[3]);
				String name = words[4];
				float yaw = Float.parseFloat(words[5]);
				float pitch = Float.parseFloat(words[6]);
				NBTTagCompound nbt = null;
				String data = "";
				for (int l = 7; l < words.length; l++) {
					data += words[l];
				}
				try {
					nbt = JsonToNBT.getTagFromJson(data);
				} catch (NBTException e) {
					e.printStackTrace();
				}
				default_entities.add(new BufferedEntity(name, i, j, k, yaw, pitch, nbt));
			}
		}
		
		loadBlockRotationsMap();
		loadEntityRotationsMap();
	}

	public void loadBlockRotationsMap() {
		for (int i = 0; i < EnumFacing.HORIZONTALS.length; i++) {
			EnumFacing facing = EnumFacing.HORIZONTALS[i];
			blockRotationsMap.put(facing, rotateBlocks(getAngle(facing)));
		}
	}

	public void loadEntityRotationsMap() {
		for (int i = 0; i < EnumFacing.HORIZONTALS.length; i++) {
			EnumFacing facing = EnumFacing.HORIZONTALS[i];
			entityRotationsMap.put(facing, rotateEntities(getAngle(facing)));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<BufferedBlock> rotateBlocks(float rot) {
		ArrayList<BufferedBlock> blocks = new ArrayList<BufferedBlock>();
		for (int block = 0; block < default_state_blocks.size(); block++) {
			BufferedBlock bufferedBlock = default_state_blocks.get(block).copy();
			if (bufferedBlock != null && bufferedBlock.getBlock() != null) {
				IBlockState state = bufferedBlock.getBlock().getStateFromMeta(bufferedBlock.getMeta());
				IProperty propertyDirection = hasProperty(state, PropertyDirection.class);
				if ((!(bufferedBlock.getBlock() instanceof BlockDoublePlant))) {
					if (propertyDirection != null) {
						EnumFacing facing = (EnumFacing) (state.getValue(propertyDirection));
						if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
							for (int i = 0; i < (int) Math.abs(rot / 90); i++) {
								facing = facing.rotateY();
							}
							if ((int) Math.abs(rot / 90) % 2 != 0) {
								facing = facing.getOpposite();
							}
						}
						bufferedBlock.setMeta(bufferedBlock.getBlock()
								.getMetaFromState(state.withProperty(propertyDirection, facing)));
					}
				}
				IProperty propertyAxis = hasProperty(state, PropertyEnum.class);
				if (propertyAxis != null && propertyAxis.getValueClass() == EnumFacing.Axis.class) {
					PropertyEnum<EnumFacing.Axis> AXIS = (PropertyEnum<EnumFacing.Axis>) (propertyAxis);
					EnumFacing.Axis axis = state.getValue(AXIS);
					for (int i = 0; i < (int) Math.abs(rot / 90); i++) {
						if (axis == EnumFacing.Axis.X) {
							axis = EnumFacing.Axis.Z;
						} else if (axis == EnumFacing.Axis.Z) {
							axis = EnumFacing.Axis.X;
						}
					}
					bufferedBlock.setMeta(bufferedBlock.getBlock().getMetaFromState(state.withProperty(AXIS, axis)));
				}
				if (propertyAxis != null && propertyAxis.getValueClass() == BlockLog.EnumAxis.class) {
					PropertyEnum<BlockLog.EnumAxis> AXIS = (PropertyEnum<BlockLog.EnumAxis>) (propertyAxis);
					BlockLog.EnumAxis axis = state.getValue(AXIS);
					for (int i = 0; i < (int) Math.abs(rot / 90); i++) {
						if (axis == BlockLog.EnumAxis.X) {
							axis = BlockLog.EnumAxis.Z;
						} else if (axis == BlockLog.EnumAxis.Z) {
							axis = BlockLog.EnumAxis.X;
						}
					}
					bufferedBlock.setMeta(bufferedBlock.getBlock().getMetaFromState(state.withProperty(AXIS, axis)));
				}
				if (bufferedBlock.getBlock() instanceof BlockVine || (hasProperties(state, PropertyBool.class) != null)) {
					ArrayList<IProperty> properties = hasProperties(state, PropertyBool.class);
					if (testForPropertyNames(properties, new String[] { "north", "east", "south", "west" })) {
						PropertyBool NORTH = (PropertyBool) getPropertyByName(properties, "north");
						PropertyBool EAST = (PropertyBool) getPropertyByName(properties, "east");
						PropertyBool SOUTH = (PropertyBool) getPropertyByName(properties, "south");
						PropertyBool WEST = (PropertyBool) getPropertyByName(properties, "west");
						boolean north = state.getValue(NORTH);
						boolean east = state.getValue(EAST);
						boolean south = state.getValue(SOUTH);
						boolean west = state.getValue(WEST);
						for (int i = 0; i < (int) Math.abs(rot / 90); i++) {
							if (north == true && east == false && south == false && west == false) {
								north = false;
								west = true;
							} else if (north == true && east == true && south == false && west == false) {
								north = false;
								south = true;
							} else if (north == true && east == false && south == false && west == true) {
								east = true;
								west = false;
							} else if (north == true && east == true && south == false && west == true) {
								north = false;
								south = true;
							} else if (north == false && east == false && south == true && west == false) {
								south = false;
								east = true;
							} else if (north == false && east == true && south == true && west == false) {
								north = false;
								south = true;
							} else if (north == false && east == false && south == false && west == true) {
								south = true;
								west = false;
							} else if (north == false && east == true && south == true && west == true) {
								north = false;
								south = true;
							} else if (north == false && east == false && south == false && west == true) {
								west = false;
								south = true;
							} else if (north == false && east == true && south == false && west == false) {
								east = false;
								north = true;
							} else if (north == true && east == false && south == true && west == true) {
								west = false;
								south = true;
							} else if (north == true && east == true && south == true && west == false) {
								east = false;
								north = true;
							}
						}
						bufferedBlock.setMeta(bufferedBlock.getBlock().getMetaFromState(state
								.withProperty(NORTH, Boolean.valueOf(north)).withProperty(EAST, Boolean.valueOf(east))
								.withProperty(SOUTH, Boolean.valueOf(south)).withProperty(WEST, Boolean.valueOf(west))));
					}
				}
				blocks.add(bufferedBlock.rotate(rot));
			} else {
				Utils.getLogger().warn("Null Block in " + name + " at relative position " + bufferedBlock.getX() + " "
						+ bufferedBlock.getY() + " " + bufferedBlock.getZ() + " " + bufferedBlock.getBlock());
			}
		}
		return blocks;
	}

	public ArrayList<BufferedEntity> rotateEntities(float rot) {
		ArrayList<BufferedEntity> entities = new ArrayList<BufferedEntity>();
		for (int entity = 0; entity < default_entities.size(); entity++) {
			BufferedEntity bufferedEntity = default_entities.get(entity).rotate(rot);
			entities.add(bufferedEntity);
		}
		return entities;
	}

	@SuppressWarnings("rawtypes")
	public <T> IProperty hasProperty(IBlockState state, Class<T> type) {
		for (IProperty property : state.getPropertyKeys()) {
			if (property.getClass() == type) {
				return property;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static <T> ArrayList<IProperty> hasProperties(IBlockState state, Class<T> type) {
		ArrayList<IProperty> result = null;
		for (IProperty property : state.getPropertyKeys()) {
			if (property.getClass() == type) {
				if (result == null) {
					result = new ArrayList<IProperty>();
				}
				result.add(property);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static boolean testForPropertyNames(ArrayList<IProperty> properties, String[] names) {
		if (properties != null && names != null && properties.size() > 0 && names.length > 0) {
			boolean[] results = new boolean[names.length];
			for (int i = 0; i < properties.size(); i++) {
				for (int j = 0; j < names.length; j++) {
					if (properties.get(i).getName().equals(names[j])) {
						results[j] = true;
						break;
					}
				}
			}
			for (int i = 0; i < results.length; i++) {
				if (results[i] == false) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static IProperty getPropertyByName(ArrayList<IProperty> properties, String name) {
		for (int i = 0; i < properties.size(); i++) {
			if (properties.get(i).getName().equals(name)) {
				return properties.get(i);
			}
		}
		return null;
	}

	@SuppressWarnings("incomplete-switch")
	public float getAngle(EnumFacing facing) {
		switch (facing) {
		case SOUTH: {
			return 0;
		}
		case WEST: {
			return 90;
		}
		case NORTH: {
			return 180;
		}
		case EAST: {
			return 270;
		}
		}
		return 0;
	}

	public boolean testForRoom(World world, BlockPos pos, EnumFacing facing) {
		ArrayList<BufferedBlock> blocks = getBlockListByFacing(facing);
		return testForRoom(world, pos, blocks);
	}

	public boolean testForRoom(World world, BlockPos pos, ArrayList<BufferedBlock> blocks) {
		int replacable = 0;
		if (underground == true) {
			replacable = blocks.size();
			if (blocks != null) {
				for (int block = 0; block < blocks.size(); block++) {
					BufferedBlock bufferedBlock = blocks.get(block);
					BlockPos pos1 = pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ());
					if (world.getBlockState(pos1).getMaterial().isReplaceable()) {
						replacable--;
						if ((float) replacable / blocks.size() >= neededRoom) {
							return true;
						}
					}
				}
			}
		}
		replacable = 0;
		if (blocks != null) {
			for (int block = 0; block < blocks.size(); block++) {
				BufferedBlock bufferedBlock = blocks.get(block);
				BlockPos pos1 = pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ());
				if (world.getBlockState(pos1).getMaterial().isReplaceable()) {
					replacable++;
					if ((float) replacable / blocks.size() >= neededRoom) {
						return true;
					}
				}
			}
			return (float) replacable / blocks.size() >= neededRoom;
		}
		return false;
	}

	public boolean testForFlatness(World world, BlockPos pos, ArrayList<BufferedBlock> blocks) {
		if (underground == true) {
			return true;
		}
		float flat = 0;
		int pedestalSize = 0;
		if (blocks != null) {
			for (int block = 0; block < blocks.size(); block++) {
				BufferedBlock bufferedBlock = blocks.get(block);
				if (bufferedBlock.getY() == 0) {
					pedestalSize++;
					BlockPos pos1 = pos.add(bufferedBlock.getX(), bufferedBlock.getY() - 1, bufferedBlock.getZ());
					IBlockState state = world.getBlockState(pos1);
					if (state.getMaterial().isSolid()) {
						flat++;
						if ((float) flat / pedestalSize >= neededFlatness) {
							return true;
						}
					} else {
						// TO-DO
					}
				}
			}
			if (pedestalSize <= 0) {
				for (int block = 0; block < blocks.size(); block++) {
					BufferedBlock bufferedBlock = blocks.get(block);
					if (bufferedBlock.getY() == lowestY) {
						pedestalSize++;
						BlockPos pos1 = pos.add(bufferedBlock.getX(), bufferedBlock.getY() - 1, bufferedBlock.getZ());
						IBlockState state = world.getBlockState(pos1);
						if (state.getMaterial().isSolid()) {
							flat++;
							if ((float) flat / pedestalSize >= neededFlatness) {
								return true;
							}
						} else {
							// TO-DO
						}
					}
				}
			}
			return (float) flat / pedestalSize >= neededFlatness;
		}
		return false;
	}

	public boolean isUsable(World world, BlockPos pos, EnumFacing facing) {
		if (world != null && pos != null && facing != null) {
			ArrayList<BufferedBlock> blocks = getBlockListByFacing(facing);
			if (blocks != null) {
				return (testForRoom(world, pos, blocks) && testForFlatness(world, pos, blocks));
			}
		}
		return false;
	}

	public boolean tryToGenerate(World world, BlockPos pos, EnumFacing facing) {
		boolean canGenerate = isUsable(world, pos, facing);
		if (canGenerate) {
			if (!world.isRemote) {
				generate(world, pos, facing);
			}
		}
		return canGenerate;
	}

	public void generate(World world, BlockPos pos, EnumFacing facing) {
		ArrayList<BufferedBlock> blocks = getBlockListByFacing(facing);
		ArrayList<BufferedEntity> entities = getEntityListByFacing(facing);
		ArrayList<BufferedBlock> bufferedBlocks_postInit = new ArrayList<BufferedBlock>();
		if (blocks != null) {
			for (int block = 0; block < blocks.size(); block++) {
				BufferedBlock bufferedBlock = blocks.get(block);
				Block unwrappedBlock = bufferedBlock.getBlock();
				int meta = bufferedBlock.getMeta();
				IBlockState state = unwrappedBlock.getStateFromMeta(meta);
				if (unwrappedBlock instanceof BlockDoor || unwrappedBlock instanceof BlockTorch
						|| unwrappedBlock instanceof BlockRailBase || unwrappedBlock instanceof BlockSign
						|| unwrappedBlock instanceof net.minecraftforge.common.IPlantable
						|| unwrappedBlock.getDefaultState().isFullCube() == false) {
					bufferedBlocks_postInit.add(bufferedBlock);
				}
				if (underground == false) {
					if ((!(unwrappedBlock instanceof BlockContainer)) && bufferedBlock.getY() == lowestY) {
						BlockPos bp = pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ());
						int o = bp.getX();
						int p = bp.getZ();
						int h = bp.getY() - 1;
						BlockPos pos1 = new BlockPos(o, h, p);
						IBlockState stateToReplace = world.getBlockState(pos1);
						Material matToReplace = stateToReplace.getMaterial();
						while ((world.isAirBlock(pos1) || matToReplace.isLiquid() || matToReplace.isReplaceable())
								&& h > 1) {
							
							if (unwrappedBlock instanceof BlockStairs) {
								Block bl = null;
								bl = (Block) (ReflectionHelper.getPrivateValue(BlockStairs.class,
										(BlockStairs) unwrappedBlock, "modelBlock", "field_150149_b"));
								if (bl != null) {
									world.setBlockState(pos1, bl.getStateFromMeta(meta), 3);
								} else {
									world.setBlockState(pos1, state, 3);
								}
							} else {
								world.setBlockState(pos1, state, 3);
							}
							h--;
							pos1 = new BlockPos(o, h, p);
						}
					}
				}
				if (world.getBlockState(pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ())) == state) {
					continue;
				}
				// if(underground == true ||
				// (bufferedBlock.block.getMaterial().isReplaceable() == false
				// || bufferedBlock.block == ModBlocks.air ||
				// (bufferedBlock.block.getMaterial().isReplaceable() &&
				// world.getBlockState(pos.add(bufferedBlock.x,bufferedBlock.y,bufferedBlock.z)).getBlock().getMaterial().isReplaceable()))){
				world.setBlockState(pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ()), state);
				TileEntity tileEntity = world.getTileEntity(pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ()));
				if (tileEntity != null && bufferedBlock.getNBT() != null) {
					NBTTagCompound defaultNBT = new NBTTagCompound();
					NBTTagCompound nbt = bufferedBlock.getNBT();
					tileEntity.writeToNBT(defaultNBT);
					nbt.setString("id", defaultNBT.getString("id"));
					nbt.setInteger("x", defaultNBT.getInteger("x"));
					nbt.setInteger("y", defaultNBT.getInteger("y"));
					nbt.setInteger("z", defaultNBT.getInteger("z"));
					tileEntity.readFromNBT(nbt);
				}
			}
			for (int block = 0; block < bufferedBlocks_postInit.size(); block++) {
				BufferedBlock bufferedBlock = bufferedBlocks_postInit.get(block);
				Block unwrappedBlock = bufferedBlock.getBlock();
				int meta = bufferedBlock.getMeta();
				IBlockState state = unwrappedBlock.getStateFromMeta(meta);
				world.setBlockState(pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ()), state);
				TileEntity tileEntity = world.getTileEntity(pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ()));
				if (tileEntity != null && bufferedBlock.getNBT() != null) {
					NBTTagCompound defaultNBT = new NBTTagCompound();
					NBTTagCompound nbt = bufferedBlock.getNBT();
					tileEntity.writeToNBT(defaultNBT);
					nbt.setString("id", defaultNBT.getString("id"));
					nbt.setInteger("x", defaultNBT.getInteger("x"));
					nbt.setInteger("y", defaultNBT.getInteger("y"));
					nbt.setInteger("z", defaultNBT.getInteger("z"));
					tileEntity.readFromNBT(nbt);
				}
			}
		}
		if (entities != null) {
			for (int entity = 0; entity < entities.size(); entity++) {
				BufferedEntity bufferedEntity = entities.get(entity);
				bufferedEntity.spawn(world, pos);
			}
		}
	}

	public void generate(World world, BlockPos pos, float rot) {
		ArrayList<BufferedBlock> blocks = rotateBlocks(rot);
		ArrayList<BufferedEntity> entities = rotateEntities(rot);
		ArrayList<BufferedBlock> bufferedBlocks_postInit = new ArrayList<BufferedBlock>();
		if (blocks != null) {
			for (int block = 0; block < blocks.size(); block++) {
				BufferedBlock bufferedBlock = blocks.get(block);
				Block unwrappedBlock = bufferedBlock.getBlock();
				int meta = bufferedBlock.getMeta();
				IBlockState state = unwrappedBlock.getStateFromMeta(meta);
				if (unwrappedBlock instanceof BlockDoor || unwrappedBlock instanceof BlockTorch
						|| unwrappedBlock instanceof BlockRailBase || unwrappedBlock instanceof BlockSign
						|| unwrappedBlock instanceof net.minecraftforge.common.IPlantable
						|| unwrappedBlock.getDefaultState().isFullCube() == false) {
					bufferedBlocks_postInit.add(bufferedBlock);
				}
				if (underground == false) {
					if ((!(unwrappedBlock instanceof BlockContainer)) && bufferedBlock.getY() == lowestY) {
						BlockPos bp = pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ());
						int o = bp.getX();
						int p = bp.getZ();
						int h = bp.getY() - 1;
						BlockPos pos1 = new BlockPos(o, h, p);
						IBlockState stateToReplace = world.getBlockState(pos1);
						Material matToReplace = stateToReplace.getMaterial();
						while ((world.isAirBlock(pos1) || matToReplace.isLiquid() || matToReplace.isReplaceable())
								&& h > 1) {
							if (unwrappedBlock instanceof BlockStairs) {
								Block bl = null;
								bl = (Block) (ReflectionHelper.getPrivateValue(BlockStairs.class,
										(BlockStairs) unwrappedBlock, "modelBlock", "field_150149_b"));
								if (bl != null) {
									world.setBlockState(pos1, bl.getStateFromMeta(meta), 3);
								} else {
									world.setBlockState(pos1, state, 3);
								}
							} else {
								world.setBlockState(pos1, state, 3);
							}
							h--;
						}
					}
				}
				if (world.getBlockState(pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ())) == state) {
					continue;
				}
				world.setBlockState(pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ()), state);
				TileEntity tileEntity = world.getTileEntity(pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ()));
				if (tileEntity != null && bufferedBlock.getNBT() != null) {
					NBTTagCompound defaultNBT = new NBTTagCompound();
					NBTTagCompound nbt = bufferedBlock.getNBT();
					tileEntity.writeToNBT(defaultNBT);
					nbt.setString("id", defaultNBT.getString("id"));
					nbt.setInteger("x", defaultNBT.getInteger("x"));
					nbt.setInteger("y", defaultNBT.getInteger("y"));
					nbt.setInteger("z", defaultNBT.getInteger("z"));
					tileEntity.readFromNBT(nbt);
				}
			}
			for (int block = 0; block < bufferedBlocks_postInit.size(); block++) {
				BufferedBlock bufferedBlock = bufferedBlocks_postInit.get(block);
				Block unwrappedBlock = bufferedBlock.getBlock();
				int meta = bufferedBlock.getMeta();
				IBlockState state = unwrappedBlock.getStateFromMeta(meta);
				world.setBlockState(pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ()), state);
				TileEntity tileEntity = world.getTileEntity(pos.add(bufferedBlock.getX(), bufferedBlock.getY(), bufferedBlock.getZ()));
				if (tileEntity != null && bufferedBlock.getNBT() != null) {
					NBTTagCompound defaultNBT = new NBTTagCompound();
					NBTTagCompound nbt = bufferedBlock.getNBT();
					tileEntity.writeToNBT(defaultNBT);
					nbt.setString("id", defaultNBT.getString("id"));
					nbt.setInteger("x", defaultNBT.getInteger("x"));
					nbt.setInteger("y", defaultNBT.getInteger("y"));
					nbt.setInteger("z", defaultNBT.getInteger("z"));
					tileEntity.readFromNBT(nbt);
				}
			}
		}
		if (entities != null) {
			for (int entity = 0; entity < entities.size(); entity++) {
				BufferedEntity bufferedEntity = entities.get(entity);
				bufferedEntity.spawn(world, pos);
			}
		}
	}

	public ArrayList<BufferedBlock> getBlockListByFacing(EnumFacing facing) {
		for (Entry<EnumFacing, ArrayList<BufferedBlock>> entry : blockRotationsMap.entrySet()) {
			EnumFacing f = entry.getKey();
			if (facing == f) {
				return (ArrayList<BufferedBlock>) (entry.getValue());
			}
		}
		return null;
	}

	public ArrayList<BufferedEntity> getEntityListByFacing(EnumFacing facing) {
		for (Entry<EnumFacing, ArrayList<BufferedEntity>> entry : entityRotationsMap.entrySet()) {
			EnumFacing f = entry.getKey();
			if (facing == f) {
				return (ArrayList<BufferedEntity>) (entry.getValue());
			}
		}
		return null;
	}
}