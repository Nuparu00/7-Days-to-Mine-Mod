package nuparu.sevendaystomine.computer.application;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ApplicationRegistry {
    
	public static final ApplicationRegistry INSTANCE = new ApplicationRegistry();
	
	private HashMap<ResourceLocation, Application> registry = new HashMap<ResourceLocation, Application>();
	
	
	public void registerApp(Application app, String name) {
		registerApp(app, new ResourceLocation(SevenDaysToMine.MODID,name));
	}
	
	public void registerApp(Application app, ResourceLocation res) {
		app.key = res;
		registry.put(res, app);
	}
	
	public Application getByRes(ResourceLocation res) {
		Application app = registry.get(res);
		return app;
	}
	
	public Application getByString(String name) {
		return getByRes(new ResourceLocation(SevenDaysToMine.MODID,name));
	}
	
	public ResourceLocation getResByApp(Application app) {
		for (Entry<ResourceLocation, Application> entry : registry.entrySet()) {
	        if (Objects.equals(app, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
		return null;
	}
	
	public void register() {
		registerApp(new Application(new ResourceLocation(SevenDaysToMine.MODID,"textures/apps/shell.png"),"shell"),"shell");
		registerApp(new Application(new ResourceLocation(SevenDaysToMine.MODID,"textures/apps/notes.png"),"notes"),"notes");
		registerApp(new Application(new ResourceLocation(SevenDaysToMine.MODID,"textures/apps/cctv.png"),"cctv"),"cctv");
		registerApp(new Application(new ResourceLocation(SevenDaysToMine.MODID,"textures/apps/explorer.png"),"explorer"),"explorer");
		registerApp(new Application(new ResourceLocation(SevenDaysToMine.MODID,"textures/apps/transit.png"),"transit"),"transit");
		registerApp(new Application(new ResourceLocation(SevenDaysToMine.MODID,"textures/apps/epidemic.png"),"epidemic"),"epidemic");
		//registerApp(new Application(new ResourceLocation(SevenDaysToMine.MODID,"textures/apps/maps.png"),"maps"),"maps");
	}
}
