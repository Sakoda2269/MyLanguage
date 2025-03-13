package parser.dataTypes;

import parser.ParseException;

public class Factor {

	private Value value;
	
	public Factor(Value value) {
		this.value = value;
	}
	
	public Value calc() throws ParseException {
		return value.calc();
	}
	
}
