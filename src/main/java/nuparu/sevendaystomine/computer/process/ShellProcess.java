package nuparu.sevendaystomine.computer.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.script.ScriptException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.Screen;
import nuparu.sevendaystomine.client.gui.monitor.elements.TextField;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.events.HandleCommandEvent;
import nuparu.sevendaystomine.proxy.CommonProxy;
import nuparu.sevendaystomine.util.ColorRGBA;
import nuparu.sevendaystomine.util.MathUtils;

public class ShellProcess extends WindowedProcess {

	private String input = "";
	private int historyPointer = 0;
	public LinkedList<String> log = new LinkedList<String>();
	public LinkedList<String> history = new LinkedList<String>();

	@SideOnly(Side.CLIENT)
	TextField field;
	@SideOnly(Side.CLIENT)
	int scrollProgress;
	@SideOnly(Side.CLIENT)
	int logLines;

	public ShellProcess() {
		this(0, 0, 0, 0);
	}

	public ShellProcess(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.application = ApplicationRegistry.INSTANCE.getByString("shell");
	}

	@Override
	public String getTitle() {
		return SevenDaysToMine.proxy.localize("computer.app.command_line");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {
		super.render(partialTicks);
		drawWindow(getTitle(), new ColorRGBA(0d, 0d, 0d), new ColorRGBA(0.8, 0.8, 0.8));
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, offsetRelativeZ(2));
		ArrayList<String> strings = new ArrayList<String>();
		String branding = "Microsoft Windows [Version 10]";
		while (branding.length() > 0) {

			strings.add(branding.substring(0, Math.min(branding.length(), (int) (width / 6))));
			branding = branding.substring(Math.min(branding.length(), (int) (width / 6)));
		}
		for (int i = 0; i < log.size(); i++) {
			if (log.get(i) != null) {
				String s = log.get(i);
				while (s.length() > 0) {

					strings.add(s.substring(0, Math.min(s.length(), (int) (width / 6))));
					s = s.substring(Math.min(s.length(), (int) (width / 6)));
				}
			}
		}
		logLines = strings.size() - (int) ((height - 10 - Screen.screen.ySize * title_bar_height - 4) / 10);
		if (logLines < 0) {
			logLines = 0;
		}
		scrollProgress = MathUtils.clamp(scrollProgress, 0, logLines);

		RenderUtils.glScissor(Screen.mc, x, y + Screen.screen.ySize * title_bar_height + 4, width,
				height - 10 - Screen.screen.ySize * title_bar_height - 4);
		GlStateManager.translate(0, (-logLines * 10) + (scrollProgress * 10 + 4), 0);
		// GlStateManager.translate(0, -10, 0);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		for (int i = 0; i < strings.size(); i++) {
			RenderUtils.drawString(strings.get(i), x, (y + 2 + Screen.screen.ySize * title_bar_height) + (10 * i),
					14737632);
		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.translate(0, 0, -offsetRelativeZ(2));
		GlStateManager.popMatrix();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientTick() {
		super.clientTick();
		if (field != null) {
			input = field.getContentText();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientInit() {
		super.clientInit();
		if (field == null) {
			field = new TextField(x, y + height - 9, this.width, 9, screen);
			field.setProcess(this);
			field.enabledColor = 0xffffff;
			field.backgroundColor = new ColorRGBA(0, 0, 0, 0);
			field.cursorColor = new ColorRGBA(1d, 1d, 1d);
			field.setZLevel(zLevel + 1);
			field.setContentText(input);
			elements.add(field);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initWindow() {
		super.initWindow();
		if (elements.size() < 2) {
			return;
		}

		field.setX(x);
		field.setY(y + height - 9);
		field.setWidth(this.width);
		field.setHeight(9);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onDragReleased() {
		if (field != null) {
			field.setContentText(input);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("input", input);
		NBTTagList logTag = new NBTTagList();
		for (String str : log) {
			logTag.appendTag(new NBTTagString(str));
		}
		nbt.setTag("log", logTag);

		NBTTagList historyTag = new NBTTagList();
		for (String str : history) {
			historyTag.appendTag(new NBTTagString(str));
		}
		nbt.setTag("history", historyTag);

		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("input")) {
			input = nbt.getString("input");
		}
		log = new LinkedList<String>();
		if (nbt.hasKey("log")) {
			NBTTagList logTag = nbt.getTagList("log", Constants.NBT.TAG_STRING);
			Iterator<NBTBase> it = logTag.iterator();
			while (it.hasNext()) {
				NBTBase base = it.next();
				if (base instanceof NBTTagString) {
					log.add(((NBTTagString) base).getString());
				}
			}
		}
		if (nbt.hasKey("history")) {
			NBTTagList historyTag = nbt.getTagList("history", Constants.NBT.TAG_STRING);
			Iterator<NBTBase> it2 = historyTag.iterator();
			while (it2.hasNext()) {
				NBTBase base = it2.next();
				if (base instanceof NBTTagString) {
					history.add(((NBTTagString) base).getString());
				}
			}
		}
	}

	public void addTextToLog(String s) {
		if (s != null) {
			while (log.size() > 10) {
				log.removeFirst();
			}
			log.addLast(s);
			sync("log");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);
		if (field.isFocused() && !isMinimized()) {
			if (keyCode == 28) {
				scrollProgress = 0;
				String t = field.getContentText();
				if (t.isEmpty()) {
					return;
				}
				addTextToLog(t);
				String s = handleCommand(t);
				sync("history");
				sync("log");
				historyPointer = 0;
				if (!s.isEmpty()) {
					addTextToLog(s);
				}
				field.setContentText("");
				input = "";
			} else if (keyCode == 200) {
				if (history.size() > 0) {
					historyPointer = MathUtils.clamp(historyPointer + 1, 0, history.size() - 1);
					this.field.setContentText(history.get(history.size() - 1 - historyPointer));
				}
			} else if (keyCode == 208) {
				if (history.size() > 0) {
					historyPointer = MathUtils.clamp(historyPointer - 1, 0, history.size() - 1);
					this.field.setContentText(history.get(history.size() - 1 - historyPointer));
				}
			}
		} else {
			// up
			if (keyCode == 201) {
				scrollProgress = MathUtils.clamp(++scrollProgress, 0, logLines);
			} else if (keyCode == 209) {
				scrollProgress = MathUtils.clamp(--scrollProgress, 0, logLines);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public String handleCommand(String command) {
		while (history.size() > 10) {
			history.removeFirst();
		}
		history.add(command);
		HandleCommandEvent event = new HandleCommandEvent(this.computerTE, this, command);
		MinecraftForge.EVENT_BUS.post(event);
		history.add(event.command);
		if (event.override) {
			return event.output;
		}
		String[] words = input.split(" ");
		switch (words[0].toLowerCase()) {
		case "help":
			help();
			return "";
		case "connections":
			connections();
			return "";
		case "ipconfig":
			ipconfig();
			return "";
		case "compute":
			return compute(command.substring(command.indexOf(' ') + 1));
		case "run":
			run(words);
			return "";
		}

		return "\"" + words[0] + "\" is not recognized as a command.";

	}

	@SideOnly(Side.CLIENT)
	public void help() {
		addTextToLog("HELP Provides Help information for Windows commands.");
		addTextToLog("IPCONFIG Displays all current TCP/IP network configuration values.");
		addTextToLog("COMPUTE Computes a JavaScript code.");
		addTextToLog("RUN Runs a program.");
		addTextToLog("CONNECTIONS Prints all connected devices.");
		addTextToLog("DISCONNECTALL Disconnets all connected devices.");
	}

	@SideOnly(Side.CLIENT)
	public void ipconfig() {
		addTextToLog("IPv4 Address: " + computerTE.getPos().getX() + "." + computerTE.getPos().getY() + "."
				+ computerTE.getPos().getZ() + "." + Minecraft.getMinecraft().world.provider.getDimension());
	}

	@SideOnly(Side.CLIENT)
	public String compute(String input) {
		try {
			CommonProxy.sw.getBuffer().setLength(0);
			Object object = CommonProxy.engine.eval(input);
			if (object != null) {
				return object.toString();
			} else {
				String s = CommonProxy.sw.toString();
				return s.substring(0, Math.max(0, s.length() - 3));
			}
		} catch (ScriptException exception) {
			exception.printStackTrace();
			return "An error occured while trying to perform the command:" + exception.getMessage();
		}
	}

	@SideOnly(Side.CLIENT)
	public void run(String[] words) {
		if (words.length == 2 && words[1].equals("cbburner.exe")) {
			addTextToLog("F");
		} else {
			addTextToLog("\"" + words[1] + "\" is not recognized as a program.");
		}
	}

	@SideOnly(Side.CLIENT)
	public void connections() {
		List<BlockPos> connections = this.computerTE.getConnections();
		for (BlockPos connection : connections) {
			addTextToLog(connection.getX() + "/" + connection.getY() + "/" + connection.getZ() + " " + new BigDecimal(connection.getDistance(this.computerTE.getPos().getX(),
					this.computerTE.getPos().getY(), this.computerTE.getPos().getZ())).setScale(3,BigDecimal.ROUND_HALF_UP).toString() +" m");
		}
	}
}
