package nuparu.sevendaystomine.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import nuparu.sevendaystomine.world.gen.city.City;
import nuparu.sevendaystomine.world.gen.city.EnumCityType;
import nuparu.sevendaystomine.world.gen.city.building.Building;
import nuparu.sevendaystomine.world.gen.city.building.Buildings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class CommandGenerateBuilding extends CommandBase {
	@SuppressWarnings("rawtypes")
	private final List aliases;

	protected String fullEntityName;
	protected Entity conjuredEntity;

	@SuppressWarnings("rawtypes")
	public CommandGenerateBuilding() {
		aliases = new ArrayList();
	}

	@Override
	public String getName() {
		return "generatebuilding";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/generatebuilding <x> <y> <z> <facing> <building> [mirrored] [seed]";
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

		if(world.isRemote) return;
		if(args.length < 5 || args.length > 7){
			sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid number of arguments"));
			sender.sendMessage(
					new TextComponentString(TextFormatting.RED + "<x> <y> <z> <facing> <building> [mirrored] [seed]"));
			return;
		}

		BlockPos blockPos = parseBlockPos(sender, args, 0, true);
		EnumFacing facing = EnumFacing.byName(args[3]);
		Optional<Building> optionalBuilding = Buildings.getBuilding(new ResourceLocation(args[4]));
		boolean mirrored = args.length > 5 && Boolean.parseBoolean(args[5]);
		long seed = args.length == 7 ? Long.parseLong(args[6]) : 0;
		optionalBuilding.ifPresent(building -> building.generate(world,blockPos,facing,mirrored,new Random(seed)));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		return args.length > 0 && args.length <= 3 ? getTabCompletionCoordinate(args, 0, pos)
				: args.length == 4
						? CommandBase.getListOfStringsMatchingLastWord(args, "NORTH", "SOUTH", "WEST", "EAST")
						: args.length == 5 ? Buildings.getBuildings().stream().map(ResourceLocation::toString).collect(Collectors.toList()) :
							args.length == 6 ? CommandBase.getListOfStringsMatchingLastWord(args, "true", "false") : super.getTabCompletions(server, sender, args, pos);
	}
}