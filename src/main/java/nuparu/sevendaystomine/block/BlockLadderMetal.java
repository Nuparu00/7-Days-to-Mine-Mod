package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockLadder;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.item.EnumMaterial;


public class BlockLadderMetal extends BlockLadder implements IBlockBase {

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
	public boolean hasCustomItemMesh() {
		return false;
	}

	@Override
	public ItemMeshDefinition getItemMesh() {
		return null;
	}

}
