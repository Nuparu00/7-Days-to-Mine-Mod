package nuparu.sevendaystomine.computer.process;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import nuparu.sevendaystomine.client.gui.monitor.Screen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.gui.monitor.elements.TextField;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.tileentity.TileEntityComputer.EnumState;
import nuparu.sevendaystomine.util.ColorRGBA;

public class WindowsLoginProcess extends TickingProcess {


	public final ResourceLocation DEFAULT_PROFILE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/computer/default_profile.png");

	public String password = "";
	public boolean completed = false;

	public WindowsLoginProcess() {

	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound nbt2 = super.writeToNBT(nbt);
		nbt2.setString("password", password);
		nbt2.setBoolean("completed", completed);
		return nbt2;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("password")) {
			this.password = nbt.getString("password");
		}
		if (nbt.hasKey("completed")) {
			this.completed = nbt.getBoolean("completed");
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (computerTE.getState() != EnumState.LOGIN) {
			computerTE.killProcess(this);
			return;
		}
		if (completed) {

			if (computerTE != null && computerTE.verifyPassword(this.password)) {
				computerTE.onLogin(this);
			} else {
				completed = false;
				password = "";
			}
			computerTE.markDirty();
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientInit() {
		super.clientInit();
		double fieldWidth = screen.xSize / 3d;
		double buttonWidth = screen.xSize / 4d;
		double iconSize = screen.ySize / 3d;
		TextField field = new TextField((float) screen.getRelativeX(0.5) - (fieldWidth / 2d),
				(float) (screen.getRelativeY(0) + 25 + iconSize), fieldWidth, 9, screen);
		field.setDefaultText("Password");
		field.setProcess(this);
		elements.add(field);

		Button button = new Button((float) screen.getRelativeX(0.5) - (buttonWidth / 2d),
				(float) (screen.getRelativeY(0) + 60 + iconSize), buttonWidth, 9, screen, "computer.win10.login", 0);
		button.textNormal = 0xffffff;
		button.normal = new ColorRGBA(0, 0.328125, 0.63671875);
		button.setFontSize(0.7);
		button.setProcess(this);
		elements.add(button);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {

		ScaledResolution sr = new ScaledResolution(Screen.mc);

		RenderUtils.drawColoredRect(new ColorRGBA(0, 0.328125, 0.63671875),
				(sr.getScaledWidth() / 2) - (screen.xSize / 2), (sr.getScaledHeight() / 2) - (screen.ySize / 2),
				screen.xSize, screen.ySize, 0);
		super.render(partialTicks);
		RenderUtils.drawString(SevenDaysToMine.proxy.localize("computer.win10.welcome_back"),
				(int) screen.localXToGlobal(20), (int) screen.getRelativeY(0.15), 0xffffff);

		double iconSize = screen.ySize / 3d;
		RenderUtils.drawCenteredString(computerTE.getUsername(), (float) screen.getRelativeX(0.5),
				(float) (screen.getRelativeY(0) + 10 + iconSize), 0xffffff);
		RenderUtils.drawCenteredString("Hint: " + computerTE.getHint(), (float) screen.getRelativeX(0.5),
				(float) (screen.getRelativeY(0) + 45 + iconSize), 0xffffff);

		GlStateManager.pushMatrix();
		GlStateManager.enableAlpha();
		RenderUtils.drawTexturedRect(DEFAULT_PROFILE, (float) screen.getRelativeX(0.5) - (iconSize / 2d),
				(float) screen.getRelativeY(0) + 10, 0, 0, iconSize, iconSize, iconSize, iconSize, 1, 1);
		GlStateManager.disableAlpha();
		GlStateManager.popMatrix();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initWindow() {
		if (elements.size() == 0) {
			return;
		}
		double fieldWidth = screen.xSize / 3d;
		double buttonWidth = screen.xSize / 4d;
		double iconSize = screen.ySize / 3d;

		IScreenElement element0 = elements.get(0);

		element0.setX((float) screen.getRelativeX(0.5) - (fieldWidth / 2d));
		element0.setY((float) (screen.getRelativeY(0) + 25 + iconSize));
		element0.setWidth(fieldWidth);
		element0.setHeight(9);

		IScreenElement element1 = elements.get(1);

		element1.setX((float) screen.getRelativeX(0.5) - (buttonWidth / 2d));
		element1.setY((float) (screen.getRelativeY(0) + 60 + iconSize));
		element1.setWidth(buttonWidth);
		element1.setHeight(9);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		int buttonId = button.ID;
		if (buttonId == 0) {
			if (computerTE.isRegistered() == true && computerTE.getState() == EnumState.LOGIN && completed == false) {
				TextField p = (TextField) elements.get(0);
				this.password = p.getContentText();
				this.completed = true;
				sync("password", "completed");
				p.setContentText("");
				this.completed = false;

			}
		}
	}
}
