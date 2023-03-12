package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.world.inventory.ItemHandlerNameable;
import nuparu.sevendaystomine.world.level.block.BookshelfBlock;

public class BookshelfBlockEntity extends SmallContainerBlockEntity{
    public BookshelfBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BOOKSHELF.get(),pos, state);
    }

    @Override
    protected ItemHandlerNameable createInventory() {
        final BookshelfBlockEntity te = this;
        return new ItemHandlerNameable(INVENTORY_SIZE, Component.translatable("container." + ForgeRegistries.BLOCKS.getKey(getBlockState().getBlock()).getPath())){
            @Override
            protected void onContentsChanged(int slot)
            {
                super.onContentsChanged(slot);
                te.updateBlock();
            }
        };
    }

    public void updateBlock() {
        if(level.isClientSide()) return;
        BlockState blockState = this.level.getBlockState(this.worldPosition);
        if(blockState.getBlock() instanceof BookshelfBlock) {
            boolean state = blockState.getValue(BookshelfBlock.FULL);
            if (getInventory().isEmpty() && state) {
                level.setBlock(worldPosition, blockState.setValue(BookshelfBlock.FULL, false), 3);
            } else if (!getInventory().isEmpty() && !state) {
                level.setBlock(worldPosition, blockState.setValue(BookshelfBlock.FULL, true), 3);
            }
        }
    }

    public void tick() {
        if(level.isClientSide()) return;
        if(this.lootTable != null){
            this.unpackLootTable(null);
        }
    }
}
