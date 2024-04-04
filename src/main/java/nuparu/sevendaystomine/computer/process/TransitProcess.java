package nuparu.sevendaystomine.computer.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.ni.CodeBlock;
import nuparu.ni.Interpreter;
import nuparu.ni.Token;
import nuparu.sevendaystomine.client.gui.monitor.Screen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SaveDataMessage;
import nuparu.sevendaystomine.network.packets.SendPacketMessage;
import nuparu.sevendaystomine.network.packets.SendRedstoneSignalMessage;
import nuparu.sevendaystomine.util.ColorRGBA;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Tree;

public class TransitProcess extends WindowedProcess {

	private String text = "";
	private String prevText = "";
	private List<ITextComponent> output = new ArrayList<ITextComponent>();
	private int cursorPosition;
	private int selectionEnd;
	private int lineScrollOffset;

	public ShellProcess shellProcess = null;

	@SideOnly(Side.CLIENT)
	private ColorRGBA cursorColor;
	@SideOnly(Side.CLIENT)
	private int cursorCounter;

	private int syncTimer = 150;

	int scrollProgress;
	int lines;
	
	@SideOnly(Side.CLIENT)
	List<Token> tokens;

	@SideOnly(Side.CLIENT)
	Button runButton;
	@SideOnly(Side.CLIENT)
	Button buildButton;

	public TransitProcess() {
		super();
	}

