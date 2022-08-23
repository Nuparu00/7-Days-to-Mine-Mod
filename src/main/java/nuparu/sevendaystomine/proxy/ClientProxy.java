package nuparu.sevendaystomine.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.client.gui.components.toasts.NotificationToast;
import nuparu.sevendaystomine.client.gui.inventory.entity.LockedCodeSafeScreen;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.item.quality.QualityManager;

import net.minecraft.network.chat.Component;
import java.util.function.Supplier;

public class ClientProxy extends CommonProxy{
    public Player getPlayerEntityFromContext(Supplier<NetworkEvent.Context> ctx) {
        return (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT ? Minecraft.getInstance().player
                : super.getPlayerEntityFromContext(ctx));
    }

    @Override
    public int getQualityForCurrentPlayer() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return 0;
        return (int) MathUtils.clamp(player.totalExperience / ServerConfig.xpPerQuality.get(), 1,
                QualityManager.maxLevel);
    }


    public Level getLevel(){
        return Minecraft.getInstance().level;
    }


    @Override
    public void openClientSideGui(int id, int x, int y, int z) {
        Minecraft mc = Minecraft.getInstance();
        BlockEntity te = mc.level.getBlockEntity(new BlockPos(x, y, z));
        switch (id) {
            case 0:
                mc.setScreen(new LockedCodeSafeScreen(te, new BlockPos(x, y, z)));
                return;
        }
    }


    @Override
    public void addNotificationToast(ItemStack stack, Component title, Component description){
        Minecraft.getInstance().getToasts()
                .addToast(new NotificationToast(stack, title,
                        description));
    }
}
