package com.nuparu.ni;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.nuparu.ni.exception.EvaluationErrorException;
import com.nuparu.ni.exception.TypeMismatchException;
import com.nuparu.ni.exception.VariableNotFoundException;
import com.nuparu.sevendaystomine.computer.process.TransitProcess;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Tree;
import com.nuparu.sevendaystomine.util.Utils;

//Ni to Java Interpreter
public class Interpreter {
	public static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
	public static final String REGEX = "[\\s+*-/;.?!:\\=\\(\\)\\{\\}\\[\\]\\|\\/\\\"]";

	// Generates a list of tokens
	public static List<Token> tokenize(String s) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		// Splits raw input into individual words by empty spaces, brackets,
		// operators... but also keeps the separators in the array
		String[] words = s.split(String.format(WITH_DELIMITER, REGEX));
		for (int i = 0; i < words.length; i++) {
			String word = words[i].trim();
			if (word.isEmpty())
				continue;
			if (NumberUtils.isCreatable(word) || word.equals("true") || word.equals("false")) {
				tokens.add(new Token(EnumTokenType.VALUE, word));
				continue;
			}
			char c = word.charAt(0);
			if (Character.isWhitespace(c))
				continue;

			switch (word) {
			case "+":
			case "++":
			case "--":
			case "-":
			case "*":
			case "/":
			case "%":
			case "!":
			case ">":
			case "<":
			case "=":
				if (i < words.length - 1 && (words[i + 1].equals(word) || words[i + 1].equals("="))) {
					tokens.add(new Token(EnumTokenType.OPERATOR, word + words[i + 1]));
					i++;
					break;
				}
				tokens.add(new Token(EnumTokenType.OPERATOR, word));
				break;
			case "int":
			case "string":
			case "char":
			case "double":
			case "boolean":
			case "float":
			case "long":
			case "short":
			case "byte":
				tokens.add(new Token(EnumTokenType.TYPE, word));
				break;
			case "for":
			case "while":
			case "if":
			case "else":
				tokens.add(new Token(EnumTokenType.KEYWORD, word));
				break;
			case "print":
			case "save":
			case "random":
			case "pow":
			case "sqrt":
				tokens.add(new Token(EnumTokenType.METHOD, word));
				break;
			case "{":
			case "}":
				tokens.add(new Token(EnumTokenType.BRACKET, word));
				break;
			case "(":
			case ")":
				tokens.add(new Token(EnumTokenType.PARENTHESES, word));
				break;
			case ";":
				tokens.add(new Token(EnumTokenType.SEMICOLON, word));
				break;
			case "\"": {
				String value = "";
				for (int j = i + 1; j < words.length; j++) {
					String word2 = words[j];
					if (word2.equals("\"") && !words[j - 1].equals("\\")) {
						tokens.add(new Token(EnumTokenType.VALUE, value));
						i = j;
						break;
					} else {
						value += word2;
					}
				}
				break;
			}
			case ",":
				tokens.add(new Token(EnumTokenType.COMMA, word));
				break;
			default:
				tokens.add(new Token(EnumTokenType.IDENTIFIER, word));
			}
		}

