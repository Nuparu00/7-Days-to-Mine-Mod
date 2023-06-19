package nuparu.sevendaystomine.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
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
import nuparu.sevendaystomine.world.level.block.SolarPanelBlock;
import nuparu.sevendaystomine.world.level.block.entity.SolarPanelBlockEntity;
import org.jetbrains.annotations.NotNull;

public class SolarPanelRenderer implements BlockEntityRenderer<SolarPanelBlockEntity> {

    private final ModelPart globe;

    private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "entity/solar_panel");
    private final Material MAT = new Material(TextureAtlas.LOCATION_BLOCKS, TEX);

    public SolarPanelRenderer(BlockEntityRendererProvider.Context context) {
        this.globe = context.bakeLayer(ClientSetup.SOLAR_PANEL);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("globe", CubeListBuilder.create().texOffs(0, 34).addBox(-7, -1, -7, 14, 2, 14), PartPose.offset(0,0,0));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void render(SolarPanelBlockEntity te, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.SOLAR_PANEL.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(SolarPanelBlock.FACING) ? blockstate.getValue(SolarPanelBlock.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof SolarPanelBlock) {
            matrixStack.pushPose();
            float f = direction.toYRot();
            float rot = 180;
            if (flag && direction.getAxis() == Direction.Axis.X) {
                rot += (float)(Math.toDegrees(world.getSunAngle(partialTicks)) * (direction == Direction.EAST ? -1 : 1));
            }

            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrixStack.translate(0, 0.1875, 0);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(rot));

            VertexConsumer vertexconsumer = MAT.buffer(buffer, RenderType::entitySolid);
            globe.render(matrixStack, vertexconsumer, combinedLight, combinedOverlay);
            matrixStack.popPose();
        }
    }
}
