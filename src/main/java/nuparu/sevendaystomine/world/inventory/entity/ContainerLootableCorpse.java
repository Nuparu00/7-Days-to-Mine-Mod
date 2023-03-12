package nuparu.sevendaystomine.world.inventory.entity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.init.ModContainers;
import org.jetbrains.annotations.NotNull;

public class ContainerLootableCorpse extends AbstractContainerMenu {

    private final Level world;
    final Entity corpseEntity;
    final IItemHandlerExtended inventory;

    protected ContainerLootableCorpse(int windowID, Inventory invPlayer, Entity corpseEntity) {
        super(ModContainers.LOOTABLE_COPRSE.get(), windowID);
        this.world = invPlayer.player.level;
        this.corpseEntity = corpseEntity;
        inventory = corpseEntity.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, null).orElse(null);

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                addSlot(new SlotItemHandler(inventory, col + row * 3, 8 + (col+3) * 18, 17 + row * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(invPlayer, k, 8 + k * 18, 142));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    public static ContainerLootableCorpse createContainerClientSide(int windowID, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        return new ContainerLootableCorpse(windowID,playerInventory, playerInventory.player.level.getEntity(packetBuffer.readInt()));
    }

    public static ContainerLootableCorpse createContainerServerSide(int windowID, Inventory playerInventory, Entity entity){
        return new ContainerLootableCorpse(windowID, playerInventory, entity);
    }


    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        final Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            final ItemStack stack = slot.getItem();
            final ItemStack originalStack = stack.copy();

            if (index < 9) {
                if (!this.moveItemStackTo(stack, 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 9, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            return originalStack;
        }

        return ItemStack.EMPTY;
    }
}
