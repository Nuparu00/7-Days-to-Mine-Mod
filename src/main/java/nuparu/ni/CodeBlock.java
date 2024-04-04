package nuparu.ni;

import java.util.HashMap;
import java.util.List;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import nuparu.ni.exception.TypeMismatchException;
import nuparu.ni.exception.VariableNotFoundException;
import nuparu.sevendaystomine.computer.process.TransitProcess;

public class CodeBlock {

	public CodeBlock parent = null;
	public HashMap<String, Value> variables = new HashMap<String, Value>();
	public List<Statement> statements;
	public HashMap<CodeBlock, Condition> codeBlocks;

	public TransitProcess process;

	public CodeBlock(TransitProcess process) {
		this.process = process;
	}

	public boolean addVariable(String name, String type, Object value) {
		variables.put(name, new Value(Value.EnumValueType.getByType(type), value));
		return true;
	}

	public boolean addVariable(String name, String type) {
		return this.addVariable(name, type, null);
	}

	public boolean setVariableValue(String name, Object value) throws VariableNotFoundException, TypeMismatchException {
		if (this.variables.containsKey(name)) {
			Value val = getVariable(name);
			addVariable(name, val.type.name, value);
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

	public Value getVariable(String name) {
		if (this.variables.containsKey(name)) {
			return variables.get(name);
		}
		if (this.parent != null) {
			return this.parent.getVariable(name);
		}
		return null;
	}

	public void print(Object s, TextFormatting... formattings) {
		if (parent != null) {
			parent.print(s, formattings);
		} else {
			StringBuilder sb = new StringBuilder();
			for (TextFormatting formatting : formattings) {
				sb.append(formatting);
			}
			process.getOutput().add(new TextComponentString(sb.append(s.toString()).toString()));
		}
	}

	public void printError(Object s) {
		print(s, TextFormatting.RED);
	}

	public void printSuccess(Object s) {
		print(s, TextFormatting.GREEN);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeBlocks == null) ? 0 : codeBlocks.hashCode());
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
