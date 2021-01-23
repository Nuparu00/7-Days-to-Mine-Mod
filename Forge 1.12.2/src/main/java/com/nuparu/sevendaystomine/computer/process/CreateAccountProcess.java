package com.nuparu.sevendaystomine.computer.process;

import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import com.nuparu.sevendaystomine.client.gui.monitor.Screen;
import com.nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import com.nuparu.sevendaystomine.client.gui.monitor.elements.TextField;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.StartProcessMessage;
import com.nuparu.sevendaystomine.util.ColorRGBA;
import com.nuparu.sevendaystomine.util.client.RenderUtils;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class CreateAccountProcess extends TickingProcess {


	public String username = "";
	public String password = "";
	public String hint = "";

	protected boolean completed = false;

	public CreateAccountProcess() {
		super();
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound nbt2 = super.writeToNBT(nbt);
		nbt2.setString("username", username);
		nbt2.setString("password", password);
		nbt2.setString("hint", hint);
		nbt2.setBoolean("completed", completed);
		return nbt2;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.username = nbt.getString("username");
		this.password = nbt.getString("password");
		this.hint = nbt.getString("hint");
		this.completed = nbt.getBoolean("completed");
	}

	@Override
	public void tick() {
		super.tick();
		if (computerTE.isRegistered()) {
			computerTE.killProcess(this);
			return;
		}
		if (completed) {
			if (!username.isEmpty()) {

				if (computerTE != null && computerTE.isRegistered() == false) {
					computerTE.onAccountCreated(this);
				} else {
					computerTE.killProcess(this);
				}
			} else {
				computerTE.killProcess(this);
			}
		}

	}

}
