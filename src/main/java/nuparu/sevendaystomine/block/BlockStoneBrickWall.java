package nuparu.sevendaystomine.block;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.ItemBlockStonebrickWall;

public class BlockStoneBrickWall extends BlockWallBase {

	
	public static final PropertyEnum<EnumVariant> VARIANT = PropertyEnum.<EnumVariant>create("variant", BlockStoneBrickWall.EnumVariant.class);
	public BlockStoneBrickWall(Block modelBlock) {
		super(modelBlock);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumVariant.DEFAULT));
        this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	}
	
	public int damageDropped(IBlockState state)
    {
        return ((EnumVariant)state.getValue(VARIANT)).getMetadata();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
	@Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (EnumVariant variant : EnumVariant.values())
        {
            items.add(new ItemStack(this, 1, variant.getMetadata()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, EnumVariant.byMetadata(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumVariant)state.getValue(VARIANT)).getMetadata();
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {UP, NORTH, EAST, WEST, SOUTH, VARIANT});
    }
    
    @Override
	public boolean metaItemBlock() {
		return true;
	}

	@Override
	public ItemBlock createItemBlock() {
		return new ItemBlockStonebrickWall(this);
	}

	
	public static enum EnumVariant implements IStringSerializable{
		DEFAULT(0, "stonebrick", "default"),
        MOSSY(1, "mossy_stonebrick", "mossy"),
        CRACKED(2, "cracked_stonebrick", "cracked"),
        MOSSY_DEAD(3, "mossy_dead_stonebrick", "mossy_dead");
		
		private static final EnumVariant[] META_LOOKUP = new EnumVariant[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private EnumVariant(int meta, String name, String unlocalizedName)
        {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.name;
        }

        public static EnumVariant byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.name;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        static
        {
            for (EnumVariant blockstonebrick$enumtype : values())
            {
                META_LOOKUP[blockstonebrick$enumtype.getMetadata()] = blockstonebrick$enumtype;
            }
        }
	}
}
