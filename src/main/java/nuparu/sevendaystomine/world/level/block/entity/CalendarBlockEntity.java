package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.init.ModBlockEntities;

public class CalendarBlockEntity extends BlockEntity {
    public CalendarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CALENDAR.get(), pos, state);
    }
}
