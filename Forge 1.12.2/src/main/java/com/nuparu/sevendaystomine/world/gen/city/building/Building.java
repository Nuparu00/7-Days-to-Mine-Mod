package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.block.BlockCupboard;
import com.nuparu.sevendaystomine.block.BlockGarbage;
import com.nuparu.sevendaystomine.block.BlockHorizontalBase;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModLootTables;
import com.nuparu.sevendaystomine.tileentity.TileEntityBackpack;
import com.nuparu.sevendaystomine.tileentity.TileEntityBookshelf;
import com.nuparu.sevendaystomine.tileentity.TileEntityCardboard;
import com.nuparu.sevendaystomine.tileentity.TileEntityCorpse;
import com.nuparu.sevendaystomine.tileentity.TileEntityCupboard;
import com.nuparu.sevendaystomine.tileentity.TileEntityDresser;
import com.nuparu.sevendaystomine.tileentity.TileEntityFileCabinet;
import com.nuparu.sevendaystomine.tileentity.TileEntityGarbage;
import com.nuparu.sevendaystomine.tileentity.TileEntityMedicalCabinet;
import com.nuparu.sevendaystomine.tileentity.TileEntityMicrowave;
import com.nuparu.sevendaystomine.tileentity.TileEntityRefrigerator;
import com.nuparu.sevendaystomine.tileentity.TileEntityTable;
import com.nuparu.sevendaystomine.tileentity.TileEntityToilet;
import com.nuparu.sevendaystomine.tileentity.TileEntityTrashBin;
import com.nuparu.sevendaystomine.util.ItemUtils;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockHopper;
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

public class Building {
	public ResourceLocation res;
	public int weight;
	public int yOffset = 0;

	public Building(ResourceLocation res, int weight) {
		this(res, weight, 0);
	}

