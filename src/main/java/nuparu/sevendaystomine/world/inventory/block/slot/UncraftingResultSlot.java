package nuparu.sevendaystomine.world.inventory.block.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.world.inventory.block.ContainerWorkbenchUncrafting;
import nuparu.sevendaystomine.world.inventory.block.UncraftingContainer;
import org.jetbrains.annotations.NotNull;

public class UncraftingResultSlot extends Slot {
    private final UncraftingContainer craftSlots;
    private final Player player;
    private int removeCount;
    private final ContainerWorkbenchUncrafting workbench;

    public UncraftingResultSlot(Player p_40166_, UncraftingContainer p_40167_, Container p_40168_, int p_40169_, int p_40170_, int p_40171_, ContainerWorkbenchUncrafting workbench) {
        super(p_40168_, p_40169_, p_40170_, p_40171_);
        this.player = p_40166_;
        this.craftSlots = p_40167_;
        this.workbench = workbench;
    }

    public boolean mayPlace(@NotNull ItemStack p_40178_) {
        return false;
    }

    public @NotNull ItemStack remove(int p_40173_) {
        if (this.hasItem()) {
            this.removeCount += Math.min(p_40173_, this.getItem().getCount());
        }

        return super.remove(p_40173_);
    }

    protected void onQuickCraft(@NotNull ItemStack p_40180_, int p_40181_) {
        this.removeCount += p_40181_;
        this.checkTakeAchievements(p_40180_);
    }

    protected void onSwapCraft(int p_40183_) {
        this.removeCount += p_40183_;
    }

    protected void checkTakeAchievements(@NotNull ItemStack p_40185_) {
        if (this.removeCount > 0) {
            p_40185_.onCraftedBy(this.player.level(), this.player, this.removeCount);
            net.minecraftforge.event.ForgeEventFactory.firePlayerCraftingEvent(this.player, p_40185_, this.craftSlots);
        }

        if (this.container instanceof RecipeHolder) {
            ((RecipeHolder)this.container).awardUsedRecipes(this.player,craftSlots.getItems());
        }

        this.removeCount = 0;
    }

    public void onTake(@NotNull Player p_150638_, @NotNull ItemStack p_150639_) {
        this.checkTakeAchievements(p_150639_);

        ItemStack itemstack = this.craftSlots.getItem(0);
        if (!itemstack.isEmpty())
        {
            this.craftSlots.removeItem(0, 1);
            itemstack = this.craftSlots.getItem(0);
            if(itemstack.getCount() <= 0) {
                this.craftSlots.setItem(0, ItemStack.EMPTY);
            }
            ItemStack scrap =  workbench.getWorkbench().getInventory().getStackInSlot(0);
            if(scrap.getCount() > 1) {
                scrap.shrink(1);
            }
            else {
                workbench.getWorkbench().getInventory().setStackInSlot(0,ItemStack.EMPTY);
                scrap = ItemStack.EMPTY;
            }
            workbench.onScrapChanged(scrap);
        }

    }
}
