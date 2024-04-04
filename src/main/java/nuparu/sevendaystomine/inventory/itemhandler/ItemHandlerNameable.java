package nuparu.sevendaystomine.inventory.itemhandler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerNameable extends ItemStackHandler implements IItemHandlerNameable {

	private final ITextComponent defaultName;
	
	private ITextComponent displayName;

	public ItemHandlerNameable(ITextComponent defaultName) {
		this.defaultName = defaultName.createCopy();
	}

	public ItemHandlerNameable(int size, ITextComponent defaultName) {
		super(size);
		this.defaultName = defaultName.createCopy();
	}

	public ItemHandlerNameable(NonNullList<ItemStack> stacks, ITextComponent defaultName) {
		super(stacks);
		this.defaultName = defaultName.createCopy();
	}
	
	@Override
	public String getName() {
		return getDisplayName().getUnformattedText();
	}

	@Override
	public boolean hasCustomName() {
		return displayName != null;
	}

	@Override
	public ITextComponent getDisplayName() {
		return hasCustomName() ? displayName : defaultName;
	}

	public void setDisplayName(ITextComponent displayName) {
		this.displayName = displayName.createCopy();
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound tagCompound = super.serializeNBT();

		if (hasCustomName()) {
			tagCompound.setString("DisplayName", ITextComponent.Serializer.componentToJson(getDisplayName()));
		}

		return tagCompound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		super.deserializeNBT(nbt);

		if (nbt.hasKey("DisplayName")) {
			setDisplayName(ITextComponent.Serializer.jsonToComponent(nbt.getString("DisplayName")));
		}
	}

}
