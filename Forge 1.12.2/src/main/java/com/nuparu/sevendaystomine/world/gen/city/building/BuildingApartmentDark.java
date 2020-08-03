package com.nuparu.sevendaystomine.world.gen.city.building;

import java.util.Map;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.city.Street;

import net.minecraft.block.Block;
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

public class BuildingApartmentDark extends BuildingApartment {

	private ResourceLocation[] mid_odd = new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_mid_a1"),
			new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_mid_a2") };
	private ResourceLocation[] mid_even = new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_mid_b1"),
			new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_mid_b2") };
	private ResourceLocation[] top_odd = new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_top") };
	private ResourceLocation[] top_even = new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_top2") };
	private ResourceLocation[] roof = new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_roof") };

	public BuildingApartmentDark(ResourceLocation res, int weight) {
		super(res, weight);
	}
	@Override
	public void generate(World world, BlockPos pos, EnumFacing facing, boolean mirror) {
		if (!world.isRemote) {
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();

			Template template = templatemanager.getTemplate(minecraftserver, res);
			if (template == null) {
				return;
			}

			Rotation rot = Utils.facingToRotation(facing.rotateYCCW());

			pos = pos.down();

			PlacementSettings placementsettings = (new PlacementSettings()).setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot)
					.setIgnoreEntities(false).setChunk((ChunkPos) null).setReplacedBlock((Block) null)
					.setIgnoreStructureBlock(false);

			template.getDataBlocks(pos, placementsettings);
			template.addBlocksToWorld(world, pos, placementsettings);

			Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {
				handleDataBlock(world, facing, entry.getKey(), entry.getValue());
			}

			int segments = world.rand.nextInt(7) + 2;

			for (int i = 0; i < segments; i++) {
				pos = pos.up(5);
				ResourceLocation res2 = null;
				if (i >= segments - 1) {
					res2 = roof[world.rand.nextInt(roof.length)];
				} else if (i >= segments - 2) {
					if (i % 2 == 0) {
						res2 = top_odd[world.rand.nextInt(top_odd.length)];
					} else {
						res2 = top_even[world.rand.nextInt(top_even.length)];
					}
				} else if (i % 2 == 0) {
					res2 = mid_odd[world.rand.nextInt(mid_odd.length)];
				} else {
					res2 = mid_even[world.rand.nextInt(mid_even.length)];
				}

				template = templatemanager.getTemplate(minecraftserver, res2);
				if (template == null) {
					return;
				}
				if(template.getSize().getY()+pos.getY() > 255) {
					i = segments-2;
					continue;
				}
				template.getDataBlocks(pos, placementsettings);
				template.addBlocksToWorld(world, pos, placementsettings);
				map = template.getDataBlocks(pos, placementsettings);

				for (Entry<BlockPos, String> entry : map.entrySet()) {
					handleDataBlock(world, facing, entry.getKey(), entry.getValue());
				}
			}

		}
	}
}
