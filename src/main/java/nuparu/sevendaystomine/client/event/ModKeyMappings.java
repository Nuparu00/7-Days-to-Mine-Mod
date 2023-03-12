package nuparu.sevendaystomine.client.event;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid=SevenDaysToMine.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModKeyMappings {
    public static final KeyMapping HONK = new KeyMapping("key.honk.desc", GLFW.GLFW_KEY_SPACE, "key.categories." + SevenDaysToMine.MODID);
    public static final KeyMapping RELOAD = new KeyMapping("key.reload.desc", GLFW.GLFW_KEY_R, "key.categories." + SevenDaysToMine.MODID);
    public static final KeyMapping CAMERA_WIDTH_INCREASE = new KeyMapping("camera.width.increase", GLFW.GLFW_KEY_KP_6, "key.categories." + SevenDaysToMine.MODID);
    public static final KeyMapping CAMERA_WIDTH_DECREASE = new KeyMapping("camera.width.decrease", GLFW.GLFW_KEY_KP_4, "key.categories." + SevenDaysToMine.MODID);
    public static final KeyMapping CAMERA_HEIGHT_INCREASE = new KeyMapping("camera.height.increase", GLFW.GLFW_KEY_KP_8, "key.categories." + SevenDaysToMine.MODID);
    public static final KeyMapping CAMERA_HEIGHT_DECREASE = new KeyMapping("camera.height.decrease", GLFW.GLFW_KEY_KP_2, "key.categories." + SevenDaysToMine.MODID);
    public static final KeyMapping CAMERA_ZOOM = new KeyMapping("camera.zoom", GLFW.GLFW_KEY_KP_ADD, "key.categories." + SevenDaysToMine.MODID);
    public static final KeyMapping CAMERA_UNZOOM = new KeyMapping("camera.unzoom", GLFW.GLFW_KEY_KP_SUBTRACT, "key.categories." + SevenDaysToMine.MODID);

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        registerKey(HONK, event);
        registerKey(RELOAD, event);
        registerKey(CAMERA_WIDTH_INCREASE, event);
        registerKey(CAMERA_WIDTH_DECREASE, event);
        registerKey(CAMERA_HEIGHT_INCREASE, event);
        registerKey(CAMERA_HEIGHT_DECREASE, event);
        registerKey(CAMERA_ZOOM, event);
        registerKey(CAMERA_UNZOOM, event);
    }

    private static void registerKey(KeyMapping mapping, RegisterKeyMappingsEvent event){
        mapping.setKeyConflictContext(KeyConflictContext.IN_GAME);
        event.register(mapping);
    }
}
