package nuparu.sevendaystomine.util;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.items.IItemHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.json.scrap.ScrapDataManager;
import nuparu.sevendaystomine.json.scrap.ScrapEntry;
import nuparu.sevendaystomine.json.upgrader.UpgraderToolDataManager;
import nuparu.sevendaystomine.world.item.EnumMaterial;

import javax.annotation.Nullable;
import java.util.*;


public class ItemUtils {

    public static final UUID ATTACK_DAMAGE_BOOST = UUID.fromString("36f89e52-c289-11ec-9d64-0242ac120002");
    public static final UUID ATTACK_SPEED_BOOST = UUID.fromString("43f395bc-c289-11ec-9d64-0242ac120002");
    public static Item getScrapResult(EnumMaterial mat) {
        ScrapEntry scrapEntry = ScrapDataManager.INSTANCE.getScrapResult(mat);
        if(scrapEntry != null){
            return scrapEntry.item;
        }
        return null;
    }

/*
    public static int getQualityFromStack(ItemStack stack) {
        if(stack == null || stack.isEmpty()) return 0;
        IQualityStack qualityStack = CapabilityHelper.getQualityStack(stack);
        if(qualityStack != null && qualityStack.hasQuality()){
            return qualityStack.getQuality();
        }
        return 0;
    }

    public static boolean isItemSuitableForQuality(Object object){
        return object instanceof TieredItem || object instanceof ArmorItem || object instanceof ProjectileWeaponItem;
    }


    public static ItemStack setQualityForStack(ItemStack stack, int quality) {
        if(stack == null || stack.isEmpty()) return stack;
        IQualityStack qualityStack = CapabilityHelper.getQualityStack(stack);
        if(qualityStack != null){
            qualityStack.setQuality(quality);
        }
        return stack;
    }*/

    public static double getUpgradeAmount(ItemStack stack){
        return UpgraderToolDataManager.INSTANCE.getItemUpgradePower(stack.getItem());
    }

    public static double getSalvageAmount(ItemStack stack){
        return UpgraderToolDataManager.INSTANCE.getItemSalvagePower(stack.getItem());
    }
    public static void eraseUpgraderData(ItemStack stack){
        stack.getOrCreateTag().remove("7D2M_UpgradeProgress");
        stack.getOrCreateTag().remove("7D2M_UpgradePos");
        stack.getOrCreateTag().remove("7D2M_UpgradeDim");
    }

    public static void fill(LootTable lootTable, IItemHandler inventory, LootContext lootContext) {
        ObjectArrayList<ItemStack> list = lootTable.getRandomItems(lootContext);
        RandomSource random = lootContext.getRandom();
        List<Integer> list1 = getAvailableSlots(inventory, random);
        shuffleAndSplitItems(list, list1.size(), random);
        for(ItemStack itemstack : list) {
            if (list1.isEmpty()) {
                SevenDaysToMine.LOGGER.warn("Tried to over-fill a container");
                return;
            }

            if (itemstack.isEmpty()) {
                inventory.insertItem(list1.remove(list1.size() - 1), ItemStack.EMPTY,false);
            } else {
                inventory.insertItem(list1.remove(list1.size() - 1), itemstack,false);
            }
        }

    }

    private static List<Integer> getAvailableSlots(IItemHandler p_230920_, RandomSource p_230921_) {
        ObjectArrayList<Integer> objectarraylist = new ObjectArrayList<>();

        for(int i = 0; i < p_230920_.getSlots(); ++i) {
            if (p_230920_.getStackInSlot(i).isEmpty()) {
                objectarraylist.add(i);
            }
        }

        Util.shuffle(objectarraylist, p_230921_);
        return objectarraylist;
    }

    private static void shuffleAndSplitItems(ObjectArrayList<ItemStack> p_230925_, int p_230926_, RandomSource p_230927_) {
        List<ItemStack> list = Lists.newArrayList();
        Iterator<ItemStack> iterator = p_230925_.iterator();

        while(iterator.hasNext()) {
            ItemStack itemstack = iterator.next();
            if (itemstack.isEmpty()) {
                iterator.remove();
            } else if (itemstack.getCount() > 1) {
                list.add(itemstack);
                iterator.remove();
            }
        }

        while(p_230926_ - p_230925_.size() - list.size() > 0 && !list.isEmpty()) {
            ItemStack itemstack2 = list.remove(Mth.nextInt(p_230927_, 0, list.size() - 1));
            int i = Mth.nextInt(p_230927_, 1, itemstack2.getCount() / 2);
            ItemStack itemstack1 = itemstack2.split(i);
            if (itemstack2.getCount() > 1 && p_230927_.nextBoolean()) {
                list.add(itemstack2);
            } else {
                p_230925_.add(itemstack2);
            }

            if (itemstack1.getCount() > 1 && p_230927_.nextBoolean()) {
                list.add(itemstack1);
            } else {
                p_230925_.add(itemstack1);
            }
        }

        p_230925_.addAll(list);
        Util.shuffle(p_230925_, p_230927_);
    }

    @Nullable
    public static Recipe getRecipesForStack(ItemStack stack, MinecraftServer server) {
        for (Recipe irecipe : server.getRecipeManager().getRecipes()) {
            if (ItemStack.isSameIgnoreDurability(stack, irecipe.getResultItem())) {
                return irecipe;
            }
        }

        return null;
    }

    public static boolean canHaveQuality(Item item){
        return item instanceof TieredItem || item instanceof ProjectileWeaponItem || item instanceof ArmorItem || item instanceof TridentItem || item == ModItems.CAR_BATTERY.get()  || item == ModItems.SMALL_ENGINE.get()  || item == ModItems.MINIBIKE_HANDLES.get() || item == ModItems.MINIBIKE_SEAT.get();
    }
}
