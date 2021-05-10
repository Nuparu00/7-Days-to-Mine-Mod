package nuparu.sevendaystomine.world.gen.city.building;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.items.IItemHandler;
import nuparu.sevendaystomine.block.BlockBackpack;
import nuparu.sevendaystomine.block.BlockBookshelfEnhanced;
import nuparu.sevendaystomine.block.BlockCardboardBox;
import nuparu.sevendaystomine.block.BlockChestOld;
import nuparu.sevendaystomine.block.BlockCodeSafe;
import nuparu.sevendaystomine.block.BlockCupboard;
import nuparu.sevendaystomine.block.BlockDresser;
import nuparu.sevendaystomine.block.BlockGarbage;
import nuparu.sevendaystomine.block.BlockHorizontalBase;
import nuparu.sevendaystomine.block.BlockSandLayer;
import nuparu.sevendaystomine.block.BlockTrashCan;
import nuparu.sevendaystomine.block.BlockWheels;
import nuparu.sevendaystomine.block.BlockWritingTable;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.entity.EntityBandit;
import nuparu.sevendaystomine.entity.EntityBlindZombie;
import nuparu.sevendaystomine.entity.EntityPlaguedNurse;
import nuparu.sevendaystomine.entity.EntityZombieMiner;
import nuparu.sevendaystomine.entity.EntityZombiePoliceman;
import nuparu.sevendaystomine.entity.EntityZombieSoldier;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.tileentity.TileEntityBackpack;
import nuparu.sevendaystomine.tileentity.TileEntityBirdNest;
import nuparu.sevendaystomine.tileentity.TileEntityBookshelf;
import nuparu.sevendaystomine.tileentity.TileEntityCardboard;
import nuparu.sevendaystomine.tileentity.TileEntityCashRegister;
import nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;
import nuparu.sevendaystomine.tileentity.TileEntityCorpse;
import nuparu.sevendaystomine.tileentity.TileEntityCupboard;
import nuparu.sevendaystomine.tileentity.TileEntityDresser;
import nuparu.sevendaystomine.tileentity.TileEntityFileCabinet;
import nuparu.sevendaystomine.tileentity.TileEntityGarbage;
import nuparu.sevendaystomine.tileentity.TileEntityMedicalCabinet;
import nuparu.sevendaystomine.tileentity.TileEntityMicrowave;
import nuparu.sevendaystomine.tileentity.TileEntityOldChest;
import nuparu.sevendaystomine.tileentity.TileEntityRefrigerator;
import nuparu.sevendaystomine.tileentity.TileEntityTable;
import nuparu.sevendaystomine.tileentity.TileEntityToilet;
import nuparu.sevendaystomine.tileentity.TileEntityTrashBin;
import nuparu.sevendaystomine.tileentity.TileEntityTrashCan;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.CityHelper;
import nuparu.sevendaystomine.world.gen.city.EnumCityType;

public class Building {
	@Nullable
	public ResourceLocation res;
	public ResourceLocation registryName;
	public int weight;
	public int yOffset = 0;
	public boolean canBeMirrored = true;
	@Nullable
	public IBlockState pedestalState;
	public boolean hasPedestal = true;

	public Set<Biome> allowedBiomes;
	public Block[] allowedBlocks;
	public Set<EnumCityType> allowedCityTypes;

	public Building(ResourceLocation res, int weight) {
		this(res, weight, 0);
	}

	public Building(ResourceLocation res, int weight, int yOffset) {
		this(res, weight, yOffset, null);
	}

	public Building(ResourceLocation res, int weight, int yOffset, IBlockState pedestalState) {
		this.res = res;
		this.registryName = res;
		this.weight = weight;
		this.yOffset = yOffset;
		this.pedestalState = pedestalState;
		this.allowedBiomes = null;
	}

	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (res == null)
			return;
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

