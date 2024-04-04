package nuparu.sevendaystomine.item;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.block.BlockCar;

public class ItemBlockCar extends ItemBlock {

	public ItemBlockCar(BlockCar block) {
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	 public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	    {
	        
		    if(!(block instanceof BlockCar)) {
		    	 return EnumActionResult.FAIL;
		    }
		    
		    BlockCar car = (BlockCar)block;
		 
		    IBlockState iblockstate = worldIn.getBlockState(pos);
	        Block block = iblockstate.getBlock();

	        if (!block.isReplaceable(worldIn, pos))
	        {
	            pos = pos.offset(facing);
	        }
	        
	        EnumFacing facing2 = player.getHorizontalFacing().getOpposite();
	        
	        /*
	        for (int width = -car.getWidthHalf(); width <= car.getWidthHalf(); width++) {
				for (int length = -car.getLengthHalf(); length <= car.getLengthHalf(); length++) {
					for (int height = 0; height < car.getHeight(); height++) {
						BlockPos pos2 = pos.offset(facing2.rotateY(), width).offset(facing2, length).up(height);
						IBlockState state = worldIn.getBlockState(pos2);
						Block block2 = state.getBlock();
						if(!block2.isReplaceable(worldIn, pos2)) {
							return EnumActionResult.FAIL;
						}
					}
				}
	        }*/
	        
	        if(!car.canBePlaced(worldIn, pos, facing2)) {
	        	return EnumActionResult.FAIL;
	        }

	        ItemStack itemstack = player.getHeldItem(hand);

	        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(this.block, pos, false, facing, (Entity)null))
	        {
	            int i = this.getMetadata(itemstack.getMetadata());
	            IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);

	            if (placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
	            {
	                iblockstate1 = worldIn.getBlockState(pos);
	                SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
	                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
	                itemstack.shrink(1);
	            }

	            return EnumActionResult.SUCCESS;
	        }
	        else
	        {
	            return EnumActionResult.FAIL;
	        }
	    }
}
