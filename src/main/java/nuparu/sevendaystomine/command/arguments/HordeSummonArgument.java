package nuparu.sevendaystomine.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;

public class HordeSummonArgument implements ArgumentType<ResourceLocation> {
   private static final Collection<String> EXAMPLES = Arrays.asList("sevendaystomine:bloodmoon_horde", "wolf_horde");
   public static final DynamicCommandExceptionType ERROR_UNKNOWN_HORDE = new DynamicCommandExceptionType((p_93342_) -> Component.translatable("horde.notFound", p_93342_));

   public static HordeSummonArgument id() {
      return new HordeSummonArgument();
   }

   public static ResourceLocation getSummonableHorde(CommandContext<CommandSourceStack> p_93339_, String p_93340_) {
      return verifyCanSummon(p_93339_.getArgument(p_93340_, ResourceLocation.class));
   }

   private static ResourceLocation verifyCanSummon(ResourceLocation p_93344_) {
      return p_93344_;
   }

   public ResourceLocation parse(StringReader p_93337_) throws CommandSyntaxException {
      return verifyCanSummon(ResourceLocation.read(p_93337_));
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}