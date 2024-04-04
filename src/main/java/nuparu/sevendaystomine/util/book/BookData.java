package nuparu.sevendaystomine.util.book;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.RenderUtils;

public class BookData {

	public List<Page> pages;

	public BookData(List<Page> pages) {
		this.pages = pages;
	}

	public static class Page {
		public ResourceLocation res;
		public List<TextBlock> textBlocks;
		public List<Image> images;
		public List<CraftingMatrix> crafting;
		public List<Stack> stacks;

		public Page(ResourceLocation res, List<TextBlock> texts, List<Image> images, List<CraftingMatrix> crafting, List<Stack> stacks) {
			this.res = res;
			this.textBlocks = texts;
			this.images = images;
			this.crafting = crafting;
			this.stacks = stacks;
		}

	}

	public static class TextBlock {
		public double x;
		public double y;
		public double z;

		public double width;
		public double height;
		public String text;

		public boolean unlocalized;
		public boolean centered;
		public boolean shadow;
		public int color;
		public int hoverColor = -1;
		public double scale = 1;
		public int link = -1;

		public TextFormatting[] formatting;

		public TextBlock(int x, int y, int z, double width, double height, String text) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.width = width;
			this.height = height;
			this.text = text;

		}
	}

	public static class Image {
		public double x;
		public double y;
		public double z;

		public double width;
		public double height;

		public ResourceLocation res;

		public Image(int x, int y, int z, double width, double height, ResourceLocation res) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.width = width;
			this.height = height;
			this.res = res;

		}
	}

	public static class CraftingMatrix {
		public int x;
		public int y;
		public int z;

		public ResourceLocation res;
		public static final ResourceLocation BACKGROUND_3X3 = new ResourceLocation(SevenDaysToMine.MODID,
				"textures/gui/books/crafting_background.png");
		public static final ResourceLocation BACKGROUND_5X5 = new ResourceLocation(SevenDaysToMine.MODID,
				"textures/gui/books/workbench_background.png");

		boolean workbench;

		public CraftingMatrix(int x, int y, int z, ResourceLocation res) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.res = res;
			IRecipe rec = CraftingManager.REGISTRY.getObject(res);
			if (rec != null) {
				setRecipe(rec);

				Iterator<Ingredient> iterator = rec.getIngredients().iterator();
				int i = 3;
				int j = rec instanceof net.minecraftforge.common.crafting.IShapedRecipe
						? Math.max(3, ((net.minecraftforge.common.crafting.IShapedRecipe) rec).getRecipeHeight())
						: i;
				int k = rec instanceof net.minecraftforge.common.crafting.IShapedRecipe
						? ((net.minecraftforge.common.crafting.IShapedRecipe) rec).getRecipeWidth()
						: i;
				int l = 1;
				workbench = j > 3 || k > 3;

				addIngredient(Ingredient.fromStacks(rec.getRecipeOutput()), workbench ? 175 : 85,
						(int) (1 + 18 * (Math.ceil((workbench ? 5 : 3) / 2f) - 1)));

				for (int i1 = 0; i1 < j; ++i1) {
					for (int j1 = 0; j1 < k; ++j1) {
						if (!iterator.hasNext()) {
							return;
						}

						Ingredient ingredient = iterator.next();

						if (ingredient.getMatchingStacks().length > 0) {
							addIngredient(ingredient, 1 + j1 * 18, 1 + i1 * 18);
						}

						++l;
					}

					if (k < i) {
						l += i - k;
					}
				}
			}
		}

		private IRecipe recipe;
		private final List<GhostIngredient> ingredients = Lists.<GhostIngredient>newArrayList();
		private float time;

		public void clear() {
			this.recipe = null;
			this.ingredients.clear();
			this.time = 0.0F;
		}

		public void addIngredient(Ingredient p_194187_1_, int p_194187_2_, int p_194187_3_) {
			this.ingredients.add(new GhostIngredient(p_194187_1_, p_194187_2_, p_194187_3_));
		}

		public GhostIngredient get(int p_192681_1_) {
			return this.ingredients.get(p_192681_1_);
		}

		public int size() {
			return this.ingredients.size();
		}

		@Nullable
		public IRecipe getRecipe() {
			return this.recipe;
		}

		public void setRecipe(IRecipe p_192685_1_) {
			this.recipe = p_192685_1_;
		}

		public void render(Minecraft p_194188_1_, int p_194188_2_, int p_194188_3_, boolean p_194188_4_,
				float p_194188_5_) {
			GlStateManager.pushMatrix();
			GlStateManager.color(1, 1, 1);

			GlStateManager.disableLighting();
			RenderUtils.drawTexturedRect(workbench ? BACKGROUND_5X5 : BACKGROUND_3X3, p_194188_2_, p_194188_3_, 0, 0,
					workbench ? 196 : 106, workbench ? 90 : 54, workbench ? 196 : 106, workbench ? 90 : 54, 1, 0);
			GlStateManager.enableLighting();

			if (!GuiScreen.isCtrlKeyDown()) {
				this.time += p_194188_5_;
			}
			RenderHelper.enableGUIStandardItemLighting();

			for (int i = 0; i < this.ingredients.size(); ++i) {
				GhostIngredient ghostrecipe$ghostingredient = this.ingredients.get(i);
				int j = ghostrecipe$ghostingredient.getX() + p_194188_2_;
				int k = ghostrecipe$ghostingredient.getY() + p_194188_3_;

				GlStateManager.disableLighting();
				ItemStack itemstack = ghostrecipe$ghostingredient.getItem();
				RenderItem renderitem = p_194188_1_.getRenderItem();
				renderitem.renderItemAndEffectIntoGUI(p_194188_1_.player, itemstack, j, k);

				if (i == 0) {
					renderitem.renderItemOverlays(p_194188_1_.fontRenderer, itemstack, j, k);
				}

				GlStateManager.enableLighting();
			}

			RenderHelper.disableStandardItemLighting();
			GlStateManager.popMatrix();
		}

		@SideOnly(Side.CLIENT)
		public class GhostIngredient {
			private final Ingredient ingredient;
			private final int x;
			private final int y;

			public GhostIngredient(Ingredient p_i47604_2_, int p_i47604_3_, int p_i47604_4_) {
				this.ingredient = p_i47604_2_;
				this.x = p_i47604_3_;
				this.y = p_i47604_4_;
			}

			public int getX() {
				return this.x;
			}

			public int getY() {
				return this.y;
			}

			public ItemStack getItem() {
				ItemStack[] aitemstack = this.ingredient.getMatchingStacks();
				return aitemstack[MathHelper.floor(CraftingMatrix.this.time / 30.0F) % aitemstack.length];
			}
		}

	}

	public static class Stack {
		public int x;
		public int y;
		public int z;

		public ItemStack stack = ItemStack.EMPTY;

		public Stack(int x, int y, int z, ItemStack stack) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.stack = stack;

		}

		public void render(Minecraft mc, int x, int y, float partialTicks) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, z);
			GlStateManager.color(1, 1, 1);
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.disableLighting();
			RenderItem renderitem = mc.getRenderItem();
			renderitem.renderItemAndEffectIntoGUI(mc.player, stack, x, y);
			renderitem.renderItemOverlays(mc.fontRenderer, stack,x, y);
			GlStateManager.enableLighting();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.translate(0, 0, -z);
			GlStateManager.popMatrix();
		}
	}

}
