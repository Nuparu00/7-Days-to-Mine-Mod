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

	public Value(double value) {
		this(EnumValueType.DOUBLE, value);
	}

	public Value(String value) {
		this(EnumValueType.STRING, value);
	}

	public Value(boolean value) {
		this(EnumValueType.BOOL, value);
	}
	
	public Value(float value) {
		this(EnumValueType.FLOAT, value);
	}
	
	public Value(long value) {
		this(EnumValueType.LONG, value);
	}

	public Value(short value) {
		this(EnumValueType.SHORT, value);
	}
	
	public Value(byte value) {
		this(EnumValueType.BYTE, value);
	}
	
	
	public Value add(Value other) {
		if (isNumerical() && other.isNumerical()) {
			if (isDouble()) {
				return new Value((double) getRealValue() + (double) other.asDouble().getRealValue());
			}
			if (isFloat()) {
				return new Value((float) getRealValue() + (float) other.asFloat().getRealValue());
			}
			if (isLong()) {
				return new Value((long) getRealValue() + (long) other.asLong().getRealValue());
			}
			if (isShort()) {
				return new Value((short) getRealValue() + (short) other.asShort().getRealValue());
			}
			if (isInt()) {
				return new Value((int) getRealValue() + (int) other.asInt().getRealValue());
			}
			if (isByte()) {
				return new Value((byte) getRealValue() + (byte) other.asByte().getRealValue());
			}
		}
		if (isString() || other.isString()) {
			return new Value(getRealValue().toString() + other.getRealValue().toString());
		}

		if (isBoolean() && other.isBoolean()) {
			return new Value((boolean) getRealValue() || (boolean) other.getRealValue());
		} else {
			return new Value(getRealValue().toString() + other.getRealValue().toString());
		}
	}

	public Value substract(Value other) {
		if (isNumerical() && other.isNumerical()) {
			if (isDouble()) {
				return new Value((double) getRealValue() - (double) other.asDouble().getRealValue());
			}
			if (isFloat()) {
				return new Value((float) getRealValue() - (float) other.asFloat().getRealValue());
			}
			if (isLong()) {
				return new Value((long) getRealValue() - (long) other.asLong().getRealValue());
			}
			if (isShort()) {
				return new Value((short) getRealValue() - (short) other.asShort().getRealValue());
			}
			if (isInt()) {
				return new Value((int) getRealValue() - (int) other.asInt().getRealValue());
			}
			if (isByte()) {
				return new Value((byte) getRealValue() - (byte) other.asByte().getRealValue());
			}
		}
		return null;
	}

	public Value multiply(Value other) {
		if (isNumerical() && other.isNumerical()) {
			if (isDouble()) {
				return new Value((double) getRealValue() * (double) other.asDouble().getRealValue());
			}
			if (isFloat()) {
				return new Value((float) getRealValue() * (float) other.asFloat().getRealValue());
			}
			if (isLong()) {
				return new Value((long) getRealValue() * (long) other.asLong().getRealValue());
			}
			if (isShort()) {
				return new Value((short) getRealValue() * (short) other.asShort().getRealValue());
			}
			if (isInt()) {
				return new Value((int) getRealValue() * (int) other.asInt().getRealValue());
			}
			if (isByte()) {
				return new Value((byte) getRealValue() * (byte) other.asByte().getRealValue());
			}
		}
		if (isBoolean() && other.isBoolean()) {
			return new Value((boolean) getRealValue() && (boolean) other.getRealValue());
		}
		return null;
	}

	public Value opposite() {
		if (isNumerical()) {
			if (isDouble()) {
				return new Value(-(double) getRealValue());
			}
			if (isFloat()) {
				return new Value(-(float) getRealValue());
			}
			if (isLong()) {
				return new Value(-(long) getRealValue());
			}
			if (isShort()) {
				return new Value(-(short) getRealValue());
			}
			if (isInt()) {
				return new Value(-(int) getRealValue());
			}
			if (isByte()) {
				return new Value(-(byte) getRealValue());
			}
		}

		if (isBoolean()) {
			return new Value(!(boolean) getRealValue());
		}
		return null;
	}

	public Value divide(Value other) {
		if (isNumerical() && other.isNumerical()) {
			if (isDouble()) {
				return new Value((double) getRealValue() / (double) other.getRealValue());
			}
			if (isFloat()) {
				return new Value((float) getRealValue() / (float) other.getRealValue());
			}
			if (isLong()) {
				return new Value((long) getRealValue() / (long) other.getRealValue());
			}
			if (isShort()) {
				return new Value((short) getRealValue() / (short) other.getRealValue());
			}
			if (isInt()) {
				return new Value((int) getRealValue() / (int) other.getRealValue());
			}
			if (isByte()) {
				return new Value((byte) getRealValue() / (byte) other.getRealValue());
			}
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
			if(value instanceof Number) {
				return ((Number)value).intValue();
			}
			return (int) value;
		}
		if (type == EnumValueType.BOOL) {
			return (boolean) value;
		}
		if (type == EnumValueType.DOUBLE) {
			if(value instanceof Number) {
				return ((Number)value).doubleValue();
			}
			return (double) value;
		}
		if (type == EnumValueType.FLOAT) {
			if(value instanceof Number) {
				return ((Number)value).floatValue();
			}
			return (float) value;
		}
		if (type == EnumValueType.LONG) {
			if(value instanceof Number) {
				return ((Number)value).longValue();
			}
			return (long) value;
		}
		if (type == EnumValueType.SHORT) {
			if(value instanceof Number) {
				return ((Number)value).shortValue();
			}
			return (short) value;
		}
		if (type == EnumValueType.BYTE) {
			if(value instanceof Number) {
				return ((Number)value).byteValue();
			}
			return (byte) value;
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

	public boolean isDouble() {
		return type == EnumValueType.DOUBLE || (getRealValue() instanceof Double);
	}
	
	public boolean isFloat() {
		return type == EnumValueType.FLOAT || (getRealValue() instanceof Float);
	}
	
	public boolean isLong() {
		return type == EnumValueType.LONG || (getRealValue() instanceof Long);
	}
	
	public boolean isShort() {
		return type == EnumValueType.LONG || (getRealValue() instanceof Short);
	}
	
	public boolean isByte() {
		return type == EnumValueType.LONG || (getRealValue() instanceof Byte);
	}

	public boolean isNumerical() {
		return isInt() || isDouble() || isFloat() || isLong() || isShort() || isByte();
	}
	
	public Value asDouble() {
		if(value instanceof Number) {
			return new Value(((Number)value).doubleValue());
		}
		return this;
	}
	
	public Value asInt() {
		if(value instanceof Number) {
			return new Value(((Number)value).intValue());
		}
		return this;
	}
	
	public Value asFloat() {
		if(value instanceof Number) {
			return new Value(((Number)value).floatValue());
		}
		return this;
	}
	
	public Value asLong() {
		if(value instanceof Number) {
			return new Value(((Number)value).longValue());
		}
		return this;
	}
	
	public Value asShort() {
		if(value instanceof Number) {
			return new Value(((Number)value).shortValue());
		}
		return this;
	}
	
	public Value asByte() {
		if(value instanceof Number) {
			return new Value(((Number)value).byteValue());
		}
		return this;
	}

	@Override
	public String toString() {
		return "[Value=" + (getRealValue() != null ? getRealValue().toString() : "null") + ", " + type + " , real_type=" + (value != null ? value.getClass().getCanonicalName() : "null")+"]";
	}

	public enum EnumValueType {
		INT("int"), STRING("string"), BOOL("bool"), DOUBLE("double"), FLOAT("float"), LONG("long"), BYTE("byte"), SHORT("short");

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
