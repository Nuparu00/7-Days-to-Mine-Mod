package nuparu.sevendaystomine.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.*;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.server.ServerLifecycleHooks;
import nuparu.sevendaystomine.capability.BreakData;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.json.IngredientStack;
import nuparu.sevendaystomine.json.upgrade.UpgradeDataManager;
import nuparu.sevendaystomine.json.upgrade.UpgradeEntry;

import java.util.*;

public class Utils {
    public static MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    public static HitResult raytraceEntities(Entity entity, double dst, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {

        Entity pointedEntity = null;

        Vec3 vec3d = entity.getEyePosition(1);

        BlockHitResult r = rayTraceServer(entity, dst, 1,blockMode,fluidMode);
        double d1 = r.getLocation().distanceTo(vec3d);

        Vec3 vec3d1 = entity.getLookAngle();
        Vec3 vec3d2 = vec3d.add(vec3d1.x * dst, vec3d1.y * dst, vec3d1.z * dst);
        List<Entity> list = entity.level.getEntities(entity,
                entity.getBoundingBox().inflate(vec3d1.x * dst, vec3d1.y * dst, vec3d1.z * dst),
                Utils::isRayCastable);

        Vec3 vec3d3 = null;
        double d2 = d1;
        for (int j = 0; j < list.size(); ++j) {
            Entity entity1 = list.get(j);
            AABB axisalignedbb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
            Optional<Vec3> optional = axisalignedbb.clip(vec3d, vec3d2);

            if (axisalignedbb.contains(vec3d)) {
                if (d2 >= 0.0D) {
                    pointedEntity = entity1;
                    vec3d3 = optional.orElse(vec3d);
                    d2 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vector3d1 = optional.get();
                double d3 = vec3d.distanceToSqr(vector3d1);

                if (d3 < d2 || d2 == 0.0D) {
                    if (entity1.getRootVehicle() == entity.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d2 == 0.0D) {
                            pointedEntity = entity1;
                            vec3d3 = vector3d1;
                        }
                    } else {
                        pointedEntity = entity1;
                        vec3d3 = vector3d1;
                        d2 = d3;
                    }
                }
            }
            int z = 0;
        }

        if (pointedEntity != null) {
            return new EntityHitResult(pointedEntity, vec3d3);
        }
        return null;
    }

    public static BlockHitResult rayTraceServer(Entity entity, double blockReachDistance, float partialTicks, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
        Vec3 vec3 = getPositionEyesServer(entity, partialTicks);
        Vec3 vec31 = entity.getLookAngle();
        Vec3 vec32 = vec3.add(vec31.x * blockReachDistance, vec31.y * blockReachDistance,
                vec31.z * blockReachDistance);
        return entity.level.clip(new ClipContext(vec3, vec32, blockMode,
                fluidMode, entity));
    }

    public static Vec3 getPositionEyesServer(Entity entity, float partialTicks) {
        if (partialTicks == 1.0F) {
            return new Vec3(entity.getX(), entity.getY() + (double) entity.getEyeHeight(), entity.getZ());
        } else {
            double d0 = entity.xOld + (entity.getX() - entity.xOld) * (double) partialTicks;
            double d1 = entity.yOld + (entity.getY() - entity.yOld) * (double) partialTicks
                    + (double) entity.getEyeHeight();
            double d2 = entity.zOld + (entity.getZ() - entity.zOld) * (double) partialTicks;
            return new Vec3(d0, d1, d2);
        }
    }

    public static boolean isRayCastable(Entity entity){
        return !(entity.isSpectator()) && entity.getBoundingBox().getSize() > 0;
    }

    public static HashMap<ItemStack, Integer[]> hasIngredients(ArrayList<IngredientStack> ingredientStacks, Player player){
        Inventory inv = player.getInventory();
        NonNullList<ItemStack> items = inv.items;

        ArrayList<IngredientStack> copyStacks = new ArrayList<IngredientStack>();
        for(IngredientStack ingredientStack : ingredientStacks){
            copyStacks.add(ingredientStack.clone());
        }
        
        HashMap<ItemStack,Integer[]> toConsume = new HashMap<>();

        for(int i = 0; i < items.size(); i++){
            ItemStack itemStack = items.get(i);
            int consumed = 0;
            ArrayList<IngredientStack> toRemove = new ArrayList<>();
            for(IngredientStack ingredientStack : copyStacks){
                if(ingredientStack.test(itemStack)){
                    int delta = Math.min(itemStack.getCount() + consumed, ingredientStack.count());
                    ingredientStack.count-=delta;
                    if(ingredientStack.count <= 0){
                        toRemove.add(ingredientStack);
                    }
                    consumed+=delta;
                }
            }
            
            toConsume.put(itemStack,new Integer[]{i,consumed});
            
            copyStacks.removeAll(toRemove);
            
            if(copyStacks.isEmpty()){
                break;
            }
        }

        return copyStacks.isEmpty() ? toConsume : null;
    }

    public static void consumeIngredients(ArrayList<IngredientStack> ingredientStacks, Player player){
        Inventory inv = player.getInventory();
        NonNullList<ItemStack> items = inv.items;

        ArrayList<IngredientStack> copyStacks = new ArrayList<IngredientStack>();
        for(IngredientStack ingredientStack : ingredientStacks){
            copyStacks.add(ingredientStack.clone());
        }

        for(int i = 0; i < items.size(); i++){
            ItemStack itemStack = items.get(i);
            ArrayList<IngredientStack> toRemove = new ArrayList<>();
            for(IngredientStack ingredientStack : copyStacks){
                if(ingredientStack.test(itemStack)){
                    int delta = Math.min(itemStack.getCount(), ingredientStack.count());
                    System.out.println(delta);
                    ingredientStack.count-=delta;
                    if(ingredientStack.count <= 0){
                        toRemove.add(ingredientStack);
                    }
                    itemStack.shrink(delta);
                    if(itemStack.getCount() <= 0){
                        items.set(i,ItemStack.EMPTY);
                        break;
                    }
                }
            }
            copyStacks.removeAll(toRemove);
        }
    }

    public static void consumeIngredients(HashMap<ItemStack, Integer[]> items, Player player){
        Inventory inv = player.getInventory();
        for(Map.Entry<ItemStack, Integer[]> entry : items.entrySet()){
            inv.removeItem(entry.getValue()[0],entry.getValue()[1]);
        }
    }

    public static int getDay(Level world) {
        return Math.round(world.getDayTime() / 24000) + 1;
    }

    public static boolean isBloodmoon(Level world) {
        return ServerConfig.bloodmoonFrequency.get() > 0 && Utils.getDay(world) % ServerConfig.bloodmoonFrequency.get() == 0;
    }

    public static boolean isBloodmoonProper(Level world) {
        return isBloodmoon(world) && !world.isDay();
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
            if(accessor instanceof Level) {
                Level level = (Level)accessor;
                for (IngredientStack stack : upgradeEntry.ingredients) {
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

    public static Entity getEntityByNBTAndResource(ResourceLocation resourceLocation, CompoundTag nbt, Level world) {

        if (nbt != null) {
            return EntityType.create(nbt, world).orElse(null);
        }
        return null;
    }

    public static NonNullList<ItemStack> dropItemHandlerContents(IItemHandler itemHandler, RandomSource random) {
        final NonNullList<ItemStack> drops = NonNullList.create();

        for (int slot = 0; slot < itemHandler.getSlots(); ++slot) {
            while (!itemHandler.getStackInSlot(slot).isEmpty()) {
                final int amount = random.nextInt(21) + 10;

                itemHandler.extractItem(slot, amount, true);
                final ItemStack itemStack = itemHandler.extractItem(slot, amount, false);
                drops.add(itemStack);
            }
        }

        return drops;
    }


    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

}
