package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.nuparu.sevendaystomine.block.BlockBookshelfEnhanced;
import com.nuparu.sevendaystomine.block.BlockChestOld;
import com.nuparu.sevendaystomine.block.BlockCodeSafe;
import com.nuparu.sevendaystomine.block.BlockCupboard;
import com.nuparu.sevendaystomine.block.BlockGarbage;
import com.nuparu.sevendaystomine.block.BlockHorizontalBase;
import com.nuparu.sevendaystomine.block.BlockTrashCan;
import com.nuparu.sevendaystomine.block.BlockWheels;
import com.nuparu.sevendaystomine.block.BlockWritingTable;
import com.nuparu.sevendaystomine.entity.EntityBandit;
import com.nuparu.sevendaystomine.entity.EntityBlindZombie;
import com.nuparu.sevendaystomine.entity.EntityZombieSoldier;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModLootTables;
import com.nuparu.sevendaystomine.tileentity.TileEntityBackpack;
import com.nuparu.sevendaystomine.tileentity.TileEntityBirdNest;
import com.nuparu.sevendaystomine.tileentity.TileEntityBookshelf;
import com.nuparu.sevendaystomine.tileentity.TileEntityCardboard;
import com.nuparu.sevendaystomine.tileentity.TileEntityCashRegister;
import com.nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;
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
import com.nuparu.sevendaystomine.world.gen.city.EnumCityType;

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

public class Building {
	public ResourceLocation res;
	public ResourceLocation registryName;
	public int weight;
	public int yOffset = 0;
	public boolean canBeMirrored = true;
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
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
		}
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
					world.setBlockState(pos, ModBlocks.CARDBOARD_BOX.getDefaultState().withProperty(BlockGarbage.FACING,
							EnumFacing.getHorizontal(world.rand.nextInt(4))));
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
				world.setBlockState(pos, ModBlocks.BACKPACK_NORMAL.getDefaultState().withProperty(BlockGarbage.FACING,
						EnumFacing.getHorizontal(world.rand.nextInt(4))));
				TileEntityBackpack te = (TileEntityBackpack) world.getTileEntity(pos);
				te.setLootTable(ModLootTables.BACKPACK, world.rand.nextLong());
				te.fillWithLoot(null);
				break;
			}
			case "medical_backpack": {
				world.setBlockState(pos, ModBlocks.BACKPACK_MEDICAL.getDefaultState().withProperty(BlockGarbage.FACING,
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
				te.setLootTable(ModLootTables.BOOKSHELF_COMMON, world.rand.nextLong());
				te.fillWithLoot(null);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				break;
			}
			case "bookshelf_bottom": {
				TileEntityBookshelf te = (TileEntityBookshelf) world.getTileEntity(pos.up());
				te.setLootTable(ModLootTables.BOOKSHELF_COMMON, world.rand.nextLong());
				te.fillWithLoot(null);
				world.setBlockState(pos, world.getBlockState(
						pos.offset(world.getBlockState(pos.up()).getValue(BlockBookshelfEnhanced.FACING))));
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
			case "bookshelf_tall": {
				TileEntityBookshelf te = (TileEntityBookshelf) world.getTileEntity(pos.down());
				te.setLootTable(ModLootTables.BOOKSHELF_COMMON, world.rand.nextLong());
				te.fillWithLoot(null);
				te = (TileEntityBookshelf) world.getTileEntity(pos.down(2));
				te.setLootTable(ModLootTables.BOOKSHELF_COMMON, world.rand.nextLong());
				te.fillWithLoot(null);
				te = (TileEntityBookshelf) world.getTileEntity(pos.down(3));
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
				world.setBlockState(pos, ModBlocks.BACKPACK_ARMY.getDefaultState().withProperty(BlockGarbage.FACING,
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
			}
		} catch (Exception e) {
			Utils.getLogger().warn(pos.toString());
			e.printStackTrace();
		}
	}

	/*
	 * Dimensions of the building, necessary for city buildings. Scattered buildings
	 * do not require overriding this, though it is encouraged.
	 */
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

	/*
	 * Generates a pedestal under the structure with the shape of the bottom most
	 * layer of the structure. Uses either the bottom most blockstate of the
	 * structure proper
	 */
	public void generatePedestal(World world, BlockPos pos, Template template, EnumFacing facing, boolean mirror) {
		if (!hasPedestal)
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
}