		return tokens;
	}

	// Gnerates a tree of tokens
	public static Tree<Token> parse(List<Token> tokens) {
		Tree<Token> root = new Tree<Token>(new Token(EnumTokenType.START, null));
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			EnumTokenType type = token.type;
			String value = token.value;

			if (root.getData().type.allowedFollowingTypes == null
					|| Arrays.stream(root.getData().type.allowedFollowingTypes).anyMatch(type.name::equals)) {
				root = root.addChild(token);
				if (type == EnumTokenType.SEMICOLON || (type == EnumTokenType.BRACKET && value.equals("}"))) {
					int brackets = 0;
					do {
						root = root.getParent();
						if (root.getData().type == EnumTokenType.BRACKET) {
							if (root.getData().value.equals("}")) {
								brackets++;
							} else if (root.getData().value.equals("{")) {
								brackets--;
							}
						}

					} while (!isBack(root, token, brackets));

					// To-do: Check for a keyword if not START

					if (type != EnumTokenType.SEMICOLON && brackets <= 0
							&& root.getData().type == EnumTokenType.BRACKET) {
						while (root.getData().type != EnumTokenType.KEYWORD
								&& root.getData().type != EnumTokenType.START) {
							root = root.getParent();
						}
						if (root.getData().type != EnumTokenType.START) {
							root = root.getParent();
						}
					}
				}
			}

		}

		return root.getTopRoot();

	}

	public static boolean isBack(Tree<Token> root, Token token, int brackets) {
		return root.getData().type == EnumTokenType.START
				|| (root.getData().type == EnumTokenType.BRACKET && root.getData().value.equals("{") && brackets <= 0);
	}

	public static CodeBlock read(Tree<Token> tree, CodeBlock parent, TransitProcess process) {
		if (tree == null)
			return null;

		int level = 0;

		CodeBlock codeBlock = new CodeBlock(process);
		codeBlock.parent = parent;

		for (int i = 0; i < tree.getChildren().size(); i++) {
			Tree<Token> t = tree.getChildren().get(i);
			EnumTokenType type = t.getData().type;
			String value = t.getData().value;

			switch (type) {

			default:
			case START: {
				break;
			}

			case BRACKET: {
				break;
			}

			case PARENTHESES: {
				break;
			}

			case TYPE: {
				if (t.isLeaf())
					return codeBlock;

				Tree<Token> name = t.getChildren().get(0);
				if (name.isLeaf() || name.getData() == null || name.getData().type != EnumTokenType.IDENTIFIER) {
					codeBlock.printError("Expected an indentifier, but did not find any");
					return codeBlock;
				}
				String identifier = (String) name.getData().value;

				codeBlock.addVariable(identifier, value);

				Tree<Token> ending = name.getChildren().get(0);
				if (ending.getData() == null) {
					codeBlock.printError("Expected ; or = but did not find any");
					return codeBlock;
				}
				if (ending.getData().type == EnumTokenType.SEMICOLON)
					break;

				if (ending.isLeaf() || ending.getData().type != EnumTokenType.OPERATOR || ending.getData().value == null
						|| !ending.getData().value.equals("=")) {
					codeBlock.printError("Expected =, but did not find any");
					return codeBlock;
				}
				Statement statement = readStatement(ending.getChildren().get(0), codeBlock, 0);
				if (statement != null) {
					Value result;
					try {
						result = statement.evaluate();
						codeBlock.setVariableValue(identifier, result.getRealValue());
					} catch (EvaluationErrorException | VariableNotFoundException | TypeMismatchException e) {
						e.printStackTrace();
						codeBlock.printError("An error occured: " + e.toString());
					}
				}
				break;

			}

			case IDENTIFIER: {

				if (t.isLeaf())
					return codeBlock;
				Tree<Token> ending = t.getChildren().get(0);
				if (ending.getData() == null) {
					codeBlock.printError("Expected = but did not find any");
					return codeBlock;
				}

				if (ending.isLeaf() || ending.getData().type != EnumTokenType.OPERATOR
						|| ending.getData().value == null) {
					codeBlock.printError("Expected an operator, but did not find any");
					return codeBlock;
				}

				String operator = ending.getData().value;
				switch (operator) {
				case "=": {
					Statement statement = readStatement(ending.getChildren().get(0), codeBlock, 0);
					if (statement != null) {
						Value result;
						try {
							result = statement.evaluate();
							codeBlock.setVariableValue(value, result.getRealValue());
						} catch (EvaluationErrorException | VariableNotFoundException | TypeMismatchException e) {
							e.printStackTrace();
							codeBlock.printError("An error occured: " + e.toString());
						}
					}
					break;
				}
				case "+=": {
					Value original = codeBlock.getVariable(value);
					if (original == null || original.isBoolean()) {
						codeBlock.printError("Variable " + value + " does not support ++ operator");
						return codeBlock;
					}
					Statement statement = readStatement(ending.getChildren().get(0), codeBlock, 0);
					if (statement != null) {
						Value result;
						try {
							result = statement.evaluate();
							System.out.println(result.add(original).toString());
							codeBlock.setVariableValue(value, original.add(result).getRealValue());
						} catch (EvaluationErrorException | VariableNotFoundException | TypeMismatchException e) {
							e.printStackTrace();
							codeBlock.printError("An error occured: " + e.toString());
						}
					}
					break;
				}
				case "-=": {
					Value original = codeBlock.getVariable(value);
					if (original == null || !original.isNumerical()) {
						codeBlock.printError("Variable " + value + " does not support ++ operator");
						return codeBlock;
					}
					Statement statement = readStatement(ending.getChildren().get(0), codeBlock, 0);
					if (statement != null) {
						Value result;
						try {
							result = statement.evaluate();
							codeBlock.setVariableValue(value, original.substract(result).getRealValue());
						} catch (EvaluationErrorException | VariableNotFoundException | TypeMismatchException e) {
							e.printStackTrace();
							codeBlock.printError("An error occured: " + e.toString());
						}
					}
					break;
				}
				case "*=": {
					Value original = codeBlock.getVariable(value);
					if (original == null || !original.isNumerical()) {
						codeBlock.printError("Variable " + value + " does not support ++ operator");
						return codeBlock;
					}
					Statement statement = readStatement(ending.getChildren().get(0), codeBlock, 0);
					if (statement != null) {
						Value result;
						try {
							result = statement.evaluate();
							codeBlock.setVariableValue(value, result.multiply(original).getRealValue());
						} catch (EvaluationErrorException | VariableNotFoundException | TypeMismatchException e) {
							e.printStackTrace();
							codeBlock.printError("An error occured: " + e.toString());
						}
					}
					break;
				}
				case "/=": {
					Value original = codeBlock.getVariable(value);
					if (original == null || !original.isNumerical()) {
						codeBlock.printError("Variable " + value + " does not support ++ operator");
						return codeBlock;
					}
					Statement statement = readStatement(ending.getChildren().get(0), codeBlock, 0);
					if (statement != null) {
						Value result;
						try {
							result = statement.evaluate();
							codeBlock.setVariableValue(value, result.divide(original).getRealValue());
						} catch (EvaluationErrorException | VariableNotFoundException | TypeMismatchException e) {
							e.printStackTrace();
							codeBlock.printError("An error occured: " + e.toString());
						}
					}
					break;
				}
				case "++": {
					Value original = codeBlock.getVariable(value);
					if (original == null || !(original.isNumerical())) {
						codeBlock.printError("Variable " + value + " does not support ++ operator");
						return codeBlock;
					}
					try {
						codeBlock.setVariableValue(value, (Integer) original.add(new Value(1)).getRealValue());
					} catch (VariableNotFoundException | TypeMismatchException e) {
						e.printStackTrace();
					}
					break;
				}
				case "--": {
					Value original = codeBlock.getVariable(value);
					if (original == null || !(original.isNumerical())) {
						codeBlock.printError("Variable " + value + " does not support -- operator");
						return codeBlock;
					}
					try {
						codeBlock.setVariableValue(value, (Integer) original.substract(new Value(1)).getRealValue());
					} catch (VariableNotFoundException | TypeMismatchException e) {
						e.printStackTrace();
					}
					break;
				}
				}
				break;
			}

			case KEYWORD: {
				switch (value) {
				default:
					break;
				case "if": {
					if (t.isLeaf())
						return codeBlock;
					Tree<Token> bracket = t.getChildren().get(0);
					if (bracket.isLeaf())
						return codeBlock;
					if (bracket.getData().type != EnumTokenType.PARENTHESES || !bracket.getData().value.equals("(")) {
						codeBlock.printError("Expected (, but did not find any");
						return codeBlock;
					}
					Condition condition = readCondition(bracket.getChildren().get(0), codeBlock);

					try {
						Value result = condition.evaluate();
						if (!result.isBoolean()) {
							codeBlock.printError("Expected boolean, found " + result.type);
						}

						Tree<Token> start = bracket;
						while (start.getData().type != EnumTokenType.BRACKET || !start.getData().value.equals("{")) {
							if (start.isLeaf()) {
								codeBlock.printError("Expected \"{\", but did not find any");
								return codeBlock;
							}
							start = start.getChildren().get(0);
						}
						if ((Boolean) result.getRealValue()) {
							CodeBlock codeBlock2 = read(start, codeBlock, process);
							start.print("-", false);
						} else {
							// Else handling
							if (i == tree.getChildren().size() - 1) {
								return codeBlock;
							}
							Tree<Token> t_else = tree.getChildren().get(i + 1);
							if (t_else.isLeaf())
								return codeBlock;
							if (t_else.getData().type != EnumTokenType.KEYWORD
									|| !t_else.getData().value.equals("else")) {
								codeBlock.printError("Expected \"else\", but did not find any");
								return codeBlock;
							}
							bracket = t_else.getChildren().get(0);
							if (bracket.isLeaf())
								return codeBlock;
							if (bracket.getData().type != EnumTokenType.BRACKET
									|| !bracket.getData().value.equals("{")) {
								codeBlock.printError("Expected \"{\", but did not find any");
								return codeBlock;
							}
							CodeBlock codeBlock2 = read(bracket, codeBlock, process);

						}
						break;

					} catch (EvaluationErrorException | TypeMismatchException e) {
						e.printStackTrace();
						codeBlock.printError("An error occured: " + e.toString());
					}
					break;
				}
				case "while": {
					if (t.isLeaf())
						return codeBlock;
					Tree<Token> bracket = t.getChildren().get(0);
					if (bracket.isLeaf())
						return codeBlock;
					if (bracket.getData().type != EnumTokenType.PARENTHESES || !bracket.getData().value.equals("(")) {
						codeBlock.printError("Expected (, but did not find any");
						return codeBlock;
					}
					Condition condition = readCondition(bracket.getChildren().get(0), codeBlock);

					try {
						Value result = condition.evaluate();
						while (result.isBoolean() && (Boolean) result.getRealValue()) {

							Tree<Token> start = bracket;
							while (start.getData().type != EnumTokenType.BRACKET
									|| !start.getData().value.equals("{")) {
								if (start.isLeaf()) {
									codeBlock.printError("Expected \"{\", but did not find any");
									return codeBlock;
								}
								start = start.getChildren().get(0);
							}

							condition = readCondition(bracket.getChildren().get(0), codeBlock);
							result = condition.evaluate();
							if ((Boolean) result.getRealValue() == false) {
								break;
							}
							CodeBlock codeBlock2 = read(start, codeBlock, process);

						}
						break;

					} catch (EvaluationErrorException | TypeMismatchException e) {
						e.printStackTrace();
						codeBlock.printError("An error occured: " + e.toString());
					}
					break;
				}
				}
				break;
			}

			case METHOD: {
				switch (value) {
				default:
					break;
				case "print": {
					if (t.isLeaf())
						return codeBlock;
					Tree<Token> bracket = t.getChildren().get(0);
					if (bracket.isLeaf())
						return codeBlock;
					if (bracket.getData().type != EnumTokenType.PARENTHESES || !bracket.getData().value.equals("(")) {
						codeBlock.printError("Expected \"(\", but did not find any");
					}
					Statement statement = readStatement(bracket.getChildren().get(0), codeBlock, 0);
					if (statement == null) {
						codeBlock.printError("Null Statement");
						break;
					}
					try {
						Value result = statement.evaluate();
						codeBlock.print(result.getRealValue().toString());
					} catch (EvaluationErrorException e) {
						e.printStackTrace();
						codeBlock.printError("An error occured: " + e.toString());
					}
					break;
				}
				case "save": {
					if (t.isLeaf())
						return codeBlock;
					Tree<Token> bracket = t.getChildren().get(0);
					if (bracket.isLeaf())
						return codeBlock;
					if (bracket.getData().type != EnumTokenType.PARENTHESES || !bracket.getData().value.equals("(")) {
						codeBlock.printError("Expected \"(\", but did not find any");
					}
					Statement statement = readStatement(bracket.getChildren().get(0), codeBlock, 0);
					codeBlock.print("Attempting to save...");
					try {
						Value result = statement.evaluate();
						process.saveToDevice(result.getRealValue().toString());
						codeBlock.printSuccess("Successfully saved " + result.getRealValue().toString());

					} catch (EvaluationErrorException e) {
						e.printStackTrace();
						codeBlock.printError("An error occured: " + e.toString());
					}
					break;
				}
				}
				break;
			}

			}
		}

		System.out.println(codeBlock.variables.toString());

		return codeBlock;

	}

	public static Statement readStatement(Tree<Token> tree, CodeBlock codeBlock, int initLvl) {
		int level = initLvl;
		Statement statement = new Statement();
		int id = MathUtils.getIntInRange(0, Integer.MAX_VALUE - 1);

		int i = 0;
		while (!tree.isLeaf()) {
			EnumTokenType type = tree.getData().type;
			String value = tree.getData().value;

			boolean flag = false;

			switch (type) {
			default:
				break;
			case IDENTIFIER: {
				statement.addValue(codeBlock.getVariable((String) tree.getData().value));
				break;
			}
			case VALUE: {
				statement.addValue(tree.getData().value);
				break;
			}

			case METHOD: {
				switch (value) {
				case "random": {
					if (tree.isLeaf()) {
						flag = true;
						break;
					}
					Tree<Token> bracket = tree.getChildren().get(0);
					if (bracket.isLeaf()) {
						flag = true;
						codeBlock.printError("Expected \"(\", but did not find any");
						break;
					}
					if (bracket.getData() == null || bracket.getData().type != EnumTokenType.PARENTHESES
							|| !bracket.getData().value.equals("(")) {
						codeBlock.printError("Expected \"(\", but did not find any");
						flag = true;
						break;
					}
					Statement arg0 = readStatement(bracket.getChildren().get(0), codeBlock, 0);
					try {
						Value min = arg0.evaluate();
						if (!min.isNumerical()) {
							codeBlock.printError("Expected a number, but did not find any");
							flag = true;
							break;
						}
						while (!bracket.isLeaf()
								&& (bracket.getData().type != EnumTokenType.PARENTHESES
										|| !bracket.getData().value.equals(")"))
								&& (bracket.getData().type != EnumTokenType.COMMA)) {
							bracket = bracket.getChildren().get(0);
						}
						if (bracket.isLeaf()) {
							flag = true;
							break;
						}

						if ((bracket.getData().type != EnumTokenType.PARENTHESES
								|| !bracket.getData().value.equals(")"))
								&& (bracket.getData().type != EnumTokenType.COMMA)) {
							codeBlock.printError("Expected \")\" or \",\", but did not find any");
							flag = true;
							break;
						}
						tree = bracket;
						if (bracket.isLeaf()) {
							flag = true;
							break;
						}
						if (bracket.getData().type == EnumTokenType.PARENTHESES
								&& bracket.getData().value.equals(")")) {
							statement.addValue(new Random().nextInt((int) min.getRealValue()));
							break;
						}

						if (bracket.getData().type != EnumTokenType.COMMA) {
							codeBlock.printError("Expected \",\", but did not find any");
							flag = true;
							break;
						}

						Tree<Token> ending = bracket.getChildren().get(0);
						if (ending.isLeaf()) {
							flag = true;
							break;
						}

						Statement arg1 = readStatement(ending, codeBlock, 0);

						Value max = arg1.evaluate();
						if (!max.isNumerical()) {
							codeBlock.printError("Expected a number, but did not find any");
							flag = true;
							break;
						}
						while (!ending.isLeaf()
								&& (ending.getData().type != EnumTokenType.PARENTHESES
										|| !ending.getData().value.equals(")"))
								&& (ending.getData().type != EnumTokenType.COMMA)) {
							ending = ending.getChildren().get(0);
						}
						if (ending.isLeaf()) {
							flag = true;
							break;
						}

						if ((ending.getData().type != EnumTokenType.PARENTHESES
								|| !ending.getData().value.equals(")"))) {
							codeBlock.printError("Expected \")\", but did not find any");
							flag = true;
							break;
						}
						tree = ending;
						if (ending.isLeaf()) {
							flag = true;
							break;
						}
						statement.addValue(MathUtils.getIntInRange(new Random(), (int)min.asInt().getRealValue(),
								(int)max.asInt().getRealValue()));

					} catch (EvaluationErrorException e) {
						e.printStackTrace();
						codeBlock.printError("An error occured: " + e.toString());
					}

					break;
				}

				case "pow": {
					if (tree.isLeaf()) {
						flag = true;
						break;
					}
					Tree<Token> bracket = tree.getChildren().get(0);
					if (bracket.isLeaf()) {
						flag = true;
						codeBlock.printError("Expected \"(\", but did not find any");
						break;
					}
					if (bracket.getData() == null || bracket.getData().type != EnumTokenType.PARENTHESES
							|| !bracket.getData().value.equals("(")) {
						codeBlock.printError("Expected \"(\", but did not find any");
						flag = true;
						break;
					}


					Statement arg0 = readStatement(bracket.getChildren().get(0), codeBlock, 0);
					try {
						Value base = arg0.evaluate();
						if (!base.isNumerical()) {
							codeBlock.printError("Expected a number, but did not find any");
							flag = true;
							break;
						}
						while (!bracket.isLeaf() && bracket.getData().type != EnumTokenType.COMMA) {
							bracket = bracket.getChildren().get(0);
						}
						if (bracket.isLeaf()) {
							flag = true;
							break;
						}

						if (bracket.getData().type != EnumTokenType.COMMA) {
							codeBlock.printError("Expected \",\", but did not find any");
							flag = true;
							break;
						}
						if (bracket.isLeaf()) {
							codeBlock.printError("Expected a value, but did not find any");
							flag = true;
							break;
						}
						Tree<Token> ending = bracket.getChildren().get(0);
						if (ending.isLeaf()) {
							flag = true;
							break;
						}

						Statement arg1 = readStatement(ending, codeBlock, 0);

						Value exp = arg1.evaluate();
						if (!exp.isNumerical()) {
							codeBlock.printError("Expected a number, but did not find any");
							flag = true;
							break;
						}
						while (!ending.isLeaf() && (ending.getData().type != EnumTokenType.PARENTHESES
								|| !ending.getData().value.equals(")"))) {
							ending = ending.getChildren().get(0);
						}
						if (ending.isLeaf()) {
							flag = true;
							break;
						}

						if ((ending.getData().type != EnumTokenType.PARENTHESES
								|| !ending.getData().value.equals(")"))) {
							codeBlock.printError("Expected \")\", but did not find any");
							flag = true;
							break;
						}
						tree = ending;
						if (ending.isLeaf()) {
							flag = true;
							break;
						}

						statement.addValue((double)Math.pow((double) base.asDouble().getRealValue(), (double) exp.asDouble().getRealValue()));

					} catch (EvaluationErrorException e) {
						e.printStackTrace();
						codeBlock.printError("An error occured: " + e.toString());
					}

					break;
				}
				
				case "sqrt": {
					if (tree.isLeaf()) {
						flag = true;
						break;
					}
					Tree<Token> bracket = tree.getChildren().get(0);
					if (bracket.isLeaf()) {
						flag = true;
						codeBlock.printError("Expected \"(\", but did not find any");
						break;
					}
					if (bracket.getData() == null || bracket.getData().type != EnumTokenType.PARENTHESES
							|| !bracket.getData().value.equals("(")) {
						codeBlock.printError("Expected \"(\", but did not find any");
						flag = true;
						break;
					}
					Statement arg0 = readStatement(bracket.getChildren().get(0), codeBlock, 0);
					try {
						Value base = arg0.evaluate();
						if (!base.isNumerical()) {
							codeBlock.printError("Expected a number, but did not find any");
							flag = true;
							break;
						}
						while (!bracket.isLeaf()
								&& (bracket.getData().type != EnumTokenType.PARENTHESES
										|| !bracket.getData().value.equals(")"))) {
							bracket = bracket.getChildren().get(0);
						}
						if (bracket.isLeaf()) {
							flag = true;
							break;
						}

						if (!bracket.isLeaf() && bracket.getData().type != EnumTokenType.PARENTHESES
								|| !bracket.getData().value.equals(")")) {
							codeBlock.printError("Expected \")\", but did not find any");
							flag = true;
							break;
						}
						tree = bracket;
						if (bracket.isLeaf()) {
							flag = true;
							break;
						}
						if (bracket.getData().type == EnumTokenType.PARENTHESES
								&& bracket.getData().value.equals(")")) {
							statement.addValue((double)Math.sqrt((double)base.asDouble().getRealValue()));
							break;
						}

					} catch (EvaluationErrorException e) {
						e.printStackTrace();
						codeBlock.printError("An error occured: " + e.toString());
					}

					break;
				}

				}
				break;
			}

			case OPERATOR: {
				statement.addOperator(new Operator(tree.getData().type, (String) tree.getData().value));
				break;
			}

			case PARENTHESES: {
				if (value.equals("(")) {
					statement.addStatement(readStatement(tree.getChildren().get(0), codeBlock, level + 1));
					int l = level + 1;
					Tree<Token> tree2 = tree;
					while (!tree2.isLeaf()) {
						tree2 = tree2.getChildren().get(0);
						EnumTokenType type2 = tree2.getData().type;
						Object value2 = tree2.getData().value;
						if (type2 == EnumTokenType.PARENTHESES) {
							if (value2.equals("(")) {
								l++;
							} else if (value2.equals(")")) {
								l--;
								if (l <= level) {
									break;
								}
							}
						}
					}
					tree = tree2;
				}
				if (value.equals(")")) {
					flag = true;
					break;
				}
				break;
			}
			case COMMA: {
				flag = true;
				break;
			}
			}

			if (flag)
				break;

			if (!tree.isLeaf()) {
				tree = tree.getChildren().get(0);
			}
			i++;
		}
		return statement;
	}

	public static Condition readCondition(Tree<Token> tree, CodeBlock codeBlock) {
		Statement s = readStatement(tree, codeBlock, 0);
		return new Condition(s);
	}

	public static void execute(Tree<Token> tree) {
	}
}
