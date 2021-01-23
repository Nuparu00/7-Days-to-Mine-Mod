package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.city.Street;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
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

public class BuildingCargoShip extends Building {
	
	private ResourceLocation BACK = new ResourceLocation(SevenDaysToMine.MODID, "cargo_ship_back");
	private ResourceLocation MIDDLE = new ResourceLocation(SevenDaysToMine.MODID, "cargo_ship_middle");
	private ResourceLocation FRONT = new ResourceLocation(SevenDaysToMine.MODID, "cargo_ship_front");
	private ResourceLocation TIP = new ResourceLocation(SevenDaysToMine.MODID, "cargo_ship_tip");
	private ResourceLocation PYLON = new ResourceLocation(SevenDaysToMine.MODID, "cargo_ship_pylon");

	public BuildingCargoShip(int weight) {
		this(weight, 0);
	}

	public BuildingCargoShip(int weight, int yOffset) {
		super(null, weight, yOffset);
		this.pedestalState = null;
		this.hasPedestal = false;
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, BACK);
			if (template == null) {
				return;
			}

			
			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
			pos = pos.up(yOffset);
			BlockPos origin = pos;
			
			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);
			template.addBlocksToWorld(world, pos, placementsettings);
			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			
			pos = pos.offset(facing,-32).offset(facing.rotateY(),-4 * (mirror ? 1 : -1));
			template = templatemanager.getTemplate(minecraftserver, MIDDLE);
			if (template == null) {
				return;
			}
			template.addBlocksToWorld(world, pos, placementsettings);
			map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			
			
			pos = pos.offset(facing,-16).offset(facing.rotateY(),4 * (mirror ? 1 : -1));
			template = templatemanager.getTemplate(minecraftserver, FRONT);
			if (template == null) {
				return;
			}
			template.addBlocksToWorld(world, pos, placementsettings);
			map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			
			
			pos = pos.offset(facing,-16);
			template = templatemanager.getTemplate(minecraftserver, TIP);
			if (template == null) {
				return;
			}

			template.addBlocksToWorld(world, pos, placementsettings);
			map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			
			pos = origin.offset(facing,-13).up(31);
			template = templatemanager.getTemplate(minecraftserver, PYLON);
			if (template == null) {
				return;
			}

			template.addBlocksToWorld(world, pos, placementsettings);
			map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
		}
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return BlockPos.ORIGIN;
	}
}
