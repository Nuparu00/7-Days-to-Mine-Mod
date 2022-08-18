package nuparu.sevendaystomine.json.upgrader;

import net.minecraft.world.item.Item;

public record UpgraderToolEntry(Item item, double upgradePower, double salvagePower) {
}
