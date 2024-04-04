package nuparu.sevendaystomine.client.renderer.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelFlagCloth;
import nuparu.sevendaystomine.client.model.ModelFlagPole;
import nuparu.sevendaystomine.tileentity.TileEntityFlag;

@SideOnly(Side.CLIENT)
public class TileEntityFlagRenderer extends TileEntitySpecialRenderer<TileEntityFlag>
{
    private static final ResourceLocation TEXTURE_CLOTH = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/flag/flag_cloth.png");
    private static final ResourceLocation TEXTURE_POLE = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/flag/flag_pole.png");

    private final ModelFlagPole modelPole = new ModelFlagPole();
    private final ModelFlagCloth modelCloth = new ModelFlagCloth();
    
    public TileEntityFlagRenderer()
    {
    }

    public void render(TileEntityFlag te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        //
        int i;

        if (te.hasWorld())
        {
            i = te.getBlockMetadata();
        }
        else
        {
            i = 0;
        }
        
        boolean flag = false;
        
        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else {
        	flag = true;
        }

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();

            if (destroyStage < 0)
            {
                GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            }

            float xx = (float)te.getPos().getX();
            float yy = (float)te.getPos().getY()+1;
            float zz = (float)te.getPos().getZ()+1;
            
            GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            //GlStateManager.translate(0.5F, 0.5F, 0.5F);
            int j = 0;

            if(i == 0) {
            	GlStateManager.translate(1F, 0F, 1F);
            	xx++;
            	zz++;
            }
            
            if (i == 1)
            {
            	GlStateManager.translate(1F, 0F, 0F);
            	xx++;
            	j = 90;
            }
            
            if (i == 2)
            {
                j = 180;
            }

            if (i == 3)
            {
            	GlStateManager.translate(0F, 0F, 1F);
            	z++;
            	j = 270;
            }

            GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);

            if(flag) {
            	this.bindTexture(TEXTURE_POLE);
            }
            modelPole.render(0.0625f);
            if(flag) {
            	this.bindTexture(TEXTURE_CLOTH);
            }
            
            GlStateManager.translate(0F, 0.3F, 0.6F);
            if(i == 0) {
            	
            }
            
            modelCloth.flagRotator.rotateAngleZ=(float)Math.sin(te.getWorld().getWorldTime()/50f)*0.122173f;
            //modelCloth.flagRotator.rotateAngleX=-1.0472f;
            //GlStateManager.translate(-xx, -yy, -zz);
            GlStateManager.rotate(-60, 1, 0, 0);
            //GlStateManager.translate(xx, yy, zz);
            //GlStateManager.rotate((float)Math.sin(te.getWorld().getWorldTime()/100f)*2, 0, 0, 1);
            
            
            modelCloth.render(0.0625f);
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (destroyStage >= 0)
            {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        
    }
}