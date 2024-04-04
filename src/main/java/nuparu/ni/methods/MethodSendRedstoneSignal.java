package nuparu.ni.methods;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import nuparu.ni.CodeBlock;
import nuparu.ni.Token;
import nuparu.ni.Value;
import nuparu.ni.Value.EnumValueType;
import nuparu.ni.exception.EvaluationErrorException;
import nuparu.sevendaystomine.util.Tree;

public class MethodSendRedstoneSignal extends Method {

	public MethodSendRedstoneSignal() {
		super("sendSignal");
		addArgument("strength", EnumValueType.INT);
		addArgument("facing", EnumValueType.STRING);
	}

	@Override
	public Value trigger(Tree<Token> method, CodeBlock codeBlock) throws EvaluationErrorException {

		Value argStrength = this.getArgumentValue(0, method, codeBlock);
		if (!argStrength.isNumerical())
			return null;
		Value argFacing = this.getArgumentValue(3, method, codeBlock);
		if (!argFacing.isString())
			return null;

		String f = argFacing.getRealValue().toString();
		EnumFacing facing = EnumFacing.NORTH;

		switch (f) {
		case "north":
			facing = EnumFacing.NORTH;
			break;
		case "south":
			facing = EnumFacing.SOUTH;
			break;
		case "east":
			facing = EnumFacing.EAST;
			break;
		case "west":
			facing = EnumFacing.WEST;
			break;
		}

		codeBlock.process.sendRedstoneSignal(facing, (int)argStrength.asInt().getRealValue());

		return null;
	}

}