	public TransitProcess(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.application = ApplicationRegistry.INSTANCE.getByString("transit");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientInit() {
		super.clientInit();
		
		cursorColor = new ColorRGBA(255, 255, 255);
		tokens = new ArrayList<Token>();

		tokens = Interpreter.tokenize(text);

		runButton = new Button(x, y + height * (0.75) - 10, 40, 10, Screen.screen, "Run", 1) {
			@Override
			public boolean isVisible() {
				return this.tickingProcess != null
						&& ((WindowedProcess) this.tickingProcess).isNotHidden((int)(this.x+this.width), (int)(this.y+this.height));
			}
		};
		runButton.background = true;
		runButton.border = false;
		runButton.textNormal = 0xffffff;
		runButton.normal = new ColorRGBA(90, 90, 90);
		runButton.hovered = new ColorRGBA(120, 120, 120);
		runButton.setZLevel(20);
		runButton.setFontSize(0.7);
		runButton.setProcess(this);
		elements.add(runButton);

		buildButton = new Button(x + 45, y + height * (0.75) - 10, 40, 10, Screen.screen, "Build", 2){
			@Override
			public boolean isVisible() {
				return this.tickingProcess != null
						&& ((WindowedProcess) this.tickingProcess).isNotHidden((int)(this.x+this.width), (int)(this.y+this.height));
			}
		};
		buildButton.background = true;
		buildButton.border = false;
		buildButton.textNormal = 0xffffff;
		buildButton.normal = new ColorRGBA(90, 90, 90);
		buildButton.hovered = new ColorRGBA(120, 120, 120);
		buildButton.setZLevel(20);
		buildButton.setFontSize(0.7);
		buildButton.setProcess(this);
		elements.add(buildButton);
	}

	@SideOnly(Side.CLIENT)
	public void clientTick() {
		super.clientTick();
		++cursorCounter;
		if (syncTimer > 0) {
			syncTimer--;
		}
		if (syncTimer == 0) {
			if (!prevText.equals(text)) {
				prevText = text;
				syncTimer = 150;
				sync();
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initWindow() {
		super.initWindow();

		if (runButton != null) {
			runButton.setX(x);
			runButton.setY(y + height * (0.75) - 10);
		}
		if (buildButton != null) {
			buildButton.setX(x + 45);
			buildButton.setY(y + height * (0.75) - 10);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		super.onButtonPressed(button, mouseButton);
		if (isMinimized())
			return;
		int buttonId = button.ID;
		switch (buttonId) {
		case 1:
			run();
			break;
		}

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("text", text);
		nbt.setInteger("progress", scrollProgress);
		nbt.setInteger("lines", lines);
		nbt.setInteger("cursorPosition", cursorPosition);
		nbt.setInteger("selectionEnd", cursorPosition);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		text = nbt.getString("text");
		prevText = text;
		lines = nbt.getInteger("lines");
		scrollProgress = nbt.getInteger("progress");
		cursorPosition = nbt.getInteger("cursorPosition");
		selectionEnd = nbt.getInteger("selectionEnd");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {
		super.render(partialTicks);
		drawWindow(getTitle(), new ColorRGBA(47, 47, 47), new ColorRGBA(67, 68, 71));
		RenderUtils.drawColoredRect(new ColorRGBA(0, 0, 0), x, y + height * (0.75), width, height / 4, zLevel + 1);

		String s = text;

		// Splits text to fit the page
		List<ITextComponent> l = GuiUtilRenderComponents.splitText(new TextComponentString(s),
				(int) Math.floor(width - 35), Screen.mc.fontRenderer, true, true);
		int k1 = Math.min(8192 / Screen.mc.fontRenderer.FONT_HEIGHT, l.size());
		lines = k1;
		scrollProgress = MathUtils.clamp(scrollProgress, 0, k1);

		GL11.glPushMatrix();
		// Cuts content outside the window
		RenderUtils.glScissor(Screen.mc, x, y + Screen.screen.ySize * title_bar_height + 2, width,
				(height * 0.75) - Screen.screen.ySize * title_bar_height - 2 - 10);
		GlStateManager.translate(0, -(scrollProgress * Screen.mc.fontRenderer.FONT_HEIGHT), 0);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		// Coordinates of top left corner
		double left = x + 2;
		double top = y + 2 + (Screen.screen.ySize * title_bar_height);

		// new line
		for (int l1 = 0; l1 < k1; ++l1) {
			ITextComponent itextcomponent2 = l.get(l1);
			GL11.glPushMatrix();
			GL11.glTranslated(0, 0, offsetRelativeZ(2));
			// Renders line number
			RenderUtils.drawString((l1 + 1) + "", left, top + l1 * Screen.mc.fontRenderer.FONT_HEIGHT, 0x939393);
			RenderUtils.drawString("|", left + 20, top + l1 * Screen.mc.fontRenderer.FONT_HEIGHT, 0x939393);

			// Gets the text and separates it by empty spaces, commas, operators, etc. but
			// also keeps them in the array
			String[] array = itextcomponent2.getUnformattedText()
					.split(String.format(Interpreter.WITH_DELIMITER, Interpreter.REGEX));
			ArrayList<String> words = (ArrayList<String>) Arrays.stream(array).collect(Collectors.toList());

			int pointer = 0;
			double xx = left + 25;
			for (int i = 0; i < words.size(); i++) {
				String word = words.get(i);
				int color = 0xbbbbbb;
				if (!word.trim().isEmpty()) {
					for (int j = pointer; j < tokens.size(); j++) {
						Token token = tokens.get(j);
						// 
						if (token.value instanceof String && word.trim().equals(token.value)) {
							color = token.type.getColor();
							pointer = j;
							break;
						}
					}
				}

				// System.out.println(l1 +" " + i + " " + word + " " + pointer + " " +
				// tokens.size());

				RenderUtils.drawString(word, xx, top + l1 * Screen.mc.fontRenderer.FONT_HEIGHT, color);
				xx += Screen.mc.fontRenderer.getStringWidth(words.get(i));
			}
			GL11.glTranslated(0, 0, -offsetRelativeZ(2));
			GL11.glPopMatrix();
		}
		if (this.isFocused()) {
			if (this.cursorCounter / 6 % 2 == 0) {
				if ((cursorPosition) <= text.length()) {
					String ss = text.substring(0, cursorPosition);
					l = GuiUtilRenderComponents.splitText(new TextComponentString(ss), (int) Math.floor(width - 35),
							Screen.mc.fontRenderer, true, true);
					k1 = Math.min(8192 / Screen.mc.fontRenderer.FONT_HEIGHT, l.size());
					int lineIndex = Math.max(0, k1 - 1);

					// 25 due to the fact that the actual text input start more right because of the
					// line counter
					renderCursor(
							left + 25 + Screen.mc.fontRenderer.getStringWidth(l.get(lineIndex).getUnformattedText()),
							top + lineIndex * Screen.mc.fontRenderer.FONT_HEIGHT, 1, Screen.mc.fontRenderer.FONT_HEIGHT,
							zLevel + 2);
				}
			}
		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslated(0, 0, offsetRelativeZ(2));
		//Copy of the origina llist so we do not get ConcurrentModificationException
		List<ITextComponent> outputFull = new ArrayList<ITextComponent>(getOutput());
		List<ITextComponent> output = outputFull.subList(Math.max(0,outputFull.size()-4), outputFull.size());
		for (int i = 0; i < output.size(); i++) {
			String out = output.get(i).getFormattedText();
			RenderUtils.drawString(out, left, y + height * (0.75) + Screen.mc.fontRenderer.FONT_HEIGHT * i + 2,
					0xEBEBEB);
		}
		GL11.glTranslated(0, 0, -offsetRelativeZ(2));

		GL11.glPopMatrix();
	}

	private void writeText(String newText) {
		if (text.length() >= Short.MAX_VALUE) {
			return;
		}
		String s = "";
		String s1 = newText;
		int i = this.cursorPosition;
		int k = text.length() - i;
		int l;
		if (i < text.length() && i >= 0) {
			String halfA = text.substring(0, i);
			String halfB = text.substring(i, text.length());
			s = halfA + s1 + halfB;
		} else {
			s = text + s1;
		}
		l = s1.length();
		text = s;
		setCursorPosition(i + l);
	}

	@SideOnly(Side.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);
		if (!isFocused() || isMinimized())
			return;
		switch (keyCode) {
		case 14: {
			if (text == null || text.length() == 0) {
				return;
			}
			int i = this.cursorPosition;
			int l;
			String s = "";
			if (i < this.text.length()) {
				String halfA = this.text.substring(0, i);
				String halfB = this.text.substring(i, this.text.length());
				if (halfA.length() > 0) {
					s = halfA.substring(0, halfA.length() - 1) + halfB;
					l = halfA.length() - 1;
				} else {
					s = halfA.substring(0, halfA.length()) + halfB;
					l = halfA.length();
				}

			} else {
				if (text.length() > 0) {
					s = text.substring(0, text.length() - 1);
					l = s.length();
				} else {
					s = text.substring(0, text.length());
					l = s.length();
				}
			}
			text = s;
			setCursorPosition(l);
			break;
		}
		case 15: {
			run();
			break;
		}
		case 28:
		case 156:
			
			this.writeText("\n");
			break;
		case 201:
			scrollProgress = MathUtils.clamp(--scrollProgress, 0, lines);
			break;
		case 209:
			scrollProgress = MathUtils.clamp(++scrollProgress, 0, lines);
			break;
		case 203:
			if (cursorPosition > 0) {
				setCursorPosition(--cursorPosition);
			}
			break;
		case 205:
			if (cursorPosition >= 0) {
				setCursorPosition(++cursorPosition);
			}
			break;
		default: {
			if (GuiScreen.isKeyComboCtrlV(keyCode)) {
				this.writeText(GuiScreen.getClipboardString());

			} else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
				this.writeText(Character.toString(typedChar));
			}
		}
		}
		tokens = Interpreter.tokenize(text);
		// 
	}

	// Runs the written code
	public void run() {
		Tree<Token> tree = Interpreter.parse(tokens);
		if (tree != null) {
			// tree.print("-", true);
			TransitProcess process = this;
			getOutput().clear();
			this.computerTE.codeBus = new Thread() {
				@Override
				public void run() {
					try {
						CodeBlock block = Interpreter.read(tree, null, process);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			
			this.computerTE.codeBus.start();

		}
	}

	public void setCursorPosition(int pos) {
		this.cursorPosition = pos;
		int i = this.text.length();
		this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
		this.setSelectionPos(this.cursorPosition);
	}

	public void setSelectionPos(int position) {
		int i = this.text.length();

		if (position > i) {
			position = i;
		}

		if (position < 0) {
			position = 0;
		}

		this.selectionEnd = position;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		double left = x + 2 + 25;
		double top = y + 2 + (Screen.screen.ySize * title_bar_height);

		if (this.isFocused()) {
			if (mouseX >= x && mouseX <= x + width && mouseY >= y
					&& mouseY <= y + (height * 0.75) - Screen.screen.ySize * title_bar_height - 2 - 10) {
				this.isFocused = true;

				// position of Mouse from top left corner of the text area
				int i = mouseX - (int) Math.ceil(left);
				int j = mouseY - (int) Math.ceil(top);

				List<ITextComponent> l = GuiUtilRenderComponents.splitText(new TextComponentString(text),
						(int) Math.floor(width - 35), Screen.mc.fontRenderer, true, true);

				int line = (int) Math.ceil((double) j / (double) Screen.mc.fontRenderer.FONT_HEIGHT) - 1;
				
				if (line < l.size() && line >= 0) {
					int newPosition = 0;
					for (int k = 0; k < line; k++) {
						String z = l.get(k).getUnformattedText();
						newPosition += l.get(k).getUnformattedText().length();
					}
					String z = Screen.mc.fontRenderer.trimStringToWidth(l.get(line).getUnformattedText(), i);
					this.setCursorPosition(z.length() + newPosition + Math.max(0, line));
					this.cursorCounter = 0;
				}

			}
		}
	}

	public void renderCursor(double x, double y, double width, double height, double zLevel) {
		GL11.glPushMatrix();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos((double) x, (double) y + height, zLevel)
				.color((float) cursorColor.R, (float) cursorColor.G, (float) cursorColor.B, (float) cursorColor.A)
				.endVertex();
		bufferbuilder.pos((double) x + width, (double) y + height, zLevel)
				.color((float) cursorColor.R, (float) cursorColor.G, (float) cursorColor.B, (float) cursorColor.A)
				.endVertex();
		bufferbuilder.pos((double) x + width, (double) y, zLevel)
				.color((float) cursorColor.R, (float) cursorColor.G, (float) cursorColor.B, (float) cursorColor.A)
				.endVertex();
		bufferbuilder.pos((double) x, (double) y, zLevel)
				.color((float) cursorColor.R, (float) cursorColor.G, (float) cursorColor.B, (float) cursorColor.A)
				.endVertex();
		tessellator.draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
		GL11.glPopMatrix();
	}

	@SideOnly(Side.CLIENT)
	public void saveToDevice(String data) {
		PacketManager.saveData.sendToServer(new SaveDataMessage(data, computerTE.getPos()));
	}

	@SideOnly(Side.CLIENT)
	public void sendPacket(String packet, BlockPos to) {
		PacketManager.sendPacket.sendToServer(new SendPacketMessage(computerTE.getPos(), to, packet));
	}
	
	@SideOnly(Side.CLIENT)
	public void sendRedstoneSignal(EnumFacing facing, int strength) {
		PacketManager.redstoneSignal.sendToServer(new SendRedstoneSignalMessage(strength,computerTE.getPos(),facing));
	}

	public List<ITextComponent> getOutput() {
		return output;
	}
}
