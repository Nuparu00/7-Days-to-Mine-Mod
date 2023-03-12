package nuparu.sevendaystomine.world.inventory.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.init.ModContainers;
import org.jetbrains.annotations.NotNull;

public class ContainerCamera extends AbstractContainerMenu {

    private final ResourceLocation NO_PAPER_SLOT = new ResourceLocation(SevenDaysToMine.MODID,"item/empty_paper_slot");

    private final Level world;
    ItemStack stack;
    final IItemHandlerExtended inventory;

    protected ContainerCamera(int windowID, Inventory invPlayer, ItemStack stack) {
        super(ModContainers.CAMERA.get(), windowID);
        this.world = invPlayer.player.level;
        inventory = stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, null).orElse(null);

        addSlot(new SlotItemHandler(inventory, 0, 80, 21){

            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(TextureAtlas.LOCATION_BLOCKS, NO_PAPER_SLOT);
            }
        });

        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(invPlayer, k, 8 + k * 18, 111));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 53 + i * 18));
            }
        }
    }

    public static ContainerCamera createContainerClientSide(int windowID, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        return new ContainerCamera(windowID,playerInventory, packetBuffer.readItem());
    }

    public static ContainerCamera createContainerServerSide(int windowID, Inventory playerInventory, ItemStack stack){
        return new ContainerCamera(windowID, playerInventory, stack);
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

            if (index == 0) {
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
}
