package com.nuparu.sevendaystomine.util.computer;

import com.nuparu.sevendaystomine.client.gui.monitor.Screen;
import com.nuparu.sevendaystomine.client.gui.monitor.elements.TextField;
import com.nuparu.sevendaystomine.util.client.ColorRGBA;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NotesProcess extends WindowedProcess {

	String text;
	TextField field;
	
	public NotesProcess() {
		super();
	}
	
	public NotesProcess(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.application = ApplicationRegistry.INSTANCE.getByString("notes");
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
			field.setContentText(text);
			field.numberOfLines = (int) Math.floor((height-Screen.screen.ySize * title_bar_height)/Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT);
			elements.add(field);
		}
	}

	@Override
	public String getTitle() {
		return "Notes";
	}
	
	@Override
	public void tick() {
		super.tick();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {
		super.render(partialTicks);
		drawWindow(getTitle(),new ColorRGBA(1,0.949,0.671),new ColorRGBA(1,0.921,0.505));
	}
}
