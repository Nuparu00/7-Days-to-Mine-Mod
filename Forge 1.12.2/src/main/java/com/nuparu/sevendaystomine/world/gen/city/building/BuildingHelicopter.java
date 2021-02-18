package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class BuildingHelicopter extends Building {
	public BuildingHelicopter(ResourceLocation res, int weight) {
		super(res, weight);
	}

	public BuildingHelicopter(ResourceLocation res, int weight, int yOffset) {
		super(res, weight, yOffset, null);
	}
	
	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (!world.isRemote) {

			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, res);
			if (template == null) {
				return;
			}

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());

			pos = pos.up(yOffset).offset(facing,6).offset(facing.rotateY(),-5);

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			template.addBlocksToWorld(world, pos, placementsettings);
			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
			coverWithSand(world, pos, template, facing, mirror,rand);
		}
	}
}
