package nuparu.sevendaystomine.crafting.forge;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeMaterial;
import nuparu.sevendaystomine.crafting.forge.IForgeRecipe;
import nuparu.sevendaystomine.init.ModFluids;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.ItemMold;
import nuparu.sevendaystomine.tileentity.TileEntityForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class ForgeRecipeManager {
	private static ForgeRecipeManager instance;

	private ArrayList<IForgeRecipe> recipes = new ArrayList<IForgeRecipe>();

	private ForgeRecipeManager() {
		instance = this;
		addRecipes();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<IForgeRecipe> getRecipes() {
		return recipes;
	}

	public void addRecipes() {
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"iron_ingot"),
				Items.IRON_INGOT,
				ModItems.MOLD_INGOT,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.IRON,10)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"gold_ingot"),
				Items.GOLD_INGOT,
				ModItems.MOLD_INGOT,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.GOLD,10)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"copper_ingot"),
				ModItems.INGOT_COPPER,
				ModItems.MOLD_INGOT,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.COPPER,10)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"zinc_ingot"),
				ModItems.INGOT_ZINC,
				ModItems.MOLD_INGOT,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.ZINC,10)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"tin_ingot"),
				ModItems.INGOT_TIN,
				ModItems.MOLD_INGOT,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.TIN,10)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"lead_ingot"),
				ModItems.INGOT_LEAD,
				ModItems.MOLD_INGOT,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.LEAD,10)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"brass_ingot"),
				ModItems.INGOT_BRASS,
				ModItems.MOLD_INGOT,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.BRASS,10)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"bronze_ingot"),
				ModItems.INGOT_BRONZE,
				ModItems.MOLD_INGOT,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.BRONZE,10)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"steel_ingot"),
				ModItems.INGOT_STEEL,
				ModItems.MOLD_INGOT,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.STEEL,10)
		);


		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"brass_ingot--alloy"),
				ModItems.INGOT_BRASS,
				ModItems.MOLD_INGOT,
				0.2f,
				600,
				new MaterialStack(EnumMaterial.COPPER,6),
				new MaterialStack(EnumMaterial.ZINC,4)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"bronze_ingot--alloy"),
				ModItems.INGOT_BRONZE,
				ModItems.MOLD_INGOT,
				0.2f,
				600,
				new MaterialStack(EnumMaterial.COPPER,6),
				new MaterialStack(EnumMaterial.TIN,4)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"steel_ingot--alloy"),
				ModItems.INGOT_STEEL,
				ModItems.MOLD_INGOT,
				0.2f,
				600,
				new MaterialStack(EnumMaterial.IRON,8),
				new MaterialStack(EnumMaterial.CARBON,2)
		);


		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"glass_block"),
				Blocks.GLASS,
				ModItems.MOLD_INGOT,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.GLASS,81)
		);


		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"glass_block--sand"),
				Blocks.GLASS,
				ModItems.MOLD_INGOT,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.SAND,12)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"empty_jar"),
				ModItems.EMPTY_JAR,
				ModItems.EMPTY_JAR_MOLD,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.GLASS,3)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"empty_jar--sand"),
				ModItems.EMPTY_JAR,
				ModItems.EMPTY_JAR_MOLD,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.SAND,3)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"empty_can"),
				ModItems.EMPTY_CAN,
				ModItems.EMPTY_CAN_MOLD,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.IRON,3)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"bullet_tip"),
				ModItems.BULLET_TIP,
				ModItems.BULLET_TIP_MOLD,
				0.1f,
				200,
				new MaterialStack(EnumMaterial.LEAD,1)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"bullet_casing"),
				ModItems.BULLET_CASING,
				ModItems.BULLET_CASING_MOLD,
				0.1f,
				200,
				new MaterialStack(EnumMaterial.BRASS,1)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"pistol_slide"),
				ModItems.PISTOL_SLIDE,
				ModItems.PISTOL_BARREL_MOLD,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.STEEL,4)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"pistol_trigger"),
				ModItems.PISTOL_TRIGGER,
				ModItems.PISTOL_TRIGGER_MOLD,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.STEEL,6)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"cement"),
				ModItems.CEMENT,
				ModItems.CEMENT_MOLD,
				0.15f,
				600,
				new MaterialStack(EnumMaterial.STONE,9)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"hunting_rifle_barrel"),
				ModItems.HUNTING_RIFLE_BARREL,
				ModItems.HUNTING_RIFLE_BARREL_MOLD,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.STEEL,6)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"hunting_rifle_bolt"),
				ModItems.HUNTING_RIFLE_BOLT,
				ModItems.HUNTING_RIFLE_BOLT_MOLD,
				0.15f,
				600,
				new MaterialStack(EnumMaterial.STEEL,8)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"mp5_stock"),
				ModItems.MP5_STOCK,
				ModItems.MP5_STOCK_MOLD,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.STEEL,4)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"mp5_barrel"),
				ModItems.MP5_BARREL,
				ModItems.MP5_BARREL_MOLD,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.STEEL,6)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"mp5_trigger"),
				ModItems.MP5_TRIGGER,
				ModItems.MP5_TRIGGER_MOLD,
				0.15f,
				800,
				new MaterialStack(EnumMaterial.STEEL,10)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"sniper_rifle_stock"),
				ModItems.SNIPER_RIFLE_STOCK,
				ModItems.SNIPER_RIFLE_STOCK_MOLD,
				0.15f,
				600,
				new MaterialStack(EnumMaterial.STEEL,8)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"sniper_rifle_trigger"),
				ModItems.SNIPER_RIFLE_TRIGGER,
				ModItems.SNIPER_RIFLE_TRIGGER_MOLD,
				0.2f,
				800,
				new MaterialStack(EnumMaterial.STEEL,14)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"shotgun_barrel"),
				ModItems.SHOTGUN_BARREL,
				ModItems.SHOTGUN_BARREL_MOLD,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.STEEL,5)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"shotgun_receiver"),
				ModItems.SHOTGUN_RECEIVER,
				ModItems.SHOTGUN_RECEIVER_MOLD,
				0.15f,
				600,
				new MaterialStack(EnumMaterial.STEEL,8)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"shotgun_barrel_short"),
				ModItems.SHOTGUN_BARREL_SHORT,
				ModItems.SHOTGUN_SHORT_BARREL_MOLD,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.STEEL,3)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"glass_pane"),
				Blocks.GLASS_PANE,
				ModItems.GLASS_PANE_MOLD,
				0.1f,
				500,
				new MaterialStack(EnumMaterial.GLASS,4)
		);
		addMaterialRecipe(
				new ResourceLocation(SevenDaysToMine.MODID,"glass_pane--sand"),
				Blocks.GLASS_PANE,
				ModItems.GLASS_PANE_MOLD,
				0.1f,
				600,
				new MaterialStack(EnumMaterial.SAND,8)
		);
	}

	public void addRecipe(IForgeRecipe recipe) {
		recipes.add(recipe);
	}

	public void removeRecipe(ResourceLocation resourceLocation) {
		recipes.removeIf(recipe -> recipe.name().equals(resourceLocation));
	}

	public void addMaterialRecipe(ResourceLocation resourceLocation, ItemStack result, ItemStack mold, float experience, int cookingTime, MaterialStack... stacks){
		addRecipe(new ForgeRecipeMaterial(resourceLocation,
				result,
				mold,
				NonNullList.from(MaterialStack.EMPTY, stacks),
				experience,
				cookingTime
		));
	}

	public void addMaterialRecipe(ResourceLocation resourceLocation,Item result, Item mold, float experience, int cookingTime, MaterialStack... stacks){
		addMaterialRecipe(resourceLocation, new ItemStack(result), new ItemStack(mold), experience, cookingTime, stacks);
	}

	public void addMaterialRecipe(ResourceLocation resourceLocation,Block result, Item mold, float experience, int cookingTime, MaterialStack... stacks){
		addMaterialRecipe(resourceLocation, new ItemStack(result), new ItemStack(mold), experience, cookingTime, stacks);
	}

	public static ForgeRecipeManager getInstance(){
		if (instance == null){
			return new ForgeRecipeManager();
		}
		return instance;
	}

	public static Optional<IForgeRecipe> getRecipeFor(TileEntityForge forge, World world){
		return getInstance().recipes.stream().filter(recipe -> recipe.matches(forge, world)).findAny();
	}

	public boolean isMold(Item item){
		return item instanceof ItemMold || recipes.stream().anyMatch(recipe -> recipe.getMoldItem().getItem() == item);
	}
}
