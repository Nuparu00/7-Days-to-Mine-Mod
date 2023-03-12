package nuparu.sevendaystomine.world.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import nuparu.sevendaystomine.world.inventory.ILootTableProvider;

public class RealityWandItem extends Item {
    public RealityWandItem() {
        super(new Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        Level worldIn = context.getLevel();
        if(worldIn.isClientSide() || player == null) return InteractionResult.PASS;
        BlockPos pos = context.getClickedPos();
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        ItemStack stack = context.getItemInHand();
        MutableComponent textComponent;
            if (tileEntity != null) {
                if(stack.hasCustomHoverName()) {
                    CompoundTag nbt = tileEntity.saveWithoutMetadata();
                    //if (nbt.contains("LootTable", Constants.NBT.TAG_STRING)) {
                    String lootTable = "sevendaystomine:containers/" + stack.getHoverName().getString();
                    if(tileEntity instanceof ILootTableProvider){
                        ILootTableProvider lootTableProvider = (ILootTableProvider)tileEntity;
                        lootTableProvider.setLootTable(new ResourceLocation(lootTable),worldIn.random.nextLong());

                        textComponent = Component.translatable("debug.loottable.set", pos.toShortString(), lootTable);
                        textComponent.setStyle(textComponent.getStyle().withColor(ChatFormatting.GREEN));
                    }
                    else{
                        nbt.putString("LootTable",lootTable);
                        tileEntity.load(nbt);
                        nbt = tileEntity.saveWithoutMetadata();
                        if (!nbt.contains("LootTable", Tag.TAG_STRING)) {
                            textComponent = Component.translatable("debug.loottable.support", pos.toShortString());
                            textComponent.setStyle(textComponent.getStyle().withColor(ChatFormatting.YELLOW));
                        }
                        else {
                            textComponent = Component.translatable("debug.loottable.set", pos.toShortString(), lootTable);
                            textComponent.setStyle(textComponent.getStyle().withColor(ChatFormatting.GREEN));
                        }
                    }
                }
                else {
                    CompoundTag nbt = tileEntity.saveWithoutMetadata();
                    if (nbt.contains("LootTable", Tag.TAG_STRING)) {
                        String lootTable = nbt.getString("LootTable");
                        textComponent = Component.translatable("debug.loottable", pos.toShortString(), lootTable);
                        textComponent.setStyle(textComponent.getStyle().withColor(ChatFormatting.GREEN));
                    } else {
                        textComponent = Component.translatable("debug.noloottable", pos.toShortString());
                        textComponent.setStyle(textComponent.getStyle().withColor(ChatFormatting.YELLOW));
                    }
                }
            } else {
                textComponent = Component.translatable("debug.notileentity", pos.toShortString());
                textComponent.setStyle(textComponent.getStyle().withColor(ChatFormatting.RED));
        }
        player.sendSystemMessage(textComponent);

        return InteractionResult.PASS;
    }


}
