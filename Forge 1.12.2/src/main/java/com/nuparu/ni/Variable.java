package com.nuparu.ni;

import com.nuparu.ni.exception.TypeMismatchException;

public class Variable {
	public String type;
	public String name;
	public CodeBlock codeBlock;
	private Object value;

	public Variable(String type, Object value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public String toString() {
		return "[Variable name=" + name + ",type=" + type + ",value=" + value + "]";
	}

	public void setValue(Object value) throws TypeMismatchException {
		switch (type) {
		case "int": {
			if (value instanceof Integer) {
				this.value = value;
				return;
			}
			if (value instanceof String) {
				try {
					this.value = Integer.parseInt((String) value);
					return;
				} catch (Exception e) {
					throw new TypeMismatchException(this, value);
				}
			}
			throw new TypeMismatchException(this, value);
		}
		case "boolean": {
			if (value instanceof Boolean) {
				this.value = value;
				return;
			}
			if (value.equals("true") || value.equals(false)) {
				this.value = value;
				return;
			}
			throw new TypeMismatchException(this, value);
		}
		case "string": {
			if (value instanceof String) {
				this.value = value;
				return;
			}
			throw new TypeMismatchException(this, value);
		}
		}
	}

	public Object getValue() {
		return value;
	}
}
