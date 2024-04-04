package nuparu.sevendaystomine.events;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.IForgeRegistry;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.IBlockBase;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.item.IItemVariant;
import nuparu.sevendaystomine.util.Utils;

@EventBusSubscriber(modid = SevenDaysToMine.MODID)
public class RegistrationHandler {

	public static List<ItemBlock> itemBlocks = new ArrayList<ItemBlock>();

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();
		registry.registerAll(SevenDaysToMine.BLOCKS);

	}

	@SubscribeEvent
	public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		for (Block block : SevenDaysToMine.BLOCKS) {
			ItemBlock itemBlock = null;
			if (block instanceof IBlockBase) {
				itemBlock = ((IBlockBase) block).createItemBlock();
			} else {
				itemBlock = new ItemBlock(block);
			}
			if (itemBlock == null) {
				Utils.getLogger().warn("A custom ItemBlock of " + block.getRegistryName()
						+ " is null. That might be a bad thing! Trying to skip!");
				continue;
			}
			itemBlock.setRegistryName(block.getRegistryName());
			itemBlocks.add(itemBlock);
			registry.register(itemBlock);
		}

	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		for (Item item : SevenDaysToMine.ITEMS) {
			registry.register(item);
		}

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelEvent(final ModelRegistryEvent event) {

		registerItemBlockModels();
		registerItemModels();

		for (Block block : SevenDaysToMine.BLOCKS) {
			registerBlockModel(block);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void registerBlockModel(Block block) {
		if (block instanceof IBlockBase) {
			IBlockBase iblockbase = (IBlockBase) block;
			if (iblockbase.hasCustomStateMapper()) {
				if (iblockbase.getStateMapper() != null) {
					ModelLoader.setCustomStateMapper(block, iblockbase.getStateMapper());
					return;
				}/* else {
					Utils.getLogger().warn("Trying to add a null IStateMapper to " + block.getRegistryName()
							+ " . That is a bad thing!");
				}*/
			}
			if (((IBlockBase) block).metaItemBlock()) {
				return;
			}
		}
		registerBlockModel(block, 0);
	}

	@SideOnly(Side.CLIENT)
	public static void registerBlockModel(Block block, int metaData) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), metaData, new ModelResourceLocation(
				SevenDaysToMine.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemBlockModels() {
		for (ItemBlock itemBlock : itemBlocks) {
			NonNullList<ItemStack> list = NonNullList.create();
			Block block = itemBlock.getBlock();
			block.getSubBlocks(CreativeTabs.SEARCH, list);
			if (block instanceof IBlockBase && ((IBlockBase) block).hasCustomItemMesh()) {
				ModelBakery.registerItemVariants(itemBlock);
				ModelLoader.setCustomMeshDefinition(itemBlock, ((IBlockBase) block).getItemMesh());
				continue;
			} else {
				if (block == ModBlocks.WRITING_TABLE) {
				}
				if (list.size() > 0 && block instanceof IBlockBase && ((IBlockBase) block).metaItemBlock()) {
					for (ItemStack stack : list) {
						registerItemBlockModel(itemBlock, "_itemblock", stack.getItemDamage(),
								"metadata=" + stack.getItemDamage());
					}
				} else {
					registerItemBlockModel(itemBlock);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemBlockModel(ItemBlock block) {
		registerItemBlockModel(block, 0);
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemBlockModel(ItemBlock block, int metaData) {
		registerItemBlockModel(block, metaData, "inventory");
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemBlockModel(ItemBlock block, int metaData, String variant) {
		ModelLoader.setCustomModelResourceLocation(block, metaData,
				new ModelResourceLocation(block.getRegistryName(), variant));
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemBlockModel(ItemBlock block, String suffix, int metaData, String variant) {

		ModelLoader.setCustomModelResourceLocation(block, metaData,
				new ModelResourceLocation(block.getRegistryName() + suffix, variant));
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemModels() {
		for (Item item : SevenDaysToMine.ITEMS) {
			if (item instanceof IItemVariant) {
				NonNullList<ItemStack> subitems = NonNullList.create();
				item.getSubItems(CreativeTabs.SEARCH, subitems);

				if (subitems.size() > 0) {
					for (ItemStack subitem : subitems) {
						registerItemModel(subitem.getItem(), "_subitem", subitem.getItemDamage(),
								"metadata=" + subitem.getItemDamage());
					}
				}
				continue;
			}
			registerItemModel(item);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemModel(Item item) {
		registerItemModel(item, 0);
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemModel(Item item, int metaData) {

		ModelLoader.setCustomModelResourceLocation(item, metaData,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemModel(Item item, String suffix, int metaData, String variant) {

		ModelLoader.setCustomModelResourceLocation(item, metaData,
				new ModelResourceLocation(item.getRegistryName() + suffix, variant));
	}

	public static void registerOreDictionaryEntries() {

	}
	
	@SubscribeEvent
	public static void registerDataSerializers(final RegistryEvent.Register<DataSerializerEntry> event) {
		final IForgeRegistry<DataSerializerEntry> registry = event.getRegistry();
		registry.register(new DataSerializerEntry(Utils.DIALOGUES).setRegistryName(SevenDaysToMine.MODID,"dialogues"));
	}
}