package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
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

public class BuildingSchool extends Building {
	
	private ResourceLocation FRONT_LEFT = new ResourceLocation(SevenDaysToMine.MODID, "school_front_left");
	private ResourceLocation FRONT_RIGHT = new ResourceLocation(SevenDaysToMine.MODID, "school_front_right");
	private ResourceLocation BACK_LEFT = new ResourceLocation(SevenDaysToMine.MODID, "school_back_left");
	private ResourceLocation BACK_RIGHT = new ResourceLocation(SevenDaysToMine.MODID, "school_back_right");

	public BuildingSchool(int weight) {
		this(weight, 0);
	}

	public BuildingSchool(int weight, int yOffset) {
		super(null, weight, yOffset);
		setAllowedCityTypes(EnumCityType.CITY);
		setPedestal(ModBlocks.ANDESITE_BRICKS.getDefaultState());
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, FRONT_LEFT);
			if (template == null) {
				return;
			}

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true);

			template = templatemanager.getTemplate(minecraftserver, FRONT_RIGHT);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing, -30);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true);
			
			template = templatemanager.getTemplate(minecraftserver, BACK_RIGHT);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing.rotateY(), mirror ? 32 : -32);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true);
			
			template = templatemanager.getTemplate(minecraftserver, BACK_LEFT);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing, 30);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true);

		}
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return new BlockPos(60, 10, 60);
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
