package nuparu.sevendaystomine.block.repair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;

public class BreakHelper {
	public static final BreakHelper INSTANCE = new BreakHelper();

	public BreakHelper() {
	}

	public static NBTTagCompound writeToNBT(ArrayList<BreakData> list) {
		NBTTagCompound nbt = new NBTTagCompound();
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(list);
			byte[] bytes = bos.toByteArray();
			nbt.setByteArray("breakData", bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nbt;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<BreakData> readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("breakData")) {
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