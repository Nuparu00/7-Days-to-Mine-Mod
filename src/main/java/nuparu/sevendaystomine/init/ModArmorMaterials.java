package nuparu.sevendaystomine.init;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ModArmorMaterials implements StringRepresentable, ArmorMaterial {
    /*LEATHER("leather", 5, Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266652_) -> {
        p_266652_.put(ArmorItem.Type.BOOTS, 1);
        p_266652_.put(ArmorItem.Type.LEGGINGS, 2);
        p_266652_.put(ArmorItem.Type.CHESTPLATE, 3);
        p_266652_.put(ArmorItem.Type.HELMET, 1);
    }), 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
        return Ingredient.of(Items.LEATHER);
    }),
    CHAIN("chainmail", 15, Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266651_) -> {
        p_266651_.put(ArmorItem.Type.BOOTS, 1);
        p_266651_.put(ArmorItem.Type.LEGGINGS, 4);
        p_266651_.put(ArmorItem.Type.CHESTPLATE, 5);
        p_266651_.put(ArmorItem.Type.HELMET, 2);
    }), 12, SoundEvents.ARMOR_EQUIP_CHAIN, 0.0F, 0.0F, () -> {
        return Ingredient.of(Items.IRON_INGOT);
    }),
    IRON("iron", 15, Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266654_) -> {
        p_266654_.put(ArmorItem.Type.BOOTS, 2);
        p_266654_.put(ArmorItem.Type.LEGGINGS, 5);
        p_266654_.put(ArmorItem.Type.CHESTPLATE, 6);
        p_266654_.put(ArmorItem.Type.HELMET, 2);
    }), 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
        return Ingredient.of(Items.IRON_INGOT);
    }),
    GOLD("gold", 7, Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266650_) -> {
        p_266650_.put(ArmorItem.Type.BOOTS, 1);
        p_266650_.put(ArmorItem.Type.LEGGINGS, 3);
        p_266650_.put(ArmorItem.Type.CHESTPLATE, 5);
        p_266650_.put(ArmorItem.Type.HELMET, 2);
    }), 25, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, () -> {
        return Ingredient.of(Items.GOLD_INGOT);
    }),
    DIAMOND("diamond", 33, Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266649_) -> {
        p_266649_.put(ArmorItem.Type.BOOTS, 3);
        p_266649_.put(ArmorItem.Type.LEGGINGS, 6);
        p_266649_.put(ArmorItem.Type.CHESTPLATE, 8);
        p_266649_.put(ArmorItem.Type.HELMET, 3);
    }), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () -> {
        return Ingredient.of(Items.DIAMOND);
    }),
    TURTLE("turtle", 25, Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266656_) -> {
        p_266656_.put(ArmorItem.Type.BOOTS, 2);
        p_266656_.put(ArmorItem.Type.LEGGINGS, 5);
        p_266656_.put(ArmorItem.Type.CHESTPLATE, 6);
        p_266656_.put(ArmorItem.Type.HELMET, 2);
    }), 9, SoundEvents.ARMOR_EQUIP_TURTLE, 0.0F, 0.0F, () -> {
        return Ingredient.of(Items.SCUTE);
    }),
    NETHERITE("netherite", 37, Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266655_) -> {
        p_266655_.put(ArmorItem.Type.BOOTS, 3);
        p_266655_.put(ArmorItem.Type.LEGGINGS, 6);
        p_266655_.put(ArmorItem.Type.CHESTPLATE, 8);
        p_266655_.put(ArmorItem.Type.HELMET, 3);
    }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    });*/

    CLOTHING("sevendaystomine:clothing", 6,Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266652_) -> {
        p_266652_.put(ArmorItem.Type.BOOTS, 1);
        p_266652_.put(ArmorItem.Type.LEGGINGS, 2);
        p_266652_.put(ArmorItem.Type.CHESTPLATE, 2);
        p_266652_.put(ArmorItem.Type.HELMET, 1);
    }), 0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(Items.IRON_INGOT)),
    FIBER("sevendaystomine:fiber", 2, Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266652_) -> {
        p_266652_.put(ArmorItem.Type.BOOTS, 1);
        p_266652_.put(ArmorItem.Type.LEGGINGS, 1);
        p_266652_.put(ArmorItem.Type.CHESTPLATE, 1);
        p_266652_.put(ArmorItem.Type.HELMET, 1);
    }), 0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(ModItems.PLANT_FIBER.get())),
    STEEL("sevendaystomine:steel", 25,Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266652_) -> {
        p_266652_.put(ArmorItem.Type.BOOTS, 3);
        p_266652_.put(ArmorItem.Type.LEGGINGS, 3);
        p_266652_.put(ArmorItem.Type.CHESTPLATE, 4);
        p_266652_.put(ArmorItem.Type.HELMET, 3);
    }), 0, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> Ingredient.of(ModItems.STEEL_INGOT.get())),
    LEATHER_IRON("sevendaystomine:leather_iron", 12, Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266652_) -> {
        p_266652_.put(ArmorItem.Type.BOOTS, 1);
        p_266652_.put(ArmorItem.Type.LEGGINGS, 2);
        p_266652_.put(ArmorItem.Type.CHESTPLATE, 3);
        p_266652_.put(ArmorItem.Type.HELMET, 2);
    }), 0, SoundEvents.ARMOR_EQUIP_LEATHER, 2.0F, 0.0F, () -> Ingredient.of(Items.LEATHER)),
    SCRAP("sevendaystomine:scrap", 10, Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266652_) -> {
        p_266652_.put(ArmorItem.Type.BOOTS, 1);
        p_266652_.put(ArmorItem.Type.LEGGINGS, 2);
        p_266652_.put(ArmorItem.Type.CHESTPLATE, 2);
        p_266652_.put(ArmorItem.Type.HELMET, 2);
    }), 0, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> Ingredient.of(ModItems.IRON_SCRAP.get())),
    MILITARY("sevendaystomine:military", 30, Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266652_) -> {
        p_266652_.put(ArmorItem.Type.BOOTS, 4);
        p_266652_.put(ArmorItem.Type.LEGGINGS, 4);
        p_266652_.put(ArmorItem.Type.CHESTPLATE, 6);
        p_266652_.put(ArmorItem.Type.HELMET, 5);
    }), 2, SoundEvents.ARMOR_EQUIP_CHAIN, 3.0F, 0.1F, Ingredient::of);


    public static final StringRepresentable.EnumCodec<net.minecraft.world.item.ArmorMaterials> CODEC = StringRepresentable.fromEnum(net.minecraft.world.item.ArmorMaterials::values);
    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266653_) -> {
        p_266653_.put(ArmorItem.Type.BOOTS, 13);
        p_266653_.put(ArmorItem.Type.LEGGINGS, 15);
        p_266653_.put(ArmorItem.Type.CHESTPLATE, 16);
        p_266653_.put(ArmorItem.Type.HELMET, 11);
    });
    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ModArmorMaterials(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionFunctionForType, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionFunctionForType = protectionFunctionForType;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = toughness;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    public int getDurabilityForType(ArmorItem.Type p_266745_) {
        return HEALTH_FUNCTION_FOR_TYPE.get(p_266745_) * this.durabilityMultiplier;
    }

    public int getDefenseForType(ArmorItem.Type p_266752_) {
        return this.protectionFunctionForType.get(p_266752_);
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public String getSerializedName() {
        return this.name;
    }
}