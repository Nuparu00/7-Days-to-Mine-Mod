package nuparu.sevendaystomine.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.init.ModBlocksTags;
import nuparu.sevendaystomine.init.ModEffects;
import nuparu.sevendaystomine.init.ModFluidTags;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.entity.EntityUtils;

import java.util.function.Supplier;

public class PlayerDrinkMessage {
    final BlockPos pos;

    public PlayerDrinkMessage() {
        pos = null;
    }

    public PlayerDrinkMessage(BlockPos pos) {
        this.pos = pos;
    }
    public static void encode(PlayerDrinkMessage msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static PlayerDrinkMessage decode(FriendlyByteBuf buf) {
        return new PlayerDrinkMessage(buf.readBlockPos());
    }

    public static class Handler {

        public static void handle(PlayerDrinkMessage msg, Supplier<NetworkEvent.Context> ctx) {

            ctx.get().enqueueWork(() -> {
                Player player = SevenDaysToMine.proxy.getPlayerEntityFromContext(ctx);
                if(player.isSpectator() || player.isCreative()) return;
                if(!player.getMainHandItem().isEmpty()) return;
                Level level = player.level();
                if(level != null){
                    BlockHitResult result = EntityUtils.rayTraceServer(player, player.getAttribute(ForgeMod.BLOCK_REACH.get()).getValue(), 1,
                            ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY);
                    if(result == null) return;

                    BlockState blockState = player.level().getBlockState(result.getBlockPos());
                    if(blockState.is(ModBlocksTags.MURKY_WATER_SOURCE) || blockState.getFluidState().is(ModFluidTags.MURKY_WATER_SOURCE)){
                        IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
                        if(extendedPlayer.getWaterLevel() == extendedPlayer.getMaxWaterLevel()) return;
                        extendedPlayer.setDrinkCounter(extendedPlayer.getDrinkCounter() + 8);
                        if(level.random.nextBoolean()) {
                            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK, SoundSource.BLOCKS, MathUtils.getFloatInRange(0.88f, 1.15f), MathUtils.getFloatInRange(0.88f, 1.15f));
                        }
                        if(extendedPlayer.getDrinkCounter() >= 30){
                            extendedPlayer.setDrinkCounter(0);
                            extendedPlayer.addWaterLevel(2);
                            if(player.getRandom().nextDouble() <= 0.35){
                                player.addEffect(new MobEffectInstance(ModEffects.DYSENTERY.get(), MathUtils.getIntInRange(player.getRandom(),600,1200),0,false,false,true));
                            }
                        }
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
