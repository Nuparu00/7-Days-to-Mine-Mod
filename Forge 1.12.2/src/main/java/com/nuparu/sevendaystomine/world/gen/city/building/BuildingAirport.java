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

public class BuildingAirport extends Building {

	public BuildingAirport(int weight) {
		this(weight, 0);
	}

	public BuildingAirport(int weight, int yOffset) {
		super(null, weight, yOffset);
		setPedestal(Blocks.STONE.getDefaultState());
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);
			
			
			for(int i = 0; i < 7;i++) {
				Template template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(SevenDaysToMine.MODID,"airport_a"+(i+1)));
				if(template != null) {
					this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true);	
				}
				pos = pos.offset(facing.rotateY(),mirror ? 32 : -32);
			}
			pos = pos.offset(facing, 32).offset(facing.rotateY(),mirror ? -32 : 32);
			for(int i = 0; i < 7;i++) {
				Template template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(SevenDaysToMine.MODID,"airport_b"+(7-i)));
				if(template != null) {
					this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true);	
				}
				pos = pos.offset(facing.rotateY(),mirror ? -32 : 32);
			}
			pos = pos.offset(facing, 32).offset(facing.rotateY(),mirror ? 32 : -32);
			for(int i = 0; i < 7;i++) {
				Template template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(SevenDaysToMine.MODID,"airport_c"+(i+1)));
				if(template != null) {
					this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true);	
				}
				pos = pos.offset(facing.rotateY(),mirror ? 32 : -32);
			}



		}
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return new BlockPos(200, 10, 200);
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
