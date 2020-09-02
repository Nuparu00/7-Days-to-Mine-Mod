package com.nuparu.ni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

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
			if (StringUtils.isNumeric(word) || word.equals("true") || word.equals("false")) {
				tokens.add(new Token(EnumTokenType.VALUE, word));
				continue;
			}
			char c = word.charAt(0);
			if (Character.isWhitespace(c))
				continue;

			switch (word) {
			case "+":
			case "-":
			case "*":
			case "/":
			case "%":
			case "!":
				tokens.add(new Token(EnumTokenType.OPERATOR, word));
				break;
			case "=": {
				if (i < words.length - 1 && words[i + 1].equals("=")) {
					tokens.add(new Token(EnumTokenType.OPERATOR, "=="));
					i++;
					break;
				}
				tokens.add(new Token(EnumTokenType.OPERATOR, word));
				break;
			}
			case "int":
			case "string":
			case "char":
			case "double":
			case "boolean":
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

			// System.out.println("FF " + i + " " + root.getData().type);

			Token token = tokens.get(i);

			// System.out.println("GGGGG " + root.getData().type + " " + token.type + " " +
			// token.value);
			if (root.getData().type == EnumTokenType.START) {
				if (token.type == EnumTokenType.TYPE || token.type == EnumTokenType.IDENTIFIER
						|| token.type == EnumTokenType.KEYWORD || token.type == EnumTokenType.METHOD || token.type == EnumTokenType.BRACKET) {
					root = root.addChild(token);
					if (token.type == EnumTokenType.SEMICOLON
							|| (token.type == EnumTokenType.BRACKET && token.value.equals("}"))) {
						root = root.getParent();

						while (root.getData().type != EnumTokenType.START) {
							root = root.getParent();
						}

					}
					continue;
				}
			}

			else if (root.getData().type == EnumTokenType.TYPE) {
				if (token.type == EnumTokenType.IDENTIFIER) {
					root = root.addChild(token);
					if (token.type == EnumTokenType.SEMICOLON
							|| (token.type == EnumTokenType.BRACKET && token.value.equals("}"))) {
						root = root.getParent();

						while (root.getData().type != EnumTokenType.START) {
							root = root.getParent();
						}

					}
					continue;
					
				}
			}

			else if (root.getData().type == EnumTokenType.IDENTIFIER) {
				if (token.type == EnumTokenType.OPERATOR || token.type == EnumTokenType.PARENTHESES
						|| token.type == EnumTokenType.SEMICOLON) {
					root = root.addChild(token);
					if (token.type == EnumTokenType.SEMICOLON
							|| (token.type == EnumTokenType.BRACKET && token.value.equals("}"))) {
						root = root.getParent();
						while (root.getData().type != EnumTokenType.START) {
							root = root.getParent();
						}
					}
					continue;
				}
			}

			else if (root.getData().type == EnumTokenType.OPERATOR) {
				if (token.type == EnumTokenType.IDENTIFIER || token.type == EnumTokenType.VALUE
						|| token.type == EnumTokenType.PARENTHESES || token.type == EnumTokenType.OPERATOR
						|| token.type == EnumTokenType.SEMICOLON) {
					root = root.addChild(token);
					if (token.type == EnumTokenType.SEMICOLON
							|| (token.type == EnumTokenType.BRACKET && token.value.equals("}"))) {
						root = root.getParent();
						while (root.getData().type != EnumTokenType.START) {
							root = root.getParent();
						}
					}
					continue;
				}
			}

			else if (root.getData().type == EnumTokenType.PARENTHESES) {
				if (token.type == EnumTokenType.IDENTIFIER || token.type == EnumTokenType.VALUE
						|| token.type == EnumTokenType.SEMICOLON || token.type == EnumTokenType.BRACKET
						|| token.type == EnumTokenType.OPERATOR || token.type == EnumTokenType.PARENTHESES) {
					root = root.addChild(token);
					if (token.type == EnumTokenType.SEMICOLON
							|| (token.type == EnumTokenType.BRACKET && token.value.equals("}"))) {
						root = root.getParent();
						while (root.getData().type != EnumTokenType.START) {
							root = root.getParent();
						}
					}
					continue;
				}
			}

			else if (root.getData().type == EnumTokenType.BRACKET) {
				if (token.type == EnumTokenType.IDENTIFIER || token.type == EnumTokenType.VALUE
						|| token.type == EnumTokenType.TYPE || token.type == EnumTokenType.SEMICOLON
						|| token.type == EnumTokenType.PARENTHESES || token.type == EnumTokenType.BRACKET
						|| token.type == EnumTokenType.KEYWORD || token.type == EnumTokenType.METHOD) {
					root = root.addChild(token);
					if (token.type == EnumTokenType.SEMICOLON
							|| (token.type == EnumTokenType.BRACKET && token.value.equals("}"))) {
						root = root.getParent();
						while (root.getData().type != EnumTokenType.START) {
							root = root.getParent();
						}
					}
					continue;
				}
			}

			else if (root.getData().type == EnumTokenType.VALUE) {
				if (token.type == EnumTokenType.OPERATOR || token.type == EnumTokenType.PARENTHESES
						|| token.type == EnumTokenType.SEMICOLON) {
					root = root.addChild(token);
					if (token.type == EnumTokenType.SEMICOLON
							|| (token.type == EnumTokenType.BRACKET && token.value.equals("}"))) {
						root = root.getParent();

						while (root.getData().type != EnumTokenType.START) {
							root = root.getParent();
						}

					}
					continue;
				}
			} else if (root.getData().type == EnumTokenType.SEMICOLON) {
				if (token.type == EnumTokenType.IDENTIFIER || token.type == EnumTokenType.TYPE
						|| token.type == EnumTokenType.KEYWORD || token.type == EnumTokenType.METHOD
						|| token.type == EnumTokenType.BRACKET) {
					root = root.addChild(token);
					if (token.type == EnumTokenType.SEMICOLON
							|| (token.type == EnumTokenType.BRACKET && token.value.equals("}"))) {
						root = root.getParent();

						while (root.getData().type != EnumTokenType.START) {
							root = root.getParent();
						}

					}
					continue;
				}
			}

			else if (root.getData().type == EnumTokenType.KEYWORD) {
				if (token.type == EnumTokenType.PARENTHESES || token.type == EnumTokenType.BRACKET) {
					root = root.addChild(token);
					if (token.type == EnumTokenType.SEMICOLON
							|| (token.type == EnumTokenType.BRACKET && token.value.equals("}"))) {
						root = root.getParent();

						while (root.getData().type != EnumTokenType.START) {
							root = root.getParent();
						}

					}
					continue;
				}
			} else if (root.getData().type == EnumTokenType.METHOD) {
				if (token.type == EnumTokenType.PARENTHESES) {
					root = root.addChild(token);
					if (token.type == EnumTokenType.SEMICOLON
							|| (token.type == EnumTokenType.BRACKET && token.value.equals("}"))) {
						root = root.getParent();

						while (root.getData().type != EnumTokenType.START) {
							root = root.getParent();
						}

					}
					continue;
				}
			}

			System.out.println("Unknown context :" + root.getData().toString() + " " + token.toString() + " " + i);
			return null;
		}

		return root.getTopRoot();

	}

	public static CodeBlock read(Tree<Token> tree, CodeBlock parent, TransitProcess process) {
		if (tree == null)
			return null;

		int level = 0;

		CodeBlock codeBlock = new CodeBlock();
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
					codeBlock.print("Expected an indentifier, but did not find any");
					return codeBlock;
				}
				String identifier = (String) name.getData().value;

				codeBlock.addVariable(identifier, value);

				Tree<Token> ending = name.getChildren().get(0);
				if (ending.getData() == null) {
					codeBlock.print("Expected ; or = but did not find any");
					return codeBlock;
				}
				if (ending.getData().type == EnumTokenType.SEMICOLON)
					break;

				if (ending.isLeaf() || ending.getData().type != EnumTokenType.OPERATOR || ending.getData().value == null
						|| !ending.getData().value.equals("=")) {
					codeBlock.print("Expected =, but did not find any");
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
						codeBlock.print("An error occured: " + e.toString());
					}
				}
				break;

			}

			case IDENTIFIER: {

				if (t.isLeaf())
					return codeBlock;
				Tree<Token> ending = t.getChildren().get(0);
				if (ending.getData() == null) {
					codeBlock.print("Expected = but did not find any");
					return codeBlock;
				}

				if (ending.isLeaf() || ending.getData().type != EnumTokenType.OPERATOR || ending.getData().value == null
						|| !ending.getData().value.equals("=")) {
					codeBlock.print("Expected =, but did not find any");
					return codeBlock;
				}

				Statement statement = readStatement(ending.getChildren().get(0), codeBlock, 0);
				if (statement != null) {
					Value result;
					try {
						result = statement.evaluate();
						codeBlock.setVariableValue(value, result.getRealValue());
					} catch (EvaluationErrorException | VariableNotFoundException | TypeMismatchException e) {
						e.printStackTrace();
						codeBlock.print("An error occured: " + e.toString());
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
						codeBlock.print("Expected (, but did not find any");
						return codeBlock;
					}
					Condition condition = readCondition(bracket.getChildren().get(0), codeBlock);

					try {
						Value result = condition.evaluate();
						if (!result.isBoolean()) {
							codeBlock.print("Expected boolean, found " + result.type);
						}

						Tree<Token> start = bracket;
						while (start.getData().type != EnumTokenType.BRACKET || !start.getData().value.equals("{")) {
							if (start.isLeaf()) {
								codeBlock.print("Expected \"{\", but did not find any");
								return codeBlock;
							}
							start = start.getChildren().get(0);
						}

						if (!(Boolean) result.getRealValue()) {
							break;
						}
						CodeBlock codeBlock2 = read(start, codeBlock, process);
						break;

					} catch (EvaluationErrorException | TypeMismatchException e) {
						e.printStackTrace();
						codeBlock.print("An error occured: " + e.toString());
					}
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
						codeBlock.print("Expected \"(\", but did not find any");
					}
					Statement statement = readStatement(bracket.getChildren().get(0), codeBlock, 0);
					try {
						Value result = statement.evaluate();
						codeBlock.print(result.getRealValue().toString());
					} catch (EvaluationErrorException e) {
						e.printStackTrace();
						codeBlock.print("An error occured: " + e.toString());
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
						codeBlock.print("Expected \"(\", but did not find any");
					}
					Statement statement = readStatement(bracket.getChildren().get(0), codeBlock, 0);
					try {
						Value result = statement.evaluate();
						process.saveToDevice(result.getRealValue().toString());
						codeBlock.print("Attempting to save " + result.getRealValue().toString());
					} catch (EvaluationErrorException e) {
						e.printStackTrace();
						codeBlock.print("An error occured: " + e.toString());
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
			Object value = tree.getData().value;
			System.out.println(tree.getData().toString());
			if (type == EnumTokenType.IDENTIFIER) {
				Variable var = codeBlock.getVariable((String) tree.getData().value);
				statement.addValue(var);
			} else if (type == EnumTokenType.VALUE) {
				statement.addValue(tree.getData().value);
			} else if (tree.getData().type == EnumTokenType.OPERATOR) {
				statement.addOperator(new Operator(tree.getData().type, (String) tree.getData().value));
			} else if (type == EnumTokenType.PARENTHESES) {
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
					tree.print("-", true);
				}
				if (value.equals(")")) {
					break;
				}
			}

			if (!tree.isLeaf()) {
				tree = tree.getChildren().get(0);
			}
			i++;
		}
		return statement;
	}

	public static Condition readCondition(Tree<Token> tree, CodeBlock codeBlock) {
		Statement s = readStatement(tree, codeBlock, 0);
		System.out.println(s.toString());
		return new Condition(s);
	}

	public static void execute(Tree<Token> tree) {
	}
}
