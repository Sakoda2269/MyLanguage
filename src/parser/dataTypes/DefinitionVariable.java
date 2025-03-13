package parser.dataTypes;

import parser.ParseException;

public class DefinitionVariable implements Program {
	
	private DefinitionVariableType defVarType;
	private Expression expression;

	public DefinitionVariable(DefinitionVariableType defVar, Expression expression) {
		this.defVarType = defVar;
		this.expression = expression;
	}
	
	@Override
	public void excecute() throws ParseException {
		if(expression != null) {
			Variable variable = defVarType.getVariable();
			variable.setType(defVarType.getType());
			variable.set(expression.calc());
		}
	}

}
