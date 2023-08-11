package nuparu.sevendaystomine.network.messages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.world.entity.item.CarEntity;

import java.util.function.Supplier;

public class HonkMessage {

    public static void encode(HonkMessage msg, FriendlyByteBuf buf) {
    }

    public static HonkMessage decode(FriendlyByteBuf buf) {
        return new HonkMessage();
    }

    public static class Handler {

        public static void handle(HonkMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ctx.get().setPacketHandled(true);
                ServerPlayer player = ctx.get().getSender();
                Entity riding = player.getVehicle();
                if (riding instanceof CarEntity car && car.honkCooldown == 0 && riding.getPassengers().indexOf(player) == 0) {
                    System.out.println("Beep beep beep");
                    car.honkCooldown = 15;
					player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.HONK.get(), SoundSource.PLAYERS, player.getRandom().nextFloat() * 0.2f + 1.4f, player.getRandom().nextFloat() * 0.05f + 0.8f);
                }
            });
        }
    }
}
