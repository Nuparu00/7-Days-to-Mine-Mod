package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ClientConfig;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.level.block.WindTurbineBlock;
import org.jetbrains.annotations.NotNull;

public class WindTurbineBlockEntity extends ElectricityBlockHandler{

    //Used only on client-side - it is not really necessary for the angles to be the same across all clients
    private float angle;
    private float anglePrev;

    private int soundTimer;
    private int productionTimer;
    private double obstructionRatio;
    private Direction direction;

    public static final int TOP_PRODUCTION = 8;

    public WindTurbineBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.WIND_TURBINE.get(), p_155229_, p_155230_);
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
        if(direction == null && hasLevel()){
            direction = getBlockState().getValue(WindTurbineBlock.FACING);
        }
        if(productionTimer % 40 == 0){
            obstructionRatio = calculateObstructionRatio();
        }
        if(this.energyStorage.canReceive() && productionTimer % 5 == 0) {
            double productivity = productivity();
            if (productivity > 0) {
                this.energyStorage.receiveEnergy((int) Math.ceil(TOP_PRODUCTION * productivity), false);
            }
        }

        productionTimer++;
    }

    public void clientTick(){
        if(direction == null && hasLevel()){
            direction = getBlockState().getValue(WindTurbineBlock.FACING);
        }
        if(productionTimer % 40 == 0){
            obstructionRatio = calculateObstructionRatio();
        }
        anglePrev = angle;
        angle += (productivity() / 10d) * ClientConfig.windTurbineRotationSpeedMultiplier.get();
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
        productionTimer++;
    }

    public double windIntensity(){
        if(!hasLevel()) return 0;

        double heightFactor = 0.25 + ((getBlockPos().getY() + getLevel().getMinBuildHeight()) / (double) getLevel().getMaxBuildHeight()) / 4;
        double weatherBonus = level.isThundering() ? 2 : (level.isRaining() ? 1.5 : 1);

        return heightFactor * weatherBonus;
    }

    public double productivity(){
        if(!hasLevel()) return 0;

        if (obstructionRatio > 0.45){
            return 0;
        }

       return windIntensity() * (1 - obstructionRatio);
    }

    public double calculateObstructionRatio(){
        if(!hasLevel()) return 0;
        int blockingBlocks = 0;
        int blocks = 0;

        for(int i = -2; i < 3; i++ ){
            for(int j = -2; j < 3; j++ ){
                for(int k = -6; k < 6; k++ ){
                    if(k == 0 && j == 0 && i == 0) continue;
                    BlockPos pos = getBlockPos().above(j).relative(direction,k).relative(direction.getClockWise(),i);
                    BlockState state = getLevel().getBlockState(pos);
                    blocks++;
                    if(state.getMaterial().isSolidBlocking()){
                        blockingBlocks++;
                    }
                }
            }
        }

        return (blockingBlocks/(double)blocks);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        if(compound.contains("ProductionTimer", Tag.TAG_INT)){
            this.productionTimer = compound.getInt("ProductionTimer");
        }
        if(compound.contains("ObstructionRatio", Tag.TAG_DOUBLE)){
            this.obstructionRatio = compound.getDouble("ObstructionRatio");
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("ProductionTimer",productionTimer);
        compound.putDouble("ObstructionRatio",obstructionRatio);
    }

    public float getAngle() {
        return angle;
    }

    public float getAnglePrev() {
        return anglePrev;
    }

    @Override
    public AABB getRenderBoundingBox()
    {
        return super.getRenderBoundingBox().inflate(1);
    }
}
