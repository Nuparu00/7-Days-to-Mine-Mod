package nuparu.sevendaystomine.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.level.block.GlobeBlock;
import nuparu.sevendaystomine.world.level.block.entity.GlobeBlockEntity;
import org.jetbrains.annotations.NotNull;

public class GlobeRenderer implements BlockEntityRenderer<GlobeBlockEntity> {

    private final ModelPart globe;

    private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "entity/globe");
    private final Material MAT = new Material(TextureAtlas.LOCATION_BLOCKS, TEX);

    public GlobeRenderer(BlockEntityRendererProvider.Context context) {
        this.globe = context.bakeLayer(ClientSetup.GLOBE_LAYER);

    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("globe", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 10.0F, 10.0F, 10.0F), PartPose.offset(-5, 13, -5));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void render(GlobeBlockEntity te, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.GLOBE.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(GlobeBlock.FACING) ? blockstate.getValue(GlobeBlock.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof GlobeBlock) {
            matrixStack.pushPose();
            float f = direction.toYRot();
            double rot = 0;
            double angle = te.getAngle();
            double prev = te.getAnglePrev();

            rot = prev + (angle - prev) * partialTicks;
            matrixStack.translate(0.5D, 1.5D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-f));
            matrixStack.translate(0D, 0, -0.605);
            matrixStack.mulPose(Axis.XP.rotationDegrees(-23.5f));
            matrixStack.mulPose(Axis.YP.rotationDegrees((float) (rot)));
            matrixStack.mulPose(Axis.XP.rotationDegrees(180));

            VertexConsumer vertexconsumer = MAT.buffer(buffer, RenderType::entitySolid);
            globe.render(matrixStack, vertexconsumer, combinedLight, combinedOverlay);
            matrixStack.popPose();
        }
    }
}
