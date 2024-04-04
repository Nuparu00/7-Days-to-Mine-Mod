package nuparu.sevendaystomine.computer.process;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import nuparu.sevendaystomine.client.gui.monitor.Screen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SyncProcessMessage;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.util.ColorRGBA;

public abstract class TickingProcess {

	protected TileEntityComputer computerTE;

	public boolean noTimeLimit = false;
	public long existedFor = 0;
	public long duration = 0;

	protected ArrayList<IScreenElement> elements = new ArrayList<IScreenElement>();

	@SideOnly(Side.CLIENT)
	protected Screen screen;
	@SideOnly(Side.CLIENT)
	public boolean clientInit;

	protected UUID id;

	public TickingProcess() {
		id = UUID.randomUUID();
	}

	@SideOnly(Side.CLIENT)
	public void clientInit() {
		clientInit = true;
	}

	@SideOnly(Side.CLIENT)
	public void clientTick() {
		if (!clientInit)
			return;
		for (IScreenElement element : elements) {
			element.update();
		}
	}

	public void tick() {
		existedFor++;
	}

	public void onOpen() {

	}

	public void onClose() {

	}

	@SideOnly(Side.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {

	}

	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {
		for (IScreenElement element : elements) {
			GlStateManager.pushMatrix();
			element.render(partialTicks);
			GlStateManager.popMatrix();
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setString(ProcessRegistry.RES_KEY, ProcessRegistry.INSTANCE.getResByClass(this.getClass()).toString());
		nbt.setBoolean("noTimeLimit", noTimeLimit);
		nbt.setLong("existedFor", existedFor);
		nbt.setLong("duration", duration);
		nbt.setUniqueId("id", id);
		return nbt;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("noTimeLimit")) {
			noTimeLimit = nbt.getBoolean("noTimeLimit");
		}
		if (nbt.hasKey("existedFor")) {
			existedFor = nbt.getLong("existedFor");
		}
		if (nbt.hasKey("duration")) {
			duration = nbt.getLong("duration");
		}
		id = nbt.getUniqueId("id");

	}

	public void setComputer(TileEntityComputer computerTE) {
		this.computerTE = computerTE;
	}

	@SideOnly(Side.CLIENT)
	public void addElement(IScreenElement element) {
		elements.add(element);
	}

	@SideOnly(Side.CLIENT)
	public void removeElement(IScreenElement element) {
		elements.remove(element);
	}

	public UUID getId() {
		return id;
	}

	@SideOnly(Side.CLIENT)
	public Screen getScreen() {
		return screen;
	}

	@SideOnly(Side.CLIENT)
	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	@SideOnly(Side.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		for (IScreenElement element : elements) {
			element.keyTyped(typedChar, keyCode);
		}
	}

	@SideOnly(Side.CLIENT)
	public void mouseReleased(int mouseX, int mouseY, int state) {
		for (IScreenElement element : elements) {
			element.mouseReleased(mouseX, mouseY, state);
		}
	}

	@SideOnly(Side.CLIENT)
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		for (IScreenElement element : elements) {
			element.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}

	@SideOnly(Side.CLIENT)
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (IScreenElement element : elements) {
			element.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@SideOnly(Side.CLIENT)
	public void initWindow() {

	}

	public TileEntityComputer getTE() {
		return this.computerTE;
	}

	@SideOnly(Side.CLIENT)
	public void sync() {
		
		NBTTagCompound nbt = writeToNBT(new NBTTagCompound());
		PacketManager.syncProcess.sendToServer(new SyncProcessMessage(computerTE.getPos(), nbt));

	}

	@SideOnly(Side.CLIENT)
	public void sync(String... fields) {
		
		NBTTagCompound nbt = new NBTTagCompound();
		for (String s : fields) {
			Class<?> clazz = this.getClass();
			Field f = null;
			while (f == null && clazz != null) // stop when we got field or reached top of class hierarchy
			{
				try {
					f = clazz.getDeclaredField(s);
				} catch (NoSuchFieldException e) {
					clazz = clazz.getSuperclass();
				}
			}

			if (f == null)
				continue;
			try {
				f.setAccessible(true);
				Object fieldValue;

				fieldValue = f.get(this);

				if (fieldValue == null)
					continue;
				if (fieldValue instanceof Integer) {
					nbt.setInteger(s, (int) fieldValue);
				} else if (fieldValue instanceof Double) {
					nbt.setDouble(s, (double) fieldValue);
				} else if (fieldValue instanceof Float) {
					nbt.setFloat(s, (float) fieldValue);
				} else if (fieldValue instanceof Long) {
					nbt.setLong(s, (long) fieldValue);
				} else if (fieldValue instanceof String) {
					nbt.setString(s, (String) fieldValue);
				} else if (fieldValue instanceof Boolean) {
					nbt.setBoolean(s, (Boolean) fieldValue);
				} else if (fieldValue instanceof Byte) {
					nbt.setByte(s, (Byte) fieldValue);
				} else if (fieldValue instanceof Short) {
					nbt.setShort(s, (short) fieldValue);
				}

				else if (fieldValue instanceof List) {
					List<?> list = (List<?>) fieldValue;
					if (list.isEmpty())
						continue;
					Object first = list.get(0);

					NBTTagList nbtList = new NBTTagList();
					if (first instanceof Integer) {
						for (Object i : list) {
							nbtList.appendTag(new NBTTagInt((int) i));
						}
					} else if (first instanceof Double) {
						for (Object i : list) {
							nbtList.appendTag(new NBTTagDouble((double) i));
						}
					} else if (first instanceof Float) {
						for (Object i : list) {
							nbtList.appendTag(new NBTTagFloat((float) i));
						}
					} else if (first instanceof Long) {
						for (Object i : list) {
							nbtList.appendTag(new NBTTagLong((long) i));
						}
					} else if (first instanceof String) {
						for (Object i : list) {
							nbtList.appendTag(new NBTTagString((String) i));
						}
					} else if (fieldValue instanceof Boolean) {
						for (Object i : list) {
							nbtList.appendTag(new NBTTagByte((byte) i));
						}
					} else if (first instanceof Byte) {
						for (Object i : list) {
							nbtList.appendTag(new NBTTagByte((byte) i));
						}
					} else if (first instanceof Short) {
						for (Object i : list) {
							nbtList.appendTag(new NBTTagShort((short) i));
						}
					}
					nbt.setTag(s, nbtList);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		nbt.setUniqueId("id", id);
		nbt.setString(ProcessRegistry.RES_KEY, ProcessRegistry.INSTANCE.getResByClass(this.getClass()).toString());
		if (computerTE != null && nbt != null) {
			PacketManager.syncProcess.sendToServer(new SyncProcessMessage(computerTE.getPos(), nbt));
			// 
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderTooltip(int mouseX, int mouseY, FontRenderer fontRenderer, Object...text) {
		int height = text.length * (fontRenderer.FONT_HEIGHT + 1);
		int width = 0;
		for (Object s : text) {
			int i = fontRenderer.getStringWidth(s.toString());
			if (i > width) {
				width = i;
			}
		}


		RenderUtils.drawColoredRect(new ColorRGBA(1d, 1d, 1d), screen.mouseX, screen.mouseY, width, height, 1);
		GL11.glTranslatef(0, 0, 1);
		for (int i = 0; i < text.length; i++) {
			RenderUtils.drawString(text[i].toString(), screen.mouseX, screen.mouseY + 1 + i * (fontRenderer.FONT_HEIGHT + 1),
					0x000000);
		}
		GL11.glTranslatef(0, 0, -1);
	}
}
