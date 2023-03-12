package nuparu.sevendaystomine.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.entity.item.AirdropEntity;

public class CommandAirdrop {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> mbesayCommand = Commands.literal("airdrop")
                .requires((commandSource) -> commandSource.hasPermission(2)).executes((p_198700_0_) -> airdrop(p_198700_0_, AirdropEntity.getAirdropPos(p_198700_0_.getSource().getLevel()))).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes((p_198700_0_) -> airdrop(p_198700_0_, BlockPosArgument.getLoadedBlockPos(p_198700_0_, "pos"))));

        dispatcher.register(mbesayCommand);
    }

    static int airdrop(CommandContext<CommandSourceStack> commandContext, BlockPos pos) {
        ServerLevel world = commandContext.getSource().getLevel();
        if (world.isClientSide())
            return 0;
        AirdropEntity e = new AirdropEntity(world, world.getSharedSpawnPos().above(world.getHeight() + 16));
		/*LootTable loottable = world.getServer().getLootTables().get(ModLootTables.AIRDROP);
		LootContext.Builder lootcontext$builder = (new LootContext.Builder(world)).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(pos));

		ItemUtils.fill(loottable,e.getInventory(), lootcontext$builder.create(LootParameterSets.CHEST));*/
        world.addFreshEntity(e);
        e.setPos(pos.getX(), pos.getY(), pos.getZ());

        commandContext.getSource().sendSuccess(Component.translatable("airdrop.message",
                        pos.getX() + MathUtils.getIntInRange(world.random, 32, 128) * (world.random.nextBoolean() ? 1 : -1),
                        pos.getZ() + MathUtils.getIntInRange(world.random, 32, 128) * (world.random.nextBoolean() ? 1 : -1)),
                true);

        return 1;
    }

}