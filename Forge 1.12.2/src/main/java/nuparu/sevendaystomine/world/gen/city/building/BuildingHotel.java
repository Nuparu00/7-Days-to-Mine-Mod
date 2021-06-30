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

public class BuildingHotel extends Building {
	

	private ResourceLocation ENTRANCE = new ResourceLocation(SevenDaysToMine.MODID, "hotel_entrance");
	private ResourceLocation ENTRANCE_TOP = new ResourceLocation(SevenDaysToMine.MODID, "hotel_entrance_top");
	private ResourceLocation CAFETERIA = new ResourceLocation(SevenDaysToMine.MODID, "hotel_cafeteria");
	private ResourceLocation CAFETERIA_TOP = new ResourceLocation(SevenDaysToMine.MODID, "hotel_cafeteria_top");
	private ResourceLocation BASEMENT = new ResourceLocation(SevenDaysToMine.MODID, "hotel_entrance_basement");


	public BuildingHotel(int weight) {
		this(weight, 0);
	}

	public BuildingHotel(int weight, int yOffset) {
		super(null, weight, yOffset);
		setAllowedCityTypes(EnumCityType.CITY);
		setPedestal(Blocks.STONE.getDefaultState());
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, ENTRANCE);
			if (template == null) {
				return;
			}
			pos = pos.up(yOffset);
			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true,rand,false);

			template = templatemanager.getTemplate(minecraftserver, CAFETERIA);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing, -32);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true,rand,false);
			
			template = templatemanager.getTemplate(minecraftserver, CAFETERIA_TOP);
			if (template == null) {
				return;
			}
			pos = pos.up(32);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, false,rand);

			pos = pos.offset(facing, 32);
			template = templatemanager.getTemplate(minecraftserver, ENTRANCE_TOP);
			if (template == null) {
				return;
			}
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, false,rand);
			pos = pos.down(37);
			template = templatemanager.getTemplate(minecraftserver, BASEMENT);
			if (template == null) {
				return;
			}
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, false,rand,false);

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
