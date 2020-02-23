package com.nuparu.sevendaystomine.client.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.tileentity.TileEntityKeySafe;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiKeySafeLocked extends GuiScreen {

	
	private static final ResourceLocation lock = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/lock.png");
	
	TileEntityKeySafe safe;
	BlockPos pos;

	int guiLeft;
	int guiTop;
	int xSize = 110;
    int ySize = 100;
    
	public GuiKeySafeLocked(TileEntity tileEntity, BlockPos pos) {
		if (!(tileEntity instanceof TileEntityKeySafe)) {
			throw new IllegalArgumentException("Passed TileEntity is not isntance of TileEntityKeySafe!");
		}
		this.safe = (TileEntityKeySafe) tileEntity;
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
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		double angle = getAngleBetweenVectors(width/2, mouseX, height/2,mouseY,0);
		System.out.println(angle);
	}
	
	public double getAngleBetweenVectors(int x1, int x2, int y1, int y2, double addRotation) {
		double angle = Math.atan2(y2 - y1, x2 - x1) * 180 / Math.PI;
		if(addRotation != 0) {
			angle+=addRotation;
		}
		return angle;
	}

	protected void drawGuiContainerBackgroundLayer(float particalTicks, int mouseX, int mouseY) {
		
		double angle = getAngleBetweenVectors(width/2, mouseX, height/2,mouseY,-90);
		
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(lock);
		GL11.glTranslatef(width/2,height/2,0);
		GL11.glRotated(angle, 0, 0, 1);
		GL11.glTranslatef(-(width/2),-(height/2),0);
		drawTexturedModalRect(width/2-50,height/2-50,0,0,100,100,100,100,1,this.zLevel);
		GlStateManager.popMatrix();
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

	}
	
	public void drawTexturedModalRect(double x, double y, int textureX, int textureY, int width, int height, int imageWidth, int imageHeight, double scale, double zLevel)
    {
		double minU = (double)textureX / (double)imageWidth;
		double maxU = (double)(textureX + width) / (double)imageWidth;
		double minV = (double)textureY / (double)imageHeight;
		double maxV = (double)(textureY + height) / (double)imageHeight;
		
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x + scale*(double)width, y + scale*(double)height,zLevel).tex(maxU, maxV).endVertex();
        bufferbuilder.pos(x + scale*(double)width, y, zLevel).tex(maxU, minV).endVertex();
        bufferbuilder.pos(x, y, zLevel).tex(minU, minV).endVertex();
        bufferbuilder.pos(x, y + scale*(double)height, zLevel).tex(minU, maxV).endVertex();
        tessellator.draw();
    }

}
