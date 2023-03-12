package nuparu.sevendaystomine.world.inventory.block;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.world.level.block.entity.WorkbenchBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ContainerWorkbench extends AbstractContainerMenu {

    private final CraftingContainer craftSlots = new CraftingContainer(this,5,5);
    private final ResultContainer resultSlots = new ResultContainer();
    private final Level world; // needed for some helper methods
    private final ContainerLevelAccess access;
    private final WorkbenchBlockEntity workbench;
    public final Player player;

    public ContainerWorkbench(int windowID, Inventory invPlayer, WorkbenchBlockEntity workbench) {
        super(ModContainers.WORKBENCH.get(), windowID);
        this.world = invPlayer.player.level;
        this.player = invPlayer.player;
        this.workbench = workbench;
        this.access = ContainerLevelAccess.create(player.level,workbench.getBlockPos());

        // server Containers
        if(workbench != null) {

            this.addSlot(new ResultSlot(player, this.craftSlots, this.resultSlots, 0, 134, 44));

            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 5; ++j) {
                    this.addSlot(new Slot(this.craftSlots, j + i * 5, 8 + j * 18, 7 + i * 18));
                }
            }

            for (int k = 0; k < 9; ++k) {
                addSlot(new Slot(invPlayer, k, 8 + k * 18, 164));
            }

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 106 + i * 18));
                }
            }
        }
    }

    // --------- Customise the different slots (in particular - what items they will
    // accept)

    public static ContainerWorkbench createContainerServerSide(int windowID, Inventory playerInventory, WorkbenchBlockEntity workbench){
        return new ContainerWorkbench(windowID, playerInventory, workbench);
    }

    public static ContainerWorkbench createContainerClientSide(int windowID, Inventory playerInventory,
                                                               FriendlyByteBuf extraData) {
        return new ContainerWorkbench(windowID, playerInventory, (WorkbenchBlockEntity) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.workbench != null && this.workbench.isUsableByPlayer(player);
    }

    protected static void slotChangedCraftingGrid(int p_217066_0_, Level p_217066_1_, Player p_217066_2_, CraftingContainer p_217066_3_, ResultContainer p_217066_4_) {
        if (!p_217066_1_.isClientSide) {
            ServerPlayer serverplayerentity = (ServerPlayer)p_217066_2_;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = p_217066_1_.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, p_217066_3_, p_217066_1_);
            if (optional.isPresent()) {
                CraftingRecipe icraftingrecipe = optional.get();
                if (p_217066_4_.setRecipeUsed(p_217066_1_, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.assemble(p_217066_3_);
                }
            }

            p_217066_4_.setItem(0, itemstack);
            serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(p_217066_0_,0, 0, itemstack));
        }
    }

    public void slotsChanged(@NotNull Container p_75130_1_) {
        this.access.execute((p_217069_1_, p_217069_2_) -> slotChangedCraftingGrid(this.containerId, p_217069_1_, this.player, this.craftSlots, this.resultSlots));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player entity, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                this.access.execute((p_217067_2_, p_217067_3_) -> itemstack1.getItem().onCraftedBy(itemstack1, p_217067_2_, entity));
                if (!this.moveItemStackTo(itemstack1, 26, 62, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 26 && index < 62) {
                if (!this.moveItemStackTo(itemstack1, 1, 26, false)) {
                    if (index < 53) {
                        if (!this.moveItemStackTo(itemstack1, 53, 62, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 26, 53, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 26, 62, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(entity, itemstack1);
            if (index == 0) {
                entity.drop(itemstack1, false);
            }
        }

        return itemstack;
    }

    @Override
    protected boolean moveItemStackTo(@NotNull ItemStack p_75135_1_, int p_75135_2_, int p_75135_3_, boolean p_75135_4_) {
        boolean flag = false;
        int i = p_75135_2_;
        if (p_75135_4_) {
            i = p_75135_3_ - 1;
        }

        if (p_75135_1_.isStackable()) {
            while(!p_75135_1_.isEmpty()) {
                if (p_75135_4_) {
                    if (i < p_75135_2_) {
                        break;
                    }
                } else if (i >= p_75135_3_) {
                    break;
                }

                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(p_75135_1_, itemstack)) {
                    int j = itemstack.getCount() + p_75135_1_.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(), p_75135_1_.getMaxStackSize());
                    if (j <= maxSize) {
                        p_75135_1_.setCount(0);
                        itemstack.setCount(j);
                        slot.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        p_75135_1_.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.setChanged();
                        flag = true;
                    }
                }

                if (p_75135_4_) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!p_75135_1_.isEmpty()) {
            if (p_75135_4_) {
                i = p_75135_3_ - 1;
            } else {
                i = p_75135_2_;
            }

            while(true) {
                if (p_75135_4_) {
                    if (i < p_75135_2_) {
                        break;
                    }
                } else if (i >= p_75135_3_) {
                    break;
                }

                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(p_75135_1_)) {
                    if (p_75135_1_.getCount() > slot1.getMaxStackSize()) {
                        slot1.set(p_75135_1_.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.set(p_75135_1_.split(p_75135_1_.getCount()));
                    }

                    slot1.setChanged();
                    flag = true;
                    break;
                }

                if (p_75135_4_) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

    public WorkbenchBlockEntity getWorkbench(){
        return this.workbench;
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack p_94530_1_, Slot p_94530_2_) {
        return p_94530_2_.container != this.resultSlots && super.canTakeItemForPickAll(p_94530_1_, p_94530_2_);
    }

    @Override
    public void removed(@NotNull Player p_75134_1_) {
        super.removed(p_75134_1_);
        this.access.execute((p_217068_2_, p_217068_3_) -> this.clearContainer(p_75134_1_, this.craftSlots));
    }
}
