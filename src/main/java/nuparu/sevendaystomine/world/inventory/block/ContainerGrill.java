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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.world.level.block.entity.GrillBlockEntity;
import org.jetbrains.annotations.NotNull;

public class ContainerGrill extends AbstractContainerMenu {
    // -------- methods used by the ContainerScreen to render parts of the display
    private final ContainerData intArray;
    private final Level world; // needed for some helper methods
    final GrillBlockEntity grill;

    public ContainerGrill(int windowID, Inventory invPlayer, GrillBlockEntity grill, ContainerData intArray) {
        super(ModContainers.COOKING_GRILL.get(), windowID);
        this.intArray = intArray;
        this.world = invPlayer.player.level;
        this.grill = grill;

        addDataSlots(intArray); // tell vanilla to keep the IIntArray synchronised between client and
        // server Containers
        if(grill != null) {
            addSlot(new SlotItemHandler(grill.getInventory(), GrillBlockEntity.EnumSlots.INPUT_SLOT.ordinal(), 47, 27));
            addSlot(new SlotItemHandler(grill.getInventory(), GrillBlockEntity.EnumSlots.INPUT_SLOT2.ordinal(), 65, 27));
            addSlot(new SlotItemHandler(grill.getInventory(), GrillBlockEntity.EnumSlots.INPUT_SLOT3.ordinal(), 47, 45));
            addSlot(new SlotItemHandler(grill.getInventory(), GrillBlockEntity.EnumSlots.INPUT_SLOT4.ordinal(), 65, 45));

            addSlot(new SlotOutput(invPlayer.player, grill, grill.getInventory(), GrillBlockEntity.EnumSlots.OUTPUT_SLOT.ordinal(), 123, 36));

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

    public static ContainerGrill createContainerServerSide(int windowID, Inventory playerInventory, GrillBlockEntity forge){
        return new ContainerGrill(windowID, playerInventory, forge, forge.dataAccess);
    }

    public static ContainerGrill createContainerClientSide(int windowID, Inventory playerInventory,
                                                           FriendlyByteBuf extraData) {
        return new ContainerGrill(windowID, playerInventory, (GrillBlockEntity) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.grill != null && this.grill.canPlayerAccessInventory(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem())
            return ItemStack.EMPTY;
        ItemStack sourceItemStack = slot.getItem();
        itemstack = sourceItemStack.copy();

        if (index < 5) {
            slot.onQuickCraft(sourceItemStack, itemstack);
            if (!this.moveItemStackTo(sourceItemStack, 5, 39, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (isIngredient(sourceItemStack.getItem())) {
                if (!this.moveItemStackTo(sourceItemStack, 0, 4, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 32) {
                if (!this.moveItemStackTo(sourceItemStack, 32, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 41 && !this.moveItemStackTo(sourceItemStack, 7, 32, false)) {
                return ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }


    /**
     * Returns the amount of cook time completed on the currently cooking item.
     *
     * @return fraction remaining, between 0 - 1
     */
    public double fractionOfCookTimeComplete() {
        if (intArray.get(1) == 0)
            return 0;
        double fraction = intArray.get(0) / (double) intArray.get(1);
        return Mth.clamp(fraction, 0.0, 1.0);
    }


    public GrillBlockEntity getGrill(){
        return this.grill;
    }

    public boolean isIngredient(Item item) {

        return false;
    }
    // SlotOutput is a slot that will not accept any item
    public static class SlotOutput extends SlotItemHandler {
        private final Player player;
        private final GrillBlockEntity forge;
        private int removeCount;

        public SlotOutput(Player player, GrillBlockEntity forge, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.player = player;
            this.forge = forge;
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
            p_39558_.onCraftedBy(this.player.level, this.player, this.removeCount);
            if (this.player instanceof ServerPlayer && this.container instanceof GrillBlockEntity) {
                ((GrillBlockEntity)this.container).awardUsedRecipesAndPopExperience(this.player);
            }

            this.removeCount = 0;
            net.minecraftforge.event.ForgeEventFactory.firePlayerSmeltedEvent(this.player, p_39558_);
        }
    }
}
