package nuparu.ni.methods;

import nuparu.ni.CodeBlock;
import nuparu.ni.Token;
import nuparu.ni.Value;
import nuparu.ni.Value.EnumValueType;
import nuparu.ni.exception.EvaluationErrorException;
import nuparu.sevendaystomine.util.Tree;

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
