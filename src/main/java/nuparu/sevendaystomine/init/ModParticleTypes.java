package nuparu.sevendaystomine.init;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES,
            SevenDaysToMine.MODID);

    public static final RegistryObject<SimpleParticleType> BLOOD = PARTICLE_TYPES.register("blood",
            () -> new SimpleParticleType(false));

}