	public Building(ResourceLocation res, int weight, int yOffset) {
		this.res = res;
		this.weight = weight;
		this.yOffset = yOffset;
	}

	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror) {
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
				handleDataBlock(world, facing, entry.getKey(), entry.getValue());
			}
		}
	}

	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data) {
		Rotation rot = Utils.facingToRotation(facing);
		IBlockState state = null;
		// To mkaing it so pos == position of the structure block
		switch (data) {
		case "garbage": {
			world.setBlockState(pos, ModBlocks.GARBAGE.getDefaultState().withProperty(BlockGarbage.FACING,
					EnumFacing.getHorizontal(world.rand.nextInt(4))));
			TileEntityGarbage te = (TileEntityGarbage) world.getTileEntity(pos);
			ItemUtils.fillWithLoot((IItemHandler) te.getInventory(), ModLootTables.TRASH, world, world.rand);
			break;
		}
		case "cobweb": {
			world.setBlockState(pos, Blocks.WEB.getDefaultState());
			break;
		}
		case "cardboard": {
			world.setBlockState(pos, ModBlocks.CARDBOARD_BOX.getDefaultState().withProperty(BlockGarbage.FACING,
					EnumFacing.getHorizontal(world.rand.nextInt(4))));
			TileEntityCardboard te = (TileEntityCardboard) world.getTileEntity(pos);
			te.setLootTable(ModLootTables.CARDBOARD, world.rand.nextLong());
			te.fillWithLoot(null);
			break;
		}
		case "writing_table": {
			TileEntityTable te = (TileEntityTable) world.getTileEntity(pos.down());
			te.setLootTable(ModLootTables.CARDBOARD, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "backpack": {
			world.setBlockState(pos, ModBlocks.BACKPACK_NORMAL.getDefaultState().withProperty(BlockGarbage.FACING,
					EnumFacing.getHorizontal(world.rand.nextInt(4))));
			TileEntityBackpack te = (TileEntityBackpack) world.getTileEntity(pos);
			te.setLootTable(ModLootTables.BACKPACK, world.rand.nextLong());
			te.fillWithLoot(null);
			break;
		}
		case "cupboard": {
			TileEntityCupboard te = (TileEntityCupboard) world.getTileEntity(pos.down());
			te.setLootTable(ModLootTables.CUPBOARD, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "cupboard_bottom": {
			TileEntityCupboard te = (TileEntityCupboard) world.getTileEntity(pos.up());
			te.setLootTable(ModLootTables.CUPBOARD, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos,
					world.getBlockState(pos.offset(world.getBlockState(pos.up()).getValue(BlockCupboard.FACING))));
			break;
		}
		case "cupboard_microwave_bottom": {
			TileEntityCupboard te = (TileEntityCupboard) world.getTileEntity(pos.up());
			te.setLootTable(ModLootTables.CUPBOARD, world.rand.nextLong());
			te.fillWithLoot(null);
			TileEntityMicrowave te2 = (TileEntityMicrowave) world.getTileEntity(pos.up(2));
			te2.setLootTable(ModLootTables.MICROWAVE, world.rand.nextLong());
			te2.fillWithLoot(null);
			world.setBlockState(pos,
					world.getBlockState(pos.offset(world.getBlockState(pos.up()).getValue(BlockCupboard.FACING))));
			break;
		}
		case "fridge": {
			TileEntityRefrigerator te = (TileEntityRefrigerator) world.getTileEntity(pos.down());
			te.setLootTable(ModLootTables.FRIDGE, world.rand.nextLong());
			te.fillWithLoot(null);
			te = (TileEntityRefrigerator) world.getTileEntity(pos.down(2));
			te.setLootTable(ModLootTables.FRIDGE, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "bin": {
			TileEntityTrashBin te = (TileEntityTrashBin) world.getTileEntity(pos.down());
			te.setLootTable(ModLootTables.TRASH, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "dresser": {
			TileEntityDresser te = (TileEntityDresser) world.getTileEntity(pos.down());
			te.setLootTable(ModLootTables.DRESSER, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "dresser_tall": {
			TileEntityDresser te = (TileEntityDresser) world.getTileEntity(pos.down());
			te.setLootTable(ModLootTables.DRESSER, world.rand.nextLong());
			te.fillWithLoot(null);
			te = (TileEntityDresser) world.getTileEntity(pos.down(2));
			te.setLootTable(ModLootTables.DRESSER, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "toilet": {
			TileEntityToilet te = (TileEntityToilet) world.getTileEntity(pos.down());
			te.setLootTable(ModLootTables.TOILET, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "sink_bottom": {
			TileEntityHopper te = (TileEntityHopper) world.getTileEntity(pos.up());
			te.setLootTable(ModLootTables.SINK, world.rand.nextLong());
			te.fillWithLoot(null);
			IBlockState hopper = world.getBlockState(pos.up());
			world.setBlockState(pos,
					world.getBlockState(pos.offset(hopper.getValue(BlockHopper.FACING).getOpposite())));
			break;
		}
		case "furnace": {
			TileEntityFurnace te = (TileEntityFurnace) world.getTileEntity(pos.down());
			ItemUtils.fillWithLoot(te, ModLootTables.FURNACE, world, world.rand);
			world.setBlockState(pos,
					world.getBlockState(pos.offset(world.getBlockState(pos.up()).getValue(BlockFurnace.FACING))));
			break;
		}
		case "bookshelf": {
			TileEntityBookshelf te = (TileEntityBookshelf) world.getTileEntity(pos.down());
			te.setLootTable(ModLootTables.BOOKSHELF_COMMON, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "bookshelves": {
			TileEntityBookshelf te = (TileEntityBookshelf) world.getTileEntity(pos.down());
			te.setLootTable(ModLootTables.BOOKSHELF_COMMON, world.rand.nextLong());
			te.fillWithLoot(null);
			te = (TileEntityBookshelf) world.getTileEntity(pos.down(2));
			te.setLootTable(ModLootTables.BOOKSHELF_COMMON, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "file_cabinet": {
			TileEntityFileCabinet te = (TileEntityFileCabinet) world.getTileEntity(pos.down());
			ItemUtils.fillWithLoot((IItemHandler) te.getInventory(), ModLootTables.FILE_CABINET, world, world.rand);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "file_cabinet_tall": {
			TileEntityFileCabinet te = (TileEntityFileCabinet) world.getTileEntity(pos.down());
			ItemUtils.fillWithLoot((IItemHandler) te.getInventory(), ModLootTables.FILE_CABINET, world, world.rand);
			te = (TileEntityFileCabinet) world.getTileEntity(pos.down(2));
			ItemUtils.fillWithLoot((IItemHandler) te.getInventory(), ModLootTables.FILE_CABINET, world, world.rand);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "microwave": {
			TileEntityMicrowave te = (TileEntityMicrowave) world.getTileEntity(pos.down());
			te.setLootTable(ModLootTables.MICROWAVE, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "medical_cabinet": {
			TileEntityMedicalCabinet te = (TileEntityMedicalCabinet) world.getTileEntity(pos.down());
			te.setLootTable(ModLootTables.MEDICAL_CABINET, world.rand.nextLong());
			te.fillWithLoot(null);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			break;
		}
		case "corpse": {
			world.setBlockState(pos,
					(world.rand.nextBoolean() ? ModBlocks.CORPSE_00 : ModBlocks.CORPSE_01).getDefaultState()
							.withProperty(BlockHorizontalBase.FACING, EnumFacing.getHorizontal(world.rand.nextInt(4))));
			TileEntityCorpse te = (TileEntityCorpse) world.getTileEntity(pos);
			te.setLootTable(ModLootTables.MEDICAL_CABINET, world.rand.nextLong());
			te.fillWithLoot(null);
			break;
		}
		}
	}

	public BlockPos getDimensions(World world, EnumFacing facing) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, res);
			if (template == null) {
				return BlockPos.ORIGIN;
			}
			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
			return template.transformedSize(rot);
		}
		return BlockPos.ORIGIN;
	}
}
