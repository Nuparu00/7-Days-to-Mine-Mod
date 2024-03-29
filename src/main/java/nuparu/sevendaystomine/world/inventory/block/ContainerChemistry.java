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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.world.level.block.entity.ChemistryBlockEntity;
import nuparu.sevendaystomine.world.level.block.entity.ForgeBlockEntity;
import org.jetbrains.annotations.NotNull;

public class ContainerChemistry extends AbstractContainerMenu {
    // -------- methods used by the ContainerScreen to render parts of the display
    private final ContainerData intArray;
    private final Level world; // needed for some helper methods
    final ChemistryBlockEntity chemistryStation;

    public ContainerChemistry(int windowID, Inventory invPlayer, ChemistryBlockEntity chemistryStation, ContainerData intArray) {
        super(ModContainers.CHEMISTRY_STATION.get(), windowID);
        this.intArray = intArray;
        this.world = invPlayer.player.level();
        this.chemistryStation = chemistryStation;

        addDataSlots(intArray); // tell vanilla to keep the IIntArray synchronised between client and
        // server Containers
        if(chemistryStation != null) {
            addSlot(new SlotItemHandler(chemistryStation.getInventory(), ChemistryBlockEntity.EnumSlots.INPUT_SLOT.ordinal(), 78, 11));
            addSlot(new SlotItemHandler(chemistryStation.getInventory(), ChemistryBlockEntity.EnumSlots.INPUT_SLOT2.ordinal(), 96, 11));
            addSlot(new SlotItemHandler(chemistryStation.getInventory(), ChemistryBlockEntity.EnumSlots.INPUT_SLOT3.ordinal(), 78, 29));
            addSlot(new SlotItemHandler(chemistryStation.getInventory(), ChemistryBlockEntity.EnumSlots.INPUT_SLOT4.ordinal(), 96, 29));

            addSlot(new SlotOutput(invPlayer.player, chemistryStation, chemistryStation.getInventory(), ForgeBlockEntity.EnumSlots.OUTPUT_SLOT.ordinal(), 148, 42));
            addSlot(new SlotFuel(chemistryStation.getInventory(), ForgeBlockEntity.EnumSlots.FUEL_SLOT.ordinal(), 88, 63));

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

    public static ContainerChemistry createContainerServerSide(int windowID, Inventory playerInventory, ChemistryBlockEntity chemistryStation){
        return new ContainerChemistry(windowID, playerInventory, chemistryStation, chemistryStation.dataAccess);
    }

    public static ContainerChemistry createContainerClientSide(int windowID, Inventory playerInventory,
                                                               FriendlyByteBuf extraData) {
        return new ContainerChemistry(windowID, playerInventory, (ChemistryBlockEntity) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.chemistryStation != null && this.chemistryStation.canPlayerAccessInventory(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
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


    public double fractionOfFuelRemaining(int fuelSlot) {
        if (intArray.get(1) <= 0)
            return 0;
        double fraction = intArray.get(0)
                / (double) intArray.get(1);
        return Mth.clamp(fraction, 0.0, 1.0);
    }

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
        public boolean mayPlace(@NotNull ItemStack stack) {
            return ForgeHooks.getBurnTime(stack,null) > 0;
        }
    }


    // SlotOutput is a slot that will not accept any item
    public static class SlotOutput extends SlotItemHandler {
        private final Player player;
        private final ChemistryBlockEntity chemistryStation;
        private int removeCount;

        public SlotOutput(Player player, ChemistryBlockEntity chemistryStation, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.player = player;
            this.chemistryStation = chemistryStation;
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }

        public @NotNull ItemStack remove(int p_75209_1_) {
            if (this.hasItem()) {
                this.removeCount += Math.min(p_75209_1_, this.getItem().getCount());
            }

            return super.remove(p_75209_1_);
        }


        public void onTake(@NotNull Player p_190901_1_, @NotNull ItemStack p_190901_2_) {
            this.checkTakeAchievements(p_190901_2_);
            super.onTake(p_190901_1_, p_190901_2_);
        }

        protected void onQuickCraft(@NotNull ItemStack p_75210_1_, int p_75210_2_) {
            this.removeCount += p_75210_2_;
            this.checkTakeAchievements(p_75210_1_);
        }

        protected void checkTakeAchievements(ItemStack p_39558_) {
            p_39558_.onCraftedBy(this.player.level(), this.player, this.removeCount);
            if (this.player instanceof ServerPlayer && this.container instanceof ChemistryBlockEntity) {
                ((ChemistryBlockEntity)this.container).awardUsedRecipesAndPopExperience(this.player);
            }

            this.removeCount = 0;
            net.minecraftforge.event.ForgeEventFactory.firePlayerSmeltedEvent(this.player, p_39558_);
        }
    }
}
