package com.nuparu.sevendaystomine.world.gen.city.plot;

import java.util.HashMap;
import java.util.List;

import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.City;
import com.nuparu.sevendaystomine.world.gen.city.Street;
import com.nuparu.sevendaystomine.world.gen.city.building.Building;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Plot {
	public City city;
	public Street street;
	public Building building;
	public int xSize;
	public int zSize;
	public BlockPos start;
	public BlockPos end;
	public boolean mirror;

	public Plot(Street street, int index, Building building, boolean mirror, BlockPos start) {
		this.street = street;
		city = street.city;
		this.building = building;
		BlockPos dimensions = building.getDimensions(street.world, street.facing);
		xSize = dimensions.getX();
		zSize = dimensions.getZ();
		this.mirror = mirror;

		int dir = street.facing.getAxis() == EnumFacing.Axis.Z ? -1 : 1;
		switch (street.facing) {
		default:
		case EAST:
			this.start = start.offset(mirror ? EnumFacing.SOUTH : EnumFacing.NORTH,
					(Street.thickness / 2) + Street.pavement + 1);
			break;
		case WEST:
			this.start = start.offset(mirror ? EnumFacing.NORTH : EnumFacing.SOUTH,
					(Street.thickness / 2) + Street.pavement + 1);
			break;
		case NORTH:
			this.start = start.offset(mirror ? EnumFacing.EAST : EnumFacing.WEST,
					(Street.thickness / 2) + Street.pavement + 1);
			break;
		case SOUTH:
			this.start = start.offset(mirror ? EnumFacing.WEST : EnumFacing.EAST,
					(Street.thickness / 2) + Street.pavement + 1);
			break;

		}

		this.start = this.start.offset(street.facing, 5);
		end = start.offset(street.facing, street.facing.getAxis() == EnumFacing.Axis.Z ? zSize : xSize).offset(
				mirror ? street.facing.rotateY() : street.facing.rotateYCCW(),
				street.facing.getAxis() == EnumFacing.Axis.X ? zSize : xSize);
		this.end = Utils.getTopSolidGroundBlock(end,street.world);
	}

	public void generate() {

		
		BlockPos pos = start.offset(street.facing, street.facing.getAxis() == EnumFacing.Axis.Z ? zSize : xSize);

		building.generate(street.world, new BlockPos(pos.getX(),Math.min(pos.getY(), end.getY()),pos.getZ()), street.facing, mirror);
	}

	public boolean intersects(Plot plot) {
		AxisAlignedBB aabb = new AxisAlignedBB(getMin(), getMax().up(2));
		AxisAlignedBB aabb2 = new AxisAlignedBB(plot.getMin().up(), plot.getMax().up(2));

		return aabb.intersects(aabb2);
	}
	
	//Generates a diagonal of the building shape (rectangle) for debug purposes
	public void generateDiagonal() {
		int minX = Math.min(start.getX(), end.getX());
		int minZ = Math.min(start.getZ(), end.getZ());

		int maxX = Math.max(start.getX(), end.getX());
		int maxZ = Math.max(start.getZ(), end.getZ());

		Vec3d dif = new Vec3d(maxX - minX, 0, maxZ - minZ);
		Vec3d vec = dif.scale(1d / dif.lengthVector());

		for (double d = 0; d < dif.lengthVector(); d++) {
			Vec3d vec2 = vec.scale(d);
			city.world.setBlockState(new BlockPos(minX + vec2.x, 50, minZ + vec2.z),
					Blocks.IRON_BLOCK.getDefaultState());
		}
		city.world.setBlockState(new BlockPos(minX, 50, minZ), Blocks.REDSTONE_BLOCK.getDefaultState());
		city.world.setBlockState(new BlockPos(maxX, 50, maxZ), Blocks.GOLD_BLOCK.getDefaultState());

	}

	public BlockPos getMin() {
		return new BlockPos(Math.min(start.getX(), end.getX()), 0, Math.min(start.getZ(), end.getZ()));
	}

	public BlockPos getMax() {
		return new BlockPos(Math.max(start.getX(), end.getX()), 0, Math.max(start.getZ(), end.getZ()));
	}
}
