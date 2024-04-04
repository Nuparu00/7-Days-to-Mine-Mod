package nuparu.sevendaystomine.computer.process;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.Screen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.entity.EntityCameraView;
import nuparu.sevendaystomine.tileentity.TileEntityCamera;
import nuparu.sevendaystomine.util.ColorRGBA;

public class CCTVProcess extends WindowedProcess {

	public int camera = 0;
	public List<TileEntityCamera> cameras = new ArrayList<TileEntityCamera>();

	@SideOnly(Side.CLIENT)
	Button button1;
	@SideOnly(Side.CLIENT)
	Button button2;

	public CCTVProcess() {
		this(0, 0, 0, 0);
	}

	public CCTVProcess(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.application = ApplicationRegistry.INSTANCE.getByString("cctv");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {
		drawWindow(getTitle(), new ColorRGBA(0.1d, 0.1d, 0.1d), new ColorRGBA(0.8, 0.8, 0.8));
		ScaledResolution r = new ScaledResolution(Screen.mc);
		int scale = r.getScaleFactor();

		GL11.glPushMatrix();
		int yy = Screen.mc.displayHeight - (int) Math.round(y * scale) - (int) Math.round(height * scale);
		if (cameras.size() > camera && cameras.get(camera) != null) {
			cameras.get(camera).validate();
			EntityCameraView view = cameras.get(camera).getCameraView(screen.gui.player);
			if (view != null) {
				RenderUtils.renderView(Screen.mc, view, (int) Math.round(width * scale),
						(int) Math.round((height - (Screen.screen.ySize * title_bar_height)) * scale),
						Screen.mc.displayWidth, Screen.mc.displayHeight, (int) Math.round(x * scale), yy, -2000, partialTicks);
				GL11.glPopMatrix();
			}
			else if(isNotHidden((int) (x + width / 2), (int) (y + (Screen.screen.ySize * title_bar_height) + 2))) {
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glTranslated(0, 0, 10);
			GL11.glTranslated(x + width / 2, y + (Screen.screen.ySize * title_bar_height) + 2, 0);
			GL11.glScaled(0.75, 0.75, 1);
			RenderUtils.drawCenteredString(SevenDaysToMine.proxy.localize("computer.app.cctv.no.signal"), 0, 0,
					0xffffff, true);
			GL11.glPopMatrix();
			}
		}

		super.render(partialTicks);
		GL11.glPushMatrix();
		GL11.glTranslated(0, 0, 10);
		if (cameras.size() > camera
				&& isNotHidden((int) (x + 1), (int) (y + (Screen.screen.ySize * title_bar_height) + 2))) {
			RenderUtils.drawString(cameras.get(camera).getCustomName(), x + 1,
					y + (Screen.screen.ySize * title_bar_height) + 2, 0xffffff, true);
		} else if (cameras.isEmpty()
				&& isNotHidden((int) (x + width / 2), (int) (y + (Screen.screen.ySize * title_bar_height) + 2))) {
			GL11.glTranslated(x + width / 2, y + (Screen.screen.ySize * title_bar_height) + 2, 0);
			GL11.glScaled(0.75, 0.75, 1);
			RenderUtils.drawCenteredString(SevenDaysToMine.proxy.localize("computer.app.cctv.no.camera"), 0, 0,
					0xffffff, true);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}

	@Override
	public void tick() {
		super.tick();
		if (camera < 0)
			camera = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientTick() {
		super.clientTick();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientInit() {
		super.clientInit();
		for (BlockPos pos : getTE().getConnections()) {
			TileEntity te = getTE().getWorld().getTileEntity(pos);
			if (te != null && te instanceof TileEntityCamera) {
				cameras.add((TileEntityCamera) te);
			}
		}

		button1 = new Button(x + 1, y + height - 16, 10, 10, Screen.screen, "<<", 1) {
			@Override
			public boolean isVisible() {
				return this.tickingProcess != null
						&& ((WindowedProcess) this.tickingProcess).isNotHidden((int) this.x, (int) this.y);
			}
		};
		button1.background = false;
		button1.border = false;
		button1.textNormal = 0xffffff;
		button1.setZLevel(20);
		button1.setFontSize(0.7);
		button1.setProcess(this);
		elements.add(button1);

		button2 = new Button(x + width - 11, y + height - 16, 10, 10, Screen.screen, ">>", 2) {
			@Override
			public boolean isVisible() {
				return this.tickingProcess != null
						&& ((WindowedProcess) this.tickingProcess).isNotHidden((int) this.x, (int) this.y);
			}
		};
		button2.background = false;
		button2.border = false;
		button2.textNormal = 0xffffff;
		button2.setZLevel(20);
		button2.setFontSize(0.7);
		button2.setProcess(this);
		elements.add(button2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initWindow() {
		super.initWindow();
		if (elements.size() < 2) {
			return;
		}
		button1.setX(x + 1);
		button1.setY(y + height - 16);

		button2.setX(x + width - 11);
		button2.setY(y + height - 16);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onDragReleased() {
		super.onDragReleased();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("cam", camera);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		camera = nbt.getInteger("cam");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		super.onButtonPressed(button, mouseButton);
		if (isMinimized())
			return;
		int buttonId = button.ID;
		if (buttonId == 1) {
			if (camera - 1 < 0) {
				camera = Math.max(0, cameras.size() - 1);
			} else {
				--camera;
			}
			sync();
		} else if (buttonId == 2) {
			if (camera + 1 > cameras.size() - 1) {
				camera = 0;
			} else {
				++camera;
			}
			sync();
		}

	}
}
