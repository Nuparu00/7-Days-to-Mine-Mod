package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.world.inventory.IContainerCallbacks;
import nuparu.sevendaystomine.world.inventory.ILootTableProvider;
import nuparu.sevendaystomine.world.inventory.InventoryUtils;
import nuparu.sevendaystomine.world.inventory.ItemHandlerNameable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ItemHandlerBlockEntity<INVENTORY extends ItemHandlerNameable>
        extends RandomizableContainerBlockEntity implements IContainerCallbacks, MenuProvider, ILootTableProvider {

    @Nullable
    protected ResourceLocation lootTable;
    protected long lootTableSeed;


    public ItemHandlerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type,pos,state);
    }

    protected final LazyOptional<INVENTORY> inventory = LazyOptional.of(this::createInventory);

    protected abstract INVENTORY createInventory();

    @Nullable
    public INVENTORY getInventory() {
        return this.inventory.orElse(null);
    }

    public NonNullList<ItemStack> getDrops() {
        this.unpackLootTable(null);
        return InventoryUtils.dropItemHandlerContents(getInventory(), level.random);
    }

    public void dropAllContents(Level world, BlockPos blockPos) {
        this.unpackLootTable(null);
        Containers.dropContents(world, blockPos, this.getDrops());
    }

    public static void setLootTable(BlockGetter p_222767_, RandomSource p_222768_, BlockPos p_222769_, ResourceLocation p_222770_) {
        BlockEntity blockentity = p_222767_.getBlockEntity(p_222769_);
        if (blockentity instanceof ItemHandlerBlockEntity<?> itemHandlerBlockEntity) {
            itemHandlerBlockEntity.setLootTable(p_222770_, p_222768_.nextLong());
        }

    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        if (!this.tryLoadLootTable(compound)) {
            if (getInventory() != null && compound.contains("ItemHandler")) {
                getInventory().deserializeNBT(compound.getCompound("ItemHandler"));
            }
        }

    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        if (!this.trySaveLootTable(compound)) {
            if (getInventory() != null) {
                compound.put("ItemHandler", getInventory().serializeNBT());
            }
        }
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if(this.lootTable != null){
                this.unpackLootTable(null);
            }
            return inventory.cast();
        }
        return super.getCapability(cap, side);
    }


    @Override
    public void invalidateCaps(){
        super.invalidateCaps();
        this.inventory.invalidate();
    }

    public ResourceLocation getLootTable() {
        return null;
    }

    public void setDisplayName(Component displayName) {
        getInventory().setDisplayName(displayName);
    }

	/*
	LOOT TABLE PART
	 */

    public boolean tryLoadLootTable(CompoundTag p_184283_1_) {
        if (p_184283_1_.contains("LootTable", 8)) {
            this.lootTable = new ResourceLocation(p_184283_1_.getString("LootTable"));
            this.lootTableSeed = p_184283_1_.getLong("LootTableSeed");
            return true;
        } else {
            return false;
        }
    }

    public boolean trySaveLootTable(CompoundTag p_184282_1_) {
        if (this.lootTable == null) {
            return false;
        } else {
            p_184282_1_.putString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                p_184282_1_.putLong("LootTableSeed", this.lootTableSeed);
            }

            return true;
        }
    }

    public void unpackLootTable(@Nullable Player p_184281_1_) {
        if (this.lootTable != null && this.level.getServer() != null) {
            LootTable loottable = this.level.getServer().getLootTables().get(this.lootTable);
            if (p_184281_1_ instanceof ServerPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)p_184281_1_, this.lootTable);
            }
            //NON PERSISTANT EVEN IF ACTIONS ARE THE SAME
            this.lootTable = null;
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel)this.level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition)).withOptionalRandomSeed(this.lootTableSeed);
            if (p_184281_1_ != null) {
                lootcontext$builder.withLuck(p_184281_1_.getLuck()).withParameter(LootContextParams.THIS_ENTITY, p_184281_1_);
            }
            LootContext context = lootcontext$builder.create(LootContextParamSets.CHEST);
            SevenDaysToMine.LOGGER.warn(this.getClass().getName() + " " + getBlockPos().toShortString() +  " // " + lootTableSeed +  " " + ((LegacyRandomSource)context.getRandom()).seed.get());

            ItemUtils.fill(loottable,this.getInventory(), context);
        }

    }

    public void setLootTable(ResourceLocation p_189404_1_, long p_189404_2_) {
        this.lootTable = p_189404_1_;
        this.lootTableSeed = p_189404_2_;
    }

    public void setLootTable(ResourceLocation p_189404_1_) {
        this.lootTable = p_189404_1_;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }



    @Override
    public int getContainerSize() {
        return getInventory().getSlots();
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }


    @Override
    protected NonNullList<ItemStack> getItems() {
        final NonNullList<ItemStack> items = NonNullList.create();

        for (int slot = 0; slot < getContainerSize(); ++slot) {
            ItemStack stack = getInventory().getStackInSlot(slot);
            if(!stack.isEmpty()){
                items.add(stack);
            }
        }
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> p_59625_) {
        for (int slot = 0; slot < p_59625_.size(); ++slot) {
            getInventory().setStackInSlot(slot,p_59625_.get(slot));
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) {
        return createMenu(p_58627_,p_58628_, p_58628_.player);
    }
}
