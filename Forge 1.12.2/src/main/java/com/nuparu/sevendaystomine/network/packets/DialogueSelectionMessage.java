package com.nuparu.sevendaystomine.network.packets;

import com.nuparu.sevendaystomine.entity.EntityHuman;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class DialogueSelectionMessage implements IMessage {

	private int entityID;
	private String dialogue;
	
	public DialogueSelectionMessage() {
		
	}
	
	public DialogueSelectionMessage(String dialogue, EntityHuman entity) {

		this.entityID = entity.getEntityId();
		this.dialogue = dialogue;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		dialogue = ByteBufUtils.readUTF8String(buf);
		entityID = buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, dialogue);
		buf.writeInt(entityID);
	}
	
	public String getDialogue() {
		return this.dialogue;
	}
	
	public int getEntityID() {
		return this.entityID;
	}

}
