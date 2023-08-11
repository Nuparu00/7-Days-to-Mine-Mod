package nuparu.sevendaystomine.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.level.block.WindTurbineBlock;
import nuparu.sevendaystomine.world.level.block.entity.WindTurbineBlockEntity;
import org.jetbrains.annotations.NotNull;

public class WindTurbineRenderer implements BlockEntityRenderer<WindTurbineBlockEntity> {



    public WindTurbineRenderer(BlockEntityRendererProvider.Context context) {
    }



    @Override
    public void render(WindTurbineBlockEntity te, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.WIND_TURBINE.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(WindTurbineBlock.FACING) ? blockstate.getValue(WindTurbineBlock.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof WindTurbineBlock) {
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(SevenDaysToMine.MODID,"wind_turbine_blades"), ""));

            //No idea why these weird transformations are needed
            int xOffset = 0, zOffset = 0;
            double xRotOffset = -0.5, yRotOffset = 0.5,zRotOffset = 0;

            switch (direction) {
                case SOUTH -> {
                    zOffset = 1;
                    xRotOffset = 0.5;
                    zRotOffset = -0.5;
                }
                case NORTH -> xOffset = -1;
                case EAST -> {
                    xOffset = -1;
                    zOffset = 1;
                }
                case WEST -> {
                    xRotOffset = 0.5;
                    zRotOffset = -0.5;
                }
            }

            float rotation = 0f;
            if (partialTicks == 1.0F) {
                rotation = te.getAngle();
            } else {
                rotation = te.getAngle() + (te.getAngle() - te.getAnglePrev()) * partialTicks;
            }

            matrixStack.pushPose();
            matrixStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            matrixStack.translate(xRotOffset,yRotOffset,zRotOffset);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(-rotation * 360));
            matrixStack.translate(-xRotOffset,-yRotOffset,-zRotOffset);
            VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.cutout());
            matrixStack.translate(xOffset,0,zOffset);

            //Minecraft.getInstance().getItemRenderer().renderModelLists(model, ItemStack.EMPTY,combinedLight,combinedOverlay,matrixStack,vertexBuilder);
            //Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(Items.BARRIER), ItemTransforms.TransformType.FIXED, true, matrixStack, type -> buffer.getBuffer(RenderType.solid()), combinedLight, combinedOverlay, model);
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(ModBlocks.WIND_TURBINE_BLADES.get().defaultBlockState(), matrixStack,buffer,combinedLight,combinedOverlay);
            matrixStack.popPose();

        }
    }


    @Override
    public int getViewDistance() {
        return 128;
    }
}
