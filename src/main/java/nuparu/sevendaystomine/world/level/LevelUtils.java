package nuparu.sevendaystomine.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.fluids.IFluidBlock;
import nuparu.sevendaystomine.capability.BreakData;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.json.IngredientStack;
import nuparu.sevendaystomine.json.upgrade.UpgradeDataManager;
import nuparu.sevendaystomine.json.upgrade.UpgradeEntry;

public class LevelUtils {
    /**
     * @param world The Level to get day from
     * @return The current day of the given level, starting from 1
     */
    public static int getDay(Level world) {
        return Math.round(world.getDayTime() / 24000f) + 1;
    }

    /**
     * Sets the current day of the given level. Days are numbered from 1 (as opposed to numbering from 0 used by the debug screen)
     * @param level Level to change the day in
     * @param day The day
     */
    public static void setDay(ServerLevel level, int day){
        level.setDayTime(Math.max(0, day - 1) * 24000L);
    }

    public static boolean isBloodmoon(Level world) {
        return ServerConfig.bloodmoonFrequency.get() > 0 && getDay(world) % ServerConfig.bloodmoonFrequency.get() == 0;
    }

    public static boolean isBloodmoonProper(Level world) {
        return isBloodmoon(world) && !world.isDay();
    }

    public static int getBloomoonsHappened(Level level){
        return ServerConfig.bloodmoonFrequency.get() > 0 ? (int) Math.floor(getDay(level) / (float) ServerConfig.bloodmoonFrequency.get()) : 0;
    }

    // returns true if the block has been destroyed
    public static boolean damageBlock(ServerLevel world, BlockPos pos, float damage, boolean breakBlock) {
        return damageBlock(world, pos, damage, breakBlock, true);
    }

    // returns true if the block has been destroyed
    public static boolean damageBlock(ServerLevel world, BlockPos pos, float damage, boolean breakBlock,
                                      boolean sound) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        float hardness = state.getDestroySpeed(world, pos);
        if (hardness <= 0) {
            return false;
        }
        if (state.isAir()) {
            return false;
        }
        if (block instanceof IFluidBlock) {
            return false;
        }
        if (hardness == 0) {
            world.destroyBlock(pos, false);
            return true;
        }

        LevelChunk chunk = world.getChunkAt(pos);
        IChunkData chunkData = CapabilityHelper.getChunkData(chunk);

        if (chunkData == null) {
            return false;
        }
        SoundType soundType = block.getSoundType(state, world, pos, null);
        if (sound) {
            world.playSound(null, pos, soundType.getHitSound(), SoundSource.NEUTRAL,
                    (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
        }
        chunkData.addBreakData(pos, damage / hardness);
        BreakData breakData = chunkData.getBreakData(pos);
        if (breakData != null && breakData.getState() >= 1f && breakBlock) {
            chunkData.removeBreakData(pos);
            UpgradeEntry downgradeEntry = UpgradeDataManager.INSTANCE.getDowngradeFromEntry(state);

            world.destroyBlock(pos, false);
            downgradeBlock(world,pos,state);
            return true;
        }

        return false;
    }

    public static boolean downgradeBlock(LevelAccessor accessor, BlockPos pos, BlockState state){
        UpgradeEntry upgradeEntry = UpgradeDataManager.INSTANCE.getDowngradeFromEntry(state);
        if(upgradeEntry != null){
            if(accessor instanceof Level level) {
                for (IngredientStack stack : upgradeEntry.ingredients()) {
                    int count = accessor.getRandom().nextInt(stack.count() + 1);
                    if (count == 0) continue;
                    Ingredient ingredient = stack.ingredient();
                    ItemStack itemStack = ingredient.getItems()[accessor.getRandom().nextInt(ingredient.getItems().length)];
                    itemStack = itemStack.copy();
                    itemStack.setCount(count);
                    Containers.dropItemStack(level,pos.getX()+0.5,pos.getY()+0.5,pos.getZ() + 0.5,itemStack);
                }
            }
            accessor.setBlock(pos,upgradeEntry.getDowngradeTo().asBlockState(upgradeEntry,state),2);
            return true;
        }
        return false;
    }

    public static boolean isThunderingAt(Level level, BlockPos p_46759_) {
        if (!level.isThundering()) {
            return false;
        } else if (!level.canSeeSky(p_46759_)) {
            return false;
        } else if (level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, p_46759_).getY() > p_46759_.getY()) {
            return false;
        } else {
            Biome biome = level.getBiome(p_46759_).value();
            return biome.getPrecipitation() == Biome.Precipitation.RAIN && biome.warmEnoughToRain(p_46759_);
        }
    }
}
