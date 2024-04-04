package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPillarBase extends BlockRotatedPillar implements IBlockBase {

	public BlockPillarBase(Material materialIn) {
		super(materialIn);
	}

	public BlockPillarBase(Material materialIn, MapColor color) {
		super(materialIn, color);
	}

	@Override
	public boolean metaItemBlock() {
		return false;
	}

	@Override
	public ItemBlock createItemBlock() {
		return new ItemBlock(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return null;
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

}
