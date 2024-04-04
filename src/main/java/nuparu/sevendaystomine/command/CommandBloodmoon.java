package nuparu.sevendaystomine.command;

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
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.util.Utils;
import scala.Int;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandBloodmoon extends CommandBase {
	@SuppressWarnings("rawtypes")
	private final List aliases;

	protected String fullEntityName;
	protected Entity conjuredEntity;

	@SuppressWarnings("rawtypes")
	public CommandBloodmoon() {
		aliases = new ArrayList();
	}

	@Override
	public String getName() {
		return "bloodmoon";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/bloodmoon [number]";
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
		if(args.length > 1){
			sender.sendMessage(new TextComponentString("Invalid arguments number"));
			sender.sendMessage(
					new TextComponentString(TextFormatting.RED + "[number]"));
		}
		if(ModConfig.world.bloodmoonFrequency == 0){
			return;
		}
		int closestBloodmoon = (int)Math.max(1,Math.ceil(Utils.getDay(world) / (float) ModConfig.world.bloodmoonFrequency));
		int bloodmoonNumber = args.length == 1 ? Integer.parseInt(args[0]) : closestBloodmoon;
		Utils.setDay(world,bloodmoonNumber * ModConfig.world.bloodmoonFrequency);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : super.getTabCompletions(server, sender, args, pos);
	}
}