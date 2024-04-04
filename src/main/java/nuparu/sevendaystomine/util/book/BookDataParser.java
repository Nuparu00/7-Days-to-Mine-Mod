package nuparu.sevendaystomine.util.book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.book.BookData.CraftingMatrix;
import nuparu.sevendaystomine.util.book.BookData.Image;
import nuparu.sevendaystomine.util.book.BookData.Page;
import nuparu.sevendaystomine.util.book.BookData.Stack;
import nuparu.sevendaystomine.util.book.BookData.TextBlock;

@SideOnly(Side.CLIENT)
public class BookDataParser {

	public static BookDataParser INSTANCE = new BookDataParser();

	public BookData getBookDataFromResource(ResourceLocation source) {
		Minecraft mc = Minecraft.getMinecraft();

		InputStream in = getClass().getResourceAsStream(new StringBuilder().append("/assets/")
				.append(source.getResourceDomain()).append("/").append(source.getResourcePath()).toString());
		if (in == null) {
			Utils.getLogger().error("Could not create an InputStream while attempting to read " + source.toString());
			return null;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		Gson gson = new Gson();
		JsonElement je = gson.fromJson(reader, JsonElement.class);
		JsonObject json = je.getAsJsonObject();

		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Page> pages = new ArrayList<Page>();
		JsonArray pgs = json.get("pages").getAsJsonArray();
		for (JsonElement je2 : pgs) {
			JsonObject page = je2.getAsJsonObject();
			if (!page.has("res")) {
				return null;
			}
			ResourceLocation res = new ResourceLocation(page.get("res").getAsString());

			List<TextBlock> textBlocks = new ArrayList<TextBlock>();
			if (page.has("texts")) {
				JsonArray tbs = page.getAsJsonArray("texts").getAsJsonArray();
				for (JsonElement je3 : tbs) {
					JsonObject textBlock = je3.getAsJsonObject();
					if (!textBlock.has("x")) {
						continue;
					}
					if (!textBlock.has("y")) {
						continue;
					}
					if (!textBlock.has("z")) {
						continue;
					}
					if (!textBlock.has("text")) {
						continue;
					}

					int x = textBlock.get("x").getAsInt();
					int y = textBlock.get("y").getAsInt();
					int z = textBlock.get("z").getAsInt();
					boolean unlocalized = false;
					if (textBlock.has("unlocalized")) {
						unlocalized = textBlock.get("unlocalized").getAsBoolean();
					}

					String text = textBlock.get("text").getAsString();

					double scale = 1;
					if (textBlock.has("scale")) {
						scale = textBlock.get("scale").getAsDouble();
					}

					double width = 0;
					if (textBlock.has("width")) {
						width = textBlock.get("width").getAsDouble();
					} else {
						width = (unlocalized ? mc.fontRenderer.getStringWidth(SevenDaysToMine.proxy.localize(text))
								: mc.fontRenderer.getStringWidth(text)) * scale;
					}

					double height = mc.fontRenderer.FONT_HEIGHT * scale;

					TextBlock tb = new TextBlock(x, y, z, width, height, text);
					tb.unlocalized = unlocalized;
					tb.scale = scale;

					if (textBlock.has("centered")) {
						tb.centered = textBlock.get("centered").getAsBoolean();
					}
					if (textBlock.has("color")) {
						tb.color = Integer.parseInt(textBlock.get("color").getAsString(), 16);
					}
					if (textBlock.has("hover_color")) {
						tb.hoverColor = Integer.parseInt(textBlock.get("hover_color").getAsString(), 16);
					}
					if (textBlock.has("shadow")) {
						tb.shadow = textBlock.get("shadow").getAsBoolean();
					}
					if (textBlock.has("link")) {
						tb.link = textBlock.get("link").getAsInt();
					}
					if (textBlock.has("formatting")) {
						JsonArray ja = textBlock.getAsJsonArray("formatting").getAsJsonArray();
						TextFormatting[] formattings = new TextFormatting[ja.size()];
						for (int i = 0; i < ja.size(); i++) {
							String formatting = ja.get(i).getAsString();
							formattings[i] = TextFormatting.getValueByName(formatting);
						}
						tb.formatting = formattings;
					}

					textBlocks.add(tb);
				}
			}

			List<Image> images = new ArrayList<Image>();
			if (page.has("images")) {
				JsonArray imgs = page.getAsJsonArray("images").getAsJsonArray();
				for (JsonElement je3 : imgs) {
					JsonObject img = je3.getAsJsonObject();
					if (!img.has("x")) {
						continue;
					}
					if (!img.has("y")) {
						continue;
					}
					if (!img.has("z")) {
						continue;
					}
					if (!img.has("width")) {
						continue;
					}
					if (!img.has("height")) {
						continue;
					}
					if (!img.has("res")) {
						continue;
					}

					int x = img.get("x").getAsInt();
					int y = img.get("y").getAsInt();
					int z = img.get("z").getAsInt();

					double width = img.get("width").getAsDouble();
					double height = img.get("height").getAsDouble();

					ResourceLocation res2 = new ResourceLocation(img.get("res").getAsString());
					images.add(new Image(x, y, z, width, height, res2));
				}
			}

			List<CraftingMatrix> crafting = new ArrayList<CraftingMatrix>();
			if (page.has("crafting")) {
				JsonArray crafts = page.getAsJsonArray("crafting").getAsJsonArray();
				for (JsonElement je3 : crafts) {
					JsonObject craft = je3.getAsJsonObject();
					if (!craft.has("x")) {
						continue;
					}
					if (!craft.has("y")) {
						continue;
					}
					if (!craft.has("z")) {
						continue;
					}
					if (!craft.has("recipe")) {
						continue;
					}
					int x = craft.get("x").getAsInt();
					int y = craft.get("y").getAsInt();
					int z = craft.get("z").getAsInt();

					crafting.add(new CraftingMatrix(x, y, z, new ResourceLocation(craft.get("recipe").getAsString())));
				}
			}
			
			List<Stack> stacks = new ArrayList<Stack>();
			if (page.has("stacks")) {
				JsonArray stackz = page.getAsJsonArray("stacks").getAsJsonArray();
				for (JsonElement je3 : stackz) {
					JsonObject stack = je3.getAsJsonObject();
					if (!stack.has("x")) {
						continue;
					}
					if (!stack.has("y")) {
						continue;
					}
					if (!stack.has("z")) {
						continue;
					}
					if (!stack.has("item")) {
						continue;
					}
					
					int x = stack.get("x").getAsInt();
					int y = stack.get("y").getAsInt();
					int z = stack.get("z").getAsInt();
					
					Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(stack.get("item").getAsString()));
					if(item == null) {
						continue;
					}
					int count = 1;
					int data = 0;
					NBTTagCompound nbt = null;
					if(stack.has("count")) {
						count = stack.get("count").getAsInt();
					}
					if(stack.has("data")) {
						count = stack.get("data").getAsInt();
					}
					ItemStack itemStack = new ItemStack(item,count,data);
					if(stack.has("nbt")) {
						try {
							nbt = JsonToNBT.getTagFromJson(stack.get("nbt").getAsString());
						} catch (NBTException e) {
							e.printStackTrace();
						}
						finally {
							itemStack.setTagCompound(nbt);
						}
					}
					stacks.add(new Stack(x,y,z,itemStack));
				}
			}
			pages.add(new Page(res, textBlocks, images, crafting, stacks));

		}

		return new BookData(pages);
	}
}
