package com.nuparu.ni;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.nuparu.ni.exception.EvaluationErrorException;

//Primitive value - int, string
public class Value implements IChainable {

	public EnumValueType type;
	@Nonnull
	public Object value;

	public Value(EnumValueType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Value(int value) {
		this(EnumValueType.INT, value);
	}

	public Value(String value) {
		this(EnumValueType.STRING, value);
	}

	public Value(boolean value) {
		this(EnumValueType.BOOL, value);
	}

	public Value add(Value other) {
		if (isInt() && other.isInt()) {
			return new Value((int) getRealValue() + (int) other.getRealValue());
		}
		if (isString() && other.isString()) {
			return new Value((String) getRealValue() + (String) other.getRealValue());
		}

		if (isBoolean() && other.isBoolean()) {
			return new Value((boolean) getRealValue() || (boolean) other.getRealValue());
		} else {
			return new Value((String) getRealValue() + (String) other.getRealValue());
		}
	}

	public Value substract(Value other) {
		if (isInt() && other.isInt()) {
			return new Value((int) getRealValue() - (int) other.getRealValue());
		}
		return null;
	}

	public Value multiply(Value other) {
		if (isInt() && other.isInt()) {
			return new Value((int) getRealValue() * (int) other.getRealValue());
		}

		if (isBoolean() && other.isBoolean()) {
			return new Value((boolean) getRealValue() && (boolean) other.getRealValue());
		}
		return null;
	}
	
	public Value opposite() {
		if (isInt()) {
			return new Value(-(int)getRealValue());
		}

		if (isBoolean()) {
			return new Value(!(boolean) getRealValue());
		}
		return null;
	}

	public Value divide(Value other) {
		if (isInt() && other.isInt()) {
			return new Value((int) ((int) getRealValue() / (int) other.getRealValue()));
		}
		return null;
	}

	@Nullable
	public Object getRealValue() {
		String s = "";

		if (type == EnumValueType.STRING) {
			return (String) value;
		}
		if (type == EnumValueType.INT) {
			return (int) value;
		}
		if (type == EnumValueType.BOOL) {
			return (boolean) value;
		}
		if (type == EnumValueType.VAR) {
			Variable var = (Variable) value;
			return var.getValue();
		}
		return value;
	}

	public boolean isString() {
		return getRealValue() instanceof String;
	}

	public boolean isInt() {
		return getRealValue() instanceof Integer;
	}

	public boolean isBoolean() {
		return getRealValue() instanceof Boolean;
	}
	
	public boolean isNumerical() {
		return isInt();
	}

	@Override
	public String toString() {
		return "[Value=" + getRealValue().toString() + ", " + type + "]";
	}

	public enum EnumValueType {
		INT, STRING, BOOL, VAR
	}

	@Override
	public Value evaluate() throws EvaluationErrorException {
		return this;
	}

	@Override
	public boolean hasValue() {
		return true;
	}
	
	public boolean equals(Value other) {
		if(this.type != other.type) return false;
		return value.equals(other.value);
	}
}
