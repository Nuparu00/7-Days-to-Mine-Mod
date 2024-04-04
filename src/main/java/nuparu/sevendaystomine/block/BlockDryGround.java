package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;

public class BlockDryGround extends BlockFalling implements IBlockBase {

	public BlockDryGround() {
		super(Material.GROUND);
		this.setSoundType(SoundType.GROUND);
		this.setHardness(0.65F);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	}

	@Override
	public boolean metaItemBlock() {
		return false;
	}

	@Override
	public ItemBlock createItemBlock() {
		return new ItemBlock(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomItemMesh() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemMeshDefinition getItemMesh() {
		return null;
	}
/*
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction,
			net.minecraftforge.common.IPlantable plantable) {
		IBlockState plant = plantable.getPlant(world, pos.offset(direction));
		net.minecraftforge.common.EnumPlantType plantType = plantable.getPlantType(world, pos.offset(direction));

		if (plant.getBlock() == net.minecraft.init.Blocks.CACTUS) {
			return false;
		}

		if (plant.getBlock() == net.minecraft.init.Blocks.REEDS) {
			return false;
		}

		if (plantable instanceof BlockBush) {
			return true;
		}

		switch (plantType) {
		case Desert:
			return true;
		case Nether:
			return false;
		case Crop:
			return false;
		case Cave:
			return false;
		case Plains:
			return true;
		case Water:
			return false;
		case Beach:
			boolean hasWater = (world.getBlockState(pos.east()).getMaterial() == Material.WATER
					|| world.getBlockState(pos.west()).getMaterial() == Material.WATER
					|| world.getBlockState(pos.north()).getMaterial() == Material.WATER
					|| world.getBlockState(pos.south()).getMaterial() == Material.WATER);
			return hasWater;
		}

		return false;
	}*/

}
