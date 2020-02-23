package com.nuparu.sevendaystomine.block.repair;

import java.util.ArrayList;
import java.util.Iterator;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.BreakSyncMessage;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;


public class BreakSavedData extends WorldSavedData {
        public static final String DATA_NAME = SevenDaysToMine.MODID+"BreakData";
        public ArrayList<BreakData> list = new ArrayList<BreakData>();

        public BreakSavedData() {
            super(DATA_NAME);
        }

        public BreakSavedData(String s) {
            super(s);
        }
        
        public void addBreakData(BlockPos pos, int dim, float delta){
            if(delta <= 0f){
                 return;
            }
            for(BreakData data : list){
                 if(data.pos == (pos.toLong()) && data.dim == dim){
                 float original = data.state;
                 data.state += delta;
                 markDirty();
                 if((int)(Math.floor(original*10f)) != (int)(Math.floor((original+delta)*10f))){
                 NBTTagCompound nbt = new NBTTagCompound();
                 writeToNBT(nbt);
                 PacketManager.blockBreakSync.sendToDimension(new BreakSyncMessage(nbt), dim);
                 }
                 return;
                 }
            }
            list.add(new BreakData(pos.toLong(),dim,delta));
            markDirty();
            NBTTagCompound nbt = new NBTTagCompound();
            writeToNBT(nbt);
            PacketManager.blockBreakSync.sendToDimension(new BreakSyncMessage(nbt), dim);
            return;
          }

        public void setBreakData(BlockPos pos, int dim, float state){
          if(state == 0f){
               removeBreakData(pos,dim);
               return;
          }
          for(BreakData data : list){
               if(data.pos == (pos.toLong()) && data.dim == dim){
               float original = data.state;
               data.state = state;
               markDirty();
               if((int)(Math.floor(original*10f)) != (int)(Math.floor(state*10f))){
               NBTTagCompound nbt = new NBTTagCompound();
               writeToNBT(nbt);
               PacketManager.blockBreakSync.sendToDimension(new BreakSyncMessage(nbt), dim);
               }
               return;
               }
          }
          list.add(new BreakData(pos.toLong(),dim,state));
          markDirty();
          NBTTagCompound nbt = new NBTTagCompound();
          writeToNBT(nbt);
          PacketManager.blockBreakSync.sendToDimension(new BreakSyncMessage(nbt), dim);
          return;
        }
        public void removeBreakData(BlockPos pos, int dim){
        Iterator<BreakData> it = list.iterator();
        while (it.hasNext()) {
        BreakData data = (BreakData)it.next();
        if (data.pos == (pos.toLong()) && data.dim == dim) {
        it.remove();
        markDirty();
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        PacketManager.blockBreakSync.sendToDimension(new BreakSyncMessage(nbt), dim);
        }
        }
        }
        public BreakData getBreakData(BlockPos pos, int dim){
        Iterator<BreakData> it = list.iterator();
        while (it.hasNext()) {
        BreakData data = (BreakData)it.next();
        if (data.pos == (pos.toLong()) && data.dim == dim) {
        return data;
        }
        }
        return null;
        }
        
        public void sync(EntityPlayerMP player) {
        	PacketManager.blockBreakSync.sendTo(new BreakSyncMessage(writeToNBT(new NBTTagCompound())), player);
        }
        
        public void readFromNBT(NBTTagCompound nbt) {
       
            NBTTagCompound tag = nbt.getCompoundTag("list");
            list = BreakHelper.readFromNBT(tag);
        }

        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        
            NBTTagCompound tag = BreakHelper.writeToNBT(list);
            nbt.setTag("list",tag);
            return nbt;
        }

        public static BreakSavedData get(World world) {
            if(world != null){
            BreakSavedData data = (BreakSavedData)world.getPerWorldStorage().getOrLoadData(BreakSavedData.class, DATA_NAME);
            if (data == null) {
                data = new BreakSavedData();
				world.getPerWorldStorage().setData(DATA_NAME, (WorldSavedData) data);
            }
            return data;
            }
            return null;
        }
        
    }