package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockChemistryStation;
import com.nuparu.sevendaystomine.block.BlockChestOld;
import com.nuparu.sevendaystomine.block.BlockWorkbench;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModLootTables;
import com.nuparu.sevendaystomine.tileentity.TileEntityOldChest;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.city.EnumCityType;
import com.nuparu.sevendaystomine.world.gen.city.Street;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
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

public class BuildingCemetery extends Building {
	
	private ResourceLocation MAIN = new ResourceLocation(SevenDaysToMine.MODID, "cemetery_main");
	private ResourceLocation REST = new ResourceLocation(SevenDaysToMine.MODID, "cemetery_rest");

	public BuildingCemetery(int weight) {
		this(weight, 0);
	}

	public BuildingCemetery(int weight, int yOffset) {
		super(null, weight, yOffset);
		setAllowedCityTypes(EnumCityType.VILLAGE);
		setPedestal(Blocks.STONE.getDefaultState());
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, MAIN);
			if (template == null) {
				return;
			}
			pos = pos.up(yOffset);
			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true,rand);
			
			template = templatemanager.getTemplate(minecraftserver, REST);
			pos = pos.offset(facing, -32);
			template = templatemanager.getTemplate(minecraftserver, REST);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true,rand);
			
		}
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return new BlockPos(40, 10, 40);
	}

	@Override
	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data, boolean mirror) {
		switch (data) {
		case "coffin": {
			TileEntity te = world.getTileEntity(pos.down());
			if(te != null && te instanceof TileEntityOldChest) {
				TileEntityOldChest chest = (TileEntityOldChest)te;
				chest.setLootTable(ModLootTables.COFFIN, world.rand.nextLong());
				chest.fillWithLoot(null);
			}
			world.setBlockState(pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, (world.rand.nextInt(4) == 0) ? BlockDirt.DirtType.COARSE_DIRT : BlockDirt.DirtType.PODZOL));
			break;
		}
		default:
			super.handleDataBlock(world, facing, pos, data, mirror);
		}
	}
}
