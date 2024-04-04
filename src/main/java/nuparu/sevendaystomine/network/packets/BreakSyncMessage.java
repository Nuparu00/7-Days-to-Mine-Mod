package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BreakSyncMessage implements IMessage{
     NBTTagCompound data;
     public BreakSyncMessage(){
          
     }
     public BreakSyncMessage(NBTTagCompound data){
          this.data = data;
     }
     public void fromBytes(ByteBuf buf){
          data = ByteBufUtils.readTag(buf);
     }
     public void toBytes(ByteBuf buf){
          ByteBufUtils.writeTag(buf,data);
     }
     public NBTTagCompound getNBT(){
          return data;
     }
}