package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
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

public class BuildingBrickHouse extends Building {

	public BuildingBrickHouse(ResourceLocation res, int weight) {
		super(res, weight);
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror) {
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
			
			pos = pos.down(3);
			//Rotation rot = Rotation.NONE;
			
			PlacementSettings placementsettings = (new PlacementSettings()).setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot)
					.setIgnoreEntities(false).setChunk((ChunkPos) null).setReplacedBlock((Block) null)
					.setIgnoreStructureBlock(false);

			template.getDataBlocks(pos, placementsettings);
			template.addBlocksToWorld(world, pos.add(0, 1, 0), placementsettings);

			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), variant);
			}
		}
	}

	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data,
			BlockPlanks.EnumType variant) {
		Rotation rot = Utils.facingToRotation(facing);
		if (data.equals("sedan")) {
			world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
			CityHelper.placeRandomCar(world, pos.up(), facing.rotateY());
		} else if (data.equals("table")) {
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
			world.setBlockState(pos.up(), state);
		} else if (data.equals("chair_west")) {
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
			world.setBlockState(pos.up(), state.withRotation(Utils.facingToRotation(facing)));
		} else if (data.equals("chair_east")) {
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
			world.setBlockState(pos.up(), state.withRotation(Utils.facingToRotation(facing.getOpposite())));
		}

		else if (data.equals("corpse")) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			if (world.rand.nextInt(5) == 0) {
				world.setBlockState(pos, ModBlocks.CORPSE_00.getDefaultState().withRotation(rot));
			}
		}
	}
}
