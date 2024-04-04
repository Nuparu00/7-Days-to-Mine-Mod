package nuparu.sevendaystomine.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RenderHolsteredItemEvent extends Event {

	public enum EnumType {
		BACK, BACKPACK, LEFT_HIP, RIGHT_HIP;
	}

	private EntityPlayer player;
	private ItemStack stack;
	private EnumType type;

	public RenderHolsteredItemEvent(EntityPlayer player, ItemStack stack, EnumType type) {
		this.player = player;
		this.stack = stack;
		this.type = type;
	}

	public EntityPlayer getPlayer() {
		return this.player;
	}

	public ItemStack getStack() {
		return this.stack;
	}

	public EnumType getType() {
		return this.type;
	}
}
