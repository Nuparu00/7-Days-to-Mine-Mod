package nuparu.sevendaystomine.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.item.CreativeModeTabProvider;

import java.util.function.Predicate;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            SevenDaysToMine.MODID);
    public static RegistryObject<CreativeModeTab> BUILDING_BLOCKS = CREATIVE_MODE_TABS.register("building_blocks",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.OAK_FRAME.get()))
                    .title(Component.translatable("itemGroup.sevendaystomine.buildingBlocks")).displayItems((displayParameters, output) ->
                            addItems(ModCreativeModeTabs.BUILDING_BLOCKS.getId(), output))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .build());
    public static RegistryObject<CreativeModeTab> FUNCTIONAl_BLOCKS = CREATIVE_MODE_TABS.register("functional_blocks",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.FORGE.get()))
                    .title(Component.translatable("itemGroup.sevendaystomine.functionalBlocks")).displayItems((displayParameters, output) ->
                            addItems(ModCreativeModeTabs.FUNCTIONAl_BLOCKS.getId(), output))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS).withTabsBefore(new ResourceLocation(SevenDaysToMine.MODID,"building_blocks"))
                    .build());

    public static RegistryObject<CreativeModeTab> FOOD = CREATIVE_MODE_TABS.register("food",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.CANNED_HAM.get()))
                    .title(Component.translatable("itemGroup.sevendaystomine.food")).displayItems((displayParameters, output) ->
                            addItems(ModCreativeModeTabs.FOOD.getId(), output))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS).withTabsBefore(new ResourceLocation(SevenDaysToMine.MODID,"combat"))
                    .build());
    public static RegistryObject<CreativeModeTab> INGREDIENTS = CREATIVE_MODE_TABS.register("ingredients",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.IRON_SCRAP.get()))
                    .title(Component.translatable("itemGroup.sevendaystomine.ingredients")).displayItems((displayParameters, output) ->
                            addItems(ModCreativeModeTabs.INGREDIENTS.getId(), output))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS).withTabsBefore(new ResourceLocation(SevenDaysToMine.MODID,"medicine"))
                    .build());
    public static RegistryObject<CreativeModeTab> TOOLS = CREATIVE_MODE_TABS.register("tools",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.CHAINSAW.get()))
                    .title(Component.translatable("itemGroup.sevendaystomine.tools")).displayItems((displayParameters, output) ->
                            addItems(ModCreativeModeTabs.TOOLS.getId(), output, item -> item instanceof DiggerItem))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS).withTabsBefore(new ResourceLocation(SevenDaysToMine.MODID,"functional_blocks"))
                    .build());
    public static RegistryObject<CreativeModeTab> COMBAT = CREATIVE_MODE_TABS.register("combat",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.PISTOL.get()))
                    .title(Component.translatable("itemGroup.sevendaystomine.combat")).displayItems((displayParameters, output) ->
                            addItems(ModCreativeModeTabs.COMBAT.getId(), output, item -> item instanceof SwordItem || item instanceof Equipable))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS).withTabsBefore(new ResourceLocation(SevenDaysToMine.MODID,"tools"))
                    .build());
    public static RegistryObject<CreativeModeTab> MEDICINE = CREATIVE_MODE_TABS.register("medicine",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.FIRST_AID_KIT.get()))
                    .title(Component.translatable("itemGroup.sevendaystomine.medicine")).displayItems((displayParameters, output) ->
                            addItems(ModCreativeModeTabs.MEDICINE.getId(), output))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS).withTabsBefore(new ResourceLocation(SevenDaysToMine.MODID,"food"))
                    .build());


    private static void addItems(ResourceLocation tab, CreativeModeTab.Output output){
       addItems(tab, output, item -> false);
    }

    private static void addItems(ResourceLocation tab, CreativeModeTab.Output output, Predicate<Item> alternativePredicate){
        ModItems.ITEMS.getEntries().stream().map(RegistryObject::get)
                .filter(item -> (item instanceof CreativeModeTabProvider creativeModeTabProvider && creativeModeTabProvider.creativeModeTab() != null
                        && creativeModeTabProvider.creativeModeTab().equals(tab)) || alternativePredicate.test(item))
                .forEach(output::accept);
    }
}
