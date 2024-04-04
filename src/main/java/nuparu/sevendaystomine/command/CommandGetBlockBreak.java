package nuparu.sevendaystomine.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import nuparu.sevendaystomine.block.repair.BreakData;
import nuparu.sevendaystomine.block.repair.BreakSavedData;

public class CommandGetBlockBreak extends CommandBase {
	@SuppressWarnings("rawtypes")
	private final List aliases;

	protected String fullEntityName;
	protected Entity conjuredEntity;

	@SuppressWarnings("rawtypes")
	public CommandGetBlockBreak() {
		aliases = new ArrayList();
	}

	@Override
	public String getName() {
		return "listBlockDamages";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "";
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
	public void execute(MinecraftServer server, final ICommandSender sender, String[] argString) {
		final World world = sender.getEntityWorld();

		if (world.isRemote) {
		} else {
			ArrayList<BreakData> list = BreakSavedData.get(world).getList();
			for (BreakData data : list) {
				sender.sendMessage(new TextComponentString(TextFormatting.GREEN + BlockPos.fromLong(data.getPos()).toString() + " " + data.getLastChange() + " "
						+ data.getState()));
			}
		}
	}
}