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
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.repair.BreakSavedData;
import com.nuparu.sevendaystomine.pathfinding.AStar;
import com.nuparu.sevendaystomine.pathfinding.AStar.Node;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingAirport;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingCemetery;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingConstructionSite;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingFactory;

public class CommandAStar extends CommandBase {
	@SuppressWarnings("rawtypes")
	private final List aliases;

	@SuppressWarnings("rawtypes")
	public CommandAStar() {
		aliases = new ArrayList();
	}

	@Override
	public String getName() {
		return "astar";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "<x> <y> <z> <x> <y> <z>";
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
			if (args.length < 6) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + "Missing arguments"));
				sender.sendMessage(new TextComponentString("\u00a7C" + "<x>" + "\u00a7C" + "<y>" + "\u00a7C" + "<z>"
						+ "\u00a7C" + "<dim>" + "\u00a7C" + "<damage>"));
				return;
			}

			BlockPos from = parseBlockPos(sender, args, 0, true);
			BlockPos to = parseBlockPos(sender, args, 3, true);
			
			BlockPos[] poses = new AStar(world,from,to).getPath();
			
			if(poses != null) {
				for(BlockPos pos : poses) {
					world.setBlockState(pos, Blocks.RED_SANDSTONE.getDefaultState());
				}
			}

			

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		return args.length > 0 && args.length < 4 ? getTabCompletionCoordinate(args, 0, pos)
				: (args.length < 6 ? getTabCompletionCoordinate(args, 1, pos) : null);
	}
}