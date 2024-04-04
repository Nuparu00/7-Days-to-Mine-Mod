package nuparu.sevendaystomine.block;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IBlockBase {
	public boolean metaItemBlock();
    public ItemBlock createItemBlock();
    @SideOnly(Side.CLIENT)
    public boolean hasCustomStateMapper();
    @SideOnly(Side.CLIENT)
    public IStateMapper getStateMapper();
    @SideOnly(Side.CLIENT)
    public boolean hasCustomItemMesh();
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getItemMesh();
}