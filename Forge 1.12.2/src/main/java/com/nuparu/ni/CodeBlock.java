package com.nuparu.ni;

import java.util.HashMap;
import java.util.List;

import com.nuparu.ni.exception.TypeMismatchException;
import com.nuparu.ni.exception.VariableNotFoundException;

public class CodeBlock {

	public CodeBlock parent = null;
	public HashMap<String, Variable> variables = new HashMap<String, Variable>();
	public List<Statement> statements;
	public HashMap<CodeBlock,Condition> codeBlocks;

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
		Variable var = getVariable(name);
		if(var != null) {
			var.setValue(value);
			return true;
		}else {
			if(parent != null) {
				if(parent.setVariableValue(name, value)) {
					return true;
				}
			}
			throw new VariableNotFoundException(name);
		}
	}

	public boolean hasVariable(String name) {
		return this.variables.containsKey(name);
	}

	public Variable getVariable(String name) {
		return this.variables.get(name);
	}
}
