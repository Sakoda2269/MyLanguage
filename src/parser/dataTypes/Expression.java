package parser.dataTypes;

import java.util.ArrayList;
import java.util.List;

import parser.ParseException;

public class Expression extends Value{
	
	private List<Term> terms = new ArrayList<>();
	private List<String> operators = new ArrayList<>();
	
	public void addTerm(Term term) {
		terms.add(term);
	}
	
	public void addOperator(String ope) {
		operators.add(ope);
	}

	@Override
	public Value calc() throws ParseException {
		Value leftValue = terms.get(0).calc();
		for(int i = 0; i < terms.size() - 1; i++) {
			String operator = operators.get(i);
			Value rightValue = terms.get(i + 1).calc();
			Type leftType = leftValue.getType();
			String leftStr = leftValue.getValue();
			Type rightType = rightValue.getType();
			String rightStr = rightValue.getValue();
			switch(operator) {
			case "+":
				if(leftType.equals(Type.INT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Integer.parseInt(leftStr) + Integer.parseInt(rightStr))+"", Type.INT);
				} else if(leftType.equals(Type.STR) && rightType.equals(Type.STR)) {
					leftValue = new Value(leftStr + rightStr, Type.STR);
				} else if(leftType.equals(Type.STR) && rightType.equals(Type.INT)) {
					leftValue = new Value(leftStr + rightStr, Type.STR);
				} else if(leftType.equals(Type.INT) && rightType.equals(Type.STR)) {
					leftValue = new Value(leftStr + rightStr, Type.STR);
				} else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) + Double.parseDouble(rightStr))+"", Type.FLOAT);
				}  else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Double.parseDouble(leftStr) + Double.parseDouble(rightStr))+"", Type.FLOAT);
				} else if(leftType.equals(Type.INT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) + Double.parseDouble(rightStr))+"", Type.FLOAT);
				} else {
					throw new ParseException("operator error");
				}
				break;
			case "-":
				if(leftType.equals(Type.INT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Integer.parseInt(leftStr) - Integer.parseInt(rightStr))+"", Type.INT);
				} else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) - Double.parseDouble(rightStr))+"", Type.FLOAT);
				}  else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Double.parseDouble(leftStr) - Double.parseDouble(rightStr))+"", Type.FLOAT);
				} else if(leftType.equals(Type.INT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) - Double.parseDouble(rightStr))+"", Type.FLOAT);
				} else {
					throw new ParseException("operator error");
				}
				break;
			case "==":
				if(leftType.equals(rightType)) {
					if(leftStr.equals(rightStr)) {
						leftValue = new Value("true", Type.BOOL);
					} else {
						leftValue = new Value("false", Type.BOOL);
					}
				} else {
					throw new ParseException("operator error");
				}
				break;
			case "!=":
				if(leftType.equals(rightType)) {
					if(!leftStr.equals(rightStr)) {
						leftValue = new Value("true", Type.BOOL);
					} else {
						leftValue = new Value("false", Type.BOOL);
					}
				} else {
					throw new ParseException("operator error");
				}
				break;
			case "<":
				if(leftType.equals(Type.INT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Integer.parseInt(leftStr) < Integer.parseInt(rightStr))+"", Type.BOOL);
				} else if(leftType.equals(Type.STR) && rightType.equals(Type.STR)) {
					leftValue = new Value((leftStr.compareTo(rightStr) < 0) + "", Type.BOOL);
				} else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) < Double.parseDouble(rightStr))+"", Type.BOOL);
				}  else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Double.parseDouble(leftStr) < Double.parseDouble(rightStr))+"", Type.BOOL);
				} else if(leftType.equals(Type.INT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) < Double.parseDouble(rightStr))+"", Type.BOOL);
				} else {
					throw new ParseException("operator error");
				}
				break;
			case "<=":
				if(leftType.equals(Type.INT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Integer.parseInt(leftStr) <= Integer.parseInt(rightStr))+"", Type.BOOL);
				} else if(leftType.equals(Type.STR) && rightType.equals(Type.STR)) {
					leftValue = new Value((leftStr.compareTo(rightStr) <= 0) + "", Type.BOOL);
				} else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) <= Double.parseDouble(rightStr))+"", Type.BOOL);
				}  else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Double.parseDouble(leftStr) <= Double.parseDouble(rightStr))+"", Type.BOOL);
				} else if(leftType.equals(Type.INT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) <= Double.parseDouble(rightStr))+"", Type.BOOL);
				} else {
					throw new ParseException("operator error");
				}
				break;
			case ">":
				if(leftType.equals(Type.INT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Integer.parseInt(leftStr) > Integer.parseInt(rightStr))+"", Type.BOOL);
				} else if(leftType.equals(Type.STR) && rightType.equals(Type.STR)) {
					leftValue = new Value((leftStr.compareTo(rightStr) > 0) + "", Type.BOOL);
				} else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) > Double.parseDouble(rightStr))+"", Type.BOOL);
				}  else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Double.parseDouble(leftStr) > Double.parseDouble(rightStr))+"", Type.BOOL);
				} else if(leftType.equals(Type.INT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) > Double.parseDouble(rightStr))+"", Type.BOOL);
				} else {
					throw new ParseException("operator error");
				}
				break;
			case ">=":
				if(leftType.equals(Type.INT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Integer.parseInt(leftStr) >= Integer.parseInt(rightStr))+"", Type.BOOL);
				} else if(leftType.equals(Type.STR) && rightType.equals(Type.STR)) {
					leftValue = new Value((leftStr.compareTo(rightStr) >= 0) + "", Type.BOOL);
				} else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) >= Double.parseDouble(rightStr))+"", Type.BOOL);
				}  else if(leftType.equals(Type.FLOAT) && rightType.equals(Type.INT)) {
					leftValue = new Value((Double.parseDouble(leftStr) >= Double.parseDouble(rightStr))+"", Type.BOOL);
				} else if(leftType.equals(Type.INT) && rightType.equals(Type.FLOAT)) {
					leftValue = new Value((Double.parseDouble(leftStr) >= Double.parseDouble(rightStr))+"", Type.BOOL);
				} else {
					throw new ParseException("operator error");
				}
				break;
			case "&&":
				if(leftType.equals(Type.BOOL) && rightType.equals(Type.BOOL)) {
					leftValue = new Value((Boolean.parseBoolean(leftStr) && Boolean.parseBoolean(rightStr))+"", Type.BOOL);
				} else {
					throw new ParseException("operator error");
				}
				break;
			case "||":
				if(leftType.equals(Type.BOOL) || rightType.equals(Type.BOOL)) {
					leftValue = new Value((Boolean.parseBoolean(leftStr) || Boolean.parseBoolean(rightStr))+"", Type.BOOL);
				} else {
					throw new ParseException("operator error");
				}
				break;
			}
		}
		return leftValue;
	}

}
