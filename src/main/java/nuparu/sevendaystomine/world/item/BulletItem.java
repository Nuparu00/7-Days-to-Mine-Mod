package nuparu.sevendaystomine.world.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;

public class BulletItem extends ItemBase {
    public BulletItem(Properties properties) {
        super(properties);
    }

    @Override
    public ResourceLocation creativeModeTab(){
        return ModCreativeModeTabs.COMBAT.getId();
    }
}
