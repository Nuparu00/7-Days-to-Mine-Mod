package nuparu.sevendaystomine.computer.process;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.computer.process.epidemic.EpidemicProcess;

public class ProcessRegistry {

	public static final String RES_KEY = "processResourceLocation";
	
	public static final ProcessRegistry INSTANCE = new ProcessRegistry();
	
	private HashMap<ResourceLocation, Class<? extends TickingProcess>> registry = new HashMap<ResourceLocation, Class<? extends TickingProcess>>();
	
	
	public void registerProcess(Class<? extends TickingProcess> process, String name) {
		registerProcess(process, new ResourceLocation(SevenDaysToMine.MODID,name));
	}
	
	public void registerProcess(Class<? extends TickingProcess> process, ResourceLocation res) {
		registry.put(res, process);
	}
	
	public TickingProcess getByRes(ResourceLocation res) {
		Class<? extends TickingProcess> clazz = registry.get(res);
		
		if(clazz == null) return null;
		TickingProcess process = null;
		try {
			process = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return process;
	}
	
	public ResourceLocation getResByClass(Class<? extends TickingProcess> clazz) {
		for (Entry<ResourceLocation, Class<? extends TickingProcess>> entry : registry.entrySet()) {
	        if (Objects.equals(clazz, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
		return null;
	}
	
	public void register() {
		registerProcess(BootingProcess.class, "booting_process");
		registerProcess(WindowsCreateAccountProcess.class,"windows_create_account_process");
		registerProcess(MacCreateAccountProcess.class,"mac_create_account_process");
		registerProcess(WindowsDesktopProcess.class,"windows_desktop_process");
		registerProcess(MacDesktopProcess.class,"mac_desktop_process");
		registerProcess(WindowsLoginProcess.class,"windows_login_process");
		registerProcess(ShellProcess.class,"shell_process");
		registerProcess(NotesProcess.class,"notes_process");
		registerProcess(CCTVProcess.class,"cctv_process");
		registerProcess(TransitProcess.class,"transit_process");
		registerProcess(MapsProcess.class,"maps_process");
		registerProcess(EpidemicProcess.class,"epidemic_process");
	}
}
