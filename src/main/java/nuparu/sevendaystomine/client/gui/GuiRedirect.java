package nuparu.sevendaystomine.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRedirect extends GuiMainMenu{
	public GuiScreen guiToShow;

	protected void keyTyped(char par1, int par2) {
	}

	public GuiRedirect(GuiScreen g) {
		guiToShow = g;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		super.initGui();
	}

	protected void actionPerformed(GuiButton butt) {
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);

		mc.displayGuiScreen(guiToShow);
	}
}
