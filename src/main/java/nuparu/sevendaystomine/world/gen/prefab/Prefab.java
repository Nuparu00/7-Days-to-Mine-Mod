package nuparu.sevendaystomine.world.gen.prefab;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.prefab.buffered.BufferedBlock;
import nuparu.sevendaystomine.world.gen.prefab.buffered.BufferedEntity;

public class Prefab {

	String name;

	int width;
	int height;
	int length;

	int offsetX;
	int offsetY;
	int offsetZ;

	int pedestalLayer = 0;

	boolean underground = false;
	double requiredRoom;
	double requiredFlatness;
	int weight;

	EnumPrefabType type = EnumPrefabType.NONE;
	List<EnumStructureBiomeType> biomeTypes;

	List<BufferedBlock> blocks;
	List<BufferedEntity> entities;

	public Prefab(String name, int width, int height, int length, int offsetX, int offsetY, int offsetZ,
			boolean underground, double requiredRoom, double requiredFlatness, int weight, EnumPrefabType type,
			List<EnumStructureBiomeType> biomeTypes, List<BufferedBlock> blocks, List<BufferedEntity> entities,
			int pedestalLayer) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.length = length;

		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;

		this.pedestalLayer = pedestalLayer;

		this.underground = underground;
		this.requiredRoom = requiredRoom;
		this.requiredFlatness = requiredFlatness;
		this.weight = weight;

		this.type = type;
		this.biomeTypes = biomeTypes;

		this.blocks = blocks;
		this.entities = entities;
	}

	public void generate(World world, BlockPos pos, int rotation) {
		this.generate(world, pos, rotation, false);
	}

	@SuppressWarnings("deprecation")
	public void generate(World world, BlockPos pos, int rotation, boolean base) {
		for (BufferedBlock buffered : blocks) {
			BufferedBlock bufferedBlock = buffered.rotate(rotation);
			IBlockState state = bufferedBlock.getBlockState();
			if (state == null)
				continue;
			BlockPos pos2 = bufferedBlock.getPos(pos);
			if (pos2 == null)
				continue;

			if (base) {
				IBlockState state2 = world.getBlockState(pos2);
				if (!state2.getBlock().isReplaceable(world, pos2)
						&& state.getBlock() instanceof BlockVine) {
					continue;
				}
			}

			Block block = state.getBlock();
			state = block.withRotation(state, Utils.intToRotation(rotation));

			world.setBlockState(pos2, state);
			if (state.getBlock().hasTileEntity(state)) {
				TileEntity TE = world.getTileEntity(pos2);
				if (TE != null) {
					NBTTagCompound oldNBT = TE.writeToNBT(new NBTTagCompound());
					NBTTagCompound newNBT = bufferedBlock.getNBT().copy();
					newNBT.setString("id", oldNBT.getString("id"));
					newNBT.setInteger("x", oldNBT.getInteger("x"));
					newNBT.setInteger("y", oldNBT.getInteger("y"));
					newNBT.setInteger("z", oldNBT.getInteger("z"));
					if (TE != null) {
						TE.readFromNBT(newNBT);
					}
				}
			}
			if (base) {
				if (bufferedBlock.getY() == pedestalLayer && !state.getBlock().isReplaceable(world, pos2)) {

					BlockPos pos3 = pos2.down();
					IBlockState state2 = world.getBlockState(pos3);
					Block block2 = state2.getBlock();
					while (block2.isReplaceable(world, pos3) || block2 instanceof BlockLog
							|| block2 instanceof BlockLeaves) {
						world.setBlockState(pos3, state);
						pos3 = pos3.down();
						state2 = world.getBlockState(pos3);
						block2 = state2.getBlock();
					}
				}
			}
		}
		for (BufferedEntity bufferedEntity : entities) {
			bufferedEntity.rotate(rotation).spawn(world, pos);
		}
	}

	public void generate(World world, BlockPos pos, EnumFacing facing) {
		this.generate(world, pos, facing, false);
	}

	public void generate(World world, BlockPos pos, EnumFacing facing, boolean base) {
		this.generate(world, pos, Utils.facingToInt(facing), base);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getLength() {
		return this.length;
	}
}