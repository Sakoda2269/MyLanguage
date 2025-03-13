package parser.dataTypes;

import main.Main;
import parser.ParseException;

public class CallFunction extends Value implements Program {

	private String functionName;
	private Arguments args;
	
	public CallFunction(String name, Arguments args) {
		functionName = name;
		this.args = args;
	}
	
	@Override
	public void excecute() throws ParseException {
		funcs();
	}

	@Override
	public Value calc() throws ParseException {
		return funcs();
	}

	private Value funcs() throws ParseException {
		switch(functionName) {
		case "print":
			String s = "";
			for(Expression expr : args.getArgs()) {
				System.out.print(s + expr.calc().getValue());
				s = " ";
			}
			System.out.println();
			break;
		case "input":
			return new Value(Main.sc.next(), Type.STR);
		case "toInt":
			if(args.getArgs().size() != 1) {
				throw new ParseException("toInt only takes one argument");
			} 
			Expression arg = args.getArgs().get(0);
			Value v = arg.calc();
			if(!v.getType().equals(Type.STR)) {
				throw new ParseException("toInt arg must be str");
			}
			return new Value(Integer.parseInt(v.getValue()) + "", Type.INT);
		}
		return new Value("0", Type.INT);
	}
	
}
