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
			System.out.println("FFFF");
			return new Value((int) getRealValue() + (int) other.getRealValue());
		}
		if (isString() || other.isString()) {
			System.out.println("GGGG");
			return new Value(getRealValue().toString() + other.getRealValue().toString());
		}

		if (isBoolean() && other.isBoolean()) {
			System.out.println("CCCC");
			return new Value((boolean) getRealValue() || (boolean) other.getRealValue());
		} else {
			System.out.println("EEEEE");
			return new Value(getRealValue().toString() + other.getRealValue().toString());
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
			return new Value(-(int) getRealValue());
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
		return value;
	}

	public boolean isString() {
		return type == EnumValueType.STRING || (getRealValue() instanceof String);
	}

	public boolean isInt() {
		return type == EnumValueType.INT || (getRealValue() instanceof Integer);
	}

	public boolean isBoolean() {
		return type == EnumValueType.BOOL && (getRealValue() instanceof Boolean);
	}

	public boolean isNumerical() {
		return isInt();
	}

	@Override
	public String toString() {
		return "[Value=" + (getRealValue() != null ? getRealValue().toString() : "null") + ", " + type + "]";
	}

	public enum EnumValueType {
		INT("int"), STRING("string"), BOOL("bool");

		String name;

		EnumValueType(String type) {
			this.name = type;
		}

		public static EnumValueType getByType(String s) {
			for (EnumValueType t : EnumValueType.values()) {
				if (t.name.equals(s))
					return t;
			}
			return null;
		}

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
		return getRealValue().equals(other.getRealValue());
	}

	public boolean isLessThan(Value other) {
		if (!isNumerical() || !other.isNumerical())
			return false;

		return (int) getRealValue() < (int) other.getRealValue();
	}

	public boolean isMoreThan(Value other) {
		if (!isNumerical() || !other.isNumerical())
			return false;

		return (int) getRealValue() > (int) other.getRealValue();
	}
}
