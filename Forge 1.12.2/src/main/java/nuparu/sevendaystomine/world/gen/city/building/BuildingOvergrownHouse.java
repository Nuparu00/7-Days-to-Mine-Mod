package nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

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
import net.minecraftforge.items.IItemHandler;
import nuparu.sevendaystomine.block.BlockGarbage;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.tileentity.TileEntityCupboard;
import nuparu.sevendaystomine.tileentity.TileEntityGarbage;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.CityHelper;
import nuparu.sevendaystomine.world.gen.city.EnumCityType;

public class BuildingOvergrownHouse extends Building {

	public BuildingOvergrownHouse(ResourceLocation res, int weight) {
		this(res, weight, 0);
	}

	public BuildingOvergrownHouse(ResourceLocation res, int weight, int yOffset) {
		super(res, weight, yOffset);
		setAllowedCityTypes(EnumCityType.CITY, EnumCityType.TOWN);
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		BlockPlanks.EnumType variant = BlockPlanks.EnumType.values()[world.rand
				.nextInt(BlockPlanks.EnumType.values().length)];
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
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(),mirror, variant);
			}
			generatePedestal(world, pos, template, facing, mirror);
			coverWithSand(world, pos, template, facing, mirror,rand);
		}
	}

	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data, boolean mirror,
			BlockPlanks.EnumType variant) {
		switch (data) {
		case "sedan": {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			CityHelper.placeRandomCar(world, pos, facing.rotateY(), world.rand);
			break;
		}
		case "body_bag": {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			if (world.rand.nextBoolean()) {
				world.setBlockState(pos, ModBlocks.GARBAGE.getDefaultState().withProperty(BlockGarbage.FACING,
						EnumFacing.getHorizontal(world.rand.nextInt(4))));
				TileEntityGarbage te = (TileEntityGarbage) world.getTileEntity(pos);
				ItemUtils.fillWithLoot((IItemHandler) te.getInventory(), ModLootTables.BODY_BAG, world, world.rand);
			}
			break;
		}
		case "nightstand": {
			TileEntityCupboard te = (TileEntityCupboard) world.getTileEntity(pos.down());
			te.setLootTable(ModLootTables.WRITING_TABLE, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		default:
			super.handleDataBlock(world, facing, pos, data, mirror);
		}
	}
	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return super.getDimensions(world, facing).add(2,0,2);
	}
}
