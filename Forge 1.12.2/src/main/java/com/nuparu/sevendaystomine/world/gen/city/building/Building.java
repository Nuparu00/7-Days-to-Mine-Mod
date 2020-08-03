package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.Street;

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

public class Building {
	public ResourceLocation res;
	public int weight;

	public Building(ResourceLocation res, int weight) {
		this.res = res;
		this.weight = weight;
	}

	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror) {
		if (!world.isRemote) {

			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, res);
			if (template == null) {
				return;
			}

			

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());

			pos = pos.down(2);

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			template.getDataBlocks(pos, placementsettings);
			template.addBlocksToWorld(world, pos.add(0, 1, 0), placementsettings);

			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue());
			}
		}
	}

	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data) {

	}

	public BlockPos getDimensions(World world, EnumFacing facing) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, res);
			if (template == null) {
				return BlockPos.ORIGIN;
			}
			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
			return template.transformedSize(rot);
		}
		return BlockPos.ORIGIN;
	}
}
