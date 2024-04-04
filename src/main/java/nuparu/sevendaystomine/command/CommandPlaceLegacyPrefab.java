package nuparu.sevendaystomine.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import nuparu.sevendaystomine.world.gen.prefab.PrefabLegacy;

/*
 * SHOULD BE USED ONLY FOR LOADING OF OLD PREFABS
 */
@SuppressWarnings("deprecation")
public class CommandPlaceLegacyPrefab extends CommandBase {

	public CommandPlaceLegacyPrefab() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "placeLegacyPrefab";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "placeLegacyPrefab <x> <y> <z> <name> <genereateAir?> [rotation]";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		if (args.length < 5) {
			sender.sendMessage(new TextComponentString(TextFormatting.RED + "Missing arguments"));
			sender.sendMessage(
					new TextComponentString(TextFormatting.RED + "<x> <y> <z> <name> <shouldContainAir?> [rotation]"));
			return;
		}
		if (args.length > 6) {
			sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid argument"));
			sender.sendMessage(
					new TextComponentString(TextFormatting.RED + "<x> <y> <z> <name> <shouldContainAir?> [rotation]"));
			return;
		}
		sender.sendMessage(new TextComponentString("Placing prefab to the world...."));
		String name = args[3];
		File file = new File("./resources/prefabs/" + name + ".prfb");
		file.getParentFile().mkdirs();
		BlockPos pos = parseBlockPos(sender, args, 0, true);
		boolean shouldContainAir = Boolean.parseBoolean(args[4]);
		float rotation = 0f;
		long startTime = 0l;
		long endTime = 0l;
		long duration = 0l;

		if (args.length == 6) {
			rotation = Float.parseFloat(args[5]);
		}
		String line;
		InputStream fis = null;
		try {
			startTime = System.nanoTime();
			fis = new FileInputStream(file.getAbsolutePath());
			PrefabLegacy prefab = null;
			if (prefab == null) {
				prefab = new PrefabLegacy(name, false);
				prefab.shouldContainAir = shouldContainAir;
				prefab.setInputStream(fis);
				prefab.run();
				if (hasFinished(prefab)) {
					prefab.generate(world, pos, rotation);
				}
			} else {
				prefab.generate(world, pos, rotation);
			}
			endTime = System.nanoTime();
			duration = (endTime - startTime);
		} catch (IOException e) {
			sender.sendMessage(new TextComponentString(
					TextFormatting.RED + "IOException:An eroor has occurred while reading file."));
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "PrefabLegacy has been placed at "
					+ TextFormatting.GREEN + pos.toString() + "in" + (duration / 1000000) + "ms."));
		}

	}

	@SuppressWarnings("deprecation")
	public boolean hasFinished(PrefabLegacy prefab) {
		if (prefab == null) {
			return false;
		}
		if (prefab.finished) {
			return true;
		}
		return hasFinished(prefab);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return args.length > 0 && args.length <= 3 ? getTabCompletionCoordinate(args, 0, pos)
				: args.length == 5 ? getListOfStringsMatchingLastWord(args, new String[] { "true", "false" }) : null;
	}

}
