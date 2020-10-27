package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockBookshelfEnhanced;
import com.nuparu.sevendaystomine.block.BlockChestOld;
import com.nuparu.sevendaystomine.block.BlockCupboard;
import com.nuparu.sevendaystomine.block.BlockGarbage;
import com.nuparu.sevendaystomine.block.BlockHorizontalBase;
import com.nuparu.sevendaystomine.block.BlockTrashCan;
import com.nuparu.sevendaystomine.block.BlockWheels;
import com.nuparu.sevendaystomine.block.BlockWritingTable;
import com.nuparu.sevendaystomine.entity.EntityBandit;
import com.nuparu.sevendaystomine.entity.EntityBlindZombie;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModLootTables;
import com.nuparu.sevendaystomine.tileentity.TileEntityBackpack;
import com.nuparu.sevendaystomine.tileentity.TileEntityBirdNest;
import com.nuparu.sevendaystomine.tileentity.TileEntityBookshelf;
import com.nuparu.sevendaystomine.tileentity.TileEntityCardboard;
import com.nuparu.sevendaystomine.tileentity.TileEntityCashRegister;
import com.nuparu.sevendaystomine.tileentity.TileEntityCorpse;
import com.nuparu.sevendaystomine.tileentity.TileEntityCupboard;
import com.nuparu.sevendaystomine.tileentity.TileEntityDresser;
import com.nuparu.sevendaystomine.tileentity.TileEntityFileCabinet;
import com.nuparu.sevendaystomine.tileentity.TileEntityGarbage;
import com.nuparu.sevendaystomine.tileentity.TileEntityMedicalCabinet;
import com.nuparu.sevendaystomine.tileentity.TileEntityMicrowave;
import com.nuparu.sevendaystomine.tileentity.TileEntityOldChest;
import com.nuparu.sevendaystomine.tileentity.TileEntityRefrigerator;
import com.nuparu.sevendaystomine.tileentity.TileEntityTable;
import com.nuparu.sevendaystomine.tileentity.TileEntityToilet;
import com.nuparu.sevendaystomine.tileentity.TileEntityTrashBin;
import com.nuparu.sevendaystomine.tileentity.TileEntityTrashCan;
import com.nuparu.sevendaystomine.util.ItemUtils;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityVindicator;
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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.IItemHandler;

public class BuildingWindTurbine extends Building{
	
	private static final ResourceLocation BASE  = new ResourceLocation(SevenDaysToMine.MODID, "wind_turbine_base");
	private static final ResourceLocation TOP  = new ResourceLocation(SevenDaysToMine.MODID, "wind_turbine_top");

	public BuildingWindTurbine(int weight) {
		this(weight, 0);
	}

	public BuildingWindTurbine(int weight, int yOffset) {
		this(weight,yOffset,null);
	}

	public BuildingWindTurbine(int weight, int yOffset, IBlockState pedestalState) {
		super(BASE,weight,yOffset,pedestalState);
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror) {
		if (!world.isRemote) {

			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, BASE);
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
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
			
			pos = pos.up(32);
			
			template = templatemanager.getTemplate(minecraftserver, TOP);
			if (template == null) {
				return;
			}
			
			template.addBlocksToWorld(world, pos, placementsettings);
			map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
		}
	}
}
