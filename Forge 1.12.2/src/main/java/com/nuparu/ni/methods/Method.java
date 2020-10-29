package com.nuparu.ni.methods;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nuparu.ni.CodeBlock;
import com.nuparu.ni.EnumTokenType;
import com.nuparu.ni.Interpreter;
import com.nuparu.ni.Token;
import com.nuparu.ni.Value;
import com.nuparu.ni.Value.EnumValueType;
import com.nuparu.ni.exception.EvaluationErrorException;
import com.nuparu.sevendaystomine.util.Tree;
import com.nuparu.sevendaystomine.util.Utils;

public abstract class Method {

	public String name;
	LinkedHashMap<String, EnumValueType> arguments = new LinkedHashMap<String, EnumValueType>();

	public Method(String name) {
		this.name = name;
	}

	public Value getArgumentValue(int n, Tree<Token> method, CodeBlock codeBlock) throws EvaluationErrorException{
		Tree<Token> bracket = method.getChildren().get(0);
		// Opening bracket
		if (bracket.isLeaf()) {
			codeBlock.printError("Expected \"(\", but did not find any");
			return null;
		}

		if (bracket.getData() == null || bracket.getData().type != EnumTokenType.PARENTHESES
				|| !bracket.getData().value.equals("(")) {
			codeBlock.printError("Expected \"(\", but did not find any");
			return null;
		}
		Tree<Token> argument = bracket.getChildren().get(0);
		while (n > 0 && !argument.isLeaf()) {
			if (argument.getData().type == EnumTokenType.COMMA) {
				n--;
			}
			argument = argument.getChildren().get(0);
		}
		if (n != 0) {
			
			return null;
		}

			return Interpreter.readStatement(argument, codeBlock, 0).evaluate();

	}

	public abstract Value trigger(Tree<Token> method, CodeBlock codeBlock) throws EvaluationErrorException;

	public boolean isValid(Tree<Token> method, CodeBlock codeBlock) throws EvaluationErrorException {

		Tree<Token> bracket = method.getChildren().get(0);
		// Opening bracket
		if (bracket.isLeaf()) {
			codeBlock.printError("Expected \"(\", but did not find any");
			return false;
		}

		if (bracket.getData() == null || bracket.getData().type != EnumTokenType.PARENTHESES
				|| !bracket.getData().value.equals("(")) {
			codeBlock.printError("Expected \"(\", but did not find any");
			return false;
		}
		int n = 0;
		for (Map.Entry<String, EnumValueType> entry : arguments.entrySet()) {
			Value value = getArgumentValue(n, method, codeBlock);
			if (value == null)
				return false;	
			if (entry.getValue() == EnumValueType.OBJECT) continue;
			if (entry.getValue() == EnumValueType.NUMERICAL && value.type.isNumerical()) continue;
			if (value.type != entry.getValue())
				return false;
			n++;
		}

		return true;
	}

	public void addArgument(String name, EnumValueType type) {
		arguments.put(name, type);
	}

}
