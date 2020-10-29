package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockCodeSafe;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.city.Street;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class BuildingMilitaryBase extends Building {

	private ResourceLocation FLAG = new ResourceLocation(SevenDaysToMine.MODID, "military_base_flag");
	private ResourceLocation STORAGE = new ResourceLocation(SevenDaysToMine.MODID, "military_base_storage");
	private ResourceLocation RESIDENT = new ResourceLocation(SevenDaysToMine.MODID, "military_base_resident");
	private ResourceLocation ADMIN = new ResourceLocation(SevenDaysToMine.MODID, "military_base_admin");

	public BuildingMilitaryBase(int weight) {
		this(weight, 0);
	}

	public BuildingMilitaryBase(int weight, int yOffset) {
		super(null, weight, yOffset);
		canBeMirrored = false;
		this.pedestalState = Blocks.STONE.getDefaultState();
	}

	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, FLAG);
			if (template == null) {
				return;
			}

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());
			pos = pos.up(yOffset);

			PlacementSettings placementsettings = (new PlacementSettings())
					.setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
					.setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			template.addBlocksToWorld(world, pos, placementsettings);

			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);

			template = templatemanager.getTemplate(minecraftserver, STORAGE);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing, -22);
			template.addBlocksToWorld(world, pos, placementsettings);
			map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
			
			template = templatemanager.getTemplate(minecraftserver, ADMIN);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing.rotateY(), mirror ? 28 : -28);
			template.addBlocksToWorld(world, pos, placementsettings);
			map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
			
			template = templatemanager.getTemplate(minecraftserver, RESIDENT);
			if (template == null) {
				return;
			}
			pos = pos.offset(facing, 22);
			template.addBlocksToWorld(world, pos, placementsettings);
			map = template.getDataBlocks(pos, placementsettings);
			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue(), mirror);
			}
			generatePedestal(world, pos, template, facing, mirror);
		}
	}

	@Override
	public BlockPos getDimensions(World world, EnumFacing facing) {
		return new BlockPos(32, 22, 32);
	}

	@Override
	public void handleDataBlock(World world, EnumFacing facing, BlockPos pos, String data, boolean mirror) {
		switch (data) {
		case "map": {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			for (int i = -1; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					BlockPos pos2 = pos.down(1 + j).offset(facing, i);
					List<EntityItemFrame> list = world.getEntitiesWithinAABB(EntityItemFrame.class,
							new AxisAlignedBB(pos2, pos2.add(1, 1, 1)));
					if (list.isEmpty())
						continue;
					EntityItemFrame frame = list.get(0);
					int x = pos.getX() + (256 * i * (mirror ? -1 : 1));
					int z = pos.getZ() + (256 * j);
					ItemStack stack = ItemMap.setupNewMap(world, x, z, (byte) 1, true, true);
					Utils.renderBiomePreviewMap(world, stack);
					MapData.addTargetDecoration(stack, pos, "+", MapDecoration.Type.TARGET_X);
					frame.setDisplayedItem(stack);
				}
			}
			break;
		}
		default:
			super.handleDataBlock(world, facing, pos, data, mirror);
		}
	}
}
