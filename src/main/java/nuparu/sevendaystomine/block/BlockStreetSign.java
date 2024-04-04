package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.tileentity.TileEntityStreetSign;
import nuparu.sevendaystomine.util.Utils;

public class BlockStreetSign extends BlockStandingSign implements IBlockBase {

	public BlockStreetSign() {
		super();
		this.setSoundType(SoundType.METAL);
		this.setHardness(1.5F);
		this.setResistance(1F);
	}

	@Override
	public boolean metaItemBlock() {
		return false;
	}

	@Override
	public ItemBlock createItemBlock() {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return new StateMap.Builder().ignore(ROTATION).build();
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

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityStreetSign();
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(ModItems.STREET_SIGN);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ModItems.STREET_SIGN;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand == EnumHand.MAIN_HAND) {
			ItemStack stack = playerIn.getHeldItem(hand);
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				if (item instanceof ItemDye) {
					TileEntity tileentity = worldIn.getTileEntity(pos);

					if (tileentity instanceof TileEntityStreetSign) {
						TileEntityStreetSign sign = (TileEntityStreetSign) tileentity;
						EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
						TextFormatting textColor = Utils.dyeColorToTextFormatting(enumdyecolor);
						sign.setTextColor(textColor);
						if (!playerIn.isCreative()) {
							stack.shrink(1);
						}
						return true;
					}
				}
			}
		}
		return false;
	}

}
