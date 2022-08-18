package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import nuparu.sevendaystomine.world.level.block.entity.SpikesBlockEntity;

public class LogSpikeBlock extends SpikeBlock {

    float damagePerBlock;

    public LogSpikeBlock(Properties p_49795_, int maxHealth, float damage) {
        super(p_49795_, maxHealth, (int)damage);
        this.damagePerBlock = damage;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (!(entityIn instanceof LivingEntity)) {
            return;
        }
        entityIn.makeStuckInBlock(state, new Vec3(0.25D, 0.05F, 0.25D));
        if (entityIn instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) {
                return;
            }
        }

        float fallDistance = entityIn.fallDistance;
        if(fallDistance > 1) {
            float damageToDeal = fallDistance * damagePerBlock;
                entityIn.hurt(DamageSource.GENERIC, damageToDeal);
                if (worldIn.getBlockEntity(pos) instanceof SpikesBlockEntity blockEntity) {
                    blockEntity.damage(1);
                }
        }
    }
    @Override
    public void degradeBlock(BlockPos pos, Level level) {
        level.destroyBlock(pos,false);
    }
}
