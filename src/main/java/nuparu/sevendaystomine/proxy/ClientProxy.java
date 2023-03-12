package nuparu.sevendaystomine.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.client.event.ClientEventHandler;
import nuparu.sevendaystomine.client.gui.components.toasts.NotificationToast;
import nuparu.sevendaystomine.client.gui.inventory.entity.LockedCodeSafeScreen;
import nuparu.sevendaystomine.client.gui.inventory.entity.PhotoScreen;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.item.PhotoItem;
import nuparu.sevendaystomine.world.item.quality.QualityManager;

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
                QualityManager.getMaxLevel());
    }

    public Player getPlayer(){
        return Minecraft.getInstance().player;
    }


    public Level getLevel(){
        return Minecraft.getInstance().level;
    }


    @Override
    public void openClientOnlyGui(int id, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || !FMLEnvironment.dist.isClient()) {
            return;
        }

        //Not sure why, not sure how this code is somehow run on the server thread? Even though isClient() / isCLientSide() is never false
        if (id == 0) {
            openPhoto(PhotoItem.getPath(stack).get());
        }

    }

    @Override
    public void openClientSideGui(int id, int x, int y, int z) {
        Minecraft mc = Minecraft.getInstance();
        BlockEntity te = mc.level.getBlockEntity(new BlockPos(x, y, z));
        if (id == 0) {
            mc.setScreen(new LockedCodeSafeScreen(te, new BlockPos(x, y, z)));
        }
    }


    @Override
    public void addNotificationToast(ItemStack stack, Component title, Component description){
        Minecraft.getInstance().getToasts()
                .addToast(new NotificationToast(stack, title,
                        description));
    }

    @Override
    public void openPhoto(String path){
        Minecraft mc = Minecraft.getInstance();
        mc.submitAsync(() -> mc.setScreen(new PhotoScreen(path)));
    }
    @Override
    public void schedulePhoto() {
        if (ServerConfig.allowPhotos.get()) {
            ClientEventHandler.takingPhoto = true;
        }
    }

}
