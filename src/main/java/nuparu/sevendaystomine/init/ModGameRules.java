package nuparu.sevendaystomine.init;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ModGameRules {

    static Method m_create_boolean = null;
    static Method m_create_int = null;

    public static GameRules.Key<GameRules.BooleanValue> handleThirst;
    public static GameRules.Key<GameRules.IntegerValue> damageDecayRate;
    public static GameRules.Key<GameRules.IntegerValue> hordeGlow;

    public static void register() {
        handleThirst = GameRules.register("handleThirst", GameRules.Category.PLAYER, createBoolean(true));
        damageDecayRate = GameRules.register("damageDecayRate", GameRules.Category.MISC, createInt(-1));
        hordeGlow = GameRules.register("hordeGlow", GameRules.Category.MOBS, createInt(0));
    }


    public static GameRules.Type<GameRules.BooleanValue> createBoolean(boolean defaultValue){
        if(m_create_boolean == null) {
            m_create_boolean = ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "m_46250_", boolean.class);
        }
        try {
            return (GameRules.Type<GameRules.BooleanValue>) m_create_boolean.invoke(null, defaultValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GameRules.Type<GameRules.IntegerValue> createInt(int defaultValue){
        if(m_create_int == null) {
            m_create_int = ObfuscationReflectionHelper.findMethod(GameRules.IntegerValue.class, "m_46312_", int.class);
        }
        try {
            return (GameRules.Type<GameRules.IntegerValue>) m_create_int.invoke(null, defaultValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
