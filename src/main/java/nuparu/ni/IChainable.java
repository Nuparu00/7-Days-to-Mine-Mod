package nuparu.ni;

import nuparu.ni.exception.EvaluationErrorException;

public interface IChainable {

	public boolean hasValue();
	public Value evaluate() throws EvaluationErrorException;
}
