package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.client.model.entity.CarModel;
import nuparu.sevendaystomine.world.entity.item.CarEntity;
import org.jetbrains.annotations.NotNull;

public class CarRenderer<T extends CarEntity, M extends CarModel<T>> extends LivingEntityRenderer<T, M> {
	private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_white.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_orange.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_magenta.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_light_blue.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_yellow.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_lime.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_pink.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_gray.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_light_gray.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_cyan.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_purple.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_blue.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_brown.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_green.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_red.png"),
			new ResourceLocation(SevenDaysToMine.MODID,
					"textures/entity/car/car_black.png"),

	};

	public CarRenderer(EntityRendererProvider.Context manager) {
		super(manager, (M) new CarModel<T>(manager.bakeLayer(ClientSetup.CAR_MODEL)), 0.6f);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(T car) {
		DyeColor color = car.getColor();

		return TEXTURES[color.ordinal()];
	}

	@Override
	protected boolean shouldShowName(@NotNull T minibike) {
		return false;
	}

}
