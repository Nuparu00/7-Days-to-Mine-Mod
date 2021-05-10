package nuparu.sevendaystomine.crafting.forge;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import nuparu.sevendaystomine.init.ModFluids;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.EnumMaterial;

public class ForgeRecipeManager {
	private static ForgeRecipeManager INSTANCE;

	private ArrayList<IForgeRecipe> recipes = new ArrayList<IForgeRecipe>();

	public ForgeRecipeManager() {
		INSTANCE = this;
		addRecipes();
	}

	public static ForgeRecipeManager getInstance() {
		return INSTANCE;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<IForgeRecipe> getRecipes() {
		return (ArrayList<IForgeRecipe>) this.recipes.clone();
	}

	public void addRecipes() {
		
		HashMap<EnumMaterial, Integer> ingotiron = new HashMap<EnumMaterial, Integer>();
		ingotiron.put(EnumMaterial.IRON, 6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(Items.IRON_INGOT), new ItemStack(ModItems.MOLD_INGOT),
				ingotiron));
		HashMap<EnumMaterial, Integer> ingotbrass1 = new HashMap<EnumMaterial, Integer>();
		ingotbrass1.put(EnumMaterial.BRASS, 6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_BRASS), new ItemStack(ModItems.MOLD_INGOT),
				ingotbrass1));
		HashMap<EnumMaterial, Integer> ingotlead = new HashMap<EnumMaterial, Integer>();
		ingotlead.put(EnumMaterial.LEAD, 6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_LEAD), new ItemStack(ModItems.MOLD_INGOT),
				ingotlead));
		HashMap<EnumMaterial, Integer> ingotcopper = new HashMap<EnumMaterial, Integer>();
		ingotcopper.put(EnumMaterial.COPPER, 6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_COPPER), new ItemStack(ModItems.MOLD_INGOT),
				ingotcopper));
		HashMap<EnumMaterial, Integer> ingottin = new HashMap<EnumMaterial, Integer>();
		ingottin.put(EnumMaterial.TIN, 6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_TIN), new ItemStack(ModItems.MOLD_INGOT),
				ingottin));
		HashMap<EnumMaterial, Integer> ingotzinc = new HashMap<EnumMaterial, Integer>();
		ingotzinc.put(EnumMaterial.ZINC, 6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_ZINC), new ItemStack(ModItems.MOLD_INGOT),
				ingotzinc));
		HashMap<EnumMaterial, Integer> ingotgold = new HashMap<EnumMaterial, Integer>();
		ingotgold.put(EnumMaterial.GOLD, 6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(Items.GOLD_INGOT), new ItemStack(ModItems.MOLD_INGOT),
				ingotgold));
		HashMap<EnumMaterial, Integer> ingotsteel = new HashMap<EnumMaterial, Integer>();
		ingotsteel.put(EnumMaterial.STEEL, 6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_STEEL), new ItemStack(ModItems.MOLD_INGOT),
				ingotsteel));
		HashMap<EnumMaterial, Integer> ingotbronze1 = new HashMap<EnumMaterial, Integer>();
		ingotbronze1.put(EnumMaterial.BRONZE, 6);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_BRONZE), new ItemStack(ModItems.MOLD_INGOT),
				ingotbronze1));
		HashMap<EnumMaterial, Integer> ingotbronze2 = new HashMap<EnumMaterial, Integer>();
		ingotbronze2.put(EnumMaterial.COPPER, 4);
		ingotbronze2.put(EnumMaterial.TIN, 2);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_BRONZE), new ItemStack(ModItems.MOLD_INGOT),
				ingotbronze2));
		HashMap<EnumMaterial, Integer> ingotbrass2 = new HashMap<EnumMaterial, Integer>();
		ingotbrass2.put(EnumMaterial.COPPER, 4);
		ingotbrass2.put(EnumMaterial.ZINC, 2);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_BRASS), new ItemStack(ModItems.MOLD_INGOT),
				ingotbrass2));
		HashMap<EnumMaterial, Integer> ingotsteel2 = new HashMap<EnumMaterial, Integer>();
		ingotsteel2.put(EnumMaterial.IRON, 4);
		ingotsteel2.put(EnumMaterial.CARBON, 2);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.INGOT_STEEL), new ItemStack(ModItems.MOLD_INGOT),
				ingotsteel2));
		HashMap<EnumMaterial, Integer> cement = new HashMap<EnumMaterial, Integer>();
		cement.put(EnumMaterial.STONE, 4);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.CEMENT), new ItemStack(ModItems.CEMENT_MOLD), cement));
		HashMap<EnumMaterial, Integer> bulletCasing = new HashMap<EnumMaterial, Integer>();
		bulletCasing.put(EnumMaterial.BRASS, 1);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.BULLET_CASING),
				new ItemStack(ModItems.BULLET_CASING_MOLD), bulletCasing));
		HashMap<EnumMaterial, Integer> bulletTip = new HashMap<EnumMaterial, Integer>();
		bulletTip.put(EnumMaterial.LEAD, 1);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.BULLET_TIP), new ItemStack(ModItems.BULLET_TIP_MOLD),
				bulletTip));
		HashMap<EnumMaterial, Integer> glass = new HashMap<EnumMaterial, Integer>();
		cement.put(EnumMaterial.SAND, 1);
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.GLASS_SCRAP), new ItemStack(ModItems.MOLD_INGOT), glass));
		addRecipe(new ForgeRecipeMaterial(new ItemStack(ModItems.GLASS_SCRAP), new ItemStack(ModItems.MOLD_INGOT), glass));

		addMaterialRecipe(new ItemStack(ModItems.EMPTY_JAR), new ItemStack(ModItems.EMPTY_JAR_MOLD),
				new EnumMaterialWrapper(EnumMaterial.GLASS, 3));
		addMaterialRecipe(new ItemStack(ModItems.PISTOL_SLIDE), new ItemStack(ModItems.PISTOL_BARREL_MOLD),
				new EnumMaterialWrapper(EnumMaterial.IRON, 2));
		addMaterialRecipe(new ItemStack(ModItems.PISTOL_TRIGGER), new ItemStack(ModItems.PISTOL_TRIGGER_MOLD),
				new EnumMaterialWrapper(EnumMaterial.IRON, 4));
		addMaterialRecipe(new ItemStack(ModItems.SNIPER_RIFLE_TRIGGER),
				new ItemStack(ModItems.SNIPER_RIFLE_TRIGGER_MOLD), new EnumMaterialWrapper(EnumMaterial.IRON, 6));
		addMaterialRecipe(new ItemStack(ModItems.SNIPER_RIFLE_STOCK), new ItemStack(ModItems.SNIPER_RIFLE_STOCK_MOLD),
				new EnumMaterialWrapper(EnumMaterial.IRON, 6));
		addMaterialRecipe(new ItemStack(ModItems.SHOTGUN_RECEIVER), new ItemStack(ModItems.SHOTGUN_RECEIVER_MOLD),
				new EnumMaterialWrapper(EnumMaterial.IRON, 4));
		addMaterialRecipe(new ItemStack(ModItems.SHOTGUN_BARREL), new ItemStack(ModItems.SHOTGUN_BARREL_MOLD),
				new EnumMaterialWrapper(EnumMaterial.IRON, 3));
		addMaterialRecipe(new ItemStack(ModItems.SHOTGUN_BARREL_SHORT),
				new ItemStack(ModItems.SHOTGUN_SHORT_BARREL_MOLD), new EnumMaterialWrapper(EnumMaterial.IRON, 2));
		addMaterialRecipe(new ItemStack(ModItems.MP5_BARREL), new ItemStack(ModItems.MP5_BARREL_MOLD),
				new EnumMaterialWrapper(EnumMaterial.IRON, 4));
		addMaterialRecipe(new ItemStack(ModItems.MP5_TRIGGER), new ItemStack(ModItems.MP5_TRIGGER_MOLD),
				new EnumMaterialWrapper(EnumMaterial.IRON, 6));
		addMaterialRecipe(new ItemStack(ModItems.MP5_STOCK), new ItemStack(ModItems.MP5_STOCK_MOLD),
				new EnumMaterialWrapper(EnumMaterial.IRON, 4));
		addMaterialRecipe(new ItemStack(ModItems.HUNTING_RIFLE_BARREL),
				new ItemStack(ModItems.HUNTING_RIFLE_BARREL_MOLD), new EnumMaterialWrapper(EnumMaterial.IRON, 3));
		addMaterialRecipe(new ItemStack(ModItems.HUNTING_RIFLE_BOLT), new ItemStack(ModItems.HUNTING_RIFLE_BOLT_MOLD),
				new EnumMaterialWrapper(EnumMaterial.IRON, 4));
		
		addMaterialRecipe(FluidUtil.getFilledBucket(new FluidStack(ModFluids.MERCURY, 1000)), new ItemStack(Items.BUCKET),
				new EnumMaterialWrapper(EnumMaterial.MERCURY, 48));
		
		
		// addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.STICK),new
		// ItemStack(Items.ARROW),new ArrayList(Arrays.asList(new
		// ItemStack(Items.COAL),new ItemStack(Items.PAPER)))));
		// addRecipe(new CampfireRecipeShaped(new ItemStack(Items.CAKE),new
		// ItemStack(Items.ARROW),new ItemStack[][]{{new ItemStack(Items.BONE),new
		// ItemStack(Items.ARROW)},{new ItemStack(Items.APPLE),new
		// ItemStack(Items.COOKIE)}}));
	}

	public void addRecipe(IForgeRecipe recipe) {
		recipes.add(recipe);
	}

	public void addMaterialRecipe(ItemStack result, ItemStack mold, EnumMaterialWrapper... mats) {
		HashMap<EnumMaterial, Integer> ingredients = new HashMap<EnumMaterial, Integer>();
		for (EnumMaterialWrapper mat : mats) {
			ingredients.put(mat.mat, mat.weight);
		}
		addRecipe(new ForgeRecipeMaterial(result, mold, ingredients));
	}

	public class EnumMaterialWrapper {
		EnumMaterial mat;
		int weight;

		public EnumMaterialWrapper(EnumMaterial mat, int weight) {
			this.mat = mat;
			this.weight = weight;
		}
	}
}
