package nuparu.ni.methods;

import java.util.Random;

import nuparu.ni.CodeBlock;
import nuparu.ni.Token;
import nuparu.ni.Value;
import nuparu.ni.Value.EnumValueType;
import nuparu.ni.exception.EvaluationErrorException;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Tree;

public class MethodRandom extends Method {

	public MethodRandom() {
		super("random");
		addArgument("max", EnumValueType.INT);
	}

	@Override
	public Value trigger(Tree<Token> method, CodeBlock codeBlock) throws EvaluationErrorException {
		Value max = this.getArgumentValue(0, method, codeBlock);
		if (!max.isNumerical()) {
			codeBlock.printError(name + "() expected a number, but did not find any");
			return null;
		}

		return new Value(MathUtils.getIntInRange(new Random(), 0, (int) max.asInt().getRealValue()));
	}

}
