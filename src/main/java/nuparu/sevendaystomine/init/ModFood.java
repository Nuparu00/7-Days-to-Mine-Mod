package nuparu.sevendaystomine.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFood {
    public static final FoodProperties ANTIBIOTICS = new FoodProperties.Builder().alwaysEat().fast().nutrition(0).saturationMod(0).build();
    public static final FoodProperties CORN = new FoodProperties.Builder().fast().nutrition(1).saturationMod(1f).build();
    public static final FoodProperties COFFEE_BERRY = new FoodProperties.Builder().fast().nutrition(1).saturationMod(0.3f).build();
    public static final FoodProperties BANEBERRY = new FoodProperties.Builder().fast().nutrition(1).saturationMod(0.3f).effect(() -> new MobEffectInstance(MobEffects.WEAKNESS,3000) ,0.75f).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,3000) ,0.65f).effect(() -> new MobEffectInstance(MobEffects.CONFUSION,1200) ,0.45f).effect(() -> new MobEffectInstance(MobEffects.BLINDNESS,1200) ,0.45f).build();
    public static final FoodProperties BLUEBERRY = new FoodProperties.Builder().fast().nutrition(1).saturationMod(0.3f).build();
    public static final FoodProperties CANNED_ANIMAL_FOOD = new FoodProperties.Builder().nutrition(3).saturationMod(1f).meat().build();
    public static final FoodProperties CANNED_HAM = new FoodProperties.Builder().nutrition(6).saturationMod(2.5f).meat().build();
    public static final FoodProperties CANNED_CHICKEN = new FoodProperties.Builder().nutrition(5).saturationMod(2f).meat().build();
    public static final FoodProperties CANNED_CHILI = new FoodProperties.Builder().nutrition(4).saturationMod(2f).build();
    public static final FoodProperties CANNED_MISO = new FoodProperties.Builder().nutrition(4).saturationMod(1.5f).alwaysEat().build();
    public static final FoodProperties CANNED_PASTA = new FoodProperties.Builder().nutrition(5).saturationMod(2f).build();
    public static final FoodProperties CANNED_FRUIT_AND_VEGETABLES = new FoodProperties.Builder().nutrition(3).saturationMod(1.5f).build();
    public static final FoodProperties CANNED_SOUP = new FoodProperties.Builder().nutrition(5).saturationMod(1.75f).alwaysEat().build();
    public static final FoodProperties CANNED_STOCK = new FoodProperties.Builder().nutrition(4).saturationMod(1.6f).alwaysEat().build();
    public static final FoodProperties CANNED_HUGE_MEAT = new FoodProperties.Builder().nutrition(7).saturationMod(3).meat().build();
    public static final FoodProperties MRE = new FoodProperties.Builder().nutrition(5).saturationMod(5).build();
    public static final FoodProperties MOLDY_BREAD = new FoodProperties.Builder().fast().nutrition(2).saturationMod(0.3f).effect(() -> new MobEffectInstance(MobEffects.POISON,120) ,0.75f).build();
    public static final FoodProperties SODA = new FoodProperties.Builder().alwaysEat().nutrition(0).saturationMod(0f).build();

}
