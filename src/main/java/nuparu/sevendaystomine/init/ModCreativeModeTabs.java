package nuparu.sevendaystomine.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModCreativeModeTabs {
    public static CreativeModeTab TAB_MATERIALS = new CreativeModeTab(SevenDaysToMine.MODID + ".materials") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.IRON_SCRAP.get());
        }
    };

    public static CreativeModeTab TAB_BLOCKS = new CreativeModeTab(SevenDaysToMine.MODID + ".blocks") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.OAK_FRAME.get());
        }
    };

    public static CreativeModeTab TAB_FOOD = new CreativeModeTab(SevenDaysToMine.MODID + ".food") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MRE.get());
        }
    };

    public static CreativeModeTab TAB_COMBAT = new CreativeModeTab(SevenDaysToMine.MODID + ".combat") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SPIKED_CLUB.get());
        }
    };

    public static CreativeModeTab TAB_TOOLS = new CreativeModeTab(SevenDaysToMine.MODID + ".tools") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SCRAP_PICKAXE.get());
        }
    };

    public static CreativeModeTab TAB_MEDICINE = new CreativeModeTab(SevenDaysToMine.MODID + ".medicine") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.FIRST_AID_KIT.get());
        }
    };

    public static CreativeModeTab TAB_FORGING = new CreativeModeTab(SevenDaysToMine.MODID + ".forging") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.FORGE.get());
        }
    };
    public static CreativeModeTab TAB_BOOKS = new CreativeModeTab(SevenDaysToMine.MODID + ".books") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SURVIVAL_GUIDE.get());
        }
    };
}
