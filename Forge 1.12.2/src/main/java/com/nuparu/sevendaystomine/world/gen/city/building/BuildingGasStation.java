package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.city.EnumCityType;
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

public class BuildingGasStation extends Building {

	private ResourceLocation REST = new ResourceLocation(SevenDaysToMine.MODID, "gas_station_0");

	public BuildingGasStation(ResourceLocation res, int weight) {
		this(res, weight, 0);
	}

	public BuildingGasStation(ResourceLocation res, int weight, int yOffset) {
		super(res, weight, yOffset);
		this.pedestalState = Blocks.STONE.getDefaultState();
		setAllowedCityTypes(EnumCityType.URBAN);
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
			pos = pos.up(yOffset).offset(facing, -27 - 10);

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			template.addBlocksToWorld(world, pos, placementsettings);

			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
			BlockPos size = template.getSize();
			template = templatemanager.getTemplate(minecraftserver, REST);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing, -size.getX() + 27 + 14);
			template.addBlocksToWorld(world, pos, placementsettings);

			map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
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
			template = templatemanager.getTemplate(minecraftserver, REST);
			if (template == null) {
				return BlockPos.ORIGIN;
			}
			return pos.add(template.transformedSize(rot)).add(10, 0, 10);
		}
		return BlockPos.ORIGIN;
	}

	@Override
	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data, boolean mirror) {
		switch (data) {
		case "sedan_v": {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			if (world.rand.nextBoolean()) {
				CityHelper.placeRandomCar(world, pos, facing.rotateY(), world.rand);
			}
			break;
		}
		case "sedan_h": {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			if (world.rand.nextBoolean()) {
				CityHelper.placeRandomCar(world, pos, facing, world.rand);
			}
			break;
		}
		default:
			super.handleDataBlock(world, facing, pos, data, mirror);
		}
	}
}
