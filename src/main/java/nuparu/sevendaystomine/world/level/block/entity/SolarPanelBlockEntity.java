package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ClientConfig;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.level.block.SolarPanelBlock;
import org.jetbrains.annotations.NotNull;

public class SolarPanelBlockEntity extends ElectricityBlockHandler{

    private int soundTimer;
    private int productionTimer;

    public static final int TOP_PRODUCTION = 5;

    public SolarPanelBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.SOLAR_PANEL.get(), p_155229_, p_155230_);
    }

    public int getCapacity(){
        return 100;
    }
    public int getMaxReceive(){
        return 100;
    }
    public int getMaxExtract(){
        return 100;
    }

    public void tick(){
        if(this.energyStorage.canReceive() && productionTimer++ % 5 == 0) {
            double productivity = productivity();
            if (productivity > 0) {
                this.energyStorage.receiveEnergy((int) Math.ceil(TOP_PRODUCTION * productivity), false);
            }
        }
    }

    public void clientTick(){
        if(!ClientConfig.solarPanelHum.get()) return;
        if (hasLevel() && soundTimer++ % 10 == 0) {
            Player player = SevenDaysToMine.proxy.getPlayer();
            if(player == null) return;

            double dstSq = player.distanceToSqr(getBlockPos().getX()  + 0.5,getBlockPos().getY()  + 0.5, getBlockPos().getZ() + 0.5);
            if(dstSq > 64) return;

            double productivity = productivity();
            if(productivity <= 0) return;
            level.playLocalSound(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5, ModSounds.SOLAR_PANEL_HUM.get(), SoundSource.BLOCKS, (float) (productivity * MathUtils.getFloatInRange(0.9f,1.1f) * 0.33f * (1 - dstSq/64f)),
                    1.0F, true);
        }
    }

    public double solarIntensity(){
        if(!hasLevel()) return 0;
        double angle = Math.toDegrees(level.getSunAngle(1));
        double delta = angle < 90 ? (90-angle)/90d : (angle > 270 ? (angle - 270)/90d : 0);

        //return (Math.pow(-1.5 * delta - 0.5,-1) + 2) / 1.5;
        return 1 - (delta - 1) * (delta - 1);
    }

    public double productivity(){
        if(!hasLevel()) return 0;
        int subtractedSkylight = level.getBrightness(LightLayer.SKY, worldPosition) - 11;
        int weatherPenalty = level.isThundering() ? 4 : (level.isRaining() ? 2 : 0);
        int penalizedSkylight = Math.max(0,subtractedSkylight - weatherPenalty);
        double intensityMultiplier = penalizedSkylight / 4d;

        if(intensityMultiplier == 0) return 0;

        Direction direction = level.getBlockState(worldPosition).getValue(SolarPanelBlock.FACING);
        if(direction.getAxis() == Direction.Axis.Z){
            intensityMultiplier = intensityMultiplier * 0.666667;
        }
        return solarIntensity() * intensityMultiplier;
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        if(compound.contains("ProductionTimer", Tag.TAG_INT)){
            this.productionTimer = compound.getInt("ProductionTimer");
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("ProductionTimer",productionTimer);
    }
}
