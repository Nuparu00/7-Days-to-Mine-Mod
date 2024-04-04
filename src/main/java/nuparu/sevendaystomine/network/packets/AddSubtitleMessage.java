package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import nuparu.sevendaystomine.entity.EntityHuman;

public class AddSubtitleMessage implements IMessage {

	private int entityID;
	private String dialogue;
	private double duration;

	public AddSubtitleMessage() {

	}

	public AddSubtitleMessage(EntityHuman entity, String dialogue, double duration) {
		this.entityID = entity.getEntityId();
		this.dialogue = dialogue;
		this.duration = duration;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityID = buf.readInt();
		this.duration = buf.readDouble();
		this.dialogue = ByteBufUtils.readUTF8String(buf);

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeDouble(duration);
		ByteBufUtils.writeUTF8String(buf, dialogue);

	}

	public int getEntityID() {
		return this.entityID;
	}

	public String getDialogue() {
		return this.dialogue;
	}

	public double getDuration() {
		return this.duration;
	}

}
