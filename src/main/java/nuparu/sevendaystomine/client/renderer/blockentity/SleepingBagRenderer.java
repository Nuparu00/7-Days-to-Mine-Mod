package nuparu.sevendaystomine.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.world.level.block.entity.SleepingBagBlockEntity;

import java.util.Arrays;
import java.util.Comparator;

public class SleepingBagRenderer implements BlockEntityRenderer<SleepingBagBlockEntity> {

    private final Material[] SLEEPING_BAG_TEXTURES = Arrays.stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).map((p_228770_0_) -> new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(SevenDaysToMine.MODID,"entity/sleeping_bag/" + p_228770_0_.getName()))).toArray(Material[]::new);
    private final ModelPart headRoot;
    private final ModelPart footRoot;

    public SleepingBagRenderer(BlockEntityRendererProvider.Context p_173540_) {
        this.headRoot = p_173540_.bakeLayer(ClientSetup.SLEEPING_BAG_HEAD_LAYER);
        this.footRoot = p_173540_.bakeLayer(ClientSetup.SLEEPING_BAG_FOOT_LAYER);
    }

    public static LayerDefinition createHeadLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 1.0F, 15.0F), PartPose.offset(-7, 23, -8));
        partdefinition.addOrReplaceChild("second", CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 12.0F, 1.0F, 7.0F), PartPose.offset(-6, 22, -8));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static LayerDefinition createFootLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, 0.0F, 0.0F, 14.0F, 1.0F, 15.0F), PartPose.offset(-7, 23, -7));
        partdefinition.addOrReplaceChild("second", CubeListBuilder.create().texOffs(0, 40).addBox(0.0F, 0.0F, 0.0F, 12.0F, 1.0F, 14.0F), PartPose.offset(-6, 22, -6));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public void render(SleepingBagBlockEntity p_112205_, float p_112206_, PoseStack p_112207_, MultiBufferSource p_112208_, int p_112209_, int p_112210_) {
        Material material = SLEEPING_BAG_TEXTURES[p_112205_.getColor().getId()];
        Level level = p_112205_.getLevel();
        if (level != null) {
            BlockState blockstate = p_112205_.getBlockState();
            DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> neighborcombineresult = DoubleBlockCombiner.combineWithNeigbour(BlockEntityType.BED, BedBlock::getBlockType, BedBlock::getConnectedDirection, ChestBlock.FACING, blockstate, level, p_112205_.getBlockPos(), (p_112202_, p_112203_) -> false);
            int i = neighborcombineresult.<Int2IntFunction>apply(new BrightnessCombiner<>()).get(p_112209_);
            this.renderPiece(p_112207_, p_112208_, blockstate.getValue(BedBlock.PART) == BedPart.HEAD ? this.headRoot : this.footRoot, blockstate.getValue(BedBlock.FACING), material, i, p_112210_, false);
        } else {
            this.renderPiece(p_112207_, p_112208_, this.headRoot, Direction.SOUTH, material, p_112209_, p_112210_, false);
            this.renderPiece(p_112207_, p_112208_, this.footRoot, Direction.SOUTH, material, p_112209_, p_112210_, true);
        }

    }

    private void renderPiece(PoseStack p_173542_, MultiBufferSource p_173543_, ModelPart p_173544_, Direction p_173545_, Material p_173546_, int p_173547_, int p_173548_, boolean p_173549_) {
        p_173542_.pushPose();
        p_173542_.translate(0.0D, 0.5, p_173549_ ? -1.0D : 0.0D);
        p_173542_.translate(0.5D, 0.5D, 0.5D);
        p_173542_.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        p_173542_.mulPose(Vector3f.YP.rotationDegrees(180.0F + p_173545_.toYRot()));
        p_173542_.translate(0D, -0.5D, 0D);
        VertexConsumer vertexconsumer = p_173546_.buffer(p_173543_, RenderType::entitySolid);
        p_173544_.render(p_173542_, vertexconsumer, p_173547_, p_173548_);
        p_173542_.popPose();
    }
}
