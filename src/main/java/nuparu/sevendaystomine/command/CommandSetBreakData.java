package nuparu.sevendaystomine.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;

public class CommandSetBreakData {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> mbesayCommand = Commands.literal("setbreakdata")
                .requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .then(Commands.argument("data", MessageArgument.message()).executes((p_198539_0_) -> breakData(p_198539_0_, BlockPosArgument.getLoadedBlockPos(p_198539_0_, "pos")))));

        dispatcher.register(mbesayCommand);
    }

    static int breakData(CommandContext<CommandSourceStack> commandContext, BlockPos from) {
        CommandSourceStack sender = commandContext.getSource();
        ServerLevel world = sender.getLevel();
        if (world.isClientSide())
            return 0;

        try {
            float state = Float.parseFloat(MessageArgument.getMessage(commandContext, "data").getString());
            LevelChunk chunk = world.getChunkAt(from);
            IChunkData data = CapabilityHelper.getChunkData(chunk);
            data.setBreakData(from, state);

        } catch (NumberFormatException | CommandSyntaxException e) {
            e.printStackTrace();
        }

        return 1;
    }
}