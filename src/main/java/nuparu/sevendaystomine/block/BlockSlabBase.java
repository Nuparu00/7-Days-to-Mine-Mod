package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.ItemStack;

public abstract class BlockSlabBase extends BlockSlab implements IBlockBase {

	private final boolean isdouble;

	public BlockSlabBase(Material materialIn, boolean isdouble) {
		super(materialIn);
		this.isdouble = isdouble;
		IBlockState iblockstate = this.blockState.getBaseState();
		if (!this.isDouble()) {
			iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
		}
		this.setDefaultState(iblockstate);
		this.useNeighborBrightness = !this.isDouble();
	}

	public BlockSlabBase(Material materialIn, MapColor colorIn, boolean isdouble) {
		super(materialIn, colorIn);
		this.isdouble = isdouble;
		IBlockState iblockstate = this.blockState.getBaseState();
		if (!this.isDouble()) {
			iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
		}
		this.setDefaultState(iblockstate);
		this.useNeighborBrightness = !this.isDouble();
	}

	@Override
	public boolean metaItemBlock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasCustomStateMapper() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IStateMapper getStateMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomItemMesh() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemMeshDefinition getItemMesh() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return super.getUnlocalizedName();
	}

	@Override
	public final IBlockState getStateFromMeta(final int meta) {
		IBlockState blockstate = this.blockState.getBaseState();
		if (!this.isDouble()) {
			blockstate = blockstate.withProperty(HALF, ((meta & 8) != 0) ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
		}
		return blockstate;
	}

	@Override
	public final int getMetaFromState(final IBlockState state) {
		int meta = 0;
		if (!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP) {
			meta |= 8;
		}
		return meta;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		if (!this.isDouble()) {
			return new BlockStateContainer(this, new IProperty[] { HALF });
		}
		return new BlockStateContainer(this, new IProperty[] {});
	}

	@Override
	public boolean isDouble() {
		return isdouble;
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return HALF;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return null;
	}

}
