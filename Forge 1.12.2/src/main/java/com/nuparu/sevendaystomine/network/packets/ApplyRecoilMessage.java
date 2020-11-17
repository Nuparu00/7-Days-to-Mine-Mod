package com.nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ApplyRecoilMessage implements IMessage {


	protected float recoil;
	public ApplyRecoilMessage() {

	}

	public ApplyRecoilMessage(float recoil) {
		this.recoil = recoil;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		recoil = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(recoil);

	}
}
