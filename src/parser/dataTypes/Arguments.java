package parser.dataTypes;

import java.util.ArrayList;
import java.util.List;

public class Arguments {
	
	private List<Expression> args = new ArrayList<>();
	
	public Arguments() {
		
	}
	
	public void addArgs(Expression expr) {
		args.add(expr);
	}
	
	public List<Expression> getArgs() {
		return args;
	}
	
}
