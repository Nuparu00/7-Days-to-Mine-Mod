package nuparu.sevendaystomine.world.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import nuparu.sevendaystomine.json.IngredientStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InventoryUtils {
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

    public static boolean hasItemStack(Player player, ItemStack itemStack) {
        int count = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.getItem() == itemStack.getItem()) {
                count += stack.getCount();
            }
        }
        return count >= itemStack.getCount();
    }

    public static void removeItemStack(Inventory inv, ItemStack itemStack) {
        int count = itemStack.getCount();
        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
            ItemStack stack = inv.getItem(slot);
            if (stack.getItem() == itemStack.getItem()) {
                int decrease = Math.min(count, stack.getCount());
                inv.removeItem(slot, decrease);
                count -= decrease;
                if (count <= 0) {
                    break;
                }
            }
        }
    }

    public static int getItemCount(Inventory inv, Item item) {
        int count = 0;
        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
            ItemStack stack = inv.getItem(slot);
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static void clearMatchingItems(Container inv, Item item, int count) {
        for (int i = 0; (i < inv.getMaxStackSize() && count > 0); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem() == item) {
                int toRemove = Math.min(stack.getCount(), count);
                stack.shrink(toRemove);
                if (stack.getCount() <= 0) {
                    inv.setItem(i, ItemStack.EMPTY);
                }
                count -= toRemove;
            }
        }
    }
}
