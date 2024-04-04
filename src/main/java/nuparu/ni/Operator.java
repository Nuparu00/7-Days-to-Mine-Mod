package nuparu.ni;

import nuparu.ni.exception.EvaluationErrorException;

public class Operator implements IChainable{

	public EnumTokenType type;
	public String value;
	
	public Operator(EnumTokenType type, String value) {
		this.type = type;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "[Operator=" + value +  "]";
	}

	@Override
	public Value evaluate() throws EvaluationErrorException {
		return null;
	}
	@Override
	public boolean hasValue() {
		return false;
	}
}
