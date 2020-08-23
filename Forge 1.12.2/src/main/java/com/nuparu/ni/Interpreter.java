package com.nuparu.ni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.nuparu.ni.exception.EvaluationErrorException;
import com.nuparu.ni.exception.TypeMismatchException;
import com.nuparu.ni.exception.VariableNotFoundException;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Tree;

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
						|| token.type == EnumTokenType.KEYWORD) {
					root = root.addChild(token);
					continue;
				}
			}

			else if (root.getData().type == EnumTokenType.TYPE) {
				if (token.type == EnumTokenType.IDENTIFIER) {
					root = root.addChild(token);
					continue;
				}
			}

			else if (root.getData().type == EnumTokenType.IDENTIFIER) {
				if (token.type == EnumTokenType.OPERATOR || token.type == EnumTokenType.PARENTHESES
						|| token.type == EnumTokenType.SEMICOLON) {
					root = root.addChild(token);
					if (token.type == EnumTokenType.SEMICOLON) {
						while (root.getData().type != EnumTokenType.START
								&& root.getData().type != EnumTokenType.BRACKET) {
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
					if (token.type == EnumTokenType.SEMICOLON) {
						while (root.getData().type != EnumTokenType.START
								&& root.getData().type != EnumTokenType.BRACKET) {
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
					if (token.type == EnumTokenType.SEMICOLON) {
						while (root.getData().type != EnumTokenType.START
								&& root.getData().type != EnumTokenType.BRACKET) {
							root = root.getParent();
						}
					}
					continue;
				}
			}

			else if (root.getData().type == EnumTokenType.BRACKET) {
				if (token.type == EnumTokenType.IDENTIFIER || token.type == EnumTokenType.VALUE
						|| token.type == EnumTokenType.SEMICOLON || token.type == EnumTokenType.PARENTHESES
						|| token.type == EnumTokenType.BRACKET) {
					root = root.addChild(token);
					if (token.type == EnumTokenType.SEMICOLON) {
						while (root.getData().type != EnumTokenType.START
								&& root.getData().type != EnumTokenType.BRACKET) {
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
					if (token.type == EnumTokenType.SEMICOLON) {
						while (root.getData().type != EnumTokenType.START
								&& root.getData().type != EnumTokenType.BRACKET) {
							root = root.getParent();
						}
					}
					continue;
				}
			} else if (root.getData().type == EnumTokenType.SEMICOLON) {
				if (token.type == EnumTokenType.IDENTIFIER || token.type == EnumTokenType.TYPE) {
					root = root.addChild(token);
					continue;
				}
			}

			else if (root.getData().type == EnumTokenType.KEYWORD) {
				if (token.type == EnumTokenType.PARENTHESES || token.type == EnumTokenType.BRACKET) {
					root = root.addChild(token);
					continue;
				}
			}

			System.out.println("Unknown context :" + root.getData().toString() + " " + token.toString() + " " + i);
			return null;
		}

		return root.getTopRoot();

	}

	public static CodeBlock read(Tree<Token> tree, CodeBlock parent) {
		if (tree == null)
			return null;

		CodeBlock codeBlock = new CodeBlock();
		codeBlock.parent = parent;
		if (codeBlock != null) {
			for (int i = 0; i < tree.getChildren().size(); i++) {
				Tree<Token> t = tree.getChildren().get(i);
				EnumTokenType type = t.getData().type;
				switch (type) {
				case TYPE: {
					// data type
					String dataType = (String) t.getData().value;
					if (t.isLeaf())
						break;
					// name
					Tree<Token> t2 = t.getChildren().get(0);
					if (t2.getData().type != EnumTokenType.IDENTIFIER)
						break;
					String name = (String) t2.getData().value;
					// optional value
					if (t2.isLeaf())
						break;
					codeBlock.addVariable(name, dataType);
					Tree<Token> t3 = t2.getChildren().get(0);
					if (t3.getData().type == EnumTokenType.SEMICOLON) {
						break;
					}
					if (t3.getData().type != EnumTokenType.OPERATOR || !t3.getData().value.equals("=") || t3.isLeaf())
						break;
					Statement statement = readStatement(t3.getChildren().get(0), codeBlock, 0);
					if (statement != null) {
						Value result;
						try {
							result = statement.evaluate();
							codeBlock.setVariableValue(name, result.getRealValue());
						} catch (EvaluationErrorException | VariableNotFoundException | TypeMismatchException e) {
							e.printStackTrace();
						}
					}
				}
				case IDENTIFIER: {
					// name
					String name = (String) t.getData().value;
					// value
					if (t.isLeaf())
						break;
					Tree<Token> t2 = t.getChildren().get(0);
					if (t2.getData().type != EnumTokenType.OPERATOR || !t2.getData().value.equals("=") || t2.isLeaf())
						break;
					Statement statement = readStatement(t2.getChildren().get(0), codeBlock, 0);
					if (statement != null) {
						Value result;
						try {
							result = statement.evaluate();
							codeBlock.setVariableValue(name, result.getRealValue());
						} catch (EvaluationErrorException | VariableNotFoundException | TypeMismatchException e) {
							e.printStackTrace();
						}
					}
				}
				case KEYWORD: {
					String keyword = (String) t.getData().value;

					if (keyword.equals("if")) {
						if (t.isLeaf())
							break;
						Tree<Token> t2 = t.getChildren().get(0);
						Condition c = readCondition(t2, codeBlock);
						try {
							Value value = c.evaluate();
							if (value.isBoolean() && (Boolean) value.getRealValue() == true) {
								while (t2.getData().type != EnumTokenType.BRACKET || !t2.getData().value.equals("{")) {
									System.out.println("FUJKY " + t2.getData().value + " " + t2.getData().type + " " + (t2.getData().type != EnumTokenType.BRACKET) + " " + (!t2.getData().value.equals("{")));
									if (t2.isLeaf()) {
										System.out.println("FUJ");
										return null;
									}
									t2 = t2.getChildren().get(0);
								}
								CodeBlock block = read(t2, codeBlock);
							}
						} catch (EvaluationErrorException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TypeMismatchException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
				}
			}
		}

		System.out.println(codeBlock.variables.toString());

		return codeBlock;

	}

	public static Statement readStatement(Tree<Token> tree, CodeBlock codeBlock, int initLvl) {
		int level = initLvl;
		Statement statement = new Statement();
		int id = MathUtils.getIntInRange(0, Integer.MAX_VALUE-1);

		while (!tree.isLeaf()) {
			EnumTokenType type = tree.getData().type;
			Object value = tree.getData().value;
			
			System.out.println(value.toString() + " " + id);

			if (type == EnumTokenType.IDENTIFIER) {
				statement.addValue(codeBlock.getVariable((String) tree.getData().value));
			} else if (type == EnumTokenType.VALUE) {
				statement.addValue(tree.getData().value);
			} else if (tree.getData().type == EnumTokenType.OPERATOR) {
				statement.addOperator(new Operator(tree.getData().type, (String) tree.getData().value));
			} else if (type == EnumTokenType.PARENTHESES) {
				if (value.equals("(")) {
					statement.addStatement(readStatement(tree.getChildren().get(0), codeBlock, level));
					tree = tree.getChildren().get(0);
					while (!tree.isLeaf() && level != initLvl) {
						tree = tree.getChildren().get(0);
						type = tree.getData().type;
						value = tree.getData().value;

						if (type == EnumTokenType.PARENTHESES) {
							if (value.equals("(")) {
								level++;
							} else if (value.equals(")")) {
								level--;
							}
						}
					}
					if (tree.isLeaf()) {
						return statement;
					}
				} else if (value.equals(")")) {

					if (level == initLvl) {
						return statement;
					}
				}
			}

			tree = tree.getChildren().get(0);
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
