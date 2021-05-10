package nuparu.sevendaystomine.world.gen.city.building;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.electricity.network.INetwork;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.tileentity.TileEntityTurret;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.CityHelper;

public class BuildingLargeBanditCamp extends Building {

	private ResourceLocation SMALL_TOWER = new ResourceLocation(SevenDaysToMine.MODID, "bandit_camp_large_small_tower");
	private ResourceLocation TENT = new ResourceLocation(SevenDaysToMine.MODID, "bandit_camp_large_tent");
	private ResourceLocation LARGE_TOWER = new ResourceLocation(SevenDaysToMine.MODID, "bandit_camp_large_large_tower");
	private ResourceLocation POOL = new ResourceLocation(SevenDaysToMine.MODID, "bandit_camp_large_pool");
	private ResourceLocation BASEMENT = new ResourceLocation(SevenDaysToMine.MODID, "bandit_camp_large_basement");

	List<INetwork> devices = new ArrayList<INetwork>();

	public BuildingLargeBanditCamp(int weight) {
		super(null, weight);
		this.canBeMirrored = false;
		this.setPedestal(Blocks.STONE.getDefaultState());
	}
	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (!world.isRemote) {
			devices.clear();
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, SMALL_TOWER);
			if (template == null) {
				return;
			}

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			pos = pos.up(yOffset);

			generateTemplate(world, pos, mirror, facing, placementsettings, template, hasPedestal,rand);
			pos = pos.offset(facing, -18);
			template = templatemanager.getTemplate(minecraftserver, TENT);
			if (template == null) {
				return;
			}
			generateTemplate(world, pos, mirror, facing, placementsettings, template, hasPedestal,rand);
			pos = pos.offset(facing.rotateY(), -20);
			template = templatemanager.getTemplate(minecraftserver, LARGE_TOWER);
			if (template == null) {
				return;
			}
			generateTemplate(world, pos, mirror, facing, placementsettings, template, hasPedestal,rand);
			pos = pos.offset(facing, 18);
			template = templatemanager.getTemplate(minecraftserver, POOL);
			if (template == null) {
				return;
			}
			generateTemplate(world, pos, mirror, facing, placementsettings, template, hasPedestal,rand);
			pos = pos.offset(facing.rotateY(), 4).down(4).offset(facing, -4);
			template = templatemanager.getTemplate(minecraftserver, BASEMENT);
			if (template == null) {
				return;
			}
			generateTemplate(world, pos, mirror, facing, placementsettings, template, false,rand);

			for (INetwork deviceFrom : devices) {
				for (INetwork deviceTo : devices) {
					if (deviceFrom == deviceTo)
						continue;
					deviceTo.connectTo(deviceFrom);
				}
			}

		}
	}

	@Override
	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data, boolean mirror) {
		try {
			switch (data) {
			case "sedan": {
				world.setBlockToAir(pos);
				CityHelper.placeRandomCar(world, pos, facing, world.rand);
				break;
			}
			case "computer": {
				TileEntity te = world.getTileEntity(pos.down());
				if (te != null) {
					TileEntityComputer computer = (TileEntityComputer) te;
					computer.createGenericAccount();
					devices.add(computer);
				}
				world.setBlockToAir(pos);
				break;
			}
			case "monitor": {
				world.setBlockToAir(pos);
				break;
			}
			case "turret": {
				TileEntity te = world.getTileEntity(pos.down());
				if (te != null) {
					TileEntityTurret turret = (TileEntityTurret) te;
					turret.whitelistedTypes.add("sevendaystomine:bandit");
					turret.setOn(true);
					turret.getInventory().insertItem(0, new ItemStack(ModItems.SEVEN_MM_BULLET, 20+world.rand.nextInt(45)), false);
					devices.add(turret);
				}
				world.setBlockToAir(pos);
				break;
			}
			default:
				super.handleDataBlock(world, facing, pos, data, mirror);
				break;
			}
		} catch (Exception e) {
			Utils.getLogger().warn(pos.toString());
			e.printStackTrace();
		}
	}

}
