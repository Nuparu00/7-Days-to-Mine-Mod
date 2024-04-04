package nuparu.sevendaystomine.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModItems;

public class SlotWorkbenchScrap extends SlotItemHandler {
	ContainerWorkbenchUncrafting container;

	public SlotWorkbenchScrap(IItemHandler itemHandler, int index, int xPosition, int yPosition,
			ContainerWorkbenchUncrafting container) {
		super(itemHandler, index, xPosition, yPosition);
		this.container = container;
	}

	@Nullable
	@SideOnly(Side.CLIENT)
	public String getSlotTexture() {
		return SevenDaysToMine.MODID + ":items/empty_scrap_slot";
	}

	public boolean isItemValid(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() == ModItems.IRON_SCRAP;
	}

	@Override
	public void onSlotChange(@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {
		super.onSlotChange(p_75220_1_, p_75220_2_);
		container.onScrapChanged(p_75220_2_);
		
	}

	@Override
	public void putStack(ItemStack stack) {
		container.onScrapChanged(stack);
		super.putStack(stack);
		
	}

	@Override
	@Nonnull
	public ItemStack decrStackSize(int amount) {

		int amountPrev = this.getStack().getCount();
		ItemStack stack = super.decrStackSize(amount);
		
		if(amountPrev-amount < 1) {
			container.onScrapChanged(ItemStack.EMPTY);
		}else {
			container.onScrapChanged(stack);
		}
		
		return stack;
	}

}
