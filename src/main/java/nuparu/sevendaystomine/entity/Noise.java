package nuparu.sevendaystomine.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Noise {

	private Entity entity;
	private BlockPos pos;
	private World world;
	
    private float volume;
    private float pitch;
    
    public Noise(Entity entity, BlockPos pos, World world,float volume, float pitch) {
    	this.entity = entity;
    	this.pos = pos;
    	this.world = world;
    	this.volume = volume;
    	this.pitch = pitch;
    }
    
    public Entity getEntity() {
    	return entity;
    }
    public BlockPos getPos() {
    	return pos;
    }
    public World getWorld() {
    	return world;
    }
    public float getVolume() {
    	return volume;
    }
    public float getPitch() {
    	return pitch;
    }
    
}
