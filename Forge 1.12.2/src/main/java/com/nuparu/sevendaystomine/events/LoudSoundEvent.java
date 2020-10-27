package com.nuparu.sevendaystomine.events;

import net.minecraftforge.fml.common.eventhandler.Event;

import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.state.IBlockState;

public class LoudSoundEvent extends Event
{

	public World world;
	public BlockPos pos;
	public ResourceLocation res;
	public float volume;
	public SoundCategory category;
	
    public LoudSoundEvent(World world, BlockPos pos, ResourceLocation resource, float volume, SoundCategory category)
    {
    	this.world = world;
    	this.pos = pos;
    	this.res = resource;
    	this.volume = volume;
    	this.category = category;
    }
}
