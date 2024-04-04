package nuparu.sevendaystomine.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.world.horde.GenericHorde;

public class CommandHorde extends CommandBase {
	@SuppressWarnings("rawtypes")
	private final List aliases;

	protected String fullEntityName;
	protected Entity conjuredEntity;

	@SuppressWarnings("rawtypes")
	public CommandHorde() {
		aliases = new ArrayList();
	}

	@Override
	public String getName() {
		return "horde";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/horde <player>";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getAliases() {
		return this.aliases;
	}

	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public void execute(MinecraftServer server, final ICommandSender sender, String[] args) {
		final World world = sender.getEntityWorld();
		EntityPlayer player = null;
		if (args.length == 0) {
			player = (EntityPlayer) sender;
		} else if (args.length == 1) {
			try {
				player = (EntityPlayer) getEntity(server, sender, args[0]);
			} catch (CommandException e) {
				e.printStackTrace();
			}
		}
		if (player == null || args.length > 1) {
			return;
		}
		if (player instanceof EntityPlayerMP) {
			GenericHorde horde = new GenericHorde(new BlockPos(player), world, player);
			horde.addTarget((EntityPlayerMP) player);
			horde.start();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames())
				: super.getTabCompletions(server, sender, args, pos);
	}
}