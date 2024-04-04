package nuparu.sevendaystomine.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MobBreakEvent extends Event
{
    public final World world;
    public final BlockPos pos;
    public final IBlockState state;
    public final EntityLivingBase entity;
    public MobBreakEvent(World world, BlockPos pos, IBlockState state, EntityLivingBase entity)
    {
        this.pos = pos;
        this.world = world;
        this.state = state;
        this.entity = entity;
    }
}
