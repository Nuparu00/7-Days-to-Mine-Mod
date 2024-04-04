package nuparu.sevendaystomine.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.EnumDifficulty;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.EnumModParticleType;
import nuparu.sevendaystomine.util.MathUtils;

public class PotionBleeding extends PotionBase {

	public PotionBleeding(boolean badEffect, int color) {
		super(badEffect, color);
		this.setIconIndex(0, 0);
		setRegistryName(SevenDaysToMine.MODID, "bleeding");
		setPotionName("effect." + getRegistryName().getResourcePath());
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void performEffect(EntityLivingBase entity, int p_76394_2_) {
		if (entity.world.rand.nextInt(12) == 0) {
			entity.attackEntityFrom(DamageSources.bleeding, 1);
		}
		if (entity.world.isRemote && entity.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
			if (entity.world.rand.nextInt(5) == 0) {

				for (int i = 0; i < (int) Math
						.round(MathUtils.getDoubleInRange(1, 5) * SevenDaysToMine.proxy.getParticleLevel()); i++) {
					double x = entity.posX + MathUtils.getDoubleInRange(-1, 1) * entity.width;
					double y = entity.posY + MathUtils.getDoubleInRange(0, 1) * entity.height;
					double z = entity.posZ + MathUtils.getDoubleInRange(-1, 1) * entity.width;
					SevenDaysToMine.proxy.spawnParticle(entity.world, EnumModParticleType.BLOOD, x, y, z,
							MathUtils.getDoubleInRange(-1d, 1d) / 7d, MathUtils.getDoubleInRange(-0.5d, 1d) / 7d,
							MathUtils.getDoubleInRange(-1d, 1d) / 7d);
				}
			}
		}

	}
}
