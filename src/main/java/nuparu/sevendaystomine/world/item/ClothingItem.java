package nuparu.sevendaystomine.world.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.event.ClientSetup;
import nuparu.sevendaystomine.init.ModArmorMaterials;
import nuparu.sevendaystomine.init.ModItems;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Function;

@SuppressWarnings("unused")
public class ClothingItem extends DyeableArmorItem implements CreativeModeTabProvider{
    public ResourceLocation texture;
    public ResourceLocation overlay;

    private boolean isDyeable = true;
    private boolean hasOverlay = false;
    private int defaultColor = 0xffffff;

    public ClothingItem(ArmorMaterial material, ArmorItem.Type slot, Properties properties, String textureName) {
        super(material, slot, properties);
        this.texture = new ResourceLocation(SevenDaysToMine.MODID,"textures/models/clothing/" + textureName +".png");
        this.overlay = new ResourceLocation(SevenDaysToMine.MODID,"textures/models/clothing/" + textureName +"_overlay.png");
    }

    public ClothingItem(ArmorItem.Type slot, Properties properties, String textureName) {
        this(ModArmorMaterials.CLOTHING, slot, properties,textureName);
    }
    public static Function<net.minecraft.client.model.geom.EntityModelSet, net.minecraft.client.model.HumanoidModel> modelFactory() {
        return modelSet -> new net.minecraft.client.model.HumanoidModel(modelSet.bakeLayer(ClientSetup.CLOTHING_OUTER));
    }

    public ClothingItem setIsDyeable(boolean isDyeable){
        this.isDyeable = isDyeable;
        return this;
    }

    public ClothingItem setHasOverlay(boolean hasOverlay){
        this.hasOverlay = hasOverlay;
        return this;
    }

    public ClothingItem setDefaultColor(int defaultColor){
        this.defaultColor = defaultColor;
        return this;
    }

    public boolean isDyeable(){
        return isDyeable;
    }

    public boolean hasOverlay(){
        return hasOverlay;
    }

    @Override
    public int getColor(ItemStack p_200886_1_) {
        CompoundTag compoundnbt = p_200886_1_.getTagElement("display");
        return compoundnbt != null && compoundnbt.contains("color", Tag.TAG_INT) ? compoundnbt.getInt("color") : defaultColor;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return SevenDaysToMine.MODID + ":textures/misc/empty.png";
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack armor, ItemStack stack) {
        return stack.getItem() == ModItems.CLOTH.get();
    }
    /*@Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            net.minecraft.client.model.HumanoidModel model;
            @Override
            public @NotNull net.minecraft.client.model.Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, net.minecraft.client.model.HumanoidModel<?> original) {
                if(model == null && net.minecraft.client.Minecraft.getInstance().getEntityModels() != null){
                    model = modelFactory().apply(net.minecraft.client.Minecraft.getInstance().getEntityModels());
                }
                return model;
            }
        });
    }*/

}
