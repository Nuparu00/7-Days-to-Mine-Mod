package nuparu.sevendaystomine.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ExtendedPlayerProvider implements ICapabilitySerializable<NBTBase> {
	@CapabilityInject(IExtendedPlayer.class)
	public static final Capability<IExtendedPlayer> EXTENDED_PLAYER_CAP = null;
	private IExtendedPlayer instance = EXTENDED_PLAYER_CAP.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)

	{

		return capability == EXTENDED_PLAYER_CAP;

	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)

	{

		return capability == EXTENDED_PLAYER_CAP ? EXTENDED_PLAYER_CAP.<T>cast(this.instance) : null;

	}

	@Override
	public NBTBase serializeNBT()

	{

		return EXTENDED_PLAYER_CAP.getStorage().writeNBT(EXTENDED_PLAYER_CAP, this.instance, null);

	}

	@Override
	public void deserializeNBT(NBTBase nbt)

	{

		EXTENDED_PLAYER_CAP.getStorage().readNBT(EXTENDED_PLAYER_CAP, this.instance, null, nbt);

	}

	public ExtendedPlayerProvider setOwner(EntityPlayer player) {
		instance.setOwner(player);
		return this;
	}

}