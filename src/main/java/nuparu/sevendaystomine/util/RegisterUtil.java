package nuparu.sevendaystomine.util;

public class RegisterUtil {
/*
	public static void registerAll(FMLPreInitializationEvent event) {
		registerItems(event, Items.IRON_SCRAP, Items.BRASS_SCRAP, Items.LEAD_SCRAP, Items.EMPTY_CAN, Items.STONE_AXE,
				Items.PLANK_WOOD, Items.SMALL_STONE);
		registerBlocks(event, Blocks.OAK_FRAME, Blocks.BIRCH_FRAME, Blocks.SPRUCE_FRAME, Blocks.JUNGLE_FRAME,
				Blocks.ACACIA_FRAME, Blocks.DARKOAK_FRAME, Blocks.OAK_PLANKS_REINFORCED, Blocks.BIRCH_PLANKS_REINFORCED,
				Blocks.SPRUCE_PLANKS_REINFORCED, Blocks.JUNGLE_PLANKS_REINFORCED, Blocks.ACACIA_PLANKS_REINFORCED,
				Blocks.DARKOAK_PLANKS_REINFORCED, Blocks.OAK_PLANKS_REINFORCED_IRON,
				Blocks.BIRCH_PLANKS_REINFORCED_IRON, Blocks.SPRUCE_PLANKS_REINFORCED_IRON,
				Blocks.JUNGLE_PLANKS_REINFORCED_IRON, Blocks.ACACIA_PLANKS_REINFORCED_IRON,
				Blocks.DARKOAK_PLANKS_REINFORCED_IRON, Blocks.SMALL_STONE, Blocks.STICK);
	}

	public static void registerBlocks(FMLPreInitializationEvent event, Block... blocks) {
		for (Block block : blocks) {
			
			final Item itemBlock = new ItemBlock(block).setRegistryName(block.getRegistryName());
			GameRegistry.register(block);
			GameRegistry.register(itemBlock);
			if (event.getSide() == Side.CLIENT) {
				ImmutableList<IBlockState> values = block.getBlockState().getValidStates();
				for (IBlockState state : values) {

					StateMapperBase statemapper = new DefaultStateMapper();
					ModelLoader.setCustomModelResourceLocation(itemBlock, block.getMetaFromState(state),
							new ModelResourceLocation(block.getRegistryName(),
									statemapper.getPropertyString(state.getProperties())));
				}
			}
		}
	}

	public static void registerItems(FMLPreInitializationEvent event, Item... items) {
		for (Item item : items) {

			GameRegistry.register(item);
			if (event.getSide() == Side.CLIENT) {
				ModelLoader.setCustomModelResourceLocation(item, 0,
						new ModelResourceLocation(item.getRegistryName(), "inventory"));
			}
		}
	}*/
}
