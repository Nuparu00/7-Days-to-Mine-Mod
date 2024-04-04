package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;

public class BlockMercury extends BlockFluidFinite implements IBlockBase {

	public BlockMercury(Fluid fluid, Material material) {
		super(fluid, material);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {

		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor,
			float partialTicks) {
		if (!isWithinFluid(world, pos, ActiveRenderInfo.projectViewFromEntity(entity, partialTicks))) {
			BlockPos otherPos = pos.down(densityDir);
			IBlockState otherState = world.getBlockState(otherPos);
			return otherState.getBlock().getFogColor(world, otherPos, otherState, entity, originalColor, partialTicks);
		}

		if (getFluid() != null) {
			return new Vec3d(0.45, 0.45, 0.45);
		}

		return super.getFogColor(world, pos, state, entity, originalColor, partialTicks);
	}

	private boolean isWithinFluid(IBlockAccess world, BlockPos pos, Vec3d vec) {
		float filled = getFilledPercentage(world, pos);
		return filled < 0 ? vec.y > pos.getY() + filled + 1 : vec.y < pos.getY() + filled;
	}

	/*
	 * @Override public int tickRate(World worldIn) { return 50; }
	 */

	public boolean metaItemBlock() {
		return false;
	}

	public ItemBlock createItemBlock() {
		return new ItemBlock(this);
	}

	public static ItemBlock createItemBlock(Block block) {
		return new ItemBlock(block);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {

		return EnumBlockRenderType.MODEL;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return new StateMapperBase() {

			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(SevenDaysToMine.MODID + ":fluids", "mercury");
			}
		};

	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomItemMesh() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemMeshDefinition getItemMesh() {
		return new ItemMeshDefinition() {

			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return new ModelResourceLocation(SevenDaysToMine.MODID + ":fluids", "mercury");
			}
		};
	}

}
