package nuparu.sevendaystomine.world.item.crafting;

public interface ILockedRecipe {
    String getRecipe();

    default boolean hasRecipe() {
        return getRecipe() != null && !getRecipe().isEmpty();
    }
}
