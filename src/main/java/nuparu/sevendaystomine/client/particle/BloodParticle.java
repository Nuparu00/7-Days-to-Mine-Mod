package nuparu.sevendaystomine.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import nuparu.sevendaystomine.util.MathUtils;

public class BloodParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    protected BloodParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize((float) 0.2, (float) 0.2);
        this.scale(MathUtils.getFloatInRange(0.15f,0.4f));
        this.setLifetime(MathUtils.getIntInRange(120,240));
        this.gravity = 0.6f;
        this.hasPhysics = true;
        this.xd = vx * 1;
        this.yd = vy * 1;
        this.zd = vz * 1;
        this.pickSprite(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        /*if(stoppedByCollision && !onGround){
            xd = 0;
            zd = 0;
            gravity = 0.05f;
        }
        else{
            gravity = 0.6f;
        }*/
    }
}
