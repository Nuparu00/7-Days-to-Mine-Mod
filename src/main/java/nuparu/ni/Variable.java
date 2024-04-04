package nuparu.ni;

public class Variable extends Value{

	public String name;

	public Variable(EnumValueType type, Object value) {
		super(type,value);
	}

	public Variable(int value) {
		this(EnumValueType.INT, value);
	}

	public Variable(String value) {
		this(EnumValueType.STRING, value);
	}

	public Variable(boolean value) {
		this(EnumValueType.BOOL, value);
	}
}
