package nuparu.sevendaystomine.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.level.LevelUtils;
import nuparu.sevendaystomine.world.level.block.CalendarBlock;
import nuparu.sevendaystomine.world.level.block.entity.CalendarBlockEntity;
import org.jetbrains.annotations.NotNull;

public class CalendarRenderer implements BlockEntityRenderer<CalendarBlockEntity> {


    public CalendarRenderer(BlockEntityRendererProvider.Context p_173540_) {

    }

    @Override
    public void render(CalendarBlockEntity te, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level world = te.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? te.getBlockState() : ModBlocks.CALENDAR.get().defaultBlockState();
        Direction direction = blockstate.hasProperty(CalendarBlock.FACING) ? blockstate.getValue(CalendarBlock.FACING) : Direction.SOUTH;
        Block block = blockstate.getBlock();

        if (block instanceof CalendarBlock) {

            String text = "?";

            if (ServerConfig.bloodmoonFrequency.get() > 0 && ServerConfig.bloodmoonFrequency.get() <= 999) {
                int mod = LevelUtils.getDay(world) % ServerConfig.bloodmoonFrequency.get();
                int i = mod == 0 ? 0 : ServerConfig.bloodmoonFrequency.get() - (mod);
                text = i + "";
            }

            Font fontrenderer = Minecraft.getInstance().font;

            matrixStack.pushPose();
            float f = direction.toYRot();
            matrixStack.translate(0.5D, 0.90625, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(-f));
            matrixStack.mulPose(Axis.XP.rotationDegrees(180));
            matrixStack.translate((1-fontrenderer.width(text))*0.03125, 0,0.4355);
            matrixStack.scale(0.0625f,0.0625f,0.0625f);

            //fontrenderer.draw(matrixStack,text,0,0,0x000000);

            matrixStack.popPose();
        }
    }
}
