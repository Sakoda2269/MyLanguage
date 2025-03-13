package parser.dataTypes;

import java.util.ArrayList;
import java.util.List;

import parser.ParseException;

public class Term {
	
	private List<Factor> factors = new ArrayList<>();
	private List<Character> operators = new ArrayList<>();
	
	public Term() {
		
	}
	
	public void addFacotr(Factor value) {
		factors.add(value);
	}
	
	public void addOperator(char ope) {
		operators.add(ope);
	}
	
	public Value calc() throws ParseException {
		Value leftValue = factors.get(0).calc();
		for(int i = 0; i < factors.size() - 1; i++) {
			Character operator = operators.get(i);
			Value rightValue = factors.get(i + 1).calc();
			Type leftType = leftValue.getType();
			String leftStr = leftValue.getValue();
			Type rightType = rightValue.getType();
			String rightStr = rightValue.getValue();
			switch(operator) {
			case '*':
				if(leftType.equals(Type.INT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Integer.parseInt(leftStr) * Integer.parseInt(rightStr))+"", Type.INT);
				} else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) * Double.parseDouble(rightStr))+"", Type.FLOAT);
				}  else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Double.parseDouble(leftStr) * Double.parseDouble(rightStr))+"", Type.FLOAT);
				} else if(leftType.equals(Type.INT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) * Double.parseDouble(rightStr))+"", Type.FLOAT);
				} else {
					throw new ParseException("operator error");
				}
				break;
			case '/':
				if(leftType.equals(Type.INT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Integer.parseInt(leftStr) / Integer.parseInt(rightStr))+"", Type.INT);
				} else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) / Double.parseDouble(rightStr))+"", Type.FLOAT);
				}  else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Double.parseDouble(leftStr) / Double.parseDouble(rightStr))+"", Type.FLOAT);
				} else if(leftType.equals(Type.INT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) / Double.parseDouble(rightStr))+"", Type.FLOAT);
				} else {
					throw new ParseException("operator error");
				}
				break;
			case '%':
				if(leftType.equals(Type.INT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Integer.parseInt(leftStr) % Integer.parseInt(rightStr))+"", Type.INT);
				} else {
					throw new ParseException("operator error");
				}
				break;
			}
		}
		return leftValue;
	}
	
}
