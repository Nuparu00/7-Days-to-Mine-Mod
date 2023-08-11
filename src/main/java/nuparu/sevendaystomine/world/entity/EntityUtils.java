package nuparu.sevendaystomine.world.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.List;
import java.util.Optional;

public class EntityUtils {
    public static HitResult raytraceEntities(Entity entity, double dst, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {

        Entity pointedEntity = null;

        Vec3 vec3d = entity.getEyePosition(1);

        BlockHitResult r = rayTraceServer(entity, dst, 1,blockMode,fluidMode);
        double d1 = r.getLocation().distanceTo(vec3d);

        Vec3 vec3d1 = entity.getLookAngle();
        Vec3 vec3d2 = vec3d.add(vec3d1.x * dst, vec3d1.y * dst, vec3d1.z * dst);
        List<Entity> list = entity.level().getEntities(entity,
                entity.getBoundingBox().inflate(vec3d1.x * dst, vec3d1.y * dst, vec3d1.z * dst),
                EntityUtils::isRayCastable);

        Vec3 vec3d3 = null;
        double d2 = d1;
        for (int j = 0; j < list.size(); ++j) {
            Entity entity1 = list.get(j);
            AABB axisalignedbb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
            Optional<Vec3> optional = axisalignedbb.clip(vec3d, vec3d2);

            if (axisalignedbb.contains(vec3d)) {
                if (d2 >= 0.0D) {
                    pointedEntity = entity1;
                    vec3d3 = optional.orElse(vec3d);
                    d2 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vector3d1 = optional.get();
                double d3 = vec3d.distanceToSqr(vector3d1);

                if (d3 < d2 || d2 == 0.0D) {
                    if (entity1.getRootVehicle() == entity.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d2 == 0.0D) {
                            pointedEntity = entity1;
                            vec3d3 = vector3d1;
                        }
                    } else {
                        pointedEntity = entity1;
                        vec3d3 = vector3d1;
                        d2 = d3;
                    }
                }
            }
            int z = 0;
        }

        if (pointedEntity != null) {
            return new EntityHitResult(pointedEntity, vec3d3);
        }
        return null;
    }

    public static BlockHitResult rayTraceServer(Entity entity, double blockReachDistance, float partialTicks, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
        Vec3 vec3 = getPositionEyesServer(entity, partialTicks);
        Vec3 vec31 = entity.getLookAngle();
        Vec3 vec32 = vec3.add(vec31.x * blockReachDistance, vec31.y * blockReachDistance,
                vec31.z * blockReachDistance);
        return entity.level().clip(new ClipContext(vec3, vec32, blockMode,
                fluidMode, entity));
    }

    public static Vec3 getPositionEyesServer(Entity entity, float partialTicks) {
        if (partialTicks == 1.0F) {
            return new Vec3(entity.getX(), entity.getY() + (double) entity.getEyeHeight(), entity.getZ());
        } else {
            double d0 = entity.xOld + (entity.getX() - entity.xOld) * (double) partialTicks;
            double d1 = entity.yOld + (entity.getY() - entity.yOld) * (double) partialTicks
                    + (double) entity.getEyeHeight();
            double d2 = entity.zOld + (entity.getZ() - entity.zOld) * (double) partialTicks;
            return new Vec3(d0, d1, d2);
        }
    }

    public static boolean isRayCastable(Entity entity){
        return !(entity.isSpectator()) && entity.getBoundingBox().getSize() > 0;
    }

    public static Entity getEntityByNBTAndResource(ResourceLocation resourceLocation, CompoundTag nbt, Level world) {

        if (nbt != null) {
            return EntityType.create(nbt, world).orElse(null);
        }
        return null;
    }
}
