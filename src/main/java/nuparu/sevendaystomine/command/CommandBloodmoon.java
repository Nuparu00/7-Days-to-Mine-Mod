package nuparu.sevendaystomine.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.util.PlayerUtils;
import nuparu.sevendaystomine.world.level.LevelUtils;

import java.util.Collection;
import java.util.Optional;

public class CommandBloodmoon {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> mbesayCommand = Commands.literal("blooodmoon")
				.requires((commandSource) -> commandSource.hasPermission(2)).executes((context) -> setBloodmoon(context))
						.then(Commands.argument("bloodmoonNumber", IntegerArgumentType.integer(1)).executes((context) -> setBloodmoon(context, IntegerArgumentType.getInteger(context, "bloodmoonNumber"))));

		dispatcher.register(mbesayCommand);
	}
	static int setBloodmoon(CommandContext<CommandSourceStack> commandContext) {
		ServerLevel world = commandContext.getSource().getLevel();

		if(ServerConfig.bloodmoonFrequency.get() == 0){
			return 1;
		}

		int closestBloodmoon = (int)Math.max(1,Math.ceil(LevelUtils.getDay(world) / (float) ServerConfig.bloodmoonFrequency.get()));
		return setBloodmoon(commandContext,closestBloodmoon);
	}

	static int setBloodmoon(CommandContext<CommandSourceStack> commandContext, int bloodmoonNumber) {
		ServerLevel world = commandContext.getSource().getLevel();
		if (world.isClientSide())
			return 0;

		if(ServerConfig.bloodmoonFrequency.get() == 0){
			return 1;
		}
		LevelUtils.setDay(world,bloodmoonNumber * ServerConfig.bloodmoonFrequency.get());

		return 1;
	}
}