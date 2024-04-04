package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;

public class TileEntityBigSignMaster extends TileEntitySign {

	protected List<BlockPos> slaves = new ArrayList<BlockPos>();

	private TextFormatting textColor = TextFormatting.WHITE;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		NBTTagList list = new NBTTagList();
		for (BlockPos blockPos : this.slaves) {
			list.appendTag(NBTUtil.createPosTag(blockPos));
		}
		compound.setTag("list", list);
		if (textColor != null) {
			compound.setString("textColor", textColor.getFriendlyName());
		}
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("textColor", Constants.NBT.TAG_STRING)) {
			this.textColor = TextFormatting.getValueByName(compound.getString("textColor"));
		}

		if (!compound.hasKey("list", Constants.NBT.TAG_LIST))
			return;

		NBTTagList list = compound.getTagList("list", Constants.NBT.TAG_COMPOUND);
		this.slaves.clear();
		for (int i = 0; i < list.tagCount(); i++) {
			this.slaves.add(NBTUtil.getPosFromTag(list.getCompoundTagAt(i)));
		}

	}

	public void setTextColor(TextFormatting color) {
		this.textColor = color;
		world.notifyBlockUpdate(pos, Blocks.AIR.getDefaultState(), world.getBlockState(pos), 3);
	}

	public TextFormatting getTextColor() {
		return this.textColor;
	}

	public void addSlave(BlockPos blockPos) {
		slaves.add(blockPos);
	}

	public List<BlockPos> getSlaves() {
		return slaves;
	}
}
