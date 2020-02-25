package com.nuparu.sevendaystomine.events;

import java.util.List;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.IUpgradeable;
import com.nuparu.sevendaystomine.block.repair.BreakData;
import com.nuparu.sevendaystomine.block.repair.BreakSavedData;
import com.nuparu.sevendaystomine.entity.INoiseListener;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.util.DamageSources;
import com.nuparu.sevendaystomine.util.VanillaManager;
import com.nuparu.sevendaystomine.util.VanillaManager.VanillaBlockUpgrade;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class WorldEventHandler {
	/*
	 * CALLED WHEN MOB MANUALLY "DIGS" A BLOCK --> HANDLES THE BREAK LOGIC
	 */
	@SubscribeEvent
	public void mobDig(MobBreakEvent event) {
		IBlockState state = event.state;
		Block block = state.getBlock();
		World world = event.world;
		BlockPos pos = event.pos;
		if (state.getBlock() instanceof IUpgradeable) {
			IUpgradeable upgradeable = (IUpgradeable) state.getBlock();
			world.setBlockState(pos, upgradeable.getPrev(world, pos));
			return;
		} else {
			VanillaBlockUpgrade upgrade = VanillaManager.getVanillaUpgrade(state);
			if (upgrade != null) {
				world.setBlockState(pos, upgrade.getPrev());
				return;
			}
		}
		
		if(!(block instanceof BlockDoublePlant)) {
			block.removedByPlayer(state, world, pos, (EntityPlayer) null, true);
		}
		else {
			world.setBlockToAir(pos);
		}
		world.notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 3);
	}

	/*
	 * CALLED WHEN PLAYER GETS A DROP(S) FROM A BLOCK -- EDITING VANILLA
	 */
	@SubscribeEvent
	public void playerdig(BlockEvent.HarvestDropsEvent event) {

		BlockPos pos = event.getPos();
		World world = event.getWorld();
		IBlockState state = event.getState();
		Block block = state.getBlock();
		BreakSavedData breakSavedData = BreakSavedData.get(world);
		BreakData data = breakSavedData.getBreakData(pos, world.provider.getDimension());
		if (block.getHarvestLevel(state) != 0) {
			if (data != null) {
				event.setDropChance(1.0f - data.getState());
				BreakSavedData.get(world).removeBreakData(pos, world.provider.getDimension());
			}
		} else {
			BreakSavedData.get(world).removeBreakData(pos, world.provider.getDimension());
		}
		if (state.getBlock() instanceof IUpgradeable && ((IUpgradeable) block).getPrev(world, pos) != null) {
			IUpgradeable upgradeable = (IUpgradeable) state.getBlock();
			event.getDrops().clear();
			world.setBlockState(pos, upgradeable.getPrev(world, pos));
			for (ItemStack stack : ((IUpgradeable) upgradeable.getPrev(world, pos).getBlock()).getItems()) {
				int count = (int) (stack.getCount() * Math.random());
				if (count > 0) {
					ItemStack s = stack.copy();
					s.setCount(count);
					event.getDrops().add(s);
				}
			}
			return;
		} else {
			VanillaBlockUpgrade upgrade = VanillaManager.getVanillaUpgrade(state);
			if (upgrade != null && upgrade.getPrev() != null) {
				event.getDrops().clear();
				world.setBlockState(pos, upgrade.getPrev());
				ItemStack[] stacks = (upgrade.getPrev().getBlock() instanceof IUpgradeable)
						? (((IUpgradeable) upgrade.getPrev().getBlock()).getItems())
						: ((VanillaManager.getVanillaUpgrade(upgrade.getPrev()) != null)
								? (VanillaManager.getVanillaUpgrade(upgrade.getPrev()).getItems())
								: (null));
				if (stacks != null) {
					for (ItemStack stack : stacks) {
						int count = (int) (stack.getCount() * Math.random());
						if (count > 0) {
							ItemStack s = stack.copy();
							s.setCount(count);
							event.getDrops().add(s);
						}
					}
				}
				return;
			}
		}
		if ((block instanceof BlockTallGrass && state.getValue(BlockTallGrass.TYPE) == BlockTallGrass.EnumType.GRASS)
				|| (block instanceof BlockDoublePlant
						&& state.getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.GRASS)) {
			if (world.rand.nextInt(3) == 0) {
				event.getDrops().add(new ItemStack(ModItems.PLANT_FIBER, 1 + world.rand.nextInt(1)));
			}
		}
	}

	/*
	 * CALLED WHEN BLOCK IS BROKEN - CAN BE BY A PLAYER
	 */
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void onBlockBreakEvent(BlockEvent.BreakEvent event) {
		if (event.getPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
			World world = event.getWorld();
			if (player.interactionManager.survivalOrAdventure()) {
				if (player.getHeldItemMainhand() == null) {
					if (event.getState().getBlock().getMaterial(event.getState()) == Material.GLASS) {
						if (event.getWorld().rand.nextInt(5) == 0) {
							player.attackEntityFrom(DamageSources.sharpGlass, 2.0F);

						}
					}
				}
			}
			BreakSavedData.get(world).removeBreakData(event.getPos(), world.provider.getDimension());
		}
	}

	/*
	 * CALLED ON WORLD LOAD - HANDLES INIIAL LOADING OF WORLD SAVED DATA
	 */
	@SubscribeEvent
	public void loadWorld(WorldEvent.Load event) {
		if (!event.getWorld().isRemote) {
			// BREAK SAVED DATA
			BreakSavedData break_data = ((BreakSavedData) event.getWorld().getPerWorldStorage()
					.getOrLoadData(BreakSavedData.class, BreakSavedData.DATA_NAME));
			if (break_data == null) {
				SevenDaysToMine.breakSavedData = new BreakSavedData();
				event.getWorld().getPerWorldStorage().setData(BreakSavedData.DATA_NAME, SevenDaysToMine.breakSavedData);
			} else {
				SevenDaysToMine.breakSavedData = break_data;
			}

		}
	}

	/*
	 * Syncs block damage
	 */
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {

		if (event.player instanceof EntityPlayerMP && !event.player.getEntityWorld().isRemote) {
			EntityPlayerMP player = (EntityPlayerMP) event.player;
			if (SevenDaysToMine.breakSavedData == null)
				return;
			SevenDaysToMine.breakSavedData.sync(player);
		}

	}

	/*
	 * Replaces vanilla torches with modded ones
	 */
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void replaceTorchs(PopulateChunkEvent.Post event) {

		World world = event.getWorld();
		Chunk chunk = world.getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
		Block fromBlock = net.minecraft.init.Blocks.TORCH;
		Block toBlock = ModBlocks.TORCH_LIT;

		int i = chunk.x * 16;
		int j = chunk.z * 16;
		for (int x = 0; x < 16; ++x) {
			for (int y = 0; y < 256; ++y) {
				for (int z = 0; z < 16; ++z) {
					BlockPos pos = new BlockPos(x + i, y, z + j);
					IBlockState oldState = world.getBlockState(pos);
					Block oldBlock = oldState.getBlock();
					if (oldBlock == fromBlock) {
						world.setBlockState(pos, toBlock.getDefaultState().withProperty(BlockTorch.FACING,
								oldState.getValue(BlockTorch.FACING)));
						chunk.markDirty();
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlaySoundAtEntity(PlaySoundAtEntityEvent event) {
		Entity entity = event.getEntity();
		if (entity != null) {
			if (!(entity instanceof EntityMob)) {
				float range = event.getVolume() * 50f;
				AxisAlignedBB aabb = new AxisAlignedBB(entity.posX-range,entity.posY-(range/5),entity.posZ-range,entity.posX+range,entity.posY+(range/5),entity.posZ+range);
				List<Entity> entities = entity.world.getEntitiesWithinAABB(Entity.class, aabb);
				for(Entity e : entities) {
					if(e instanceof INoiseListener) {
						INoiseListener noiseListener = (INoiseListener)e;
					}
				}
			}
		}
	}
	
}