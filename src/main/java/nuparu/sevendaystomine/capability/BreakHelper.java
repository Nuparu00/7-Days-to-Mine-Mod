package nuparu.sevendaystomine.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BreakHelper {
    public static CompoundTag save(HashMap<BlockPos, BreakData> map) {

        CompoundTag nbt = new CompoundTag();
        ListTag list = new ListTag();
        Iterator<Map.Entry<BlockPos, BreakData>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<BlockPos, BreakData> pair = it.next();
            CompoundTag entryNBT = new CompoundTag();
            entryNBT.putLong("pos", pair.getKey().asLong());
            pair.getValue().save(entryNBT);
            list.add(entryNBT);
        }
        nbt.put("map", list);

        return nbt;
    }

    public static HashMap<BlockPos, BreakData> of(CompoundTag nbt) {
        HashMap<BlockPos, BreakData> map = new HashMap<>();
        if (nbt.contains("map")) {
            ListTag list = nbt.getList("map", Tag.TAG_COMPOUND);
            Iterator<Tag> it = list.iterator();
            while(it.hasNext()) {
                Tag inbt = it.next();
                if(inbt instanceof CompoundTag entryNBT) {
                    if(!entryNBT.contains("pos")) continue;
                    if(!entryNBT.contains("lastChange")) continue;
                    if(!entryNBT.contains("state")) continue;
                    BlockPos pos = BlockPos.of(entryNBT.getLong("pos"));
                    BreakData data = BreakData.of(entryNBT);

                    map.put(pos, data);
                }
            }

        }

        return map;
    }

    public static CompoundTag save(ArrayList<BreakData> list) {
        CompoundTag nbt = new CompoundTag();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(list);
            byte[] bytes = bos.toByteArray();
            nbt.putByteArray("breakData", bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nbt;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<BreakData> readFromNBT(CompoundTag nbt) {
        if (nbt.contains("breakData")) {
            byte[] bytes = nbt.getByteArray("breakData");

            Object obj = null;
            ByteArrayInputStream bis = null;
            ObjectInputStream ois = null;
            try {
                bis = new ByteArrayInputStream(bytes);
                ois = new ObjectInputStream(bis);
                obj = ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return (ArrayList<BreakData>) obj;
        }
        return null;
    }
}
