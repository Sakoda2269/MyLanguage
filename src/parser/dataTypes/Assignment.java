package parser.dataTypes;

import parser.ParseException;

public class Assignment implements Program {
	
	private Variable variable;
	private Expression expression;
	
	public Assignment(Variable var, Expression expr) {
		variable = var;
		expression = expr;
	}
	
	@Override
	public void excecute() throws ParseException {
		variable.set(expression.calc());
	}

}
