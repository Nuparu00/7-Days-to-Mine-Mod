package nuparu.sevendaystomine.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.inventory.block.*;
import nuparu.sevendaystomine.world.inventory.entity.ContainerLootableCorpse;

public class ModContainers {
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
			SevenDaysToMine.MODID);

	public static final RegistryObject<MenuType<ContainerLootableCorpse>> LOOTABLE_COPRSE = CONTAINERS.register("lootable_corpse",
			() -> IForgeMenuType.create(ContainerLootableCorpse::createContainerClientSide));


	public static final RegistryObject<MenuType<ContainerForge>> FORGE = CONTAINERS.register("forge",
			() -> IForgeMenuType.create(ContainerForge::createContainerClientSide));

	public static final RegistryObject<MenuType<ContainerGrill>> COOKING_GRILL = CONTAINERS.register("cooking_grill",
			() -> IForgeMenuType.create(ContainerGrill::createContainerClientSide));

	public static final RegistryObject<MenuType<ContainerChemistry>> CHEMISTRY_STATION = CONTAINERS.register("chemistry_station",
			() -> IForgeMenuType.create(ContainerChemistry::createContainerClientSide));
	public static final RegistryObject<MenuType<ContainerWorkbench>> WORKBENCH = CONTAINERS.register("workbench",
			() -> IForgeMenuType.create(ContainerWorkbench::createContainerClientSide));
	public static final RegistryObject<MenuType<ContainerWorkbenchUncrafting>> WORKBENCH_UNCRAFTING = CONTAINERS.register("workbench_uncrafting",
			() -> IForgeMenuType.create(ContainerWorkbenchUncrafting::createContainerClientSide));

	public static final RegistryObject<MenuType<ContainerSmall>> SMALL = CONTAINERS.register("small",
			() -> IForgeMenuType.create(ContainerSmall::createContainerClientSide));
}
