package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.city.Street;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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

public class BuildingChurch extends Building {

	private ResourceLocation BACK = new ResourceLocation(SevenDaysToMine.MODID, "church_back");
	private ResourceLocation TOP = new ResourceLocation(SevenDaysToMine.MODID, "church_top");

	public BuildingChurch(ResourceLocation res, int weight) {
		this(res, weight,0);
	}
	public BuildingChurch(ResourceLocation res, int weight, int yOffset) {
		super(res, weight,yOffset);
	}

	@Override
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
			pos = pos.up(yOffset);
			
			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			template.addBlocksToWorld(world, pos, placementsettings);

			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue());
			}
			BlockPos size = template.getSize();
			template = templatemanager.getTemplate(minecraftserver, BACK);
			if (template == null) {
				return;
			}
			BlockPos pos2 = pos.offset(facing.rotateY(), mirror ? size.getZ() : -size.getZ());
			template.addBlocksToWorld(world, pos2, placementsettings);

			map = template.getDataBlocks(pos2, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue());
			}
			
			template = templatemanager.getTemplate(minecraftserver, TOP);
			if (template == null) {
				return;
			}
			BlockPos pos3 = pos.up(32);
			template.addBlocksToWorld(world, pos3, placementsettings);

			map = template.getDataBlocks(pos3, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
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
			BlockPos pos = template.transformedSize(rot);
			template = templatemanager.getTemplate(minecraftserver, BACK);
			if (template == null) {
				return BlockPos.ORIGIN;
			}
			return pos.add(template.transformedSize(rot.add(Rotation.CLOCKWISE_90))).add(1, 0, 1);
		}
		return BlockPos.ORIGIN;
	}
}
