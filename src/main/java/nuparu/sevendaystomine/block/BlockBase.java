package nuparu.sevendaystomine.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block implements IBlockBase {
	public BlockBase(Material blockMaterialIn, MapColor blockMapColorIn)
    {
        super(blockMaterialIn,blockMapColorIn);
    }

    public BlockBase(Material materialIn)
    {
        this(materialIn, materialIn.getMaterialMapColor());
    }
    
    public boolean metaItemBlock() {
    	return false;
    }
    
    public ItemBlock createItemBlock() {
    	return new ItemBlock(this);
    }
    public static ItemBlock createItemBlock(Block block) {
    	return new ItemBlock(block);
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    public boolean hasCustomStateMapper() {
		return false;
	}
	@Override
    @SideOnly(Side.CLIENT)
    public IStateMapper getStateMapper(){
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
