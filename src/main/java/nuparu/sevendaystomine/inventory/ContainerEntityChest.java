package nuparu.sevendaystomine.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerEntityChest extends InventoryBasic
{
    public ContainerEntityChest(String inventoryTitle, int slotCount)
    {
        super(inventoryTitle, false, slotCount);
    }

    @SideOnly(Side.CLIENT)
    public ContainerEntityChest(ITextComponent inventoryTitle, int slotCount)
    {
        super(inventoryTitle, slotCount);
    }
}