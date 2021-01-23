package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockCodeSafe;
import com.nuparu.sevendaystomine.entity.EntitySoldier;
import com.nuparu.sevendaystomine.entity.EntitySurvivor;
import com.nuparu.sevendaystomine.entity.EntityZombieSoldier;
import com.nuparu.sevendaystomine.entity.EntitySurvivor.EnumCarrier;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.city.Street;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class BuildingSettlement extends Building {

	private static final ResourceLocation FARM = new ResourceLocation(SevenDaysToMine.MODID, "settlement_farm");
	private static final ResourceLocation HOUSES = new ResourceLocation(SevenDaysToMine.MODID, "settlement_houses");
	private static final ResourceLocation BARRACKS = new ResourceLocation(SevenDaysToMine.MODID, "settlement_barracks");
	private static final ResourceLocation PUB = new ResourceLocation(SevenDaysToMine.MODID, "settlement_pub");

	public BuildingSettlement(int weight) {
		this(weight, 2);
	}

	public BuildingSettlement(int weight, int yOffset) {
		super(null, weight, yOffset);
		canBeMirrored = false;
		this.pedestalState = Blocks.STONE.getDefaultState();
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, FARM);
			if (template == null) {
				return;
			}

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
			pos = pos.up(yOffset);

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true);

			template = templatemanager.getTemplate(minecraftserver, HOUSES);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing, -26);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true);
			
			template = templatemanager.getTemplate(minecraftserver, PUB);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing.rotateY(), mirror ? 26 : -26);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true);
			
			template = templatemanager.getTemplate(minecraftserver, BARRACKS);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing, 26);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true);
		}
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return new BlockPos(50, 22, 50);
	}

	@Override
	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data, boolean mirror) {
		switch (data) {
		case "map": {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			for (int i = -1; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					BlockPos pos2 = pos.down(1 + j).offset(facing, i);
					List<EntityItemFrame> list = world.getEntitiesWithinAABB(EntityItemFrame.class,
							new AxisAlignedBB(pos2, pos2.add(1, 1, 1)));
					if (list.isEmpty())
						continue;
					EntityItemFrame frame = list.get(0);
					int x = pos.getX() + (256 * i * (mirror ? -1 : 1));
					int z = pos.getZ() + (256 * j);
					ItemStack stack = ItemMap.setupNewMap(world, x, z, (byte) 1, true, true);
					Utils.renderBiomePreviewMap(world, stack);
					MapData.addTargetDecoration(stack, pos, "+", MapDecoration.Type.TARGET_X);
					frame.setDisplayedItem(stack);
				}
			}
			break;
		}
		case "doctor": {
			EntitySurvivor survivor = new EntitySurvivor(world);
			survivor.setCarrier(EnumCarrier.DOCTOR);
			survivor.enablePersistence();
			survivor.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
			survivor.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(survivor)), (IEntityLivingData) null);
			world.spawnEntity(survivor);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			break;
		}
		case "farmer": {
			EntitySurvivor survivor = new EntitySurvivor(world);
			survivor.setCarrier(EnumCarrier.FARMER);
			survivor.enablePersistence();
			survivor.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
			survivor.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(survivor)), (IEntityLivingData) null);
			world.spawnEntity(survivor);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			break;
		}
		case "electrician": {
			EntitySurvivor survivor = new EntitySurvivor(world);
			survivor.setCarrier(EnumCarrier.ELECTRICIAN);
			survivor.enablePersistence();
			survivor.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
			survivor.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(survivor)), (IEntityLivingData) null);
			world.spawnEntity(survivor);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			break;
		}
		case "miner": {
			EntitySurvivor survivor = new EntitySurvivor(world);
			survivor.setCarrier(EnumCarrier.MINER);
			survivor.enablePersistence();
			survivor.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
			survivor.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(survivor)), (IEntityLivingData) null);
			world.spawnEntity(survivor);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			break;
		}
		case "soldier": {
			EntitySoldier survivor = new EntitySoldier(world);
			survivor.enablePersistence();
			survivor.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
			survivor.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(survivor)), (IEntityLivingData) null);
			world.spawnEntity(survivor);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			break;
		}
		default:
			super.handleDataBlock(world, facing, pos, data, mirror);
		}
	}
}
