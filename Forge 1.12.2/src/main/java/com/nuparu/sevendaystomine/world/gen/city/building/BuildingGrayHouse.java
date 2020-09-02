package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
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

public class BuildingGrayHouse extends Building {

	public BuildingGrayHouse(ResourceLocation res, int weight) {
		this(res, weight, 0);
	}

	public BuildingGrayHouse(ResourceLocation res, int weight, int yOffset) {
		super(res, weight, yOffset);
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
			pos = pos.up(yOffset);

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			template.addBlocksToWorld(world, pos, placementsettings);

			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), variant);
			}
			generatePedestal(world, pos, template, facing, mirror);
		}
	}

	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data,
			BlockPlanks.EnumType variant) {
		switch (data) {
		case "sedan": {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			CityHelper.placeRandomCar(world, pos, facing.rotateY());
			break;
		}
		default:
			super.handleDataBlock(world, facing, pos, data);
		}
	}
}
