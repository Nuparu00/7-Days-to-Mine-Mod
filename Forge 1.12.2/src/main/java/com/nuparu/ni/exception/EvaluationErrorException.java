package com.nuparu.ni.exception;

import com.nuparu.ni.IChainable;
import com.nuparu.ni.Statement;

@SuppressWarnings("serial")
public class EvaluationErrorException extends Exception{

	public EvaluationErrorException(Statement statement, IChainable chainable) {
		super("An error occured while trying to evaluate Statement " + statement.toString() + " for element " +  chainable.toString());
	}
	
	public EvaluationErrorException(Statement statement) {
		super("An error occured while trying to evaluate Statement " + statement.toString());
	}
}
