package nuparu.sevendaystomine.block;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public interface ISalvageable {

	default List<ItemStack> getItems(World world, BlockPos pos, IBlockState oldState, EntityPlayer player){
		LootTable lootTable = world.getLootTableManager().getLootTableFromLocation(getSalvageLootTable());
		LootContext context = new LootContext.Builder((WorldServer)world).withPlayer(player).withLuck(player.getLuck()).build();

		return lootTable.generateLootForPools(world.rand, context);
	}

	ResourceLocation getSalvageLootTable();

	default SoundEvent getSalvageSound(){
		return SoundEvents.BLOCK_ANVIL_LAND;
	}
	
	default float getUpgradeRate(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		return 5;
	}

	default void onSalvage(World world, BlockPos pos, IBlockState oldState){
		world.destroyBlock(pos, false);
	}

}