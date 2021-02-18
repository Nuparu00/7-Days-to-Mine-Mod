package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockChemistryStation;
import com.nuparu.sevendaystomine.block.BlockWorkbench;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.city.EnumCityType;
import com.nuparu.sevendaystomine.world.gen.city.Street;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
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

public class BuildingHospital extends Building {

	private ResourceLocation FRONT_RIGHT = new ResourceLocation(SevenDaysToMine.MODID, "hospital_front_right");
	private ResourceLocation BACK_LEFT = new ResourceLocation(SevenDaysToMine.MODID, "hospital_back_left");
	private ResourceLocation BACK_RIGHT = new ResourceLocation(SevenDaysToMine.MODID, "hospital_back_right");

	public BuildingHospital(ResourceLocation res, int weight) {
		this(res, weight, 0);
	}

	public BuildingHospital(ResourceLocation res, int weight, int yOffset) {
		super(res, weight, yOffset);
		setAllowedCityTypes(EnumCityType.CITY);
		setPedestal(ModBlocks.STRUCTURE_STONE.getDefaultState());
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, FRONT_RIGHT);
			if (template == null) {
				return;
			}

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
			pos = pos.up(yOffset).offset(facing, -32);

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
			BlockPos size = template.getSize();
			template = templatemanager.getTemplate(minecraftserver, res);
			if (template == null) {
				return;
			}

			pos = pos.offset(facing, -size.getX() + 32 + 21);
			template.addBlocksToWorld(world, pos, placementsettings);

			map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
			coverWithSand(world, pos, template, facing, mirror,rand);
			template = templatemanager.getTemplate(minecraftserver, BACK_LEFT);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing.rotateY(), mirror ? 32 : -32);
			template.addBlocksToWorld(world, pos, placementsettings);

			map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
			coverWithSand(world, pos, template, facing, mirror,rand);
			template = templatemanager.getTemplate(minecraftserver, BACK_RIGHT);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing, -32);
			template.addBlocksToWorld(world, pos, placementsettings);

			map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
			coverWithSand(world, pos, template, facing, mirror,rand);

		}
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return new BlockPos(53, 10, 53);
	}

	@Override
	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data, boolean mirror) {
		switch (data) {
		case "chemistry_station": {
			IBlockState state = Blocks.AIR.getDefaultState();
			int r = world.rand.nextInt(60);
			if (r <= 3) {
				state = ModBlocks.CHEMISTRY_STATION.getDefaultState().withProperty(BlockChemistryStation.FACING,
						facing.rotateY());
			} else if (r <= 30) {
				state = Blocks.CRAFTING_TABLE.getDefaultState();
			}

			world.setBlockState(pos, state);
			break;
		}
		default:
			super.handleDataBlock(world, facing, pos, data, mirror);
		}
	}
}
