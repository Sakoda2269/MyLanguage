package parser.dataTypes;

public class Constant extends Value{
	
	public Constant(Type type, String value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public Value calc() {
		return this;
	}

}
