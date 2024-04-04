package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.ItemSmallRock;

public class BlockSmallRock extends BlockPickable {

	public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.<EnumType>create("variant",
			BlockSmallRock.EnumType.class);

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.2125D, 0.0D, 0.3125D, 0.6875D, 0.1875D, 0.6875D);

	public BlockSmallRock() {
		super(Material.GROUND);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.STONE));
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		setHarvestLevel("pickaxe", 0);
	}

	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return AABB;
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return AABB.offset(pos).offset(state.getOffset(worldIn, pos));
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB.offset(state.getOffset(source, pos));
	}	
	
	public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XZ;
    }

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ModItems.SMALL_STONE;
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
    {
		return 1;
    }

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(ModItems.SMALL_STONE);
	}

	@Override
	public ItemBlock createItemBlock() {
		return new ItemSmallRock(this);
	}

	@Override
	public boolean metaItemBlock() {
		return true;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (EnumType enumtype : EnumType.values()) {
			list.add(new ItemStack(this, 1, enumtype.getMetadata()));
		}
	}
	
	public IBlockState getRandomVariant(Random rand) {
		return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(rand.nextInt(EnumType.values().length)));
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
	}

	public int getMetaFromState(IBlockState state) {
		return ((EnumType) state.getValue(VARIANT)).getMetadata();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { VARIANT });
	}

	public static enum EnumType implements IStringSerializable {
		STONE("stone", 0), GRANITE("granite", 1), DIORITE("diorite", 2), ANDESITE("andesite", 3);

		private static final BlockSmallRock.EnumType[] META_LOOKUP = new BlockSmallRock.EnumType[values().length];

		private final String name;
		private final int metadata;

		private EnumType(String name, int meta) {
			this.name = name;
			this.metadata = meta;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return getName();
		}

		public int getMetadata() {
			return metadata;
		}

		public static BlockSmallRock.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		static {
			for (BlockSmallRock.EnumType enumtype : values()) {
				META_LOOKUP[enumtype.getMetadata()] = enumtype;
			}
		}
	}

}
