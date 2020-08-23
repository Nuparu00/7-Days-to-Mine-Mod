package com.nuparu.ni;

public class Token {

	public EnumTokenType type = EnumTokenType.NONE;
	public Object value;

	public Token(EnumTokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public String toString() {
		return "[Type=" + type.name + ", Value \"" + (value != null ? value.toString() : "null") + "\"]";
	}
}
