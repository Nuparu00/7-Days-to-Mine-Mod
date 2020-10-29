package com.nuparu.ni.methods;

import java.util.Random;

import com.nuparu.ni.CodeBlock;
import com.nuparu.ni.Token;
import com.nuparu.ni.Value;
import com.nuparu.ni.Value.EnumValueType;
import com.nuparu.ni.exception.EvaluationErrorException;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Tree;

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
