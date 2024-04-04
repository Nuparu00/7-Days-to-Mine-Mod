package nuparu.sevendaystomine.command;

import java.io.FileNotFoundException;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import nuparu.sevendaystomine.world.gen.prefab.Prefab;
import nuparu.sevendaystomine.world.gen.prefab.PrefabParser;

public class CommandPlacePrefab extends CommandBase {

	public CommandPlacePrefab() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "placePrefab";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "<x> <y> <z> <name> <genereateAir?> [rotation]";	
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		final World world = sender.getEntityWorld();
		if (world.isRemote) {
			return;
			
		}

		if (args.length != 5 && args.length != 6) {
			sender.sendMessage(new TextComponentString(TextFormatting.RED + " Usage: /placePrefab <x> <y> <z> <name> <genereateAir?> [rotation]"));
			return;
		}
		
		/*if(sender instanceof EntityPlayer && !Utils.isOperator((EntityPlayer)sender)){
			return;
		}*/
		
		BlockPos pos = parseBlockPos(sender, args, 0, true);
		
		String name = args[3];
		boolean generateAir = Boolean.parseBoolean(args[4]);
		int rotation = args.length == 6 ? Integer.parseInt(args[5]) : 0;
		
		long startTime = 0l;
		long endTime = 0l;
		long duration = 0l;
		
		Prefab prefab = null;
		try {
			prefab = PrefabParser.INSTANCE.getPrefabFromFile(name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(prefab != null) {
			startTime = System.nanoTime();
			prefab.generate(world, pos, rotation, false);
			endTime = System.nanoTime();
			duration = (endTime - startTime);
			sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Prefab has been placed at "
					+ TextFormatting.GREEN + pos.toString() + " within" + (duration / 1000000) + "ms."));
		}

	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return args.length > 0 && args.length <= 3 ? getTabCompletionCoordinate(args, 0, pos) : args.length == 5
				? getListOfStringsMatchingLastWord(args, new String[]{"true", "false"})
				: null;
	}

}
