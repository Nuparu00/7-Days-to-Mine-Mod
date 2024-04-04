package nuparu.ni.methods;

import nuparu.ni.CodeBlock;
import nuparu.ni.Token;
import nuparu.ni.Value;
import nuparu.ni.exception.EvaluationErrorException;
import nuparu.sevendaystomine.util.Tree;

public class MethodGetWorldTicks extends Method {

	public MethodGetWorldTicks() {
		super("getWorldTicks");
	}

	@Override
	public Value trigger(Tree<Token> method, CodeBlock codeBlock) throws EvaluationErrorException {
		return new Value(codeBlock.process.getTE().getWorld().getWorldTime());
	}

}
