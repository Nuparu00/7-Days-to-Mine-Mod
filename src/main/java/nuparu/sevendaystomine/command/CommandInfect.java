package nuparu.sevendaystomine.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import nuparu.sevendaystomine.util.PlayerUtils;

import java.util.Collection;

public class CommandInfect {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> mbesayCommand = Commands.literal("infect")
				.requires((commandSource) -> commandSource.hasPermission(2)).then(Commands.argument("targets", EntityArgument.players()).executes((p_198539_0_) -> cure(p_198539_0_, EntityArgument.getPlayers(p_198539_0_, "targets"))));

		dispatcher.register(mbesayCommand);
	}

	static int cure(CommandContext<CommandSourceStack> commandContext, Collection<ServerPlayer> collection) {
		ServerLevel world = commandContext.getSource().getLevel();
		if (world.isClientSide())
			return 0;
		for (ServerPlayer player : collection) {
			PlayerUtils.infectPlayer(player, 0);
		}
		return 1;
	}
}