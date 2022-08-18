package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.CropBlock.getGrowthSpeed;

public class CornPlantBlock extends BushBlock implements BonemealableBlock ,IBlockBase {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public CornPlantBlock(Properties p_51021_) {
        super(p_51021_);
        this.registerDefaultState(this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER).setValue(AGE, 0));
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter p_52258_, BlockPos p_52259_, BlockState p_52260_, boolean p_52261_) {
        return !this.isMaxAge(p_52260_);
    }

    @Override
    public boolean isBonemealSuccess(Level p_221045_, RandomSource p_221046_, BlockPos p_221047_, BlockState p_221048_) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel p_221040_, RandomSource p_221041_, BlockPos p_221042_, BlockState p_221043_) {
        this.growCrops(p_221040_, p_221042_, p_221043_);
    }

    @Override
    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
        if(!(p_196260_2_.getRawBrightness(p_196260_3_, 0) >= 8 || p_196260_2_.canSeeSky(p_196260_3_))) return false;
        if (p_196260_1_.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(p_196260_1_, p_196260_2_, p_196260_3_);
        } else {
            BlockState blockstate = p_196260_2_.getBlockState(p_196260_3_.below());
            if (p_196260_1_.getBlock() != this) return super.canSurvive(p_196260_1_, p_196260_2_, p_196260_3_); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState p_51042_, BlockGetter p_51043_, BlockPos p_51044_) {
        return p_51042_.is(BlockTags.DIRT) || p_51042_.is(Blocks.FARMLAND) || p_51042_.is(Blocks.GRASS_BLOCK);
    }

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 3;
    }

    protected int getAge(BlockState p_185527_1_) {
        return p_185527_1_.getValue(this.getAgeProperty());
    }

    public BlockState getStateForAge(int p_185528_1_) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), p_185528_1_);
    }

    public boolean isMaxAge(BlockState p_185525_1_) {
        return p_185525_1_.getValue(this.getAgeProperty()) >= this.getMaxAge();
    }

    @Override
    public boolean isRandomlyTicking(BlockState p_149653_1_) {
        return !this.isMaxAge(p_149653_1_);
    }

    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!world.isAreaLoaded(pos, 1))
            return;
        if (world.getRawBrightness(pos, 0) >= 9) {
            int i = this.getAge(state);
            int j = this.getMaxAge();
            if (i < j) {
                float f = getGrowthSpeed(this, world, pos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, pos, state,
                        random.nextInt((int) (25.0F / f) + 1) == 0)) {
                    DoubleBlockHalf half = state.getValue(HALF);

                    int newAge = i+1;
                    if(half == DoubleBlockHalf.LOWER){
                        BlockState topState = world.getBlockState(pos.above());
                        if(i <= 1){
                            world.setBlock(pos, this.getStateForAge(newAge), 2);
                        }
                        else if(i==2) {

                            if (!(topState.getBlock() instanceof CornPlantBlock)) {
                                world.setBlock(pos, this.getStateForAge(i), 2);
                                world.setBlock(pos.above(), this.getStateForAge(0).setValue(HALF, DoubleBlockHalf.UPPER), 2);
                            } else {
                                int topAge = getAge(topState);
                                if (topAge == 2) {
                                    world.setBlock(pos, this.getStateForAge(newAge), 2);
                                    world.setBlock(pos.above(), this.getStateForAge(newAge).setValue(HALF, DoubleBlockHalf.UPPER), 2);
                                }
                            }
                        }
                    }
                    else if (newAge < getMaxAge()){
                        world.setBlock(pos, this.getStateForAge(newAge), 2);
                    }
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos, state);
                }
            }
        }
    }

    public void growCrops(Level world, BlockPos pos, BlockState state) {
        int increase = this.getBonemealAgeIncrease(world);
        int i = this.getAge(state) + increase;
        int j = this.getMaxAge();

        if(i > j){
            i = j;
        }

        DoubleBlockHalf half = state.getValue(HALF);

        if(half == DoubleBlockHalf.LOWER){
            BlockState topState = world.getBlockState(pos.above());
            if(i==j-1 && topState.getBlock() != this){
                world.setBlock(pos, this.getStateForAge(i), 2);
                world.setBlock(pos.above(), this.getStateForAge(0).setValue(HALF,DoubleBlockHalf.UPPER), 2);
            }
            else if(topState.getBlock() == this){
                int topAge = getAge(topState);
                if(topAge == getMaxAge()-1){
                    world.setBlock(pos, this.getStateForAge(i), 2);
                    world.setBlock(pos.above(), this.getStateForAge(i).setValue(HALF,DoubleBlockHalf.UPPER), 2);
                }
                else{
                    int topAgeNew = topAge+increase;
                    if(topAgeNew == getMaxAge()){
                        world.setBlock(pos, this.getStateForAge(topAgeNew), 2);
                    }
                    world.setBlock(pos.above(), this.getStateForAge(topAgeNew).setValue(HALF,DoubleBlockHalf.UPPER), 2);
                }
            }
        }
        else{
            BlockState lowerState = world.getBlockState(pos.below());
            if(lowerState.getBlock() != this){
                return;
            }
            growCrops(world,pos.below(),lowerState);
        }

    }


    protected int getBonemealAgeIncrease(Level p_52262_) {
        return Mth.nextInt(p_52262_.random, 0, 2);
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_52286_) {
        p_52286_.add(AGE, HALF);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties().tab(getItemGroup());
        return new BlockItem(this, properties);
    }
}
