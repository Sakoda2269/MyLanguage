package parser.dataTypes;

import parser.ParseException;

public class While extends Blocks {

	private Expression expression;
	private Block block;
	
	public While(Expression expr, Block block) {
		expression = expr;
		this.block = block;
	}
	
	@Override
	public void excecute() throws ParseException {
		while(expression.calc().getValue().equals("true")) {
			block.execute();
			if(Break.breaked) {
				Break.breaked = false;
				break;
			}
		}
	}

}
