package nuparu.sevendaystomine.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import nuparu.sevendaystomine.util.PrefabHelper;
import nuparu.sevendaystomine.util.json.JsonBlock;
import nuparu.sevendaystomine.util.json.JsonEntity;
import nuparu.sevendaystomine.util.json.JsonPrefab;
import nuparu.sevendaystomine.world.gen.prefab.EnumPrefabType;
import nuparu.sevendaystomine.world.gen.prefab.EnumStructureBiomeType;

public class CommandSavePrefab extends CommandBase {

	public CommandSavePrefab() {

	}

	@Override
	public String getName() {
		return "savePrefab";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "<name> <shouldContainAir?> <saveTileEntityData?> <saveEntities> <only:exclude> [tile] <data>";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute(MinecraftServer server, final ICommandSender sender, String[] args) throws CommandException {
		final World world = sender.getEntityWorld();
		if (world.isRemote) {
			return;
		}

		if (args.length != 7 && args.length != 4) {
			return;
		}
		
		/*if(sender instanceof EntityPlayer && !Utils.isOperator((EntityPlayer)sender)){
			return;
		}*/

		PrefabHelper helper = PrefabHelper.getInstance();
		final BlockPos posA = helper.posA;
		final BlockPos posB = helper.posB;
		if (helper.posA == null || helper.posB == null) {
			return;
		}
		final String name = args[0];

		if (name.isEmpty()) {
			return;
		}

		final boolean shouldContainAir = Boolean.parseBoolean(args[1]);
		final boolean saveTileEntity = Boolean.parseBoolean(args[2]);
		final boolean saveEntities = Boolean.parseBoolean(args[3]);
		final int maskMode = args.length == 7 ? (args[4].equals("only") ? 0 : 1) : -1;
		final Block blockMask = args.length == 7 ? Block.getBlockFromName(args[5]) : null;
		final int metaMask = args.length == 7 ? Integer.parseInt(args[6]) : -1;

		final BlockPos origin = sender.getPosition();

		sender.sendMessage(new TextComponentString("Saving as prefab file...."));
		
		 final File file = new File("./resources/prefabs/" + name + ".prfb");
		 file.delete(); new File("./resources/prefabs/").mkdirs();
		 

		final List<JsonBlock> blocks = new ArrayList<JsonBlock>();
		final List<JsonEntity> entities = new ArrayList<JsonEntity>();
		new Thread(new Runnable() {

			@Override
			public void run() {
				Iterable<BlockPos> it = BlockPos.getAllInBox(posA, posB);
				for (BlockPos p : it) {
					if (p != null) {
						IBlockState state = world.getBlockState(p);
						if (state != null) {
							Block block = state.getBlock();
							if (block == Blocks.AIR && !shouldContainAir) {
								continue;
							}
							int meta = 0;
							if (block != null && block != Blocks.AIR) {
								meta = block.getMetaFromState(state);
							}
							if(maskMode == 1 &&( block == blockMask && (metaMask == meta || metaMask == -1))) {
								continue;
							}
							if(maskMode == 0 &&( block != blockMask || (metaMask != meta && metaMask != -1))) {
								continue;
							}
							int x = p.getX() - origin.getX();
							int y = p.getY() - origin.getY();
							int z = p.getZ() - origin.getZ();

							NBTTagCompound nbt = null;
							if (p != null) {
								TileEntity te = world.getTileEntity(p);
								if (te != null && saveTileEntity) {
									nbt = new NBTTagCompound();
									te.writeToNBT(nbt);
								}
							}
							blocks.add(new JsonBlock(x, y, z, Block.REGISTRY.getNameForObject(block).toString(), meta,
									nbt != null ?nbt.toString() : "", ""));
						}
					}
				}

				if (saveEntities) {
					List<Entity> entitiez = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(posA, posB));
					for (Entity entity : entitiez) {
						if (entity == null || entity instanceof EntityPlayer) {
							continue;
						}
						String name = EntityList.getKey(entity.getClass()).toString();
						double x = entity.posX - origin.getX();
						double y = entity.posY - origin.getY();
						double z = entity.posZ - origin.getZ();
						float yaw = entity.rotationYaw;
						float pitch = entity.rotationPitch;
						NBTTagCompound nbt = entity.writeToNBT(new NBTTagCompound());

						entities.add(new JsonEntity(name, x, y, z, yaw, pitch, nbt != null ?nbt.toString() : ""));
					}
				}
				
				
				List<EnumStructureBiomeType> biomes = new ArrayList<EnumStructureBiomeType>();
				biomes.add(EnumStructureBiomeType.ALL);
				JsonPrefab prefab = new JsonPrefab(name, Math.abs(posA.getX() - posB.getX()),
						Math.abs(posA.getY() - posB.getY()), Math.abs(posA.getZ() - posB.getZ()), 0, 0, 0, false, 0.75, 0.75, 1,
						EnumPrefabType.NONE, biomes, blocks, entities);

				Gson gson = new GsonBuilder().setPrettyPrinting().create(); //allows having line breaks - longer but neater json file
				String json = gson.toJson(prefab);
				try {
					FileUtils.writeStringToFile(file, json);
				} catch (IOException e) {
					e.printStackTrace();
					sender.sendMessage(new TextComponentString(TextFormatting.RED + e.getClass().getSimpleName()
							+ " : Unable to generate the file"));
					return;
				}
				sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Prefab has been saved to " + TextFormatting.GREEN
						+ file.getPath()));
			}
		}).start();

	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return args.length == 2 ? getListOfStringsMatchingLastWord(args, new String[] { "true", "false" })
				: args.length == 3 ? getListOfStringsMatchingLastWord(args, new String[] { "true", "false" })
						: args.length == 4 ? getListOfStringsMatchingLastWord(args, new String[] { "true", "false" })
								: args.length == 5
										? getListOfStringsMatchingLastWord(args, new String[] { "only", "exclude" })
										: args.length == 6
												? getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys())
												: null;
	}

}
