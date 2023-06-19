package nuparu.sevendaystomine.world.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class BookItem extends Item {
    public final boolean unlocksRecipes;
    public BookItem(Properties properties, boolean unlocksRecipes) {
        super(properties.stacksTo(1).tab(ModCreativeModeTabs.TAB_BOOKS));
        this.unlocksRecipes = unlocksRecipes;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip,
                                @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable(getBookName() + ".subtitle"));
        if(!Screen.hasShiftDown() || Minecraft.getInstance().player == null) return;
        boolean known = hasRecipe(Minecraft.getInstance().player);
        boolean read = isRead(stack);

        MutableComponent knownText = Component.translatable(known ? "tooltip.sevendaystomine.known" : "tooltip.sevendaystomine.unknown");
        MutableComponent readText = Component.translatable(read ? "tooltip.sevendaystomine.used" : "tooltip.sevendaystomine.unused");

        Style knownStyle = knownText.getStyle().withColor(known ? ChatFormatting.GREEN : ChatFormatting.RED);
        knownText.setStyle(knownStyle);

        Style readStyle = knownText.getStyle().withColor(read ? ChatFormatting.GREEN : ChatFormatting.RED);
        readText.setStyle(readStyle);

        tooltip.add(knownText);
        tooltip.add(readText);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (playerIn.isCrouching())
            return InteractionResultHolder.pass(stack);

        if(!unlocksRecipes || isRead(stack) || hasRecipe(playerIn))
            return InteractionResultHolder.success(stack);

        if(worldIn.isClientSide()){
            SevenDaysToMine.proxy.addNotificationToast(stack,Component.translatable("recipe_unlocked.toast"),Component.translatable(getBookName() + ".subtitle"));
        }
        IExtendedPlayer iExtendedPlayer = CapabilityHelper.getExtendedPlayer(playerIn);
        iExtendedPlayer.unlockRecipe(getBookRegistryName().toString());
        setRead(stack,true);

        return InteractionResultHolder.fail(stack);
    }

    public static boolean isRead(ItemStack stack) {
        if(!stack.hasTag()) return false;
        CompoundTag nbt = stack.getTag();
        if (nbt.contains("read")) {
            return nbt.getBoolean("read");
        }
        return false;
    }

    public void setRead(ItemStack stack, boolean read) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putBoolean("read", read);
    }

    public boolean hasRecipe(Player player){
        IExtendedPlayer iExtendedPlayer = CapabilityHelper.getExtendedPlayer(player);
        return iExtendedPlayer == null || iExtendedPlayer.hasRecipe(getBookRegistryName().toString());
    }

    public ResourceLocation getBookRegistryName(){
        return ForgeRegistries.ITEMS.getKey(this);
    }

    public String getBookName(){
        return getBookRegistryName().getPath();
    }
}
