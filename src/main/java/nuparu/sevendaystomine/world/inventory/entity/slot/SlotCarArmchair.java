package nuparu.sevendaystomine.world.inventory.entity.slot;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.world.level.block.ArmchairBlock;
import nuparu.sevendaystomine.world.level.block.SofaBlock;
import org.jetbrains.annotations.NotNull;

public class SlotCarArmchair extends SlotItemHandler {


    public SlotCarArmchair(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof BlockItem blockItem && (blockItem.getBlock() instanceof ArmchairBlock || blockItem.getBlock() instanceof SofaBlock);
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return 1;
    }
}
