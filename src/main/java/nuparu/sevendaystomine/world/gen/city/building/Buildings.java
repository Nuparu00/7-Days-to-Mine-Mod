package nuparu.sevendaystomine.world.gen.city.building;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class Buildings {

    private static HashMap<ResourceLocation, Supplier<Building>> buildings = new HashMap<>();

    public static void registerBuilding(ResourceLocation resourceLocation, Supplier<Building> buildingSupplier){
        buildings.putIfAbsent(resourceLocation, buildingSupplier);
    }

    public static void registerBuilding(ResourceLocation resourceLocation, Building building){
        registerBuilding(resourceLocation, () -> building);
    }

    public static Optional<Supplier<Building>> getBuildingSupplier(ResourceLocation resourceLocation){
        return Optional.ofNullable(buildings.getOrDefault(resourceLocation, null));
    }

    public static Optional<Building> getBuilding(ResourceLocation resourceLocation){
        return getBuildingSupplier(resourceLocation).map(Supplier::get);
    }

    public static Set<ResourceLocation> getBuildings(){
        return buildings.keySet();
    }
}
