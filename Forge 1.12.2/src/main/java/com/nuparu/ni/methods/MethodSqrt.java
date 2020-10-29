package com.nuparu.ni.methods;

import java.util.Random;

import com.nuparu.ni.CodeBlock;
import com.nuparu.ni.Token;
import com.nuparu.ni.Value;
import com.nuparu.ni.Value.EnumValueType;
import com.nuparu.ni.exception.EvaluationErrorException;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Tree;

public class MethodSqrt extends Method {

	public MethodSqrt() {
		super("sqrt");
		addArgument("base", EnumValueType.NUMERICAL);
	}

	@Override
	public Value trigger(Tree<Token> method, CodeBlock codeBlock) throws EvaluationErrorException {

		Value base = this.getArgumentValue(0, method, codeBlock);
		if (!base.isNumerical()) {
			codeBlock.printError(name + "() expected a number, but did not find any");
			return null;
		}

		return new Value((double)Math.sqrt((double)base.asDouble().getRealValue()));
	}

}
