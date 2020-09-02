package com.nuparu.sevendaystomine.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.block.repair.BreakData;
import com.nuparu.sevendaystomine.block.repair.BreakSavedData;
import com.nuparu.sevendaystomine.entity.EntityAirdrop;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.City;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class CommandAirdrop extends CommandBase {
	@SuppressWarnings("rawtypes")
	private final List aliases;

	protected String fullEntityName;
	protected Entity conjuredEntity;

	@SuppressWarnings("rawtypes")
	public CommandAirdrop() {
		aliases = new ArrayList();
	}

	@Override
	public String getName() {
		return "airdrop";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/airdrop";
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
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, final ICommandSender sender, String[] args) {
		final World world = sender.getEntityWorld();

		if (world.isRemote)
			return;
		if (args.length == 0) {
			BlockPos pos = Utils.getAirdropPos(world);
			sender.sendMessage(new TextComponentTranslation("airdrop.message",
					pos.getX() + MathUtils.getIntInRange(world.rand, 32, 128) * (world.rand.nextBoolean() ? 1 : -1),
					pos.getZ() + MathUtils.getIntInRange(world.rand, 32, 128) * (world.rand.nextBoolean() ? 1 : -1)));
			EntityAirdrop e = new EntityAirdrop(world, pos);
			world.spawnEntity(e);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos pos) {
		return args.length > 0 && args.length <= 3 ? getTabCompletionCoordinate(args, 0, pos) : null;
	}
}