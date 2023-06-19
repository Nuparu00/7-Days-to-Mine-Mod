package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.inventory.ItemHandlerNameable;
import nuparu.sevendaystomine.world.inventory.block.ContainerSmall;
import nuparu.sevendaystomine.world.level.block.IContainerBlockWithSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallContainerBlockEntity extends GenericContainerBlockEntity{
    protected static final int INVENTORY_SIZE = 9;

    public SmallContainerBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.SMALL_CONTAINER.get(), pos, state);
    }

    public SmallContainerBlockEntity(BlockEntityType<? extends SmallContainerBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state, INVENTORY_SIZE);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, @NotNull Inventory playerInventory, @NotNull Player player) {
        return ContainerSmall.createContainerServerSide(windowID, playerInventory, this);
    }
}
