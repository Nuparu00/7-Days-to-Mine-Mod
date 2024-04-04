package nuparu.sevendaystomine.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import nuparu.sevendaystomine.block.repair.BreakSavedData;

public class CommandSetBlockBreak extends CommandBase {
	@SuppressWarnings("rawtypes")
	private final List aliases;

	@SuppressWarnings("rawtypes")
	public CommandSetBlockBreak() {
		aliases = new ArrayList();
	}

	@Override
	public String getName() {
		return "setBlockDamage";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "<x> <y> <z> <dim> <damage>";
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
	public void execute(MinecraftServer server, final ICommandSender sender, String[] args) throws CommandException {
		final World world = sender.getEntityWorld();

		if (world.isRemote) {

		} else {
			if (args.length < 5) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + "Missing arguments"));
				sender.sendMessage(new TextComponentString("\u00a7C" + "<x>" + "\u00a7C" + "<y>" + "\u00a7C" + "<z>" + "\u00a7C" + "<dim>"
						+ "\u00a7C" + "<damage>"));
				return;
			}
			if (args.length > 5) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid argument"));
				sender.sendMessage(new TextComponentString("\u00a7C" + "<x>" + "\u00a7C" + "<y>" + "\u00a7C" + "<z>" + "\u00a7C" + "<dim>"
						+ "\u00a7C" + "<damage>"));
				return;
			}

			BreakSavedData.get(world).setBreakData(parseBlockPos(sender, args, 0, true),world, Float.parseFloat(args[4]));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		return args.length > 0 && args.length <= 3 ? getTabCompletionCoordinate(args, 0, pos) : args.length == 3 ? Lists
				.newArrayList(new String[]{Integer.toString(sender.getEntityWorld().provider.getDimension())}) : null;
	}
}