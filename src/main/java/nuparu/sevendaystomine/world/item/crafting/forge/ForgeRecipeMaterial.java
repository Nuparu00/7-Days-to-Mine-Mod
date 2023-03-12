package nuparu.sevendaystomine.world.item.crafting.forge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.init.ModRecipeTypes;
import nuparu.sevendaystomine.json.scrap.ScrapDataManager;
import nuparu.sevendaystomine.world.item.EnumMaterial;
import nuparu.sevendaystomine.world.level.block.entity.ForgeBlockEntity;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ForgeRecipeMaterial implements IForgeRecipe<ForgeBlockEntity> {
    private final ResourceLocation id;
    private final String group;
    private final ItemStack result;
    private final ItemStack mold;
    private final NonNullList<MaterialStack> ingredients;
    protected final float experience;
    protected final int cookingTime;

    public ForgeRecipeMaterial(ResourceLocation resourceLocation, String group, ItemStack result, ItemStack mold, NonNullList<MaterialStack> ingredients, float experience, int cookingTime) {
        this.id = resourceLocation;
        this.group = group;
        this.result = result;
        this.mold = mold;
        this.ingredients = ingredients;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(ForgeBlockEntity forge, @NotNull Level world) {
        if(!ItemStack.isSameIgnoreDurability(forge.getMoldSlot(),mold)) return false;

        HashMap<EnumMaterial,Fraction> invMap = new HashMap<>();
        int i = 0;
        for(ItemStack stack : forge.getActiveInventory()){
            if(stack.isEmpty()) continue;
            if(!ScrapDataManager.INSTANCE.hasEntry(stack)) return false;
            MaterialStack materialStack = ScrapDataManager.INSTANCE.getEntry(stack).toMaterialStack();
            Fraction weight = materialStack.weight.asFraction().multiplyBy(Fraction.getFraction(stack.getCount()));
            if(invMap.containsKey(materialStack.material)){
                weight = weight.add(invMap.get(materialStack.material));
            }
            invMap.put(materialStack.material,weight);
        }

        if(invMap.size() != ingredients.size()) return false;

        Fraction ratio = null;
        for(MaterialStack materialStack : ingredients){
            EnumMaterial material = materialStack.material;
            if(!invMap.containsKey(material)) return false;
            if(invMap.get(material).compareTo(materialStack.weight.asFraction()) < 0) return false;
            Fraction r = invMap.get(material).divideBy(materialStack.weight.asFraction());
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
    public Fraction getRatio(ForgeBlockEntity forge){
        HashMap<EnumMaterial,Fraction> invMap = new HashMap<>();
        int i = 0;
        for(ItemStack stack : forge.getActiveInventory()){
            if(stack.isEmpty()) continue;
            if(!ScrapDataManager.INSTANCE.hasEntry(stack)) return null;
            MaterialStack materialStack = ScrapDataManager.INSTANCE.getEntry(stack).toMaterialStack();
            Fraction weight = materialStack.weight.asFraction().multiplyBy(Fraction.getFraction(stack.getCount()));
            if(invMap.containsKey(materialStack.material)){
                weight = weight.add(invMap.get(materialStack.material));
            }
            invMap.put(materialStack.material,weight);
        }

        if(invMap.size() != ingredients.size()) return null;

        Fraction ratio = null;
        for(MaterialStack materialStack : ingredients){
            EnumMaterial material = materialStack.material;
            if(!invMap.containsKey(material)) return null;
            if(invMap.get(material).compareTo(materialStack.weight.asFraction()) < 0) return null;
            Fraction r = invMap.get(material).divideBy(materialStack.weight.asFraction());
            if(ratio == null){
                ratio = r;
                continue;
            }
            if(!ratio.equals(r)) return null;
        }

        return ratio;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull ForgeBlockEntity forge) {

        Fraction ratio = this.getRatio(forge);
        if(ratio == null) return ItemStack.EMPTY;

        int wholeRatio = ratio.getProperWhole();
        if(wholeRatio <= 0) return ItemStack.EMPTY;
        ItemStack stack = this.result.copy();
        stack.setCount(stack.getCount()*wholeRatio);
        return stack;
    }

    @Override
    public boolean consume(ForgeBlockEntity forge){
        Fraction ratio = this.getRatio(forge);
        if(ratio == null) return true;

        int wholeRatio = ratio.getProperWhole();
        if(wholeRatio <= 0) return true;

        Fraction wholeRatioAsFraction = Fraction.getFraction(ratio.getProperWhole());

        ArrayList<ItemStack> rest = new ArrayList<>();

        for(MaterialStack materialStack : ingredients){
            EnumMaterial material = materialStack.material;
            Fraction weightToConsume = materialStack.weight.asFraction().multiplyBy(wholeRatioAsFraction);
            ItemStack stack = consumeWeight(forge,material,weightToConsume);
            if(!stack.isEmpty()) {
                rest.add(stack);
            }
        }
        for(ItemStack stack : rest){
            for(int i = ForgeBlockEntity.EnumSlots.INPUT_SLOT.ordinal();i < ForgeBlockEntity.EnumSlots.INPUT_SLOT4.ordinal(); i++){
                ItemStack slotStack = forge.getInventory().getStackInSlot(i);
                if(slotStack.isEmpty()){
                    int countToAdd = Math.min(Math.min(stack.getCount(),forge.getMaxStackSize()),stack.getMaxStackSize());
                    ItemStack stackToAdd = stack.copy();
                    stackToAdd.setCount(countToAdd);
                    forge.getInventory().setStackInSlot(i,stackToAdd);
                    stack.shrink(countToAdd);
                }
                else if(ItemStack.isSame(stack,slotStack) && slotStack.getCount() < slotStack.getMaxStackSize()){
                    int delta = Math.min(slotStack.getMaxStackSize() - slotStack.getCount(), stack.getCount());
                    slotStack.grow(delta);
                    stack.shrink(delta);
                }

                if(stack.isEmpty()) break;
            }
            if (!stack.isEmpty()) {
                Containers.dropItemStack(forge.getLevel(), forge.getBlockPos().getX() + 0.5, forge.getBlockPos().getY() + 1, forge.getBlockPos().getZ() + 0.5, stack);
            }
        }

        return true;
    }


    public ItemStack consumeWeight(ForgeBlockEntity forge, EnumMaterial material, Fraction weightToConsume){
        if(weightToConsume.compareTo(Fraction.ZERO) <= 0) return ItemStack.EMPTY;
        List<ItemStack> inv = forge.getActiveInventory();
        for(int i = 0; i < inv.size(); i++){
            ItemStack stack = inv.get(i);
            if(!ScrapDataManager.INSTANCE.hasEntry(stack)) continue;
            MaterialStack materialStack = ScrapDataManager.INSTANCE.getEntry(stack).toMaterialStack();
            if(materialStack.material!=material) continue;
            Fraction weight = materialStack.weight.asFraction().multiplyBy(Fraction.getFraction(stack.getCount()));

            Fraction currentConsume = weight.compareTo(weightToConsume) < 0 ? weight : weightToConsume;
            int itemCount = currentConsume.divideBy(materialStack.weight.asFraction()).getProperWhole();
            Fraction consumedWeight = materialStack.weight.asFraction().multiplyBy(Fraction.getFraction(itemCount));

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


    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return i * i1 >= this.ingredients.size();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull String getGroup() {
        return this.group;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return this.result.copy();
    }

    public ItemStack getMoldItem() {
        return this.mold.copy();
    }
    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }

    public NonNullList<MaterialStack> getMaterialIngredients() {
        return this.ingredients;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.FORGE_MATERIAL.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.FORGE_RECIPE_TYPE.get();
    }

    public float getExperience() {
        return this.experience;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    public static class Factory implements RecipeSerializer<ForgeRecipeMaterial> {
        int defaultCookingTime = 600;

        @Override
        public @NotNull ForgeRecipeMaterial fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            String s = GsonHelper.getAsString(json, "group", "");
            NonNullList<MaterialStack> nonnulllist = materialsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"),recipeId);
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 2 * 2) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 2 * 2);
            } else {
                ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
                ItemStack mold = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "mold"));
                float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
                int cookingTime = GsonHelper.getAsInt(json, "cookingtime", this.defaultCookingTime);
                return new ForgeRecipeMaterial(recipeId, s, itemstack ,mold, nonnulllist,experience,cookingTime);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray p_199568_0_) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < p_199568_0_.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(p_199568_0_.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        private static NonNullList<MaterialStack> materialsFromJson(JsonArray p_199568_0_, ResourceLocation recipeId) {
            NonNullList<MaterialStack> nonnulllist = NonNullList.create();

            for(int i = 0; i < p_199568_0_.size(); ++i) {
                JsonElement element = p_199568_0_.get(i);
                JsonObject object = element.getAsJsonObject();
                if(!object.has("material")){
                    throw new JsonParseException("No materialstack material for forge material recipe " + recipeId.toString());
                }
                EnumMaterial material = EnumMaterial.byName(object.get("material").getAsString());
                Number weightValue = Fraction.ONE;
                if(object.has("weight")){
                    String weight = object.get("weight").getAsString();
                    if(weight.contains("/")){
                        weightValue = Fraction.getFraction(weight);
                    }
                    else{
                        weightValue = Fraction.getFraction(object.get("weight").getAsDouble());
                    }
                }
                nonnulllist.add(new MaterialStack(material,weightValue));
            }

            return nonnulllist;
        }

        @Nullable
        @Override
        public ForgeRecipeMaterial fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String s = buffer.readUtf(32767);
            int i = buffer.readVarInt();
            NonNullList<MaterialStack> nonnulllist = NonNullList.withSize(i, MaterialStack.EMPTY);

            nonnulllist.replaceAll(ignored -> MaterialStack.fromNetwork(buffer));

            ItemStack itemstack = buffer.readItem();
            ItemStack mold = buffer.readItem();
            float experience = buffer.readFloat();
            int cookingTime = buffer.readVarInt();
            return new ForgeRecipeMaterial(recipeId, s, itemstack, mold, nonnulllist,experience,cookingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ForgeRecipeMaterial recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(recipe.ingredients.size());
            Iterator var3 = recipe.ingredients.iterator();

            while(var3.hasNext()) {
                MaterialStack ingredient = (MaterialStack)var3.next();
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.result);
            buffer.writeItem(recipe.mold);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookingTime);
        }
    }
}
