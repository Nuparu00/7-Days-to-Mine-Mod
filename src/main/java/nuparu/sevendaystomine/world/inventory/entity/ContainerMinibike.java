package nuparu.sevendaystomine.world.inventory.entity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.world.entity.item.MinibikeEntity;
import nuparu.sevendaystomine.world.inventory.entity.slot.SlotVehicleComponent;
import nuparu.sevendaystomine.world.inventory.entity.slot.SlotVehicleChest;
import org.jetbrains.annotations.NotNull;

public class ContainerMinibike extends ContainerVehicle {

    public final MinibikeEntity minibike;
    protected ContainerMinibike(int windowID, Inventory invPlayer, MinibikeEntity minibike) {
        super(windowID,invPlayer,minibike,ModContainers.MINIBIKE.get());
        this.minibike = minibike;

        this.addSlot(new SlotVehicleComponent(inventory, 0, 8, 8, ModItems.MINIBIKE_HANDLES.get()));
        this.addSlot(new SlotVehicleComponent(inventory, 1, 8, 26, ModBlocks.WHEELS.get().asItem()));
        this.addSlot(new SlotVehicleComponent(inventory, 2, 8, 44, ModItems.MINIBIKE_SEAT.get()));

        this.addSlot(new SlotVehicleComponent(inventory, 3, 152, 8, ModItems.CAR_BATTERY.get()));
        this.addSlot(new SlotVehicleComponent(inventory, 4, 152, 26, ModItems.SMALL_ENGINE.get()));
        this.addSlot(
                new SlotVehicleChest(inventory, 5, 152, 44, this));

        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(invPlayer, k, 8 + k * 18, 142));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    public static ContainerMinibike createContainerClientSide(int windowID, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        return new ContainerMinibike(windowID,playerInventory, (MinibikeEntity) playerInventory.player.level().getEntity(packetBuffer.readInt()));
    }

    public static ContainerMinibike createContainerServerSide(int windowID, Inventory playerInventory, MinibikeEntity entity){
        return new ContainerMinibike(windowID, playerInventory, entity);
    }

    public void bindChest() {
        if (minibike.getChest()) {
            addedChest = true;
            int l = 0;
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {

                    Slot slot = new SlotItemHandler(inventory, 6 + l, 181 + j * 18, 8 + i * 18);
                    this.chestSlots.add(slot);
                    this.addSlot(slot);
                    l++;
                }
            }
        }
    }

    public void unbindChest() {
        addedChest = false;
        for (Slot slot : chestSlots) {
            //this.inventoryItemStacks.remove(this.inventorySlots.indexOf(slot));
            if (!this.vehicle.level().isClientSide()) {
                float width = this.vehicle.getDimensions(Pose.STANDING).width/2f;
                float height = this.vehicle.getDimensions(Pose.STANDING).height;
                Containers.dropItemStack(this.vehicle.level(), this.vehicle.position().x+width, this.vehicle.position().y + height, this.vehicle.position().z+width, slot.getItem());
            }
            this.vehicle.replaceItemInInventory(slot.getSlotIndex(), ItemStack.EMPTY);
            slot.setChanged();

            this.slots.remove(slot);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        final Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            final ItemStack stack = slot.getItem();
            final ItemStack originalStack = stack.copy();
            Item item = stack.getItem();

            if (index < 7) {
                if (!this.moveItemStackTo(stack, 7, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(originalStack, stack);
                if (!vehicle.level().isClientSide()) {
                    vehicle.updateInventory();
                }
            } else {
                if (item == ModItems.SMALL_ENGINE.get()) {
                    if (!this.moveItemStackTo(stack, 0, 6, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                } else if (item == ModItems.MINIBIKE_HANDLES.get()) {
                    if (!this.moveItemStackTo(stack, 0, 6, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                } else if (item == ModItems.MINIBIKE_SEAT.get()) {
                    if (!this.moveItemStackTo(stack, 0, 6, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                } else if (item == ModItems.CAR_BATTERY.get()) {
                    if (!this.moveItemStackTo(stack, 0, 6, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                }
                else if (item == ModBlocks.WHEELS.get().asItem()) {
                    if (!this.moveItemStackTo(stack, 0, 6, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                }
                else if (item == Blocks.CHEST.asItem()) {
                    if (!this.moveItemStackTo(stack, 0, 6, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                }

                else if (index <= 32) {
                    if (!this.moveItemStackTo(stack, 33, 41, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                } else if (index <= 41) {
                    if (!this.moveItemStackTo(stack, 6, 32, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(originalStack, stack);
                }
            }



               /* if (!this.moveItemStackTo(stack, 0, 9, false)) {
                return ItemStack.EMPTY;
            }*/

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
