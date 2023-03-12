package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.init.ModBlockEntities;

public class RadioBlockEntity extends BlockEntity {
    public RadioBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RADIO.get(), pos, state);
    }
}
