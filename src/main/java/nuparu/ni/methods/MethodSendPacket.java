package nuparu.ni.methods;

import net.minecraft.util.math.BlockPos;
import nuparu.ni.CodeBlock;
import nuparu.ni.Token;
import nuparu.ni.Value;
import nuparu.ni.Value.EnumValueType;
import nuparu.ni.exception.EvaluationErrorException;
import nuparu.sevendaystomine.util.Tree;

public class MethodSendPacket extends Method {

	public MethodSendPacket() {
		super("sendPacket");
		addArgument("x", EnumValueType.INT);
		addArgument("y", EnumValueType.INT);
		addArgument("z", EnumValueType.INT);
		addArgument("packet", EnumValueType.STRING);
	}

	@Override
	public Value trigger(Tree<Token> method, CodeBlock codeBlock) throws EvaluationErrorException {

		Value argX = this.getArgumentValue(0, method, codeBlock);
		if(!argX.isNumerical()) return null;
		Value argY = this.getArgumentValue(1, method, codeBlock);
		if(!argY.isNumerical()) return null;
		Value argZ = this.getArgumentValue(2, method, codeBlock);
		if(!argZ.isNumerical()) return null;
		
		Value argPacket = this.getArgumentValue(3, method, codeBlock);
		if(!argPacket.isString()) return null;
		
		BlockPos to = new BlockPos((int)argX.asInt().getRealValue(),(int)argY.asInt().getRealValue(),(int)argZ.asInt().getRealValue());
		String packet = argPacket.getRealValue().toString();
		codeBlock.process.sendPacket(packet, to);
		
		return null;
	}

}
