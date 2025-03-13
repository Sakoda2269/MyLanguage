package parser.dataTypes;

import parser.ParseException;

public class Value {
	
	protected String value;
	protected Type type;
	public Value() {}
	
	public Value(String value, Type type) {
		this.value = value;
		this.type = type;
	}
	
	public Value calc() throws ParseException {
		return this;
	}
	
	public String getValue() {
		return value;
	}
	
	public Type getType() {
		return type;
	}
	
}
