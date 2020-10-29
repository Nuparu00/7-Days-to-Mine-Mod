package com.nuparu.ni.methods;

import java.util.Random;

import com.nuparu.ni.CodeBlock;
import com.nuparu.ni.Token;
import com.nuparu.ni.Value;
import com.nuparu.ni.Value.EnumValueType;
import com.nuparu.ni.exception.EvaluationErrorException;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Tree;

public class MethodSave extends Method {

	public MethodSave() {
		super("save");
		addArgument("obj", EnumValueType.OBJECT);
	}

	@Override
	public Value trigger(Tree<Token> method, CodeBlock codeBlock) throws EvaluationErrorException {
		codeBlock.print("Attempting to save...");
		Value obj = this.getArgumentValue(0, method, codeBlock);
		codeBlock.process.saveToDevice(obj.getRealValue().toString());
		codeBlock.printSuccess("Successfully saved " + obj.getRealValue().toString());
		return null;
	}

}
