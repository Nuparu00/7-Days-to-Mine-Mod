package nuparu.sevendaystomine.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.world.World;
import nuparu.sevendaystomine.util.Utils;

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
		return "<x> <y> <z> [maxDst]";
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
			if (args.length < 3 || args.length > 4) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + "Missing arguments"));
				sender.sendMessage(new TextComponentString(
						"\u00a7C" + "<x>" + "\u00a7C" + "<y>" + "\u00a7C" + "<z>" + "\u00a7C" + "[maxDst]"));
				return;
			}

			BlockPos from = parseBlockPos(sender, args, 0, true);
			// BlockPos to = parseBlockPos(sender, args, 3, true);

			int chunkX = from.getX() >> 4;
			int chunkZ = from.getZ() >> 4;

			final int maxDst = args.length == 4 ? Math.abs(Integer.parseInt(args[3])) : 128;

			new Thread() {
				@Override
				public void run() {
					List<ChunkPos> poses = Utils.getClosestCities(world, chunkX, chunkZ, maxDst);
					if (poses.isEmpty()) {
						sender.sendMessage(new TextComponentString("No city located"));

					} else {
						for (ChunkPos pos : poses) {
							int x = (pos.x * 16);
							int z = (pos.z * 16);
							TextComponentString componenet = new TextComponentString("City is located at " + x + " " + z
									+ " ("
									+ Math.round(Math.sqrt(Math.pow(pos.x - chunkX, 2) + Math.pow(pos.z - chunkZ, 2)))
									+ " chunks)");
							Style style = new Style().setClickEvent(
									new ClickEvent(Action.RUN_COMMAND, "/tp " + x + " " + 120 + " " + z) {
										@Override
										public Action getAction() {
											return Action.RUN_COMMAND;
										}
									});
							if (world.getChunkFromChunkCoords(pos.x, pos.z).isTerrainPopulated()) {
								style.setColor(TextFormatting.GREEN);
							}
							componenet.setStyle(style);
							sender.sendMessage(componenet);
						}
					}
				}

			}.start();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		return (args.length > 0 && args.length <= 3) ? getTabCompletionCoordinate(args, 0, pos)
				:  (args.length == 4 ? Arrays.asList("32","64","128") : null);
	}
}