package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.BlockStone;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.item.ItemSmallRock;

public class BlockStructureStone extends BlockBase {

	public BlockStructureStone() {
		super(Material.ROCK);
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE));
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setRegistryName("structure_stone");
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setUnlocalizedName("stone");
	}

	@Override
	public boolean metaItemBlock() {
		return true;
	}

	@Override
	public ItemBlock createItemBlock() {
		return new ItemSmallRock(this);
	}

	@Override
	public String getLocalizedName() {
		return I18n.translateToLocal(
				this.getUnlocalizedName() + "." + BlockStone.EnumType.STONE.getUnlocalizedName() + ".name");
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return ((BlockStone.EnumType) state.getValue(BlockStone.VARIANT)).getMapColor();
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(BlockStone.VARIANT) == BlockStone.EnumType.STONE
				? Item.getItemFromBlock(Blocks.COBBLESTONE)
				: Item.getItemFromBlock(Blocks.STONE);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		BlockStone.EnumType type = state.getValue(BlockStone.VARIANT);
		return new ItemStack(Blocks.STONE, 1, type.getMetadata());
	}

	@Override
	public int damageDropped(IBlockState state) {
		return ((BlockStone.EnumType) state.getValue(BlockStone.VARIANT)).getMetadata();
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (BlockStone.EnumType blockstone$enumtype : BlockStone.EnumType.values()) {
			items.add(new ItemStack(this, 1, blockstone$enumtype.getMetadata()));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((BlockStone.EnumType) state.getValue(BlockStone.VARIANT)).getMetadata();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockStone.VARIANT });
	}
}
