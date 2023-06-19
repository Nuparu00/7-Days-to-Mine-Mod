package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.world.inventory.block.ContainerSmall;
import nuparu.sevendaystomine.world.inventory.block.ContainerTiny;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TinyContainerBlockEntity extends GenericContainerBlockEntity{
    protected static final int INVENTORY_SIZE = 1;

    public TinyContainerBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.TINY_CONTAINER.get(), pos, state);
    }

    public TinyContainerBlockEntity(BlockEntityType<? extends TinyContainerBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state, INVENTORY_SIZE);
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, @NotNull Inventory playerInventory, @NotNull Player player) {
        return ContainerTiny.createContainerServerSide(windowID, playerInventory, this);
    }
}
