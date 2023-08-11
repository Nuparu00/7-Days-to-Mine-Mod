package nuparu.sevendaystomine.world.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;

import java.util.Optional;

public class GunItem extends ItemBase /*implements GeoAnimatable, ISyncable*/ {

    private final Wield wield;
    private final int baseCapacity;

    public GunItem(Properties properties, Wield wield, int baseCapacity) {
        super(properties.stacksTo(1));
        this.wield = wield;
        this.baseCapacity = baseCapacity;
        //GeckoLibNetwork.registerSyncable(this);
    }

    public void initNBT(ItemStack itemstack) {
        CompoundTag nbt = itemstack.getOrCreateTag();
        nbt.putInt("Ammo", 0);
        nbt.putInt("Cooldown", 0);
        nbt.putInt("CooldownDuration", 0);
    }

    @Override
    public void onCraftedBy(ItemStack itemstack, Level world, Player player) {
        super.onCraftedBy(itemstack, world, player);
        initNBT(itemstack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if(stack.getItem() instanceof GunItem gunItem){
            CompoundTag tag = stack.getOrCreateTag();
            if(tag.contains("Cooldown", Tag.TAG_INT)){
                int cooldown = tag.getInt("Cooldown");
                if(cooldown-- > 0){
                    tag.putInt("Cooldown", cooldown);
                }
            }
        }
    }


    public boolean canHaveQuality() {
        return true;
    }

    public static Optional<Wield> getWield(ItemStack stack){
        if(stack.getItem() instanceof GunItem gunItem){
            return Optional.ofNullable(gunItem.wield);
        }
        return Optional.empty();
    }

    public static int getAmmo(ItemStack stack){
        if(stack.getItem() instanceof GunItem gunItem){
            CompoundTag tag = stack.getOrCreateTag();
            if(tag.contains("Ammo", Tag.TAG_INT)){
                return tag.getInt("Ammo");
            }
        }
        return -1;
    }

    public static int getCapacity(ItemStack stack){
        if(stack.getItem() instanceof GunItem gunItem){
            return gunItem.baseCapacity;
        }
        return 0;
    }

    public float getCooldownPercent(ItemStack stack){
        if(stack.getItem() instanceof GunItem gunItem){
            CompoundTag tag = stack.getOrCreateTag();
            if(tag.contains("Cooldown", Tag.TAG_INT) && tag.contains("CooldownDuration", Tag.TAG_INT)){
                int cooldown = tag.getInt("Cooldown");
                int duration = tag.getInt("CooldownDuration");
                return duration == 0 ? 0 : (float) cooldown / duration;
            }
        }
        return 0;
    }

    public int getCooldown(ItemStack stack){
        if(stack.getItem() instanceof GunItem gunItem){
            CompoundTag tag = stack.getOrCreateTag();
            if(tag.contains("Cooldown", Tag.TAG_INT)){
                return tag.getInt("Cooldown");
            }
        }
        return 0;
    }

    public void setCooldown(ItemStack stack, int cooldown){
        if(stack.getItem() instanceof GunItem gunItem) {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt("Cooldown", cooldown);
            tag.putInt("CooldownDuration", cooldown);
        }
    }

    @Override
    public ResourceLocation creativeModeTab(){
        return ModCreativeModeTabs.COMBAT.getId();
    }

    public enum Wield{
        ONE_HAND,
        TWO_HAND;
    }
/*
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if(getCooldown(itemstack) != 0) return InteractionResultHolder.pass(itemstack);
        player.awardStat(Stats.ITEM_USED.get(this));
        setCooldown(itemstack, 10);
        if (!level.isClientSide) {
            final int id = GeckoLibUtil.guaranteeIDForStack(itemstack, (ServerLevel) level);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF
                    .with(() -> player);
            GeckoLibNetwork.syncAnimation(target, this, id, AnimationState.SHOT.ordinal());
        }
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return !oldStack.is(newStack.getItem()) || slotChanged;
    }

    public enum AnimationState{
        IDLE("idle"),
        SHOT("shot"),
        RELOAD("reload");

        public final String path;

        AnimationState(String path){
            this.path = path;
        }

        public static final Optional<AnimationState> get(int id){
            if(id < values().length){
                return Optional.ofNullable(values()[id]);
            }
            return Optional.empty();
        }
    }

    private final AnimationFactory animationFactory = GeckoLibUtil.createFactory(this);

    public String controllerName = "controller";

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            final BlockEntityWithoutLevelRenderer renderer = new PistolRenderer();
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this,controllerName, 1, event -> PlayState.CONTINUE));
    }

    @Override
    public void onAnimationSync(int id, int state) {
        AnimationState.get(state).ifPresent(
                animationState -> {
                    final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.animationFactory, id, controllerName);
                        controller.clearAnimationCache();
                        controller.markNeedsReload();
                        controller.setAnimation(new AnimationBuilder().addAnimation(animationState.path, ILoopType.EDefaultLoopTypes.PLAY_ONCE));
                }
        );
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }
*/
}
