package com.nuparu.ni.exception;

import com.nuparu.ni.Value;
import com.nuparu.ni.Variable;

@SuppressWarnings("serial")
public class TypeMismatchException extends Exception {

	public TypeMismatchException(Variable var, Object value) {
		super("The variable type of " + var.name + " can not store given value " + value);
	}
	
	public TypeMismatchException(Value value) {
		super("Value type of " + value.toString() + " does not meet expected value type");
	}
}
