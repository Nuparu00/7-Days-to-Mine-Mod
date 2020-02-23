package com.nuparu.sevendaystomine.util.computer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.script.ScriptException;

import com.nuparu.sevendaystomine.client.gui.monitor.Screen;
import com.nuparu.sevendaystomine.client.gui.monitor.elements.TextField;
import com.nuparu.sevendaystomine.events.HandleCommandEvent;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.StartProcessMessage;
import com.nuparu.sevendaystomine.proxy.CommonProxy;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.client.ColorRGBA;
import com.nuparu.sevendaystomine.util.client.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShellProcess extends WindowedProcess {

	private String input = "";
	private int historyPointer = 0;
	public LinkedList<String> log = new LinkedList<String>();
	public LinkedList<String> history = new LinkedList<String>();

	@SideOnly(Side.CLIENT)
	TextField field;

	public ShellProcess() {
		this(0, 0, 0, 0);
	}

	public ShellProcess(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.application = ApplicationRegistry.INSTANCE.getByString("shell");
	}

	@Override
	public String getTitle() {
		return "Command Line";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {
		super.render(partialTicks);
		drawWindow(getTitle(), new ColorRGBA(0, 0, 0), new ColorRGBA(0.8, 0.8, 0.8));
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, zLevel + 2);
		ArrayList<String> strings = new ArrayList<String>();
		for (int i = 0; i < log.size(); i++) {
			if (log.get(i) != null) {
				String s = log.get(i);
				while (s.length() > 0) {

					strings.add(s.substring(0, Math.min(s.length(), (int) (width / 6))));
					s = s.substring(Math.min(s.length(), (int) (width / 6)));
				}
			}
		}
		for (int i = 0; i < strings.size(); i++) {
			RenderUtils.drawString(strings.get(i), x, (y + 2 + Screen.screen.ySize * title_bar_height) + (10 * i),
					14737632);
		}
		GlStateManager.translate(0, 0, -zLevel - 2);
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
			field.cursorColor = new ColorRGBA(1, 1, 1);
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
		input = nbt.getString("input");
		log = new LinkedList<String>();
		NBTTagList logTag = nbt.getTagList("log", Constants.NBT.TAG_STRING);
		Iterator<NBTBase> it = logTag.iterator();
		while (it.hasNext()) {
			NBTBase base = it.next();
			if (base instanceof NBTTagString) {
				log.add(((NBTTagString) base).getString());
			}
		}
		
		NBTTagList historyTag = nbt.getTagList("history", Constants.NBT.TAG_STRING);
		Iterator<NBTBase> it2 = historyTag.iterator();
		while (it2.hasNext()) {
			NBTBase base = it2.next();
			if (base instanceof NBTTagString) {
				history.add(((NBTTagString) base).getString());
			}
		}
	}

	public void addTextToLog(String s) {
		if (s != null) {
			log.addLast(s);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);
		if (field.isFocused()) {
			if (keyCode == 28) {
				String t = field.getContentText();
				if (t.isEmpty()) {
					return;
				}
				addTextToLog(t);
				String s = handleCommand(t);
				historyPointer = 0;
				if (!s.isEmpty()) {
					addTextToLog(s);
				}
				field.setContentText("");
				input="";
				NBTTagCompound nbt = writeToNBT(new NBTTagCompound());
				PacketManager.startProcess.sendToServer(new StartProcessMessage(computerTE.getPos(), nbt));
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
		}
	}

	@SideOnly(Side.CLIENT)
	public String handleCommand(String command) {
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
		case "ipconfig":
			ipconfig();
			return "";
		case "compute":
			return compute(command.substring(command.indexOf(' ') + 1));
		}

		return "\"" + words[0] + "\" is not recognized as a command.";

	}

	@SideOnly(Side.CLIENT)
	public void help() {
		addTextToLog("HELP Provides Help information for Windows commands.");
		addTextToLog("IPCONFIG Displays all current TCP/IP network configuration values.");
		addTextToLog("COMPUTE Computes a JavaScript code.");
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
			if(object != null) {
				return object.toString();
			}
			else {
				String s = CommonProxy.sw.toString();
				return s.substring(0, Math.max(0,s.length()-3));
			}
		} catch (ScriptException exception) {
			exception.printStackTrace();
			return "An error occured while trying to perform the command:" + exception.getMessage();
		}
	}
}
