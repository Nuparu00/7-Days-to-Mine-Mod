package nuparu.sevendaystomine.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;

public class BlockFlowerPotEnhanced extends BlockTileProvider<TileEntityFlowerPot> {
	public static final PropertyEnum<EnumFlowerType> CONTENTS = PropertyEnum.<EnumFlowerType>create("contents",
			EnumFlowerType.class);
	protected static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D,
			0.6875D);

	public BlockFlowerPotEnhanced() {
		super(Material.CIRCUITS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(CONTENTS, EnumFlowerType.EMPTY));
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);

		if (tileentityflowerpot != null) {
			ItemStack itemstack = tileentityflowerpot.getFlowerItemStack();

			if (!itemstack.isEmpty()) {
				return itemstack;
			}
		}

		return new ItemStack(Items.FLOWER_POT);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FLOWER_POT_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return true;
	}

	public boolean canBePotted(ItemStack stack) {
		Item item = stack.getItem();
		Block block = Block.getBlockFromItem(item);
		return (block == ModBlocks.GOLDENROD || item == ModItems.BLUEBERRY || item == ModItems.BANEBERRY
				|| block == ModBlocks.BLUEBERRY_PLANT || block == ModBlocks.BANEBERRY_PLANT);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState downState = worldIn.getBlockState(pos.down());
		return super.canPlaceBlockAt(worldIn, pos) && (downState.isTopSolid()
				|| downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		IBlockState downState = worldIn.getBlockState(pos.down());
		if (!downState.isTopSolid()
				&& downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) != BlockFaceShape.SOLID) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		super.onBlockHarvested(worldIn, pos, state, player);

		if (player.capabilities.isCreativeMode) {
			TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);

			if (tileentityflowerpot != null) {
				tileentityflowerpot.setItemStack(ItemStack.EMPTY);
			}
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.FLOWER_POT);
	}

	@Override
	public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos,
			IBlockState state, int fortune) {
		drops.add(new ItemStack(Items.FLOWER_POT));
		TileEntityFlowerPot te = world.getTileEntity(pos) instanceof TileEntityFlowerPot
				? (TileEntityFlowerPot) world.getTileEntity(pos)
				: null;
		if (te != null && te.getFlowerPotItem() != null)
			drops.add(new ItemStack(te.getFlowerPotItem(), 1, te.getFlowerPotData()));
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te,
			ItemStack tool) {
		super.harvestBlock(world, player, pos, state, te, tool);
		world.setBlockToAir(pos);
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		if (willHarvest)
			return true; // If it will harvest, delay deletion of the block until after getDrops
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Nullable
	public TileEntityFlowerPot getTileEntity(World worldIn, BlockPos pos) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity instanceof TileEntityFlowerPot ? (TileEntityFlowerPot) tileentity : null;
	}

	@Override
	public TileEntityFlowerPot createTileEntity(World world, IBlockState state) {
		Block block = null;
		switch (state.getValue(CONTENTS)) {
		case GOLDENROD:
			block = ModBlocks.GOLDENROD;
			break;
		case BLUEBERRY_BUSH:
			block = ModBlocks.BLUEBERRY_PLANT;
			break;
		case BANEBERRY_BUSH:
			block = ModBlocks.BANEBERRY_PLANT;
			break;
		default:
			break;
		}
		return new TileEntityFlowerPot(Item.getItemFromBlock(block), 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		Block block = null;
		int i = 0;

		switch (meta) {
		case 1:
			block = ModBlocks.GOLDENROD;
			break;
		case 2:
			block = ModBlocks.BLUEBERRY_PLANT;
			break;
		case 3:
			block = ModBlocks.BANEBERRY_PLANT;
			break;
		}

		return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		EnumFlowerType blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
		TileEntity tileentity = worldIn instanceof ChunkCache
				? ((ChunkCache) worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK)
				: worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityFlowerPot) {
			TileEntityFlowerPot tileentityflowerpot = (TileEntityFlowerPot) tileentity;
			Item item = tileentityflowerpot.getFlowerPotItem();

			if (item instanceof ItemBlock) {
				int i = tileentityflowerpot.getFlowerPotData();
				Block block = Block.getBlockFromItem(item);

				if (block == ModBlocks.GOLDENROD) {
					blockflowerpot$enumflowertype = EnumFlowerType.GOLDENROD;
				} else if (block == ModBlocks.BLUEBERRY_PLANT) {
					blockflowerpot$enumflowertype = EnumFlowerType.BLUEBERRY_BUSH;
				} else if (block == ModBlocks.BANEBERRY_PLANT) {
					blockflowerpot$enumflowertype = EnumFlowerType.BANEBERRY_BUSH;
				}
			} else {
				if (item == ModItems.BLUEBERRY) {
					blockflowerpot$enumflowertype = EnumFlowerType.BLUEBERRY_BUSH;
				} else if (item == ModItems.BANEBERRY) {
					blockflowerpot$enumflowertype = EnumFlowerType.BANEBERRY_BUSH;
				}
			}
		}

		return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { CONTENTS });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	public static enum EnumFlowerType implements IStringSerializable {
		EMPTY("empty"), GOLDENROD("goldenrod"), BLUEBERRY_BUSH("blueberry_bush"), BANEBERRY_BUSH("baneberry_bush");

		private final String name;

		private EnumFlowerType(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}

}
