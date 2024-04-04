package nuparu.sevendaystomine.crafting.forge;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.tileentity.TileEntityForge;
import nuparu.sevendaystomine.util.ItemUtils;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ForgeRecipeMaterial implements IForgeRecipe{
    private final ResourceLocation name;
    private final ItemStack result;
    private final ItemStack mold;
    private final NonNullList<MaterialStack> ingredients;
    protected final float experience;
    protected final int cookingTime;

    public ForgeRecipeMaterial(ResourceLocation name, ItemStack result, ItemStack mold, NonNullList<MaterialStack> ingredients, float experience, int cookingTime) {
        this.result = result;
        this.mold = mold;
        this.ingredients = ingredients;
        this.experience = experience;
        this.cookingTime = cookingTime;
        this.name = name;
    }

    @Override
    public boolean matches(TileEntityForge forge, @Nonnull World world) {
        if(!ItemStack.areItemsEqualIgnoreDurability(forge.getMoldSlot(),mold)) return false;

        HashMap<EnumMaterial,Fraction> invMap = new HashMap<>();
        int i = 0;
        for(ItemStack stack : forge.getActiveInventory()){
            if(stack.isEmpty()) continue;
            MaterialStack materialStack = ItemUtils.stackToMaterialStack(stack);
            if(materialStack == null) return false;
            Fraction weight = materialStack.weight().asFraction().multiplyBy(Fraction.getFraction(stack.getCount()));
            if(invMap.containsKey(materialStack.material())){
                weight = weight.add(invMap.get(materialStack.material()));
            }
            invMap.put(materialStack.material(),weight);
        }

        if(invMap.size() != ingredients.size()) return false;

        Fraction ratio = null;
        for(MaterialStack materialStack : ingredients){
            EnumMaterial material = materialStack.material();
            if(!invMap.containsKey(material)) return false;
            if(invMap.get(material).compareTo(materialStack.weight().asFraction()) < 0) return false;
            Fraction r = invMap.get(material).divideBy(materialStack.weight().asFraction());
            if(ratio == null){
                ratio = r;
                continue;
            }
            if(!ratio.equals(r)) return false;
        }

        return true;
    }

    @Override
    @Nullable
    public Fraction getRatio(TileEntityForge forge){
        HashMap<EnumMaterial,Fraction> invMap = new HashMap<>();
        for(ItemStack stack : forge.getActiveInventory()){
            if(stack.isEmpty()) continue;
            MaterialStack materialStack = ItemUtils.stackToMaterialStack(stack);
            if(materialStack == null) return null;
            Fraction weight = materialStack.weight().asFraction().multiplyBy(Fraction.getFraction(stack.getCount()));
            if(invMap.containsKey(materialStack.material())){
                weight = weight.add(invMap.get(materialStack.material()));
            }
            invMap.put(materialStack.material(),weight);
        }

        if(invMap.size() != ingredients.size()) return null;

        Fraction ratio = null;
        for(MaterialStack materialStack : ingredients){
            EnumMaterial material = materialStack.material();
            if(!invMap.containsKey(material)) return null;
            if(invMap.get(material).compareTo(materialStack.weight().asFraction()) < 0) return null;
            Fraction r = invMap.get(material).divideBy(materialStack.weight().asFraction());
            if(ratio == null){
                ratio = r;
                continue;
            }
            if(!ratio.equals(r)) return null;
        }

        return ratio;
    }

    public @Nonnull ItemStack assemble(@Nonnull TileEntityForge forge) {

        Fraction ratio = this.getRatio(forge);
        if(ratio == null) return ItemStack.EMPTY;

        int wholeRatio = ratio.getProperWhole();
        if(wholeRatio <= 0) return ItemStack.EMPTY;
        wholeRatio = 1;
        ItemStack stack = this.result.copy();
        stack.setCount(stack.getCount()*wholeRatio);
        return stack;
    }

    @Override
    public ResourceLocation name() {
        return this.name;
    }

    @Override
    public boolean consume(TileEntityForge forge){
        Fraction ratio = this.getRatio(forge);
        if(ratio == null) return true;

        int wholeRatio = ratio.getProperWhole();
        if(wholeRatio <= 0) return true;
        wholeRatio = 1;

        Fraction wholeRatioAsFraction = Fraction.getFraction(wholeRatio);

        ArrayList<ItemStack> rest = new ArrayList<>();

        for(MaterialStack materialStack : ingredients){
            EnumMaterial material = materialStack.material();
            Fraction weightToConsume = materialStack.weight().asFraction().multiplyBy(wholeRatioAsFraction);
            ItemStack stack = consumeWeight(forge,material,weightToConsume);
            if(!stack.isEmpty()) {
                rest.add(stack);
            }
        }
        for(ItemStack stack : rest){
            for(int i = TileEntityForge.EnumSlots.INPUT_SLOT.ordinal();i < TileEntityForge.EnumSlots.INPUT_SLOT4.ordinal(); i++){
                ItemStack slotStack = forge.getInventory().getStackInSlot(i);
                if(slotStack.isEmpty()){
                    int countToAdd = Math.min(Math.min(stack.getCount(),forge.getMaxStackSize()),stack.getMaxStackSize());
                    ItemStack stackToAdd = stack.copy();
                    stackToAdd.setCount(countToAdd);
                    forge.getInventory().setStackInSlot(i,stackToAdd);
                    stack.shrink(countToAdd);
                }
                else if(ItemStack.areItemsEqual(stack,slotStack) && slotStack.getCount() < slotStack.getMaxStackSize()){
                    int delta = Math.min(slotStack.getMaxStackSize() - slotStack.getCount(), stack.getCount());
                    slotStack.grow(delta);
                    stack.shrink(delta);
                }

                if(stack.isEmpty()) break;
            }
            if (!stack.isEmpty()) {
                InventoryHelper.spawnItemStack(forge.getWorld(), forge.getPos().getX() + 0.5, forge.getPos().getY() + 1, forge.getPos().getZ() + 0.5, stack);
            }
        }

        return true;
    }


    public ItemStack consumeWeight(TileEntityForge forge, EnumMaterial material, Fraction weightToConsume){
        if(weightToConsume.compareTo(Fraction.ZERO) <= 0) return ItemStack.EMPTY;
        List<ItemStack> inv = forge.getActiveInventory();
        for(int i = 0; i < inv.size(); i++){
            ItemStack stack = inv.get(i);
            MaterialStack materialStack = ItemUtils.stackToMaterialStack(stack);
            if(materialStack == null) continue;
            if(materialStack.material()!=material) continue;
            Fraction weight = materialStack.weight().asFraction().multiplyBy(Fraction.getFraction(stack.getCount()));

            Fraction currentConsume = weight.compareTo(weightToConsume) < 0 ? weight : weightToConsume;
            int itemCount = currentConsume.divideBy(materialStack.weight().asFraction()).getProperWhole();
            Fraction consumedWeight = materialStack.weight().asFraction().multiplyBy(Fraction.getFraction(itemCount));

            weightToConsume= weightToConsume.subtract(consumedWeight);

            stack.shrink(itemCount);
            if(stack.isEmpty()){
                forge.getInventory().setStackInSlot(i,ItemStack.EMPTY);
                inv.set(i,ItemStack.EMPTY);
            }

            if(weightToConsume.compareTo(Fraction.ZERO) <= 0){
                break;
            }
        }

        //Gets leftover stack
        /*if(weightToConsume.compareTo(Fraction.ZERO) < 0){
            ScrapDataManager.ScrapEntry entry = ScrapDataManager.INSTANCE.get(material);
            return new ItemStack(entry.item, (int) Math.ceil(-(double)weightToConsume/entry.weight));
        }*/

        return ItemStack.EMPTY;
    }
    public ItemStack getMoldItem() {
        return this.mold.copy();
    }

    @Override
    public List<ItemStack> getJeiIngredients() {
        return ingredients.stream().map(materialStack -> new ItemStack(ScrapDataManager.getInstance().getScrapResult(materialStack.material()).item(), materialStack.weight().asFraction().intValue())).collect(Collectors.toList());
    }

    @Override
    public ItemStack getJeiOutput() {
        return result;
    }

    public NonNullList<MaterialStack> getMaterialIngredients() {
        return this.ingredients;
    }

    public float getExperience() {
        return this.experience;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }
}
