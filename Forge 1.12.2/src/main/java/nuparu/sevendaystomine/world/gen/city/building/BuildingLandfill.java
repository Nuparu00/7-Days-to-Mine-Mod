package nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
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
import nuparu.sevendaystomine.block.BlockChemistryStation;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.EnumCityType;

public class BuildingLandfill extends Building {

	private final ResourceLocation TRUCKS = new ResourceLocation(SevenDaysToMine.MODID, "landfill_trucks");
	private final ResourceLocation OFFICE = new ResourceLocation(SevenDaysToMine.MODID, "landfill_office");
	private final ResourceLocation BULDOZER = new ResourceLocation(SevenDaysToMine.MODID, "landfill_buldozer");
	private final ResourceLocation CHIMNEY = new ResourceLocation(SevenDaysToMine.MODID, "landfill_chimney");
	private final ResourceLocation BACK_R = new ResourceLocation(SevenDaysToMine.MODID, "landfill_back_r");
	private final ResourceLocation BACK_L = new ResourceLocation(SevenDaysToMine.MODID, "landfill_back_l");

	public BuildingLandfill(int weight) {
		this(weight, 2);
	}

	public BuildingLandfill(int weight, int yOffset) {
		super(null, weight, yOffset);
		canBeMirrored = false;
		this.pedestalState = Blocks.STONE.getDefaultState();
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror, Random rand) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, OFFICE);
			if (template == null) {
				return;
			}

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
			pos = pos.up(yOffset);

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true, rand);

			template = templatemanager.getTemplate(minecraftserver, BULDOZER);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing.rotateY(), -32);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true, rand);

			template = templatemanager.getTemplate(minecraftserver, BACK_R);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing.rotateY(), -32);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true, rand);

			template = templatemanager.getTemplate(minecraftserver, BACK_L);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing, mirror ? 32 : -32);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true, rand);

			template = templatemanager.getTemplate(minecraftserver, CHIMNEY);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing.rotateY(), 32);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true, rand);

			template = templatemanager.getTemplate(minecraftserver, TRUCKS);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing.rotateY(), 32);
			this.generateTemplate(worldserver, pos, mirror, facing, placementsettings, template, true, rand);
		}
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return new BlockPos(50, 22, 50);
	}

}