			generateTemplate(world, pos, mirror, facing, placementsettings, template, hasPedestal, rand);
			/*
			 * template.addBlocksToWorld(world, pos, placementsettings); Map<BlockPos,
			 * String> map = template.getDataBlocks(pos, placementsettings); for
			 * (Entry<BlockPos, String> entry : map.entrySet()) { handleDataBlock(world,
			 * facing, entry.getKey(), entry.getValue(), mirror); } generatePedestal(world,
			 * pos, template, facing, mirror);
			 */
		}
	}

	public void generateTemplate(World world, BlockPos pos, boolean mirror, EnumFacing facing,
			PlacementSettings placementsettings, Template template, boolean pedestal, Random rand) {
		template.addBlocksToWorld(world, pos, placementsettings);
		Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);
		for (Entry<BlockPos, String> entry : map.entrySet()) {
			handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
		}
		if (pedestal) {
			generatePedestal(world, pos, template, facing, mirror);
		}
		coverWithSand(world, pos, template, facing, mirror, rand);
	}

	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data, boolean mirror) {
		try {
			Rotation rot = Utils.facingToRotation(facing);
			// To mkaing it so pos == position of the structure block
			switch (data) {
			case "garbage": {
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				if (world.rand.nextBoolean()) {
					world.setBlockState(pos, ModBlocks.GARBAGE.getDefaultState().withProperty(BlockGarbage.FACING,
							EnumFacing.getHorizontal(world.rand.nextInt(4))));
					TileEntityGarbage te = (TileEntityGarbage) world.getTileEntity(pos);
					ItemUtils.fillWithLoot((IItemHandler) te.getInventory(), ModLootTables.TRASH, world, world.rand);
				}
				break;
			}
			case "cobweb": {
				IBlockState state = Blocks.AIR.getDefaultState();
				if (world.rand.nextBoolean()) {
					state = Blocks.WEB.getDefaultState();
				}
				world.setBlockState(pos, state);
				break;
			}
			case "cardboard": {
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				if (world.rand.nextInt(5) == 0) {
					world.setBlockState(pos, ModBlocks.CARDBOARD_BOX.getDefaultState()
							.withProperty(BlockCardboardBox.FACING, EnumFacing.getHorizontal(world.rand.nextInt(4))));
					TileEntityCardboard te = (TileEntityCardboard) world.getTileEntity(pos);
					te.setLootTable(ModLootTables.CARDBOARD, world.rand.nextLong());
					te.fillWithLoot(null);
				}
				break;
			}
			case "writing_table": {
				TileEntityTable te = (TileEntityTable) world.getTileEntity(pos.down());
				te.setLootTable(ModLootTables.CARDBOARD, world.rand.nextLong());
				te.fillWithLoot(null);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				break;
			}
			case "writing_table_bottom": {
				TileEntityTable te = (TileEntityTable) world.getTileEntity(pos.up());
				te.setLootTable(ModLootTables.CUPBOARD, world.rand.nextLong());
				te.fillWithLoot(null);
				world.setBlockState(pos, world
						.getBlockState(pos.offset(world.getBlockState(pos.up()).getValue(BlockWritingTable.FACING))));
				break;
			}
			case "backpack": {
				world.setBlockState(pos, ModBlocks.BACKPACK_NORMAL.getDefaultState().withProperty(BlockBackpack.FACING,
						EnumFacing.getHorizontal(world.rand.nextInt(4))));
				TileEntityBackpack te = (TileEntityBackpack) world.getTileEntity(pos);
				te.setLootTable(ModLootTables.BACKPACK, world.rand.nextLong());
				te.fillWithLoot(null);
				break;
			}
			case "medical_backpack": {
				world.setBlockState(pos, ModBlocks.BACKPACK_MEDICAL.getDefaultState().withProperty(BlockBackpack.FACING,
						EnumFacing.getHorizontal(world.rand.nextInt(4))));
				TileEntityBackpack te = (TileEntityBackpack) world.getTileEntity(pos);
				te.setLootTable(ModLootTables.MEDICAL_CABINET, world.rand.nextLong());
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
				if (te == null)
					break;
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
			case "dresser_bottom": {
				TileEntityDresser te = (TileEntityDresser) world.getTileEntity(pos.up());
				te.setLootTable(ModLootTables.DRESSER, world.rand.nextLong());
				te.fillWithLoot(null);
				IBlockState dresser = world.getBlockState(pos.up());
				world.setBlockState(pos,
						world.getBlockState(pos.offset(dresser.getValue(BlockDresser.FACING).getOpposite())));
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
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				break;
			}
			case "bookshelf": {
				TileEntityBookshelf te = (TileEntityBookshelf) world.getTileEntity(pos.down());
				te.setLootTable(getBookshelfLootTable(world.rand), world.rand.nextLong());
				te.fillWithLoot(null);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				break;
			}
			case "bookshelf_bottom": {
				TileEntityBookshelf te = (TileEntityBookshelf) world.getTileEntity(pos.up());
				te.setLootTable(getBookshelfLootTable(world.rand), world.rand.nextLong());
				te.fillWithLoot(null);
				world.setBlockState(pos, world.getBlockState(
						pos.offset(world.getBlockState(pos.up()).getValue(BlockBookshelfEnhanced.FACING))));
				break;
			}
			case "bookshelves": {
				TileEntityBookshelf te = (TileEntityBookshelf) world.getTileEntity(pos.down());
				te.setLootTable(getBookshelfLootTable(world.rand), world.rand.nextLong());
				te.fillWithLoot(null);
				te = (TileEntityBookshelf) world.getTileEntity(pos.down(2));
				te.setLootTable(getBookshelfLootTable(world.rand), world.rand.nextLong());
				te.fillWithLoot(null);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				break;
			}
			case "bookshelf_tall": {
				TileEntityBookshelf te = (TileEntityBookshelf) world.getTileEntity(pos.down());
				te.setLootTable(getBookshelfLootTable(world.rand), world.rand.nextLong());
				te.fillWithLoot(null);
				te = (TileEntityBookshelf) world.getTileEntity(pos.down(2));
				te.setLootTable(getBookshelfLootTable(world.rand), world.rand.nextLong());
				te.fillWithLoot(null);
				te = (TileEntityBookshelf) world.getTileEntity(pos.down(3));
				te.setLootTable(getBookshelfLootTable(world.rand), world.rand.nextLong());
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
								.withProperty(BlockHorizontalBase.FACING,
										EnumFacing.getHorizontal(world.rand.nextInt(4))));
				TileEntityCorpse te = (TileEntityCorpse) world.getTileEntity(pos);
				te.setLootTable(ModLootTables.MEDICAL_CABINET, world.rand.nextLong());
				te.fillWithLoot(null);
				break;
			}
			case "nest": {
				world.setBlockState(pos, ModBlocks.BIRD_NEST.getDefaultState());
				TileEntityBirdNest te = (TileEntityBirdNest) world.getTileEntity(pos);
				te.setLootTable(ModLootTables.NEST, world.rand.nextLong());
				te.fillWithLoot(null);
				break;
			}
			case "wheels": {
				IBlockState state = Blocks.AIR.getDefaultState();
				if (world.rand.nextInt(40) == 0) {
					state = ModBlocks.WHEELS.getDefaultState().withProperty(BlockWheels.FACING,
							EnumFacing.getHorizontal(world.rand.nextInt(4)));
				}
				world.setBlockState(pos, state);
				break;
			}
			case "cooking_pot": {
				IBlockState state = Blocks.AIR.getDefaultState();
				if (world.rand.nextInt(20) == 0) {
					state = ModBlocks.COOKING_POT.getDefaultState().withProperty(BlockWheels.FACING,
							EnumFacing.getHorizontal(world.rand.nextInt(4)));
				}
				world.setBlockState(pos, state);
				break;
			}
			case "shower_drain": {
				TileEntityHopper te = (TileEntityHopper) world.getTileEntity(pos.down());
				te.setLootTable(ModLootTables.SHOWER_DRAIN, world.rand.nextLong());
				te.fillWithLoot(null);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				break;
			}
			case "trash_can": {
				world.setBlockState(pos, ModBlocks.TRASH_CAN.getDefaultState().withProperty(BlockTrashCan.FACING,
						EnumFacing.getHorizontal(world.rand.nextInt(4))));
				TileEntityTrashCan te = (TileEntityTrashCan) world.getTileEntity(pos);
				te.setLootTable(ModLootTables.TRASH, world.rand.nextLong());
				te.fillWithLoot(null);
				break;
			}
			case "cash_register": {
				TileEntityCashRegister te = (TileEntityCashRegister) world.getTileEntity(pos.down());
				ItemUtils.fillWithLoot((IItemHandler) te.getInventory(), ModLootTables.CASH_REGISTER, world,
						world.rand);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				break;
			}
			case "old_chest": {
				world.setBlockState(pos, ModBlocks.CHEST_OLD.getDefaultState().withProperty(BlockChestOld.FACING,
						EnumFacing.getHorizontal(world.rand.nextInt(4))));
				TileEntityOldChest te = (TileEntityOldChest) world.getTileEntity(pos);
				te.setLootTable(ModLootTables.MEDICAL_CABINET, world.rand.nextLong());
				te.fillWithLoot(null);
				break;
			}
			case "supply_chest": {
				TileEntityChest te = (TileEntityChest) world.getTileEntity(pos.down());
				te.setLootTable(ModLootTables.SUPPLY_CHEST, world.rand.nextLong());
				te.fillWithLoot(null);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				break;
			}
			case "trapped_chest": {
				TileEntityChest te = (TileEntityChest) world.getTileEntity(pos.down());
				te.setLootTable(ModLootTables.TRAPPED_CHEST, world.rand.nextLong());
				te.fillWithLoot(null);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				break;
			}
			case "bandit": {
				EntityBandit bandit = new EntityBandit(world);
				bandit.enablePersistence();
				bandit.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
				bandit.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(bandit)), (IEntityLivingData) null);
				world.spawnEntity(bandit);
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				break;
			}
			case "blind_zombie": {
				EntityBlindZombie zombie = new EntityBlindZombie(world);
				zombie.enablePersistence();
				zombie.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
				zombie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(zombie)), (IEntityLivingData) null);
				world.spawnEntity(zombie);
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				break;
			}
			case "zombie_soldier": {
				EntityZombieSoldier zombie = new EntityZombieSoldier(world);
				zombie.enablePersistence();
				zombie.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
				zombie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(zombie)), (IEntityLivingData) null);
				world.spawnEntity(zombie);
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				break;
			}
			case "military_backpack": {
				world.setBlockState(pos, ModBlocks.BACKPACK_ARMY.getDefaultState().withProperty(BlockBackpack.FACING,
						EnumFacing.getHorizontal(world.rand.nextInt(4))));
				TileEntityBackpack te = (TileEntityBackpack) world.getTileEntity(pos);
				te.setLootTable(ModLootTables.BACKPACK, world.rand.nextLong());
				te.fillWithLoot(null);
				break;
			}
			case "code_safe": {
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				IBlockState furnaceState = world.getBlockState(pos.down());
				world.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
				if (furnaceState.getBlock() != Blocks.FURNACE)
					break;
				world.setBlockState(pos.down(), ModBlocks.CODE_SAFE.getDefaultState().withProperty(BlockCodeSafe.FACING,
						furnaceState.getValue(BlockFurnace.FACING)));
				TileEntityCodeSafe te = (TileEntityCodeSafe) world.getTileEntity(pos.down());
				ItemUtils.fillWithLoot((IItemHandler) te.getInventory(), ModLootTables.CODE_SAFE, world, world.rand);
				break;
			}
			case "zombie_policeman": {
				EntityZombiePoliceman zombie = new EntityZombiePoliceman(world);
				zombie.enablePersistence();
				zombie.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
				zombie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(zombie)), (IEntityLivingData) null);
				world.spawnEntity(zombie);
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				break;
			}
			case "plagued_nurse": {
				EntityPlaguedNurse zombie = new EntityPlaguedNurse(world);
				zombie.enablePersistence();
				zombie.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
				zombie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(zombie)), (IEntityLivingData) null);
				world.spawnEntity(zombie);
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				break;
			}
			case "zombie_miner": {
				EntityZombieMiner zombie = new EntityZombieMiner(world);
				zombie.enablePersistence();
				zombie.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
				zombie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(zombie)), (IEntityLivingData) null);
				world.spawnEntity(zombie);
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				break;
			}
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
			case "workbench": {
				IBlockState state = Blocks.AIR.getDefaultState();
				int r = world.rand.nextInt(100);
				if (r <= 2) {
					state = ModBlocks.WORKBENCH.getDefaultState().withProperty(BlockHorizontalBase.FACING, facing);
				} else if (r <= 30) {
					state = Blocks.CRAFTING_TABLE.getDefaultState();
				}

				world.setBlockState(pos, state);
				break;
			}
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
			}
		} catch (Exception e) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			Utils.getLogger().warn(pos.toString());
			e.printStackTrace();
		}
	}

	/*
	 * Dimensions of the building, necessary for city buildings. Scattered buildings
	 * do not require overriding this, though it is encouraged.
	 */
	public BlockPos getDimensions(World world, EnumFacing facing) {
		if (res == null)
			return BlockPos.ORIGIN;
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

	/*
	 * Generates a pedestal under the structure with the shape of the bottom most
	 * layer of the structure. Uses either the bottom most blockstate of the
	 * structure proper
	 */
	public void generatePedestal(World world, BlockPos pos, Template template, EnumFacing facing, boolean mirror) {
		if (!hasPedestal || !ModConfig.worldGen.structurePedestal)
			return;
		Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
		BlockPos size = template.transformedSize(rot);
		for (int i = 0; i < size.getX(); i++) {
			for (int j = 0; j < size.getZ(); j++) {
				int x = i;
				int z = j;

				if (mirror) {
					if (facing == EnumFacing.EAST || facing == EnumFacing.SOUTH) {
						x = -x;
					}
					if (facing == EnumFacing.WEST || facing == EnumFacing.SOUTH) {
						z = -z;
					}
				} else {
					if (facing == EnumFacing.NORTH || facing == EnumFacing.EAST) {
						x = -x;
					}
					if (facing == EnumFacing.SOUTH || facing == EnumFacing.EAST) {
						z = -z;
					}
				}

				BlockPos pos2 = pos.add(x, -1, z);
				IBlockState newState = pedestalState != null ? pedestalState : world.getBlockState(pos2.up());
				if (world.getBlockState(pos2.up()).getMaterial().isSolid()) {
					for (; pos2.getY() > 0; pos2 = pos2.down()) {

						IBlockState state = world.getBlockState(pos2);
						if (Utils.isSolid(world, pos2, state))
							break;

						world.setBlockState(pos2, newState);
					}
				}
			}
		}
	}

	public void coverWithSand(World world, BlockPos pos, Template template, EnumFacing facing, boolean mirror,
			Random rand) {
		if (!ModConfig.worldGen.snowSandBuildingCover)
			return;
		Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
		BlockPos size = template.transformedSize(rot);
		int minY = pos.getY();
		pos = new BlockPos(pos.getX(), minY + 33, pos.getZ());
		for (int i = 0; i < size.getX(); i++) {
			for (int j = 0; j < size.getZ(); j++) {
				if (rand.nextInt(5) == 0)
					continue;
				int x = i;
				int z = j;

				if (mirror) {
					if (facing == EnumFacing.EAST || facing == EnumFacing.SOUTH) {
						x = -x;
					}
					if (facing == EnumFacing.WEST || facing == EnumFacing.SOUTH) {
						z = -z;
					}
				} else {
					if (facing == EnumFacing.NORTH || facing == EnumFacing.EAST) {
						x = -x;
					}
					if (facing == EnumFacing.SOUTH || facing == EnumFacing.EAST) {
						z = -z;
					}
				}

				BlockPos pos2 = pos.add(x, 0, z);
				Biome biome = world.getBiome(pos2);

				if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) || biome.topBlock.getBlock() != Blocks.SAND)
					continue;
				IBlockState sand = ModBlocks.SAND_LAYER.getDefaultState();
				if (biome.topBlock.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND) {
					sand = ModBlocks.RED_SAND_LAYER.getDefaultState();
				}

				for (; pos2.getY() > minY - 1; pos2 = pos2.down()) {

					IBlockState state = world.getBlockState(pos2);
					if (state.getMaterial() != Material.AIR) {
						if (Utils.isSolid(world, pos2, state)) {
							world.setBlockState(pos2.up(),
									sand.withProperty(BlockSandLayer.LAYERS, 1 + rand.nextInt(2)));
						}
						break;
					}
				}
			}
		}
	}

	public Building setAllowedBiomes(Biome... biomes) {
		this.allowedBiomes = new HashSet<Biome>(Arrays.asList(biomes));
		return this;
	}

	public Building setAllowedBlocks(Block... blocks) {
		this.allowedBlocks = blocks;
		return this;
	}

	public Building setAllowedCityTypes(EnumCityType... types) {
		this.allowedCityTypes = new HashSet<EnumCityType>(Arrays.asList(types));
		return this;
	}

	public Building setPedestal(IBlockState pedestal) {
		this.pedestalState = pedestal;
		return this;
	}

	public Building setHasPedestal(boolean hasPedestal) {
		this.hasPedestal = hasPedestal;
		return this;
	}

	public Building setCanBeMirrored(boolean canBeMirrored) {
		this.canBeMirrored = canBeMirrored;
		return this;
	}

	public ResourceLocation getBookshelfLootTable(Random rand) {
		return rand.nextInt(12) == 0 ? ModLootTables.BOOKSHELF_RARE : ModLootTables.BOOKSHELF_COMMON;
	}
}
