package nuparu.sevendaystomine.world.inventory.block;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.inventory.block.slot.UncraftingResultSlot;
import nuparu.sevendaystomine.world.inventory.block.slot.WorkbenchScrapSlot;
import nuparu.sevendaystomine.world.inventory.block.slot.WorkbenchUncraftingInputSlot;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityManager;
import nuparu.sevendaystomine.world.level.block.ChemistryStationBlock;
import nuparu.sevendaystomine.world.level.block.WorkbenchBlock;
import nuparu.sevendaystomine.world.level.block.entity.WorkbenchBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContainerWorkbenchUncrafting extends AbstractContainerMenu {

    private final UncraftingContainer craftSlots = new UncraftingContainer(this,1,1);
    private final UncraftingResultContainer resultSlots = new UncraftingResultContainer();
    private final Level world; // needed for some helper methods
    private final ContainerLevelAccess access;
    private final WorkbenchBlockEntity workbench;
    public final Player player;

    public ContainerWorkbenchUncrafting(int windowID, Inventory invPlayer, WorkbenchBlockEntity workbench) {
        super(ModContainers.WORKBENCH_UNCRAFTING.get(), windowID);
        this.world = invPlayer.player.level;
        this.player = invPlayer.player;
        this.workbench = workbench;
        this.access = ContainerLevelAccess.create(player.level,workbench.getBlockPos());

        // server Containers
        if(workbench != null) {

            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 5; ++j) {
                    this.addSlot(new UncraftingResultSlot(player,this.craftSlots,this.resultSlots, j + i * 5, 80 + j * 18, 7 + i * 18,this));
                }
            }
            this.addSlot(new WorkbenchUncraftingInputSlot(this.craftSlots, 0, 26, 44,this));

            this.addSlot(new WorkbenchScrapSlot(workbench.getInventory(), 0, 26, 70, this));

            for (int k = 0; k < 9; ++k) {
                addSlot(new Slot(invPlayer, k, 8 + k * 18, 164));
            }

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 106 + i * 18));
                }
            }
        }
    }

    // --------- Customise the different slots (in particular - what items they will
    // accept)

    public static ContainerWorkbenchUncrafting createContainerServerSide(int windowID, Inventory playerInventory, WorkbenchBlockEntity workbench){
        return new ContainerWorkbenchUncrafting(windowID, playerInventory, workbench);
    }

    public static ContainerWorkbenchUncrafting createContainerClientSide(int windowID, Inventory playerInventory,
                                                                         FriendlyByteBuf extraData) {
        return new ContainerWorkbenchUncrafting(windowID, playerInventory, (WorkbenchBlockEntity) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public boolean stillValid(Player player) {
        return this.workbench != null && this.workbench.isUsableByPlayer(player);
    }

    protected static void slotChangedCraftingGrid(int p_217066_0_, Level p_217066_1_, Player p_217066_2_, CraftingContainer p_217066_3_, ResultContainer p_217066_4_) {
        if (!p_217066_1_.isClientSide) {
            ServerPlayer serverplayerentity = (ServerPlayer)p_217066_2_;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = p_217066_1_.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, p_217066_3_, p_217066_1_);
            if (optional.isPresent()) {
                CraftingRecipe icraftingrecipe = optional.get();
                if (p_217066_4_.setRecipeUsed(p_217066_1_, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.assemble(p_217066_3_);
                }
            }

            p_217066_4_.setItem(0, itemstack);
            serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(p_217066_0_,0, 0, itemstack));
        }
    }

    public void slotsChanged(Container p_75130_1_) {
        //this.access.execute((p_217069_1_, p_217069_2_) -> updateCraftingGrid(world, player, craftSlots, resultSlots,this.workbench.getInventory().getStackInSlot(0)));
    }

    public void onScrapChanged(ItemStack scrap) {
        this.access.execute((p_217069_1_, p_217069_2_) -> updateCraftingGrid(world, player, craftSlots, resultSlots,scrap));
    }

    public void updateCraftingGrid(){
        this.access.execute((p_217069_1_, p_217069_2_) -> updateCraftingGrid(world, player, craftSlots, resultSlots,this.workbench.getInventory().getStackInSlot(0)));
    }

    protected void updateCraftingGrid(Level world, Player player, UncraftingContainer input,
                                      UncraftingResultContainer output, ItemStack scrap) {
        /*if (!world.isClientSide()) {
            ServerPlayerEntity entityplayermp = (ServerPlayerEntity) player;
            Object[] items = ForgeRegistries.ITEMS.getValues().toArray();
            for (int m = 0; m < 25; m++) {
                ItemStack stack = new ItemStack((IItemProvider) items[world.random.nextInt(items.length)]);
                entityplayermp.connection.send(new SSetSlotPacket(this.containerId, m, stack));
                this.setItem(m, stack);
            }
        }
        if(true) return;*/

        if (!world.isClientSide()) {
            ServerPlayer entityplayermp = (ServerPlayer) player;
            if (scrap.isEmpty()) {
                for (int m = 0; m < 25; m++) {
                    //output.setItem(m, ItemStack.EMPTY);
                    entityplayermp.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, m,0, ItemStack.EMPTY));
                    this.setItem(m,0,ItemStack.EMPTY);
                    //setSlot(m,ItemStack.EMPTY);
                }
                return;
            }
            ItemStack itemstack = input.getItem(0);
            if(!isUncraftable(itemstack)) {
                for (int m = 0; m < 25; m++) {
                    //output.setItem(m, ItemStack.EMPTY);
                    entityplayermp.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, m,0, ItemStack.EMPTY));
                    this.setItem(m,0,ItemStack.EMPTY);
                    //setSlot(m,ItemStack.EMPTY);
                }
                return;
            }
            Recipe irecipe = ItemUtils.getRecipesForStack(itemstack, world.getServer());
            if (irecipe != null) {
                NonNullList<Ingredient> list = irecipe.getIngredients();
                int i = 3;
                int j = irecipe instanceof net.minecraftforge.common.crafting.IShapedRecipe
                        ? Math.max(3, ((net.minecraftforge.common.crafting.IShapedRecipe) irecipe).getRecipeHeight())
                        : i;
                int k = irecipe instanceof net.minecraftforge.common.crafting.IShapedRecipe
                        ? ((net.minecraftforge.common.crafting.IShapedRecipe) irecipe).getRecipeWidth()
                        : i;
                int l = 1;

                int qualityItems = 0;
                List<ItemStack> items = new ArrayList<>();
                for (int m = 0; m < 25; m++) {
                    ItemStack stack = ItemStack.EMPTY;
                    if (list.size() > m) {
                        Ingredient ingredient = list.get(m);
                        if (ingredient.getItems().length > 0) {
                            stack = ingredient.getItems()[0].copy();
                            IQualityStack qualityStack = (IQualityStack) (Object) stack;
                            if(qualityStack.canHaveQuality()) {
                                qualityItems++;
                            }
                        }
                    }
                    items.add(stack);
                }

                int originalQuality = -1;
                IQualityStack qualityStack = (IQualityStack) (Object) itemstack;
                if(qualityStack.canHaveQuality()) {
                    originalQuality = qualityStack.getQuality();
                }
                else{
                    originalQuality = (int) Math.min(Math.floor(player.totalExperience / ServerConfig.xpPerQuality.get()),
                            QualityManager.maxLevel);
                }

                for (int m = 0; m < 25; m++) {
                    ItemStack stack = items.get(m);
                    qualityStack = (IQualityStack) (Object) itemstack;
                    if (originalQuality > 0 && qualityStack.canHaveQuality()) {
                        qualityStack.setQuality(Math.max(1, (int) (originalQuality * 0.75f - world.random.nextInt(10))));
                    }

                    //output.setItem(m, stack);
                    entityplayermp.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, m,0, stack));
                    this.setItem(m,0,stack);
                    //setSlot(m,stack);
                }

            }
        }
    }

    void setSlot(int id, ItemStack stack){
        Slot slot = this.getSlot(id);
        slot.container.setItem(slot.getContainerSlot(),ItemStack.EMPTY);
    }

    public static boolean isUncraftable(ItemStack stack) {
        if (stack.isEmpty())
            return false;
        Item item = stack.getItem();
        /*if (item instanceof ItemGun) {
            return true;
        } else*/ /*if (item instanceof BlockItem) {
            BlockItem itemBlock = (BlockItem) item;
            Block block = itemBlock.getBlock();
            if (block instanceof BlockGenerator) {
                return true;
            }
            if (block instanceof WorkbenchBlock) {
                return true;
            }
            if (block instanceof BlockCombustionGenerator) {
                return true;
            }
            return block instanceof ChemistryStationBlock;
        }*/ /*else if (item instanceof ItemBullet) {
            return true;
        } else return item instanceof ItemFuelTool;*/
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player entity, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == 0) {
                // itemstack1.getItem().onCreated(itemstack1, this.worldObj, playerIn);

                if (!this.moveItemStackTo(itemstack1, 27, 63, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 27 && index < 63) {
                if (itemstack1.getItem() == ModItems.IRON_SCRAP.get()) {
                    if (!this.moveItemStackTo(itemstack1, 26, 27, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (index == 26) {
                if (!this.moveItemStackTo(itemstack1, 27, 63, true)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);

            if (index == 0) {
                player.drop(itemstack1, false);
            }
        }

        return itemstack;
    }

    public WorkbenchBlockEntity getWorkbench(){
        return this.workbench;
    }
/*
    @Override
    public boolean canTakeItemForPickAll(ItemStack p_94530_1_, Slot p_94530_2_) {
        return p_94530_2_.container != this.resultSlots && super.canTakeItemForPickAll(p_94530_1_, p_94530_2_);
    }*/

    @Override
    public void removed(Player p_75134_1_) {
        super.removed(p_75134_1_);
        this.access.execute((p_217068_2_, p_217068_3_) -> this.clearContainer(p_75134_1_, this.craftSlots));
    }
}
