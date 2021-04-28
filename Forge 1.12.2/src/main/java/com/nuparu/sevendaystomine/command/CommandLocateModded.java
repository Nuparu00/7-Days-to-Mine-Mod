package com.nuparu.sevendaystomine.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.block.repair.BreakData;
import com.nuparu.sevendaystomine.block.repair.BreakSavedData;
import com.nuparu.sevendaystomine.entity.EntityAirdrop;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.StructureGenerator;
import com.nuparu.sevendaystomine.world.gen.city.City;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.world.World;

public class CommandLocateModded extends CommandBase {
	@SuppressWarnings("rawtypes")
	private final List aliases;

	protected String fullEntityName;
	protected Entity conjuredEntity;

	@SuppressWarnings("rawtypes")
	public CommandLocateModded() {
		aliases = new ArrayList();
	}

	@Override
	public String getName() {
		return "locatecity";
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
				sender.sendMessage(new TextComponentString("\u00a7C" + "<x>" + "\u00a7C" + "<y>" + "\u00a7C" + "<z>"));
				return;
			}

			BlockPos from = parseBlockPos(sender, args, 0, true);
			// BlockPos to = parseBlockPos(sender, args, 3, true);

			int chunkX = from.getX() >> 4;
			int chunkZ = from.getZ() >> 4;

			List<ChunkPos> poses = Utils.getClosestCities(world, chunkX, chunkZ, 128);
			if (poses.isEmpty()) {
				sender.sendMessage(new TextComponentString("No city located"));

			} else {
				for (ChunkPos pos : poses) {
					int x = (pos.x * 16);
					int z = (pos.z * 16);
					TextComponentString componenet = new TextComponentString(
							"City is located at " + x + " " + z);
					Style style = new Style().setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tp " + x + " " + 120 + " " + z) {
						@Override
						public Action getAction() {
							return Action.RUN_COMMAND;
						}
						});
					componenet.setStyle(style);
					sender.sendMessage(componenet);
				}
			}

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		return args.length > 0 && args.length <= 3 ? getTabCompletionCoordinate(args, 0, pos)
				: (args.length < 6 ? getTabCompletionCoordinate(args, 1, pos) : null);
	}
}