package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import nuparu.sevendaystomine.world.level.block.entity.SpikesBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpikeBlock extends WaterloggableHorizontalBlockBase implements EntityBlock, ISpikeBlock {

    protected int maxHealth;
    protected int damage;

    public SpikeBlock(Properties p_49795_, int maxHealth, int damage) {
        super(p_49795_);
        this.maxHealth = maxHealth;
        this.damage = damage;
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Entity entityIn) {
        if (!(entityIn instanceof LivingEntity)) {
            return;
        }
        entityIn.makeStuckInBlock(state, new Vec3(0.25D, 0.05F, 0.25D));
        if (entityIn instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) {
                return;
            }
        }
        entityIn.hurt(DamageSource.GENERIC, damage);
        if (worldIn.getBlockEntity(pos) instanceof SpikesBlockEntity blockEntity) {
            blockEntity.damage(1);
        }
    }

    @Override
    public void degradeBlock(BlockPos pos, Level level) {

    }

    @Override
    public int getMaxHealth(BlockPos pos, BlockState state, Level level) {
        return maxHealth;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SpikesBlockEntity(pos,state);
    }
}
