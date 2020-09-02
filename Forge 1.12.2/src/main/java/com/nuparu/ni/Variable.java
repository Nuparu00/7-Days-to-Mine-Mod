package com.nuparu.ni;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeBlock == null) ? 0 : codeBlock.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
		if (codeBlock == null) {
			if (other.codeBlock != null)
				return false;
		} else if (!codeBlock.equals(other.codeBlock))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public Object getValue() {
		return value;
	}


}
