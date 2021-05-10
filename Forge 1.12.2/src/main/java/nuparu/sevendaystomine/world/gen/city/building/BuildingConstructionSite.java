package nuparu.sevendaystomine.world.gen.city.building;

import java.util.Random;

import net.minecraft.block.Block;
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
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockChemistryStation;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.EnumCityType;

public class BuildingConstructionSite extends Building {

	public BuildingConstructionSite(int weight) {
		this(weight, 0);
	}

	public BuildingConstructionSite(int weight, int yOffset) {
		super(null, weight, yOffset);
		setPedestal(ModBlocks.ANDESITE_BRICKS.getDefaultState());
		setAllowedCityTypes(EnumCityType.CITY);
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
			
			
			for(int i = 1; i < 3;i++) {
				Template template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(SevenDaysToMine.MODID,"construction_site_a"+(i)));
				if(template != null) {
					this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true,rand);	
				}
				pos = pos.offset(facing.rotateY(),mirror ? 32 : -32);
			}
			pos = pos.offset(facing, -32).offset(facing.rotateY(),mirror ? -32 : 32);
			for(int i = 0; i < 2;i++) {
				Template template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(SevenDaysToMine.MODID,"construction_site_b"+(2-i)));
				if(template != null) {
					this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true,rand);	
				}
				pos = pos.offset(facing.rotateY(),mirror ? -32 : 32);
			}
			
			pos = pos.offset(facing, 41).offset(facing.rotateY(),mirror ? 32 : -32);
			Template template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(SevenDaysToMine.MODID,"construction_site_crane_bottom"));
			if(template != null) {
				this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true,rand);	
			}
			
			pos = pos.up(32);
			
			template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(SevenDaysToMine.MODID,"construction_site_crane_cab"));
			if(template != null) {
				this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, false,rand);	
			}

			pos = pos.offset(facing, -32);
			template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(SevenDaysToMine.MODID,"construction_site_crane_front"));
			if(template != null) {
				this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, false,rand);	
			}
			
			pos = pos.offset(facing, 47);
			template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(SevenDaysToMine.MODID,"construction_site_crane_end"));
			if(template != null) {
				this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, false,rand);	
			}
		}
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return new BlockPos(70, 10, 70);
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
