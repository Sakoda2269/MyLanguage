package parser.dataTypes;

import java.util.ArrayList;
import java.util.List;

import parser.ParseException;

public class If extends Blocks {
	
	private Expression ifExpression;
	private Block ifBlock;
	
	private List<Expression> elifExpressions = new ArrayList<>();
	private List<Block> elifBlocks = new ArrayList<>();
	
	private Block elseBlock;
	
	public If(Expression expr, Block block) {
		ifExpression = expr;
		ifBlock = block;
	}
	
	public void addElif(Expression expr, Block block) {
		elifExpressions.add(expr);
		elifBlocks.add(block);
	}
	
	public void setElse(Block block) {
		elseBlock = block;
	}
	
	
	@Override
	public void excecute() throws ParseException {
		if(ifExpression.calc().getValue().equals("true")) {
			ifBlock.execute();
			return;
		}
		for(int i = 0; i < elifExpressions.size(); i++) {
			if(elifExpressions.get(i).calc().getValue().equals("true")) {
				elifBlocks.get(i).execute();
				return;
			}
		}
		if(elseBlock != null) {
			elseBlock.execute();
		}
	}

}
