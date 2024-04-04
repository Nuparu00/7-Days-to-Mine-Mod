package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import nuparu.sevendaystomine.capability.ExtendedPlayer;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

public class PlayerCapabilitySyncMessage implements IMessage {

	IExtendedPlayer extendedPlayer;
	int playerID;

	public PlayerCapabilitySyncMessage() {
		this.extendedPlayer = null;
		this.playerID = 0;
	}

	public PlayerCapabilitySyncMessage(IExtendedPlayer extendedPlayer, EntityPlayer player) {
		this.extendedPlayer = extendedPlayer;
		this.playerID = player.getEntityId();
	}

	public PlayerCapabilitySyncMessage(IExtendedPlayer extendedPlayer, int playerID) {
		this.extendedPlayer = extendedPlayer;
		this.playerID = playerID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		playerID = buf.readInt();
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		if (nbt == null)
			return;
		extendedPlayer = new ExtendedPlayer();
		extendedPlayer.readNBT(nbt);
	}

	@Override
	public void toBytes(ByteBuf buf) {

		buf.writeInt(playerID);
		NBTTagCompound nbt = new NBTTagCompound();
		if (extendedPlayer != null) {
			extendedPlayer.writeNBT(nbt);
		}
		ByteBufUtils.writeTag(buf, nbt);
	}

	public IExtendedPlayer getExtendedPlayer() {
		return this.extendedPlayer;
	}

	public int getPlayerID() {
		return playerID;
	}
}