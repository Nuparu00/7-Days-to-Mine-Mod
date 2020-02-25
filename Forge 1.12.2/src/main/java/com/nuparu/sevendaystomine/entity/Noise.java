package com.nuparu.sevendaystomine.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Noise {

	private Entity entity;
	private BlockPos pos;
	private World world;
	
	private SoundEvent name;
    private SoundCategory category;
    private float volume;
    private float pitch;
}
