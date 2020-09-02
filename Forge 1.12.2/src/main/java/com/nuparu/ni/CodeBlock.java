package com.nuparu.ni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nuparu.ni.exception.TypeMismatchException;
import com.nuparu.ni.exception.VariableNotFoundException;

public class CodeBlock {

	public CodeBlock parent = null;
	public HashMap<String, Variable> variables = new HashMap<String, Variable>();
	public List<Statement> statements;
	public HashMap<CodeBlock, Condition> codeBlocks;

	public List<String> output = new ArrayList<String>();

	public boolean addVariable(String name, String type, Object value) {
		Variable var = new Variable(type, value);
		var.name = name;
		var.codeBlock = this;
		variables.put(name, var);
		return true;
	}

	public boolean addVariable(String name, String type) {
		return this.addVariable(name, type, null);
	}

	public boolean setVariableValue(String name, Object value) throws VariableNotFoundException, TypeMismatchException {
		if (this.variables.containsKey(name)) {
			getVariable(name).setValue(value);
			return true;
		} else {
			if (parent != null && parent.setVariableValue(name, value)) {
				return true;
			}
		}
		throw new VariableNotFoundException(name);
	}

	public boolean existsVarInContext(String name) {
		if (this.variables.containsKey(name))
			return true;
		if (this.parent != null) {
			return this.parent.existsVarInContext(name);
		}
		return false;
	}

	public Variable getVariable(String name) {
		if (this.variables.containsKey(name)) {
			return variables.get(name);
		}
		if (this.parent != null) {
			return this.parent.getVariable(name);
		}
		return null;
	}

	public void print(Object s) {
		if (parent != null) {
			parent.print(s);
		} else {
			output.add(s.toString());
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeBlocks == null) ? 0 : codeBlocks.hashCode());
		result = prime * result + ((output == null) ? 0 : output.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((statements == null) ? 0 : statements.hashCode());
		result = prime * result + ((variables == null) ? 0 : variables.hashCode());
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
		CodeBlock other = (CodeBlock) obj;
		if (codeBlocks == null) {
			if (other.codeBlocks != null)
				return false;
		} else if (!codeBlocks.equals(other.codeBlocks))
			return false;
		if (output == null) {
			if (other.output != null)
				return false;
		} else if (!output.equals(other.output))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (statements == null) {
			if (other.statements != null)
				return false;
		} else if (!statements.equals(other.statements))
			return false;
		if (variables == null) {
			if (other.variables != null)
				return false;
		} else if (!variables.equals(other.variables))
			return false;
		return true;
	}
}
