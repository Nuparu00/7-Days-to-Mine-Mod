package com.nuparu.ni;

import com.nuparu.ni.exception.EvaluationErrorException;
import com.nuparu.ni.exception.TypeMismatchException;

public class Condition {

	public Statement statement;
	
	public Condition(Statement statement) {
		this.statement = statement;
	}
	
	public Value evaluate() throws EvaluationErrorException, TypeMismatchException {
		Value value = statement.evaluate();
		if(!value.isBoolean()) {
			throw new TypeMismatchException(value);
		}
		return value;
	}
}
