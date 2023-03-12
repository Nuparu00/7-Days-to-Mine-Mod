package nuparu.sevendaystomine.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;

import java.util.Collection;

public class CommandSetQuality {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> mbesayCommand = Commands.literal("setquality")
				.requires((commandSource) -> commandSource.hasPermission(2))
				.then(Commands.argument("targets", EntityArgument.players())
				.then(Commands.argument("quality", MessageArgument.message()).executes((p_198539_0_) -> airdrop(p_198539_0_, EntityArgument.getPlayers(p_198539_0_, "targets")))));

		dispatcher.register(mbesayCommand);
	}

	static int airdrop(CommandContext<CommandSourceStack> commandContext, Collection<ServerPlayer> collection) {
		ServerLevel world = commandContext.getSource().getLevel();
		if (world.isClientSide())
			return 0;
		for (ServerPlayer player : collection) {
			int quality;
			try {
				quality = Integer.parseInt(MessageArgument.getMessage(commandContext, "quality").getString());
				ItemStack stack = player.getMainHandItem();
				if (!stack.isEmpty() && (Object)stack instanceof IQualityStack qualityStack) {
					qualityStack.setQuality(quality);
				}
			} catch (NumberFormatException | CommandSyntaxException e) {
				e.printStackTrace();
			}
		}
		return 1;
	}
}