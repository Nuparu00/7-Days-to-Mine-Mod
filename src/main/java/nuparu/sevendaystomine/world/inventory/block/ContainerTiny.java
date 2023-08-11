package nuparu.sevendaystomine.world.inventory.block;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.world.inventory.ItemHandlerNameable;
import nuparu.sevendaystomine.world.level.block.entity.ItemHandlerBlockEntity;
import org.jetbrains.annotations.NotNull;

public class ContainerTiny extends AbstractContainerMenu {

    private final Level world;
    private final ItemHandlerBlockEntity<ItemHandlerNameable> blockEntity;
    public final Player player;

    public ContainerTiny(int windowID, Inventory invPlayer, ItemHandlerBlockEntity<ItemHandlerNameable> blockEntity) {
        super(ModContainers.TINY.get(), windowID);
        this.world = invPlayer.player.level();
        this.player = invPlayer.player;
        this.blockEntity = blockEntity;
        blockEntity.onContainerOpened(player);

        // server Containers
        if(blockEntity != null) {


            addSlot(new SlotItemHandler(blockEntity.getInventory(), 0, 80, 54));

            for (int k = 0; k < 9; ++k) {
                addSlot(new Slot(invPlayer, k, 8 + k * 18, 144));
            }

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
                }
            }
        }
    }

    // --------- Customise the different slots (in particular - what items they will
    // accept)

    public static ContainerTiny createContainerServerSide(int windowID, Inventory playerInventory, ItemHandlerBlockEntity<ItemHandlerNameable> blockEntity){
        return new ContainerTiny(windowID, playerInventory, blockEntity);
    }

    public static ContainerTiny createContainerClientSide(int windowID, Inventory playerInventory,
                                                          FriendlyByteBuf extraData) {
        return new ContainerTiny(windowID, playerInventory, (ItemHandlerBlockEntity<ItemHandlerNameable>) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.blockEntity != null && this.blockEntity.isUsableByPlayer(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        final Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            final ItemStack stack = slot.getItem();
            final ItemStack originalStack = stack.copy();

            if (index < 1) {
                if (!this.moveItemStackTo(stack, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 1, false)) {
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

    @Override
    public void removed(@NotNull Player p_75134_1_) {
        super.removed(p_75134_1_);
        this.blockEntity.onContainerClosed(p_75134_1_);
    }
}
