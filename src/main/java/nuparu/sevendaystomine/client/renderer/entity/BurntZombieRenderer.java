package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.client.renderer.entity.layers.BurnsLayer;
import nuparu.sevendaystomine.world.entity.zombie.BurntZombieEntity;
import org.jetbrains.annotations.NotNull;

public class BurntZombieRenderer<T extends BurntZombieEntity, M extends HumanoidModel<T>>
		extends ZombieBipedRenderer<T, M> {
	private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/entity/zombie/burnt/burnt_zombie.png");
	public BurnsLayer burnsLayer;
	public BurntZombieRenderer(EntityRendererProvider.Context context) {
		super(context, ClientSetup.BURNT_ZOMBIE_LAYER);
		this.addLayer(burnsLayer = new BurnsLayer<T, M, M>(this));
	}

	public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
		return ZOMBIE_LOCATION;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	protected boolean isShaking(T entity) {
		return entity.isSoulFireConverting();
	}
}
