package nuparu.sevendaystomine.world.inventory.entity;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.world.entity.item.VehicleEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class ContainerVehicle extends AbstractContainerMenu {

    private final Level world;
    public boolean addedChest = false;
    public final ArrayList<Slot> chestSlots = new ArrayList<>();
    public final VehicleEntity vehicle;
    final IItemHandlerExtended inventory;

    protected ContainerVehicle(int windowID, Inventory invPlayer, VehicleEntity vehicle, MenuType<? extends ContainerVehicle> menuType) {
        super(menuType, windowID);
        this.world = invPlayer.player.level;
        this.vehicle = vehicle;
        inventory = vehicle.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, null).orElse(null);
    }

    public void bindChest() {

    }

    public void unbindChest() {

    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

}
