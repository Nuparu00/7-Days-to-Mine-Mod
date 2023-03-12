package nuparu.sevendaystomine.world.inventory.block.slot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.world.inventory.block.ContainerWorkbenchUncrafting;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class WorkbenchScrapSlot extends SlotItemHandler {
    private final ResourceLocation NO_SCRAP_SLOT = new ResourceLocation(SevenDaysToMine.MODID, "items/empty_scrap_slot");
    ContainerWorkbenchUncrafting container;

    public WorkbenchScrapSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, ContainerWorkbenchUncrafting container) {
        super(itemHandler, index, xPosition, yPosition);
        this.container = container;
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(PlayerContainer.BLOCK_ATLAS, NO_SCRAP_SLOT);
    }
    */

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == ModItems.IRON_SCRAP.get();
    }

    @Override
    public void onQuickCraft(@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {
        super.onQuickCraft(p_75220_1_, p_75220_2_);
        container.onScrapChanged(p_75220_2_);

    }

    @Override
    public void set(@NotNull ItemStack stack) {
        container.onScrapChanged(stack);
        super.set(stack);

    }

    @Override
    @Nonnull
    public ItemStack remove(int amount) {

        int amountPrev = this.getItem().getCount();
        ItemStack stack = super.remove(amount);

        if(amountPrev-amount < 1) {
            container.onScrapChanged(ItemStack.EMPTY);
        }else {
            container.onScrapChanged(stack);
        }

        return stack;
    }
}