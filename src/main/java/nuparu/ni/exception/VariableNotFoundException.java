package nuparu.ni.exception;

@SuppressWarnings("serial")
public class VariableNotFoundException extends Exception {

	public VariableNotFoundException(String name) {
		super("Could not find a varaible with give name: " + name);
	}
}
