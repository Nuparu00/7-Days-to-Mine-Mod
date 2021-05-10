package nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

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
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.CityHelper;
import nuparu.sevendaystomine.world.gen.city.EnumCityType;

public class BuildingOffice extends Building {

	private ResourceLocation LEFT  = new ResourceLocation(SevenDaysToMine.MODID, "office_building_left");


	public BuildingOffice(ResourceLocation res, int weight) {
		this(res, weight, 0);
	}

	public BuildingOffice(ResourceLocation res, int weight, int yOffset) {
		super(res, weight, yOffset);
		setAllowedCityTypes(EnumCityType.CITY);
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, res);
			if (template == null) {
				return;
			}

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
			pos = pos.up(yOffset).offset(facing, -34);

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			template.addBlocksToWorld(world, pos, placementsettings);

			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(),mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
			BlockPos size = template.getSize();
			template = templatemanager.getTemplate(minecraftserver, LEFT);
			if (template == null) {
				return;
			}
			
			pos = pos.offset(facing, -size.getX() + 32 + 10);
			template.addBlocksToWorld(world, pos, placementsettings);

			map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(),mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
		}
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return new BlockPos(42,10,42);
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
			super.handleDataBlock(world, facing, pos, data,mirror);
		}
	}
}
