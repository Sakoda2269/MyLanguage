package parser.dataTypes;

import parser.ParseException;

public class For extends Blocks {

	private DefinitionVariable defVar;
	private Expression expression;
	private Assignment assign;
	private Block block;
	
	public For(DefinitionVariable def, Expression expr, Assignment assign, Block block) {
		defVar = def;
		expression = expr;
		this.assign = assign; 
		this.block = block;
	}
	
	@Override
	public void excecute() throws ParseException {
		defVar.excecute();
		while(expression.calc().getValue().equals("true")) {
			block.execute();
			if(Break.breaked) {
				Break.breaked = false;
				break;
			}
			assign.excecute();
		}
	}

}
