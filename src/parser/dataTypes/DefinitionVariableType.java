package parser.dataTypes;

public class DefinitionVariableType {
	
	private Type type;
	private Variable variable;
	
	public DefinitionVariableType(Type type, Variable variable) {
		this.type = type;
		this.variable = variable;
	}
	
	public Type getType() {
		return type;
	}
	
	public Variable getVariable() {
		return variable;
	}
	
}
