package nuparu.sevendaystomine.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModTiers {

    public static final Tier SCRAP = new ForgeTier(0,75,1,0f,4, ModBlocksTags.NEEDS_SCRAP_TOOL,() -> Ingredient.of(ModItems.IRON_SCRAP.get()));
    public static final Tier COPPER = new ForgeTier(1,100,3,0.5f,8, ModBlocksTags.NEEDS_COPPER_TOOL,() -> Ingredient.of(Items.COPPER_INGOT));
    public static final Tier BRONZE = new ForgeTier(2,150,6,1.5f,10, ModBlocksTags.NEEDS_BRONZE_TOOL,() -> Ingredient.of(ModItems.BRONZE_INGOT.get()));
    public static final Tier STEEL = new ForgeTier(3,500,7,2.5f,12, ModBlocksTags.NEEDS_STEEL_TOOL,() -> Ingredient.of(ModItems.STEEL_INGOT.get()));
}
