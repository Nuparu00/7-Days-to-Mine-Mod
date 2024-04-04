package nuparu.sevendaystomine.integration.crafttweaker.forge;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.BracketHandler;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.zenscript.IBracketHandler;
import nuparu.sevendaystomine.crafting.forge.MaterialStack;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.util.Utils;
import org.apache.commons.lang3.math.Fraction;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.util.List;
import java.util.Optional;

@BracketHandler
@ZenRegister
public class CraftTweakerMaterialStackBracketHandler implements IBracketHandler {

    private final IJavaMethod createMaterialStack;
    public CraftTweakerMaterialStackBracketHandler(){
        createMaterialStack = CraftTweakerAPI.getJavaMethod(CraftTweakerMaterialStackBracketHandler.class,"createMaterialStack", String.class, String.class);
    }

    public static CraftTweakerMaterialStack createMaterialStack(String materialName, String weightString){
        Optional<EnumMaterial> material = EnumMaterial.byName(materialName);
        if(!material.isPresent()){
            return null;
        }
        Optional<Number> weight = Utils.parseNumber(weightString);
        return weight.map(number -> new CraftTweakerMaterialStack(material.get(), number)).orElse(null);
    }

    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        if(tokens.size() < 3 || tokens.size() > 4) return null;
        if (!tokens.get(0).getValue().equalsIgnoreCase("MaterialStack")) return null;
        if (!tokens.get(1).getValue().equalsIgnoreCase(":")) return null;
        return position -> new ExpressionCallStatic(position, environment, createMaterialStack,
                new ExpressionString(position,tokens.get(2).getValue()),
                new ExpressionString(position,tokens.size() == 4 ? tokens.get(3).getValue() : "1"));
    }


}
