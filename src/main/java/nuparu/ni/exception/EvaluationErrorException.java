package nuparu.ni.exception;

import nuparu.ni.IChainable;
import nuparu.ni.Statement;

@SuppressWarnings("serial")
public class EvaluationErrorException extends Exception{

	public EvaluationErrorException(Statement statement, IChainable chainable) {
		super("An error occured while trying to evaluate Statement " + statement.toString() + " for element " +  chainable.toString());
	}
	
	public EvaluationErrorException(Statement statement) {
		super("An error occured while trying to evaluate Statement " + statement.toString());
	}
}
