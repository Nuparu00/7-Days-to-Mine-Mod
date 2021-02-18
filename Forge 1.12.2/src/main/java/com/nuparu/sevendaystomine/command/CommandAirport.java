package com.nuparu.sevendaystomine.command;

import net.minecraft.world.World;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;

import java.util.List;
import java.util.ArrayList;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.repair.BreakSavedData;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingAirplane;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingAirport;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingCemetery;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingConstructionSite;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingFactory;

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
			
			new BuildingAirport(30).generate(world, parseBlockPos(sender, args, 0, true), EnumFacing.SOUTH, false, world.rand);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		return args.length > 0 && args.length <= 3 ? getTabCompletionCoordinate(args, 0, pos) : null;
	}
}