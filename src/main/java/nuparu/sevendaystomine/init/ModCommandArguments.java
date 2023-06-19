package nuparu.sevendaystomine.init;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.command.arguments.HordeSummonArgument;

public class ModCommandArguments {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, SevenDaysToMine.MODID);

    public static final RegistryObject<ArgumentTypeInfo<?, ?>> HORDE = register("horde", HordeSummonArgument.class, SingletonArgumentInfo.contextFree(HordeSummonArgument::id));


    private static <A extends ArgumentType<?>>  RegistryObject<ArgumentTypeInfo<?, ?>> register(String name, Class<A> clazz, ArgumentTypeInfo<A, ?> argumentTypeInfo){
        return ARGUMENT_TYPES.register(name, () -> ArgumentTypeInfos.registerByClass(clazz,argumentTypeInfo));
    }
}
