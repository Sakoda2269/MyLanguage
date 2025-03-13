package parser.dataTypes;

import java.util.HashMap;
import java.util.Map;

import parser.ParseException;

public class Variable extends Value{

	private String name;
	
	private static Map<String, Variable> variables = new HashMap<>();
	
	public Variable(String name) {
		this.name = name;
		variables.put(name, this);
	}
	
	public static Variable getVariable(String name) {
		if(variables.containsKey(name)) {
			return variables.get(name);
		} else {
			return new Variable(name);
		}
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public void set(Value value) throws ParseException {
		if(this.type != value.getType()) {
			throw new ParseException("different type cannot assign");
		}
		this.value = value.getValue();
		variables.put(name, this);
	}
	
	@Override
	public Value calc() {
		return this;
	}

}
