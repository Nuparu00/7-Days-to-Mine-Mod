package nuparu.sevendaystomine.world.level.block.entity;

import com.google.common.collect.Lists;
import com.sun.jdi.InvalidTypeException;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.init.ModRecipeTypes;
import nuparu.sevendaystomine.world.inventory.ItemHandlerNameable;
import nuparu.sevendaystomine.world.inventory.block.ContainerForge;
import nuparu.sevendaystomine.world.inventory.block.ContainerGrill;
import nuparu.sevendaystomine.world.item.crafting.cooking.ICookingRecipe;
import nuparu.sevendaystomine.world.level.block.CookwareBlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GrillBlockEntity extends ItemHandlerBlockEntity implements Container {
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

    public int cookTime;
    public int totalCookTime;

    ICookingRecipe<GrillBlockEntity> currentRecipe;


    public enum EnumSlots {
        INPUT_SLOT, INPUT_SLOT2, INPUT_SLOT3, INPUT_SLOT4, OUTPUT_SLOT
    }

    @Override
    protected ItemHandlerNameable createInventory() {
        return new ItemHandlerNameable(EnumSlots.values().length, Component.translatable("container." + ForgeRegistries.BLOCKS.getKey(block).getPath()));
    }

    public final ContainerData dataAccess = new ContainerData() {
        public int get(int p_221476_1_) {
            switch (p_221476_1_) {
                case 0:
                    return GrillBlockEntity.this.cookTime;
                case 1:
                    return GrillBlockEntity.this.totalCookTime;
                default:
                    return 0;
            }
        }

        public void set(int p_221477_1_, int p_221477_2_) {
            switch (p_221477_1_) {
                case 0:
                    GrillBlockEntity.this.cookTime = p_221477_2_;
                    break;
                case 1:
                    GrillBlockEntity.this.totalCookTime = p_221477_2_;
            }

        }

        public int getCount() {
            return 2;
        }
    };

    CookwareBlock block;

    public GrillBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COOKING_GRILL.get(), pos, state);
        this.block = (CookwareBlock) state.getBlock();
    }

    public boolean canPlayerAccessInventory(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this)
            return false;
        final double X_CENTRE_OFFSET = 0.5;
        final double Y_CENTRE_OFFSET = 0.5;
        final double Z_CENTRE_OFFSET = 0.5;
        final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
        return player.distanceToSqr(worldPosition.getX() + X_CENTRE_OFFSET, worldPosition.getY() + Y_CENTRE_OFFSET,
                worldPosition.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }


    public void tick() {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (!this.level.isClientSide()) {
            if (this.isBurning() && !isInputEmpty()) {
                if (this.totalCookTime == 0) {
                    this.totalCookTime = this.getCookTime(null);
                }
                if (this.isBurning() && this.canSmelt()) {
                    ++this.cookTime;
                    if (this.cookTime >= this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(null);
                        this.smeltItem();
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = Mth.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (flag != this.isBurning()) {
                flag1 = true;
            }
        }


        if (flag1) {
            this.setChanged();
        }

    }

    private boolean canSmelt() {
        ICookingRecipe<GrillBlockEntity> irecipe = this.level.getRecipeManager().getRecipeFor(ModRecipeTypes.COOKING_RECIPE_TYPE.get(), this, this.level).orElse(null);

        if (irecipe != null) {
            this.currentRecipe = irecipe;
            return true;
        }
        return false;
    }

    public void smeltItem() {
        if (currentRecipe != null) {
            ItemStack result = currentRecipe.assemble(this);
            ItemStack currentOutput = getOutputSlot();
            if (currentOutput.isEmpty()) {
                this.getInventory().setStackInSlot(GrillBlockEntity.EnumSlots.OUTPUT_SLOT.ordinal(), result);
            } else if (ItemStack.isSame(currentOutput, result)
                    && currentOutput.getCount() + result.getCount() <= Math.min(
                    getOutputSlot().getMaxStackSize(),
                    this.getInventory().getStackInSlot(GrillBlockEntity.EnumSlots.OUTPUT_SLOT.ordinal()).getMaxStackSize())) {
                currentOutput.grow(result.getCount());
                this.getInventory().setStackInSlot(GrillBlockEntity.EnumSlots.OUTPUT_SLOT.ordinal(), currentOutput);
            }
            ResourceLocation resourcelocation = currentRecipe.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
            consumeInput();
        }
    }

    public void consumeInput() {
        if (currentRecipe.consume(this)) return;
        //Generic consume implementation - move to ForgeRecipeShapeless later
        for (int i = 0; i < 4; i++) {
            ItemStack stack = this.getInventory().getStackInSlot(i);
            stack.shrink(1);
            if (stack.isEmpty()) {
                getInventory().setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    public boolean isInputEmpty() {
        for (int i = EnumSlots.INPUT_SLOT.ordinal(); i < EnumSlots.INPUT_SLOT4.ordinal(); i++) {
            if (!this.getInventory().getStackInSlot(i).isEmpty())
                return false;
        }
        return true;
    }

    public ItemStack getOutputSlot() {
        return this.getInventory().getStackInSlot(EnumSlots.OUTPUT_SLOT.ordinal());
    }

    public ItemStack getIntputSlot(int i) {
        return this.getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal() + i);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);

        this.cookTime = compound.getInt("CookTime");
        this.totalCookTime = compound.getInt("CookTimeTotal");

        CompoundTag CompoundTag = compound.getCompound("RecipesUsed");

        for (String s : CompoundTag.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), CompoundTag.getInt(s));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);

        compound.putInt("CookTime", (short) this.cookTime);
        compound.putInt("CookTimeTotal", (short) this.totalCookTime);

        CompoundTag CompoundTag = new CompoundTag();
        this.recipesUsed.forEach((p_235643_1_, p_235643_2_) -> CompoundTag.putInt(p_235643_1_.toString(), p_235643_2_));
        compound.put("RecipesUsed", CompoundTag);
    }

    public void setRecipeUsed(@Nullable Recipe<?> p_193056_1_) {
        if (p_193056_1_ != null) {
            ResourceLocation resourcelocation = p_193056_1_.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }

    }


    public void awardUsedRecipesAndPopExperience(Player p_235645_1_) {
        List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(p_235645_1_.level, p_235645_1_.position());
        p_235645_1_.awardRecipes(list);
        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(Level p_235640_1_, Vec3 p_235640_2_) {
        List<Recipe<?>> list = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            p_235640_1_.getRecipeManager().byKey(entry.getKey()).ifPresent((p_235642_4_) -> {
                list.add(p_235642_4_);
                createExperience(p_235640_1_, p_235640_2_, entry.getIntValue(), ((ICookingRecipe) p_235642_4_).getExperience());
            });
        }

        return list;
    }

    private static void createExperience(Level p_235641_0_, Vec3 p_235641_1_, int p_235641_2_, float p_235641_3_) {
        int i = Mth.floor((float) p_235641_2_ * p_235641_3_);
        float f = Mth.frac((float) p_235641_2_ * p_235641_3_);
        if (f != 0.0F && Math.random() < (double) f) {
            ++i;
        }

        while (i > 0) {
            int j = ExperienceOrb.getExperienceValue(i);
            i -= j;
            p_235641_0_.addFreshEntity(new ExperienceOrb(p_235641_0_, p_235641_1_.x, p_235641_1_.y, p_235641_1_.z, j));
        }

    }

    public int getCookTime(ItemStack stack) {
        return currentRecipe == null ? 600 : currentRecipe.getCookingTime();
    }

    public List<ItemStack> getActiveInventory() {
        List<ItemStack> list = new ArrayList<>();
        list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal()));
        list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal()));
        list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal()));
        list.add(getInventory().getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal()));
        return list;
    }

    public ItemStack[][] getActiveInventoryAsArray() {
        ItemStack[][] array = new ItemStack[2][2];

        array[0][0] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT.ordinal());
        array[0][1] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT2.ordinal());
        array[1][0] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT3.ordinal());
        array[1][1] = getInventory().getStackInSlot(EnumSlots.INPUT_SLOT4.ordinal());

        return array;
    }

    public boolean isUsableByPlayer(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
                    (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    public boolean isBurning() {
        return block.isBurning(level,getBlockPos());
    }

    @Override
    public void onContainerOpened(Player player) {

    }

    @Override
    public void onContainerClosed(Player player) {

    }

    public void dropAllContents(Level world, BlockPos blockPos) {
        Containers.dropContents(world, blockPos, this.getDrops());
    }

    public Component getDisplayName() {
        return getInventory().getDisplayName();
    }


    @Override
    public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player Player) {
        return ContainerGrill.createContainerServerSide(windowID, playerInventory, this);
    }

    /*
    Present only because of IRecipe implementation, do not use!
     */
    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int i) {
        return getInventory().getStackInSlot(i);
    }

    @Override
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        return null;
    }

    @Override
    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {

    }

    @Override
    public int getMaxStackSize() {
        return Container.super.getMaxStackSize();
    }

    @Override
    public boolean stillValid(Player p_70300_1_) {
        return false;
    }

    @Override
    public void startOpen(Player p_18955_) {
        Container.super.startOpen(p_18955_);
    }

    @Override
    public void clearContent() {

    }
}
