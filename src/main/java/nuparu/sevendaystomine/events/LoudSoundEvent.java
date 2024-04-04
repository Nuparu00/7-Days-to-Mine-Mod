package nuparu.sevendaystomine.events;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LoudSoundEvent extends Event
{

	public World world;
	public BlockPos pos;
	public SoundEvent res;
	public float volume;
	public SoundCategory category;
	
    public LoudSoundEvent(World world, BlockPos pos, SoundEvent resource, float volume, SoundCategory category)
    {
    	this.world = world;
    	this.pos = pos;
    	this.res = resource;
    	this.volume = volume;
    	this.category = category;
    }
}
