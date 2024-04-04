package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.entity.EntityCar;
import nuparu.sevendaystomine.entity.EntityMinibike;
import nuparu.sevendaystomine.util.MathUtils;

public class BlockFruitBush extends net.minecraft.block.BlockBush implements IGrowable, IBlockBase {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

	private static final AxisAlignedBB AABB_0 = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D);
	private static final AxisAlignedBB AABB_1 = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D);
	private static final AxisAlignedBB AABB_2 = new AxisAlignedBB(0.21875D, 0.0D, 0.21875D, 0.78125D, 0.5625D,
			0.78125D);
	private static final AxisAlignedBB AABB_3 = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.625D, 0.8125D);
	private static final AxisAlignedBB AABB_4 = new AxisAlignedBB(0.15625D, 0.0D, 0.15625D, 0.84375D, 0.6875D,
			0.84375D);
	private static final AxisAlignedBB AABB_5 = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.75D, 0.875D);
	private static final AxisAlignedBB AABB_6 = new AxisAlignedBB(0.09375D, 0.0D, 0.09375D, 0.90625D, 0.8125D,
			0.90625D);
	private static final AxisAlignedBB AABB_7 = new AxisAlignedBB(0.03125D, 0.0D, 0.03125D, 0.96875D, 0.9375D,
			0.96875D);

	public BlockFruitBush() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		this.setTickRandomly(true);
		this.setSoundType(SoundType.PLANT);
		this.disableStats();
		this.setCreativeTab((CreativeTabs) null);
		setHardness(0.1f);
		setResistance(0.2f);
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return null;
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		int age = getAge(state);
		switch (age) {
		case 0:
			return AABB_0.offset(pos);
		case 1:
			return AABB_1.offset(pos);
		case 2:
			return AABB_2.offset(pos);
		case 3:
			return AABB_3.offset(pos);
		case 4:
			return AABB_4.offset(pos);
		case 5:
			return AABB_5.offset(pos);
		case 6:
			return AABB_6.offset(pos);
		case 7:
			return AABB_7.offset(pos);
		}
		return super.getBoundingBox(state, worldIn, pos);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		int age = getAge(state);
		switch (age) {
		case 0:
			return AABB_0;
		case 1:
			return AABB_1;
		case 2:
			return AABB_2;
		case 3:
			return AABB_3;
		case 4:
			return AABB_4;
		case 5:
			return AABB_5;
		case 6:
			return AABB_6;
		case 7:
			return AABB_7;
		}
		return super.getBoundingBox(state, source, pos);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking()) {
			int age = getAge(state);
			boolean shears = playerIn.getHeldItem(hand).getItem() == Items.SHEARS;
			if (age == getMaxAge()) {
				state.getBlock().dropBlockAsItem(worldIn, pos, state, shears ? 3 : 1);
				worldIn.setBlockState(pos, withAge(getMaxAge() - 1));
				worldIn.playSound((EntityPlayer) null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
						SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.9f, 1.1f),
						MathUtils.getFloatInRange(1.2f, 1.5f));
				return true;
			} else if (age > 0 && shears) {
				worldIn.setBlockState(pos, withAge(age - 1));
				spawnAsEntity(worldIn, pos, new ItemStack(Items.STICK));
				if (playerIn instanceof EntityPlayerMP) {
					playerIn.getHeldItem(hand).attemptDamageItem(1, worldIn.rand, (EntityPlayerMP) playerIn);
				}
				worldIn.playSound((EntityPlayer) null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
						SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.9f, 1.1f),
						MathUtils.getFloatInRange(0.95f, 1.05f));
			}
		}
		return false;
	}

	public boolean canSustainBush(IBlockState state) {
		return state.getBlock() == Blocks.FARMLAND || state.getBlock() == Blocks.GRASS
				|| state.getBlock() == Blocks.DIRT;
	}

	protected PropertyInteger getAgeProperty() {
		return AGE;
	}

	public int getMaxAge() {
		return 7;
	}

	protected int getAge(IBlockState state) {
		return ((Integer) state.getValue(this.getAgeProperty())).intValue();
	}

	public IBlockState withAge(int age) {
		return this.getDefaultState().withProperty(this.getAgeProperty(), Integer.valueOf(age));
	}

	public boolean isMaxAge(IBlockState state) {
		return ((Integer) state.getValue(this.getAgeProperty())).intValue() >= this.getMaxAge();
	}

	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityCar || entityIn instanceof EntityMinibike) {
			worldIn.destroyBlock(pos, false);
			return;
		}
		entityIn.setInWeb();
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);

		if (!worldIn.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
			int i = this.getAge(state);

			if (i < this.getMaxAge()) {
				float f = getGrowthChance(this, worldIn, pos);

				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state,
						rand.nextInt((int) (25.0F / f) + 1) == 0)) {
					worldIn.setBlockState(pos, this.withAge(i + 1), 2);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state,
							worldIn.getBlockState(pos));
				}
			}
		}
	}

	public void grow(World worldIn, BlockPos pos, IBlockState state) {
		int i = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
		int j = this.getMaxAge();

		if (i > j) {
			i = j;
		}

		worldIn.setBlockState(pos, this.withAge(i), 2);
	}

	protected int getBonemealAgeIncrease(World worldIn) {
		return MathHelper.getInt(worldIn.rand, 2, 5);
	}

	protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos) {
		float f = 1.0F;
		BlockPos blockpos = pos.down();

		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				float f1 = 0.0F;
				IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));

				if (iblockstate.getBlock().canSustainPlant(iblockstate, worldIn, blockpos.add(i, 0, j),
						net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable) blockIn)) {
					f1 = 1.0F;

					if (iblockstate.getBlock().isFertile(worldIn, blockpos.add(i, 0, j))) {
						f1 = 3.0F;
					}
				}

				if (i != 0 || j != 0) {
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		BlockPos blockpos1 = pos.north();
		BlockPos blockpos2 = pos.south();
		BlockPos blockpos3 = pos.west();
		BlockPos blockpos4 = pos.east();
		boolean flag = blockIn == worldIn.getBlockState(blockpos3).getBlock()
				|| blockIn == worldIn.getBlockState(blockpos4).getBlock();
		boolean flag1 = blockIn == worldIn.getBlockState(blockpos1).getBlock()
				|| blockIn == worldIn.getBlockState(blockpos2).getBlock();

		if (flag && flag1) {
			f /= 2.0F;
		} else {
			boolean flag2 = blockIn == worldIn.getBlockState(blockpos3.north()).getBlock()
					|| blockIn == worldIn.getBlockState(blockpos4.north()).getBlock()
					|| blockIn == worldIn.getBlockState(blockpos4.south()).getBlock()
					|| blockIn == worldIn.getBlockState(blockpos3.south()).getBlock();

			if (flag2) {
				f /= 2.0F;
			}
		}

		return f;
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		IBlockState soil = worldIn.getBlockState(pos.down());
		return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos))
				&& soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
	}

	protected Item getCrop() {
		return null;
	}

	@Override
	public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, net.minecraft.world.IBlockAccess world,
			BlockPos pos, IBlockState state, int fortune) {

		int age = getAge(state);
		Random rand = world instanceof World ? ((World) world).rand : new Random();

		if (age >= getMaxAge()) {
			for (int i = 0; i < 2 + fortune; ++i) {
				if (rand.nextInt(2 * getMaxAge()) <= age) {
					drops.add(new ItemStack(this.getCrop(), 1, 0));
				}
			}
		}
	}

	/**
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
		super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this.isMaxAge(state) ? this.getCrop() : null;
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this.getCrop());
	}

	/**
	 * Whether this IGrowable can grow
	 */
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return !this.isMaxAge(state);
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		this.grow(worldIn, pos, state);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta) {
		return this.withAge(meta);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state) {
		return this.getAge(state);
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { AGE });
	}

	public boolean metaItemBlock() {
		return false;
	}

	public ItemBlock createItemBlock() {
		return new ItemBlock(this);
	}

	public static ItemBlock createItemBlock(Block block) {
		return new ItemBlock(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomItemMesh() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemMeshDefinition getItemMesh() {
		return null;
	}
}
