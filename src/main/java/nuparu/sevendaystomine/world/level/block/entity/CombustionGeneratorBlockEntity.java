package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeHooks;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.world.inventory.ItemHandlerNameable;
import nuparu.sevendaystomine.world.inventory.block.ContainerCombustionGenerator;
import nuparu.sevendaystomine.world.level.block.CombustionGeneratorBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class CombustionGeneratorBlockEntity extends ElectricityItemBlockHandler {

    private int soundTimer;
    private int productionTimer;
    private int burnTime;
    private int currentItemBurnTime;

    private int temperature;
    private int heatScore;
    private int production;
    private boolean isOn;

    public final ContainerData dataAccess = new ContainerData() {
        public int get(int p_221476_1_) {
            switch (p_221476_1_) {
                case 0:
                    return CombustionGeneratorBlockEntity.this.burnTime;
                case 1:
                    return CombustionGeneratorBlockEntity.this.currentItemBurnTime;
                case 2:
                    return CombustionGeneratorBlockEntity.this.temperature;
                case 3:
                    return CombustionGeneratorBlockEntity.this.production;
                case 4:
                    return CombustionGeneratorBlockEntity.this.energyStorage.getEnergyStored();
                default:
                    return 0;
            }
        }

        public void set(int p_221477_1_, int p_221477_2_) {
            switch (p_221477_1_) {
                case 0:
                    CombustionGeneratorBlockEntity.this.burnTime = p_221477_2_;
                    break;
                case 1:
                    CombustionGeneratorBlockEntity.this.currentItemBurnTime = p_221477_2_;
                    break;
                case 2:
                    CombustionGeneratorBlockEntity.this.temperature = p_221477_2_;
                    break;
                case 3:
                    CombustionGeneratorBlockEntity.this.production = p_221477_2_;
                case 4:
                    CombustionGeneratorBlockEntity.this.energyStorage.setEnergy(p_221477_2_);
            }

        }

        public int getCount() {
            return 5;
        }
    };

    public static final int TOP_PRODUCTION = 50;

    public CombustionGeneratorBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.COMBUSTION_GENERATOR.get(), p_155229_, p_155230_);
    }

    public int getCapacity() {
        return 10000;
    }

    public int getMaxReceive() {
        return 100;
    }

    public int getMaxExtract() {
        return 100;
    }

    public void tick() {


        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning()) {
            --this.burnTime;
        }

        if (productionTimer++ % 5 == 0) {
            int temperatureLimit = Mth.clamp(500 + 500 * (heatScore / 16), 0, 1000);
            System.out.println(heatScore + " ///" + temperature + " " + temperatureLimit + " +" +  Math.ceil(Math.pow(1.33,heatScore)) + " -" + Math.ceil(Math.pow(1.2,-heatScore)));
            if (this.isBurning() && temperature < temperatureLimit) {
                temperature += Math.ceil(Math.pow(1.15, heatScore + 8));
            }

            heatScore = calculateHeatScore();
            if (this.energyStorage.canReceive()) {
                double productivity = productivity();
                production = (int) Math.ceil(TOP_PRODUCTION * productivity);
                if (production > 0) {
                    this.energyStorage.receiveEnergy(production, false);
                }
            }
            if (!this.energyStorage.canReceive()) {
                production = 0;
            }

            if (temperature > 0) {
                temperature -= Math.ceil(Math.pow(1.075, -heatScore));
            }

            if (temperature > 1000) {
                this.level.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 2, true, Explosion.BlockInteraction.BREAK);
            }
            if (temperature < 0) {
                temperature = 0;
            }
        }

        ItemStack itemstack = this.getFuelSlot();
        if (isOn && !this.isBurning()) {
            this.burnTime = ForgeHooks.getBurnTime(itemstack, null);
            this.currentItemBurnTime = this.burnTime;

            if (this.isBurning()) {
                flag1 = true;
                if (!itemstack.isEmpty()) {
                    Item item = itemstack.getItem();
                    itemstack.shrink(1);

                    if (itemstack.isEmpty()) {
                        ItemStack item1 = item.getCraftingRemainingItem(itemstack);
                        this.getInventory().setStackInSlot(0, item1);
                    }
                }
            }
        }

        if (flag != this.isBurning()) {
            flag1 = true;
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition)
                    .setValue(CombustionGeneratorBlock.LIT, this.isBurning()), 3);
        }

        if (flag1) {
            this.setChanged();
        }
    }

    public void clientTick() {

    }

    public boolean isBurning() {
        return this.burnTime > 0;
    }

    public ItemStack getFuelSlot() {
        return this.getInventory() == null ? ItemStack.EMPTY : this.getInventory().getStackInSlot(0);
    }

    public double productivity() {
        return Math.min(1d, temperature / 1000d);
    }

    public int calculateHeatScore() {
        if (!hasLevel()) return 0;

        HashMap<Material, Integer> counts = new HashMap<>();

        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    BlockPos pos = worldPosition.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    Material material = state.getMaterial();
                    counts.put(material, counts.getOrDefault(material, 0) + 1);
                }
            }
        }

        int hot = counts.getOrDefault(Material.FIRE, 0) + counts.getOrDefault(Material.LAVA, 0) * 2;
        int cold = counts.getOrDefault(Material.SNOW, 0)
                + counts.getOrDefault(Material.POWDER_SNOW, 0)
                + counts.getOrDefault(Material.TOP_SNOW, 0)
                + counts.getOrDefault(Material.ICE_SOLID, 0) * 2
                + counts.getOrDefault(Material.ICE, 0) * 2 + counts.getOrDefault(Material.WATER, 0);

        return hot - cold;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean state) {
        this.isOn = state;
    }

    public void switchOn() {
        setOn(!isOn());
    }

    @Override
    protected ItemHandlerNameable createInventory() {
        return new ItemHandlerNameable(2, Component.translatable("container.combustion_generator"));
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        this.burnTime = compound.getInt("BurnTime");
        this.productionTimer = compound.getInt("ProductionTimer");
        this.isOn = compound.getBoolean("IsOn");
        ;
        this.heatScore = compound.getInt("HeatScore");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("ProductionTimer", productionTimer);
        compound.putInt("BurnTime", (short) this.burnTime);
        compound.putBoolean("IsOn", this.isOn);
        compound.putInt("HeatScore", this.heatScore);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return getInventory().getDisplayName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, @NotNull Inventory playerInventory, @NotNull Player player) {
        return ContainerCombustionGenerator.createContainerServerSide(windowID, playerInventory, this);
    }

    @Override
    public void onContainerOpened(Player player) {

    }

    @Override
    public void onContainerClosed(Player player) {

    }

    @Override
    public boolean isUsableByPlayer(Player player) {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
    }
}
