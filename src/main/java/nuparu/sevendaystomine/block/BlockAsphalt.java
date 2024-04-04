package nuparu.sevendaystomine.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;

public class BlockAsphalt extends BlockBase {
	public static final PropertyBool CITY = PropertyBool.create("city");

	public BlockAsphalt() {
		super(Material.ROCK, MapColor.BLACK);
		setHardness(7.0F);
		setResistance(15.0F);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);

		setDefaultState(this.blockState.getBaseState().withProperty(CITY, Boolean.valueOf(false)));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return new StateMap.Builder().ignore(CITY).build();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(CITY, meta == 1);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(CITY) ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { CITY });
	}

	public static boolean isCityAsphalt(IBlockState state) {
		return state.getBlock() instanceof BlockAsphalt && state.getValue(CITY);
	}

}
