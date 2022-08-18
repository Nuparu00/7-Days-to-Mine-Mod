package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.world.level.block.TorchLitBlockBase;
import nuparu.sevendaystomine.world.level.block.TorchWallLitBlockBase;

public class TorchBlockEntity extends BlockEntity{

    public int age = 0;
    public TorchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TORCH.get(),pos,state);
    }

    public void tickServer() {
        if(hasLevel() && level instanceof ServerLevel){
            Biome.Precipitation precipitation = level.getBiome(worldPosition).value().getPrecipitation();
            boolean raining = level.isRaining() && level.canSeeSky(this.worldPosition);
            if(ServerConfig.torchBurnTime.get() >= 0){
                age += raining && precipitation == Biome.Precipitation.SNOW ? 4 : 1;
            }
            if ((precipitation == Biome.Precipitation.RAIN && raining && ServerConfig.torchRainExtinguish.get()) || (ServerConfig.torchBurnTime.get() >= 0 && age > ServerConfig.torchBurnTime.get())) {
                Block block = getBlockState().getBlock();

                if (block instanceof TorchLitBlockBase) {
                    TorchLitBlockBase.extinguish((ServerLevel) level, worldPosition);

                } else if (block instanceof TorchWallLitBlockBase) {
                    TorchWallLitBlockBase.extinguish((ServerLevel) level, worldPosition);
                }
            }
        }
    }

    public void load(CompoundTag compound) {
        super.load(compound);
        if(compound.contains("age", Tag.TAG_INT)){
            this.age = compound.getInt("age");
        }
    }

    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("age", this.age);
    }
}
