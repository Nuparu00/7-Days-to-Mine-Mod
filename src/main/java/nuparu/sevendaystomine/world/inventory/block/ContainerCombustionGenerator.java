package nuparu.sevendaystomine.world.inventory.block;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.world.level.block.entity.CombustionGeneratorBlockEntity;
import org.jetbrains.annotations.NotNull;

public class ContainerCombustionGenerator extends AbstractContainerMenu {

    private final Level world;
    private final CombustionGeneratorBlockEntity blockEntity;
    public final Player player;
    private final ContainerData containerData;

    public ContainerCombustionGenerator(int windowID, Inventory invPlayer, CombustionGeneratorBlockEntity blockEntity, ContainerData containerData) {
        super(ModContainers.COMBUSTION_GENERATOR.get(), windowID);
        this.world = invPlayer.player.level;
        this.player = invPlayer.player;
        this.blockEntity = blockEntity;
        this.containerData = containerData;

        blockEntity.onContainerOpened(player);
        addDataSlots(containerData);

        // server Containers
        if(blockEntity != null) {
            addSlot(new SlotItemHandler(blockEntity.getInventory(), 0, 8, 54));
            addSlot(new SlotItemHandler(blockEntity.getInventory(), 1, 152, 54));

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

    public static ContainerCombustionGenerator createContainerServerSide(int windowID, Inventory playerInventory, CombustionGeneratorBlockEntity blockEntity){
        return new ContainerCombustionGenerator(windowID, playerInventory, blockEntity, blockEntity.dataAccess);
    }

    public static ContainerCombustionGenerator createContainerClientSide(int windowID, Inventory playerInventory,
                                                                         FriendlyByteBuf extraData) {
        return new ContainerCombustionGenerator(windowID, playerInventory, (CombustionGeneratorBlockEntity) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(5));
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

    @Override
    public void removed(@NotNull Player p_75134_1_) {
        super.removed(p_75134_1_);
        this.blockEntity.onContainerClosed(p_75134_1_);
    }

    public double fractionOfFuelRemaining() {
        if (containerData.get(1) <= 0)
            return 0;
        double fraction = containerData.get(0)
                / (double) containerData.get(1);
        return Mth.clamp(fraction, 0.0, 1.0);
    }

    public double getTemperature(){
        return containerData.get(2) / 1000d;
    }

    public int getProduction(){
        return containerData.get(3);
    }

    public int getStored(){
        return containerData.get(4);
    }
    public int getCapacity(){
        return blockEntity.getCapacity();
    }

}
