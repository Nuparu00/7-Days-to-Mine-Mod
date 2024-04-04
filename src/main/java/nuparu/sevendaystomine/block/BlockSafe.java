package nuparu.sevendaystomine.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntitySafe;

public abstract class BlockSafe<TE extends TileEntitySafe> extends BlockTileProvider<TE> {

	boolean keepInventory = false;
	
	public BlockSafe() {
		super(Material.IRON);
		setHardness(6.0F);
		setResistance(15.0F);
		setHarvestLevel("pickaxe", 2);
		setSoundType(SoundType.ANVIL);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntitySafe) {
				((TileEntitySafe) tileentity).setCustomInventoryName(stack.getDisplayName());
			}
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntitySafe) {
				//InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntitySafe) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(worldIn, pos, state);
	}
}
