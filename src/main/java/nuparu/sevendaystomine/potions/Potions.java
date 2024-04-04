package nuparu.sevendaystomine.potions;

import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;

public class Potions {

public static PotionThirst thirst = new PotionThirst(true, 1930808);
public static PotionDysentery dysentery = new PotionDysentery(true, 10092544);
public static PotionAlcoholBuzz alcoholBuzz = new PotionAlcoholBuzz(false, 20092544);
public static PotionDrunk drunk = new PotionDrunk(true, 22092544);
public static PotionAlcoholPoison alcoholPoison = new PotionAlcoholPoison(true, 1092544);
public static PotionCaffeineBuzz caffeineBuzz = new PotionCaffeineBuzz(false, 5092544);
public static PotionBleeding bleeding = new PotionBleeding(true, 5092544);
public static PotionInfection infection = new PotionInfection(true, 1930808);
public static PotionBrokenLeg brokenLeg = new PotionBrokenLeg(true, 0xaaaaaa);
public static PotionMercuryPoisoning mercuryPoison = new PotionMercuryPoisoning(true, 1930808);
public static PotionChlorinePoisoning chlorinePoison = new PotionChlorinePoisoning(true, 1930808);
public static PotionSplintedLeg splintedLeg = new PotionSplintedLeg(true, 0xaaaaaa);

public static void register(IForgeRegistry<Potion> registry) {
    registry.registerAll(
            thirst,dysentery,alcoholBuzz,drunk,alcoholPoison,caffeineBuzz,bleeding,infection,brokenLeg, mercuryPoison, chlorinePoison, splintedLeg
    );
}

}