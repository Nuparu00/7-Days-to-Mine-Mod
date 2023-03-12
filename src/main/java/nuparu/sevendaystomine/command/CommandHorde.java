package nuparu.sevendaystomine.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import nuparu.sevendaystomine.command.arguments.HordeSummonArgument;
import nuparu.sevendaystomine.json.horde.HordeDataManager;
import nuparu.sevendaystomine.world.level.horde.Horde;
import nuparu.sevendaystomine.world.level.horde.HordeManager;

import java.util.Optional;

public class CommandHorde {

	public static final SuggestionProvider<CommandSourceStack> HORDES = SuggestionProviders.register(new ResourceLocation("hordes"),
			(p_121667_, p_121668_) -> SharedSuggestionProvider.suggestResource(HordeDataManager.INSTANCE.getHordeNames(), p_121668_));

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> mbesayCommand = Commands.literal("horde")
				.requires((commandSource) -> commandSource.hasPermission(2))
				.then(Commands.argument("horde", HordeSummonArgument.id()).suggests(HORDES)
						.executes((context) -> summonHorde(context, HordeSummonArgument.getSummonableHorde(context, "horde"))));

		dispatcher.register(mbesayCommand);
	}
	static int summonHorde(CommandContext<CommandSourceStack> commandContext, ResourceLocation hordeKey) {
		ServerLevel world = commandContext.getSource().getLevel();
		if (world.isClientSide())
			return 0;
		Optional<Horde> horde = HordeManager.getOrCreate(commandContext.getSource().getServer())
				.startHorde(hordeKey,world,new BlockPos(commandContext.getSource().getPosition()),commandContext.getSource().getPlayer());
		if(horde.isPresent()){
			commandContext.getSource().sendSuccess(Component.translatable("horde.summon.success"),true);
			return 1;
		}
		commandContext.getSource().sendFailure(Component.translatable("horde.summon.failure"));
		return 0;
	}
}