package com.nuparu.ni;

public enum EnumTokenType {

	NONE("none",0xbbbbbb),
	START("start",0xbbbbbb),
	TYPE("type",0xCC6C1D),
	KEYWORD("keyword",0xCC6C1D),
	IDENTIFIER("identifier",0xF3EC79),
	COMMENT("comment",0x808080),
	OPERATOR("operator",0xbbbbbb),
	PARENTHESES("parentheses",0xbbbbbb),
	BRACKET("bracket",0xbbbbbb),
	VALUE("value",0xffff00),
	SEMICOLON("semicolon",0xbbbbbb),
	METHOD("method",0x6FCF21);
	
	String name;
	int color;
	EnumTokenType(String name, int color) {
		this.name = name;
		this.color = color;
	}
	public int getColor() {
		return color;
	}
}
