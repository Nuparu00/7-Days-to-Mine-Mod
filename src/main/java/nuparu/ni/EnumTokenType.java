package nuparu.ni;

public enum EnumTokenType {

	NONE("none", 0xbbbbbb),
	START("start", 0xbbbbbb, "type", "identifier", "keyword", "method", "bracket"),
	TYPE("type", 0xCC6C1D, "identifier"), 
	KEYWORD("keyword", 0xCC6C1D,"parentheses", "bracket"),
	IDENTIFIER("identifier", 0xF3EC79, "operator", "parentheses", "semicolon", "comma"),
	COMMENT("comment", 0x808080),
	OPERATOR("operator", 0xbbbbbb, "identifier","value","parentheses","operator","semicolon","method"),
	PARENTHESES("parentheses", 0xbbbbbb,"identifier","value","semicolon","bracket","operator","parentheses","method","comma"),
	BRACKET("bracket", 0xbbbbbb,"identifier","value","type","semicolon","parentheses","bracket","keyword","method"),
	VALUE("value", 0xffff00,"operator","parentheses","semicolon","comma"),
	SEMICOLON("semicolon", 0xbbbbbb, "identifier", "type", "keyword","method", "bracket"),
	COMMA("comma", 0xbbbbbb, "identifier", "value", "parentheses"),
	METHOD("method", 0x6FCF21,"parentheses");

	String name;
	int color;
	String[] allowedFollowingTypes;

	EnumTokenType(String name, int color, String... allowed) {
		this.name = name;
		this.color = color;
	}

	public int getColor() {
		return color;
	}
}
