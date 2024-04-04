package nuparu.ni.methods;

import nuparu.ni.CodeBlock;
import nuparu.ni.Token;
import nuparu.ni.Value;
import nuparu.ni.Value.EnumValueType;
import nuparu.ni.exception.EvaluationErrorException;
import nuparu.sevendaystomine.util.Tree;

public class MethodPow extends Method {

	public MethodPow() {
		super("pow");
		addArgument("base", EnumValueType.NUMERICAL);
		addArgument("exp", EnumValueType.NUMERICAL);
	}

	@Override
	public Value trigger(Tree<Token> method, CodeBlock codeBlock) throws EvaluationErrorException {

		Value base = this.getArgumentValue(0, method, codeBlock);
		if (!base.isNumerical()) {
			codeBlock.printError(name + "() expected a number, but did not find any");
			return null;
		}

		Value exp = this.getArgumentValue(1, method, codeBlock);
		if (!exp.isNumerical()) {
			codeBlock.printError(name + "() expected a number, but did not find any");
			return null;
		}

		return new Value(
				(double) Math.pow((double) base.asDouble().getRealValue(), (double) exp.asDouble().getRealValue()));
	}

}
