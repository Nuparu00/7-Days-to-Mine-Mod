package nuparu.ni.exception;

import nuparu.ni.Value;
import nuparu.ni.Variable;

@SuppressWarnings("serial")
public class TypeMismatchException extends Exception {

	public TypeMismatchException(Variable var, Object value) {
		super("The variable" + var.name + " type of " + var.type + " can not store given value " + value);
	}
	
	public TypeMismatchException(Value value) {
		super("Value type of " + value.toString() + " does not meet expected value type");
	}
}
