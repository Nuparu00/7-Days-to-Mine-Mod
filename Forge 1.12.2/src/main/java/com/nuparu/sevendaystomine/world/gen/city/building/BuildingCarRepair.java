package com.nuparu.sevendaystomine.world.gen.city.building;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.city.EnumCityType;

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

public class BuildingCarRepair extends Building {

	private ResourceLocation MAIN = new ResourceLocation(SevenDaysToMine.MODID, "car_repair");
	private ResourceLocation REST = new ResourceLocation(SevenDaysToMine.MODID, "car_repair_end");

	public BuildingCarRepair(int weight) {
		super(null, weight, -1);
		this.setAllowedCityTypes(EnumCityType.CITY);
		this.setPedestal(ModBlocks.STRUCTURE_STONE.getDefaultState());
		this.setHasPedestal(true);
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, MAIN);
			if (template == null) {
				return;
			}

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			pos = pos.up(yOffset);

			generateTemplate(world, pos, mirror, facing, placementsettings, template, hasPedestal);
			pos = pos.offset(facing.rotateY(), mirror ? 32 : -32);
			template = templatemanager.getTemplate(minecraftserver, REST);
			if (template == null) {
				return;
			}
			generateTemplate(world, pos, mirror, facing, placementsettings, template, hasPedestal);

		}
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return new BlockPos(40, 22, 40);
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
