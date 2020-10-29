package com.nuparu.ni.methods;

import java.util.Random;

import com.nuparu.ni.CodeBlock;
import com.nuparu.ni.Token;
import com.nuparu.ni.Value;
import com.nuparu.ni.Value.EnumValueType;
import com.nuparu.ni.exception.EvaluationErrorException;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Tree;

public class MethodPrint extends Method {

	public MethodPrint() {
		super("print");
		addArgument("obj", EnumValueType.OBJECT);
	}

	@Override
	public Value trigger(Tree<Token> method, CodeBlock codeBlock) throws EvaluationErrorException {
		Value obj = this.getArgumentValue(0, method, codeBlock);
		codeBlock.print(obj.getRealValue().toString());
		return null;
	}

}
