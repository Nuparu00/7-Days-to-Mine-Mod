package nuparu.sevendaystomine.world.gen.city.plot;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.City;
import nuparu.sevendaystomine.world.gen.city.Street;
import nuparu.sevendaystomine.world.gen.city.building.Building;

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
					(city.type.getRoadWidth() / 2) + city.type.getPavementWidth() + 1);
			break;
		case WEST:
			this.start = start.offset(mirror ? EnumFacing.NORTH : EnumFacing.SOUTH,
					(city.type.getRoadWidth() / 2) + city.type.getPavementWidth() + 1);
			break;
		case NORTH:
			this.start = start.offset(mirror ? EnumFacing.EAST : EnumFacing.WEST,
					(city.type.getRoadWidth() / 2) + city.type.getPavementWidth() + 1);
			break;
		case SOUTH:
			this.start = start.offset(mirror ? EnumFacing.WEST : EnumFacing.EAST,
					(city.type.getRoadWidth() / 2) + city.type.getPavementWidth() + 1);
			break;

		}

		this.start = this.start.offset(street.facing, 5);
		end = start.offset(street.facing, street.facing.getAxis() == EnumFacing.Axis.Z ? zSize : xSize).offset(
				mirror ? street.facing.rotateY() : street.facing.rotateYCCW(),
				street.facing.getAxis() == EnumFacing.Axis.X ? zSize : xSize);
		this.end = Utils.getTopGroundBlock(end, street.world, true);
	}

	public void generate() {

		BlockPos pos = start.offset(street.facing, street.facing.getAxis() == EnumFacing.Axis.Z ? zSize : xSize);
		int yStart = start.getY();
		int yEnd = end.getY();
		

		building.generate(street.world, new BlockPos(pos.getX(), MathUtils.lerp(yStart, yEnd, 0.5f), pos.getZ()),
				street.facing, mirror, street.city.rand);
	}

	public boolean intersects(Plot plot) {
		AxisAlignedBB aabb = new AxisAlignedBB(getMin(), getMax().up(2));
		AxisAlignedBB aabb2 = new AxisAlignedBB(plot.getMin().up(), plot.getMax().up(2));

		return aabb.intersects(aabb2);
	}

	public BlockPos getMin() {
		return new BlockPos(Math.min(start.getX(), end.getX()), 0, Math.min(start.getZ(), end.getZ()));
	}

	public BlockPos getMax() {
		return new BlockPos(Math.max(start.getX(), end.getX()), 0, Math.max(start.getZ(), end.getZ()));
	}
}
