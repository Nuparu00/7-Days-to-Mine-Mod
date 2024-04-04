package nuparu.ni.methods;

import nuparu.ni.CodeBlock;
import nuparu.ni.Token;
import nuparu.ni.Value;
import nuparu.ni.Value.EnumValueType;
import nuparu.ni.exception.EvaluationErrorException;
import nuparu.sevendaystomine.util.Tree;

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
