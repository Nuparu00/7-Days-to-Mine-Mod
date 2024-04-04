package nuparu.sevendaystomine.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.gen.city.building.BuildingAirplane;
import nuparu.sevendaystomine.world.gen.city.building.BuildingHotel;
import nuparu.sevendaystomine.world.gen.city.building.BuildingLandfill;

public class CommandAirport extends CommandBase {
	@SuppressWarnings("rawtypes")
	private final List aliases;

	@SuppressWarnings("rawtypes")
	public CommandAirport() {
		aliases = new ArrayList();
	}

	@Override
	public String getName() {
		return "airport";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "<x> <y> <z>";
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
			if (args.length < 3) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + "Missing arguments"));
				sender.sendMessage(new TextComponentString("\u00a7C" + "<x>" + "\u00a7C" + "<y>" + "\u00a7C" + "<z>" + "\u00a7C" + "<dim>"
						+ "\u00a7C" + "<damage>"));
				return;
			}
			
			new BuildingHotel(40, -4).generate(world, parseBlockPos(sender, args, 0, true), EnumFacing.EAST, false, world.rand);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		return args.length > 0 && args.length <= 3 ? getTabCompletionCoordinate(args, 0, pos) : null;
	}
}