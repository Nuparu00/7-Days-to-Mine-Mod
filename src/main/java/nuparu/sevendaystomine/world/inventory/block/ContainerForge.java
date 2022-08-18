package nuparu.sevendaystomine.world.inventory.block;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.world.level.block.entity.ForgeBlockEntity;

public class ContainerForge extends AbstractContainerMenu {
    // -------- methods used by the ContainerScreen to render parts of the display
    private final ContainerData intArray;
    private final Level world; // needed for some helper methods
    ForgeBlockEntity forge;

    public ContainerForge(int windowID, Inventory invPlayer, ForgeBlockEntity forge, ContainerData intArray) {
        super(ModContainers.FORGE.get(), windowID);
        this.intArray = intArray;
        this.world = invPlayer.player.level;
        this.forge = forge;

        addDataSlots(intArray); // tell vanilla to keep the IIntArray synchronised between client and
        // server Containers
        if(forge != null) {
            addSlot(new SlotItemHandler(forge.getInventory(), ForgeBlockEntity.EnumSlots.MOLD_SLOT.ordinal(), 45, 42));

            addSlot(new SlotItemHandler(forge.getInventory(), ForgeBlockEntity.EnumSlots.INPUT_SLOT.ordinal(), 78, 11));
            addSlot(new SlotItemHandler(forge.getInventory(), ForgeBlockEntity.EnumSlots.INPUT_SLOT2.ordinal(), 96, 11));
            addSlot(new SlotItemHandler(forge.getInventory(), ForgeBlockEntity.EnumSlots.INPUT_SLOT3.ordinal(), 78, 29));
            addSlot(new SlotItemHandler(forge.getInventory(), ForgeBlockEntity.EnumSlots.INPUT_SLOT4.ordinal(), 96, 29));

            addSlot(new SlotOutput(invPlayer.player, forge, forge.getInventory(), ForgeBlockEntity.EnumSlots.OUTPUT_SLOT.ordinal(), 148, 42));
            addSlot(new SlotFuel(forge.getInventory(), ForgeBlockEntity.EnumSlots.FUEL_SLOT.ordinal(), 88, 63));

            for (int k = 0; k < 9; ++k) {
                addSlot(new Slot(invPlayer, k, 8 + k * 18, 142));
            }

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
                }
            }
        }
    }

    // --------- Customise the different slots (in particular - what items they will
    // accept)

    public static ContainerForge createContainerServerSide(int windowID, Inventory playerInventory,ForgeBlockEntity forge){
        return new ContainerForge(windowID, playerInventory, forge, forge.dataAccess);
    }

    public static ContainerForge createContainerClientSide(int windowID, Inventory playerInventory,
                                                           FriendlyByteBuf extraData) {
        return new ContainerForge(windowID, playerInventory, (ForgeBlockEntity) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    @Override
    public boolean stillValid(Player player) {
        return this.forge != null && this.forge.canPlayerAccessInventory(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem())
            return ItemStack.EMPTY;
        ItemStack sourceItemStack = slot.getItem();
        itemstack = sourceItemStack.copy();

        if (index < 7) {
            slot.onQuickCraft(sourceItemStack, itemstack);
            if (!this.moveItemStackTo(sourceItemStack, 7, 39, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (ForgeBlockEntity.isItemFuel(sourceItemStack)) {
                if (!this.moveItemStackTo(sourceItemStack, 5, 6, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (isMold(sourceItemStack.getItem())) {
                if (!this.moveItemStackTo(sourceItemStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (isIngredient(sourceItemStack.getItem())) {
                if (!this.moveItemStackTo(sourceItemStack, 1, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 34) {
                if (!this.moveItemStackTo(sourceItemStack, 34, 43, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 43 && !this.moveItemStackTo(sourceItemStack, 7, 34, false)) {
                return ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Returns the amount of fuel remaining on the currently burning item in the
     * given fuel slot.
     *
     * @return fraction remaining, between 0.0 - 1.0
     * @fuelSlot the number of the fuel slot (0..3)
     */
    public double fractionOfFuelRemaining(int fuelSlot) {
        if (intArray.get(1) <= 0)
            return 0;
        double fraction = intArray.get(0)
                / (double) intArray.get(1);
        return Mth.clamp(fraction, 0.0, 1.0);
    }

    /**
     * return the remaining burn time of the fuel in the given slot
     *
     * @param fuelSlot the number of the fuel slot (0..3)
     * @return seconds remaining
     */
    public int secondsOfFuelRemaining(int fuelSlot) {
        if (intArray.get(0) <= 0)
            return 0;
        return intArray.get(0) / 20; // 20 ticks per second
    }

    /**
     * Returns the amount of cook time completed on the currently cooking item.
     *
     * @return fraction remaining, between 0 - 1
     */
    public double fractionOfCookTimeComplete() {
        if (intArray.get(3) == 0)
            return 0;
        double fraction = intArray.get(2) / (double) intArray.get(3);
        return Mth.clamp(fraction, 0.0, 1.0);
    }

    public boolean isMold(Item item) {

        return false;
    }

    public boolean isIngredient(Item item) {

        return false;
    }

    // SlotFuel is a slot for fuel items
    public static class SlotFuel extends SlotItemHandler {

        public SlotFuel(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given
        // item into this slot
        @Override
        public boolean mayPlace(ItemStack stack) {
            return ForgeHooks.getBurnTime(stack,null) > 0;
        }
    }


    // SlotOutput is a slot that will not accept any item
    public static class SlotOutput extends SlotItemHandler {
        private final Player player;
        private final ForgeBlockEntity forge;
        private int removeCount;

        public SlotOutput(Player player, ForgeBlockEntity forge, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.player = player;
            this.forge = forge;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        public ItemStack remove(int p_75209_1_) {
            if (this.hasItem()) {
                this.removeCount += Math.min(p_75209_1_, this.getItem().getCount());
            }

            return super.remove(p_75209_1_);
        }


        public void onTake(Player p_190901_1_, ItemStack p_190901_2_) {
            this.checkTakeAchievements(p_190901_2_);
            super.onTake(p_190901_1_, p_190901_2_);
        }

        protected void onQuickCraft(ItemStack p_75210_1_, int p_75210_2_) {
            this.removeCount += p_75210_2_;
            this.checkTakeAchievements(p_75210_1_);
        }

        protected void checkTakeAchievements(ItemStack p_39558_) {
            p_39558_.onCraftedBy(this.player.level, this.player, this.removeCount);
            if (this.player instanceof ServerPlayer && this.container instanceof ForgeBlockEntity) {
                ((ForgeBlockEntity)this.container).awardUsedRecipesAndPopExperience((ServerPlayer)this.player);
            }

            this.removeCount = 0;
            net.minecraftforge.event.ForgeEventFactory.firePlayerSmeltedEvent(this.player, p_39558_);
        }
    }
}
