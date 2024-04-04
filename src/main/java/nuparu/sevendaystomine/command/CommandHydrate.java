package nuparu.sevendaystomine.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.ExtendedPlayer;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

public class CommandHydrate extends CommandBase {
	@SuppressWarnings("rawtypes")
	private final List aliases;

	protected String fullEntityName;
	protected Entity conjuredEntity;

	@SuppressWarnings("rawtypes")
	public CommandHydrate() {
		aliases = new ArrayList();
	}

	@Override
	public String getName() {
		return "hydrate";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/hydrate <player>";
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
			player = (EntityPlayer)sender;
		}
		else if (args.length == 1) {
			try {
				player = (EntityPlayer) getEntity(server, sender, args[0]);
			} catch (CommandException e) {
				e.printStackTrace();
			}
		}
		if(player == null || args.length > 1) {
			sender.sendMessage(new TextComponentString(TextFormatting.RED+"No player found."));
			return;
		}
		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		if(iep == null) return;
		iep.setThirst(ExtendedPlayer.MAX_THIRST);
		iep.setStamina(ExtendedPlayer.MAX_STAMINA);
		iep.setDrinkCounter(0);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : super.getTabCompletions(server, sender, args, pos);
	}
}