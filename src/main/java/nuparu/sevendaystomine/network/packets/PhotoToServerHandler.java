package nuparu.sevendaystomine.network.packets;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.photo.PhotoCatcherServer;

public class PhotoToServerHandler implements IMessageHandler<PhotoToServerMessage, IMessage> {

	@Override
	public IMessage onMessage(PhotoToServerMessage message, MessageContext ctx) {
		if (!ModConfig.players.allowPhotos) return null;
		EntityPlayer player = ctx.getServerHandler().player;

		File file = PhotoCatcherServer.addBytesToMap(message.getBytes(), message.getID(), message.getParts(),
				message.getIndex(), player.getName());

		if (file == null)
			return null;

		player.world.playSound(null, new BlockPos(player), SoundHelper.CAMERA_TAKE, SoundCategory.PLAYERS, 0.3F,
				1.0F / (player.world.rand.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
		ItemStack stack = new ItemStack(ModItems.PHOTO);
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setString("path", FilenameUtils.getName(file.getPath()));
		if (!player.inventory.addItemStackToInventory(stack)) {
			player.dropItem(stack, false);
		}

		return null;
	}

}
