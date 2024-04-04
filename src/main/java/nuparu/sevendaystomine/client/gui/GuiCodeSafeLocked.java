package nuparu.sevendaystomine.client.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SafeCodeMessage;
import nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;

@SideOnly(Side.CLIENT)
public class GuiCodeSafeLocked extends GuiScreen {

	private static final ResourceLocation resourceLocation = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/code_safe_locked.png");
	
	TileEntityCodeSafe safe;
	BlockPos pos;

	int guiLeft;
	int guiTop;
	int xSize = 110;
    int ySize = 100;

	public GuiCodeSafeLocked(TileEntity tileEntity, BlockPos pos) {
		if (!(tileEntity instanceof TileEntityCodeSafe)) {
			throw new IllegalArgumentException("Passed TileEntity is not isntance of TileEntityCodeSafe!");
		}
		this.safe = (TileEntityCodeSafe) tileEntity;
		this.pos = pos;
		Mouse.setGrabbed(false);
	}

	public void initGui() {
		super.initGui();
		Mouse.setGrabbed(false);
		guiLeft = (this.width - xSize) / 2;
		guiTop = (this.height - ySize) / 2;
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();

		int x1 = guiLeft + xSize/2 - 26;
		int x2 = guiLeft + xSize/2 - 6;
		int x3 = guiLeft + xSize/2 + 14;
		
		int y1 = guiTop + 23;
		int y2 = guiTop + 53;
		
		this.buttonList.add(new GuiButton(4, x1, y1, 12, 20, "+"));
		this.buttonList.add(new GuiButton(5, x1, y2, 12, 20, "-"));

		this.buttonList.add(new GuiButton(2, x2, y1, 12, 20, "+"));
		this.buttonList.add(new GuiButton(3, x2, y2, 12, 20, "-"));

		this.buttonList.add(new GuiButton(0, x3, y1, 12, 20, "+"));
		this.buttonList.add(new GuiButton(1, x3, y2, 12, 20, "-"));

	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	protected void drawGuiContainerBackgroundLayer(float particalTicks, int mouseX, int mouseY) {
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(resourceLocation);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);
		GlStateManager.popMatrix();
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int selectedCode = safe.getSelectedCode();

		int h = (selectedCode / 100) % 10;
		int d = (selectedCode / 10) % 10;
		int u = selectedCode % 10;
		
		int x1 = guiLeft + xSize/2 - 23;
		int x2 = guiLeft + xSize/2 - 3;
		int x3 = guiLeft + xSize/2 + 17;
		
		int y1 = guiTop + 15;
		int y2 = guiTop + 45;
		int y3 = guiTop + 75;
		
		this.fontRenderer.drawString(String.valueOf(h), x1, y2, 0xffffff);
		this.fontRenderer.drawString(String.valueOf(d), x2, y2, 0xffffff);
		this.fontRenderer.drawString(String.valueOf(u), x3, y2, 0xffffff);
		
		this.fontRenderer.drawString(String.valueOf(h==9?0:h+1), x1, y1, 0x555555);
		this.fontRenderer.drawString(String.valueOf(d==9?0:d+1), x2, y1, 0x555555);
		this.fontRenderer.drawString(String.valueOf(u==9?0:u+1), x3, y1, 0x555555);
		
		this.fontRenderer.drawString(String.valueOf(h==0?9:h-1), x1, y3, 0x555555);
		this.fontRenderer.drawString(String.valueOf(d==0?9:d-1), x2, y3, 0x555555);
		this.fontRenderer.drawString(String.valueOf(u==0?9:u-1), x3, y3, 0x555555);
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		int buttonID = button.id;
		if(buttonID >= 0 && buttonID <= 5) {
			
			int multpleOfTwo = buttonID/2;
			
			int toAdd = (int) Math.pow(10, multpleOfTwo+1);
			if(buttonID%2==0) {
				
			}else {
				toAdd = -toAdd;
			}
			PacketManager.safeCodeUpdate.sendToServer(new SafeCodeMessage(pos,toAdd));
			TileEntity te = Minecraft.getMinecraft().world.getTileEntity(pos);
			if(te != null && te instanceof TileEntityCodeSafe) {
				safe = (TileEntityCodeSafe)te;
			}
		}
	}

}
