package nuparu.sevendaystomine.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;

public class TileEntityStreetSign extends TileEntitySign {

	private TextFormatting textColor = TextFormatting.WHITE;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		if (textColor != null) {
			compound.setString("textColor", textColor.getFriendlyName());
		}
		return compound;
	}

	public void setTextColor(TextFormatting color) {
		this.textColor = color;
		world.notifyBlockUpdate(pos, Blocks.AIR.getDefaultState(), world.getBlockState(pos), 3);
	}

	public TextFormatting getTextColor() {
		return this.textColor;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		if (compound.hasKey("textColor", Constants.NBT.TAG_STRING)) {
			this.textColor = TextFormatting.getValueByName(compound.getString("textColor"));
		}
	}
}
