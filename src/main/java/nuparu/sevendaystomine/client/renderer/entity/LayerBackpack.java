package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelBackpack;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.PlayerInventorySyncHelper;
import nuparu.sevendaystomine.util.item.ItemCache;

@SideOnly(Side.CLIENT)
public class LayerBackpack implements LayerRenderer<EntityPlayer> {
	private final RenderPlayer playerRenderer;

	private ModelBackpack modelBackpack;

	public LayerBackpack(RenderPlayer defaultModel) {
		this.playerRenderer = defaultModel;
	}

	@Override
	public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		/*
		 * ExtendedInventory inv =
		 * (ExtendedInventory)CapabilityHelper.getExtendedInventory(player); if(inv ==
		 * null) return;
		 */
		if (ModConfig.players.renderPlayerInventory == true) {
			if (PlayerInventorySyncHelper.itemsCache != null
					&& PlayerInventorySyncHelper.itemsCache.containsKey(player.getName())) {
				ItemCache cache = PlayerInventorySyncHelper.itemsCache.get(player.getName());
				if (cache == null || cache.isEmpty())
					return;

				ItemStack stack = cache.backpack;
				if (stack.getItem() == ModItems.BACKPACK) {

					modelBackpack = new ModelBackpack(this.playerRenderer.getMainModel());
					modelBackpack.setModelAttributes(this.playerRenderer.getMainModel());
					modelBackpack.setLivingAnimations(player, limbSwing, limbSwingAmount, partialTicks);

					this.playerRenderer
							.bindTexture(new ResourceLocation(SevenDaysToMine.MODID, "textures/entity/backpack.png"));
					modelBackpack.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
				}
			}
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
