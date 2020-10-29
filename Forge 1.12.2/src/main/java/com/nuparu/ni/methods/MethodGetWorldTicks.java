package com.nuparu.ni.methods;

import java.util.Random;

import com.nuparu.ni.CodeBlock;
import com.nuparu.ni.Token;
import com.nuparu.ni.Value;
import com.nuparu.ni.Value.EnumValueType;
import com.nuparu.ni.exception.EvaluationErrorException;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Tree;

public class MethodGetWorldTicks extends Method {

	public MethodGetWorldTicks() {
		super("getWorldTicks");
	}

	@Override
	public Value trigger(Tree<Token> method, CodeBlock codeBlock) throws EvaluationErrorException {
		return new Value(codeBlock.process.getTE().getWorld().getWorldTime());
	}

}
