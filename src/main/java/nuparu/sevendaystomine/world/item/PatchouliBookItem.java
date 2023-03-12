package nuparu.sevendaystomine.world.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliBookItem extends BookItem{
    public PatchouliBookItem(Properties properties, boolean unlocksRecipes) {
        super(properties,unlocksRecipes);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        InteractionResultHolder<ItemStack> result = super.use(worldIn, playerIn, handIn);
        if(result.getResult() == InteractionResult.SUCCESS){
            PatchouliAPI.get().openBookGUI(ForgeRegistries.ITEMS.getKey(this));
        }
        return result;
    }

}
