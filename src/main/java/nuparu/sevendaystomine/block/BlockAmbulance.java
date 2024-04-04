package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.tileentity.TileEntityCar;
import nuparu.sevendaystomine.world.gen.city.CityHelper;

public class BlockAmbulance extends BlockCar {

	public BlockAmbulance() {
		super(new byte[][][] { { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } },
				{ { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 }, { 0, 0, 0 } } });
		CityHelper.cars.add(this);
		lootTable = ModLootTables.MEDICAL_CABINET;
		special = true;
		setHardness(20.0F);
		setResistance(5.0F);
	}

	/*
	 * Have to cache this somewhere someday so we do not have to chec kall the
	 * blocks and tileentites each tick
	 */
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		double height = 1;
		double width = 1;
		double length = 1;
		
		if(!(world.getTileEntity(pos) instanceof TileEntityCar)) {
			return FULL_BLOCK_AABB;
		}
		
		if(world == null || pos == null) {
			return FULL_BLOCK_AABB;
		}

		TileEntityCar te = (TileEntityCar) world.getTileEntity(pos);
		TileEntity teUp = world.getTileEntity(pos.up());

		EnumFacing facing = state.getValue(BlockCar.FACING);
		if (world.getBlockState(pos.down()).getBlock() instanceof BlockAmbulance
				&& (te == null || ((TileEntityCar) world.getTileEntity(pos.down())).getMaster() == te.getMaster())) {
			height = 1d;
			IBlockState front = world.getBlockState(pos.offset(facing, 1));
			IBlockState back = world.getBlockState(pos.offset(facing, -1));

			TileEntity teFront = world.getTileEntity(pos.offset(facing, 1));
			TileEntity teBack = world.getTileEntity(pos.offset(facing, -1));

			if (!(front.getBlock() instanceof BlockCar) || te == null || teFront == null
					|| ((TileEntityCar) teFront).getMaster() != te.getMaster()) {
				if (facing.getAxis() == EnumFacing.Axis.X) {
					if (facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
						width = 0.5f;
					} else {
						width = -0.5f;

					}
				}else {
					if (facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
						length = 0.5f;
					} else {
						length = -0.5f;
					}
				}
			}

			else if (!(back.getBlock() instanceof BlockCar) || te == null || teBack == null
					|| ((TileEntityCar) teBack).getMaster() != te.getMaster()) {
				if (facing.getAxis() == EnumFacing.Axis.X) {
					if (facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
						width = 0.5f;
					} else {
						width = -0.5f;

					}
				}
			}
		}
		EnumFacing rightDirection = facing.rotateY();
		IBlockState right = world.getBlockState(pos.offset(rightDirection, 1));
		EnumFacing leftDirection = facing.rotateYCCW();
		IBlockState left = world.getBlockState(pos.offset(leftDirection, 1));

		TileEntity teRight = world.getTileEntity(pos.offset(rightDirection, 1));
		TileEntity teLeft = world.getTileEntity(pos.offset(leftDirection, 1));

		if (!(right.getBlock() instanceof BlockCar) || te == null || teRight == null
				|| ((TileEntityCar) teRight).getMaster() != te.getMaster()) {
			if (rightDirection.getAxis() == EnumFacing.Axis.X) {
				if (rightDirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
					width = 0.5f;
				} else {
					width = -0.5f;

				}
			} else {
				if (rightDirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
					length = 0.5f;
				} else {
					length = -0.5f;
				}
			}
		}

		if (!(left.getBlock() instanceof BlockCar) || te == null || teLeft == null
				|| ((TileEntityCar) teLeft).getMaster() != te.getMaster()) {
			if (leftDirection.getAxis() == EnumFacing.Axis.X) {
				if (leftDirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
					width = 0.5f;
				} else {
					width = -0.5f;

				}
			} else {
				if (facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
					length = -0.5f;
				} else {
					length = 0.5f;

				}
			}
		}
		AxisAlignedBB aabb = new AxisAlignedBB(width > 0 ? 0 : -width, 0, length > 0 ? 0 : -length,
				width > 0 ? width : 1, height, length > 0 ? length : 1);

		return aabb;
	}

	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return this.getCollisionBoundingBox(state, world, pos);
	}

}
