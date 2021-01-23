package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.block.BlockBackpack;
import com.nuparu.sevendaystomine.block.BlockCardboardBox;
import com.nuparu.sevendaystomine.block.BlockComputer;
import com.nuparu.sevendaystomine.block.BlockMonitor;
import com.nuparu.sevendaystomine.block.BlockWorkbench;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModLootTables;
import com.nuparu.sevendaystomine.tileentity.TileEntityBackpack;
import com.nuparu.sevendaystomine.tileentity.TileEntityBookshelf;
import com.nuparu.sevendaystomine.tileentity.TileEntityCardboard;
import com.nuparu.sevendaystomine.tileentity.TileEntityCupboard;
import com.nuparu.sevendaystomine.tileentity.TileEntityDresser;
import com.nuparu.sevendaystomine.tileentity.TileEntityFileCabinet;
import com.nuparu.sevendaystomine.tileentity.TileEntityMedicalCabinet;
import com.nuparu.sevendaystomine.tileentity.TileEntityMicrowave;
import com.nuparu.sevendaystomine.tileentity.TileEntityRefrigerator;
import com.nuparu.sevendaystomine.tileentity.TileEntityTable;
import com.nuparu.sevendaystomine.tileentity.TileEntityToilet;
import com.nuparu.sevendaystomine.tileentity.TileEntityTrashBin;
import com.nuparu.sevendaystomine.util.ItemUtils;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.city.EnumCityType;
import com.nuparu.sevendaystomine.world.gen.city.Street;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
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

public class BuildingBrickHouse extends Building {

	public BuildingBrickHouse(ResourceLocation res, int weight) {
		this(res, weight, 0);
	}

	public BuildingBrickHouse(ResourceLocation res, int weight, int yOffset) {
		super(res, weight, yOffset);
		setAllowedCityTypes(EnumCityType.CITY, EnumCityType.TOWN, EnumCityType.VILLAGE);
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
		case "workbench": {
			IBlockState state = Blocks.AIR.getDefaultState();
			int r = world.rand.nextInt(100);
			if (r <= 3) {
				state = ModBlocks.WORKBENCH.getDefaultState().withProperty(BlockWorkbench.FACING, facing);
			}
			else if(r <= 30) {
				state = Blocks.CRAFTING_TABLE.getDefaultState();
			}
				
			world.setBlockState(pos, state);
			break;
		}
		case "table": {
			IBlockState state = null;
			switch (variant) {
			default:
			case OAK:
				state = ModBlocks.TABLE_OAK.getDefaultState();
				break;
			case BIRCH:
				state = ModBlocks.TABLE_BIRCH.getDefaultState();
				break;
			case SPRUCE:
				state = ModBlocks.TABLE_SPRUCE.getDefaultState();
				break;
			case JUNGLE:
				state = ModBlocks.TABLE_JUNGLE.getDefaultState();
				break;
			case DARK_OAK:
				state = ModBlocks.TABLE_BIG_OAK.getDefaultState();
				break;
			case ACACIA:
				state = ModBlocks.TABLE_ACACIA.getDefaultState();
				break;
			}
			world.setBlockState(pos, state);
			break;
		}
		case "chair_west": {
			IBlockState state = null;
			switch (variant) {
			default:
			case OAK:
				state = ModBlocks.CHAIR_OAK.getDefaultState();
				break;
			case BIRCH:
				state = ModBlocks.CHAIR_BIRCH.getDefaultState();
				break;
			case SPRUCE:
				state = ModBlocks.CHAIR_SPRUCE.getDefaultState();
				break;
			case JUNGLE:
				state = ModBlocks.CHAIR_JUNGLE.getDefaultState();
				break;
			case DARK_OAK:
				state = ModBlocks.CHAIR_BIG_OAK.getDefaultState();
				break;
			case ACACIA:
				state = ModBlocks.CHAIR_ACACIA.getDefaultState();
				break;
			}
			world.setBlockState(pos, state.withRotation(Utils.facingToRotation(facing)));
			break;
		}
		case "chair_east": {
			IBlockState state = null;
			switch (variant) {
			default:
			case OAK:
				state = ModBlocks.CHAIR_OAK.getDefaultState();
				break;
			case BIRCH:
				state = ModBlocks.CHAIR_BIRCH.getDefaultState();
				break;
			case SPRUCE:
				state = ModBlocks.CHAIR_SPRUCE.getDefaultState();
				break;
			case JUNGLE:
				state = ModBlocks.CHAIR_JUNGLE.getDefaultState();
				break;
			case DARK_OAK:
				state = ModBlocks.CHAIR_BIG_OAK.getDefaultState();
				break;
			case ACACIA:
				state = ModBlocks.CHAIR_ACACIA.getDefaultState();
				break;
			}
			world.setBlockState(pos, state.withRotation(Utils.facingToRotation(facing.getOpposite())));
			break;
		}
		case "monitor": {
			IBlockState state = Blocks.AIR.getDefaultState();
			if(world.rand.nextInt(6) == 0) {
				state = ModBlocks.MONITOR_OFF.getDefaultState().withRotation(Utils.facingToRotation(facing));
			}
			world.setBlockState(pos, state);
			break;
		}
		case "computer": {
			IBlockState state = Blocks.AIR.getDefaultState();
			if(world.rand.nextInt(6) == 0) {
				state = ModBlocks.COMPUTER.getDefaultState().withRotation(Utils.facingToRotation(facing));
			}
			world.setBlockState(pos, state);
			break;
		}
		default:
			super.handleDataBlock(world, facing, pos, data, mirror);
			break;
		}
	}
	
	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return super.getDimensions(world, facing).add(2,0,2);
	}
}
