package single;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Main {

	public static Scanner sc;
	
	public static void main(String[] args) {
		
		sc = new Scanner(System.in);
		
		String program = 
"""
str S = input();
for(int i = 1; i < length(S); i = i + 1) {
	int ans = 0;
	while(charAt(S, i) == "-"){ 
		ans = ans + 1;
		i = i + 1;
	}
	print2(ans);
	print2(" ");
}

""";
		Parser p = new Parser(program);
		try {
			p.programs().execute();
		} catch (ParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}

class Parser extends Source{
	
	public Parser(String str) {
		super(str);
	}
	
	public Programs programs() throws ParseException {
		Programs progs = new Programs();
		while (peek() != -1) {
			lfs();
			if((char) peek() == '}') {
				break;
			}
			if(isBlocks() < 0) {
				progs.addProgram(program());
				if((char) peek() != ';') {
					throw new ParseException("; was not found");
				}
				next();
			} else {
				progs.addProgram(blocks());
			}
			lfs();
		}
		
		return progs;
	}
	
	public Program program() throws ParseException {
		if(isType()) {
			return defVar();
		} else if(isFunc()) {
			return callFunc();
		} else if(isBreak() == 1){
			return _break();
		} else if(isBreak() == 2) {
			return _continue();
		}else {
			return assign();
		}
	}
	
	public Break _break() {
		next();//b
		next();//r
		next();//e
		next();//a
		next();//k
		return Break.getBreak();
	}
	
	public Continue _continue() {
		next();//c
		next();//o
		next();//n
		next();//t
		next();//i
		next();//n
		next();//u
		next();//e
		return Continue.getContinue();
	}
	
	public Blocks blocks() throws ParseException {
		switch(isBlocks()) {
		case 2:
			return _if();
		case 3:
			throw new ParseException("else if cannot be here");
		case 4:
			throw new ParseException("else cannot be here");
		case 5:
			return _for();
		case 6:
			return _while();
		}
		return null;
	}
	
	public If _if() throws ParseException {
		next();//i
		next();//f
		spaces();
		next();//(
		If res;
		Expression ifExpr = expression();
		if((char) peek() != ')') {
			throw new ParseException(") was not found");
		}
		next();//)
		spaces();
		Block ifBlock = block();
		res = new If(ifExpr, ifBlock);
		lfs();
		while(isBlocks() == 3) {
			next();//e
			next();//l
			next();//s
			next();//e
			next();// 
			next();//i
			next();//f
			spaces();
			if((char) peek() != '(') {
				throw new ParseException("( was not found");
			}
			next();//(
			Expression elifExpr = expression();
			if((char) peek() != ')') {
				throw new ParseException(") was not found");
			}
			next();//)
			spaces();
			Block elifBlock = block();
			res.addElif(elifExpr, elifBlock);
			lfs();
		}
		if(isBlocks() == 4) {
			next();//e
			next();//l
			next();//s
			next();//e
			spaces();
			Block elifBlock = block();
			res.setElse(elifBlock);
		}
		return res;
	}
	
	public While _while() throws ParseException {
		next();//w
		next();//h
		next();//i
		next();//l
		next();//e
		spaces();
		if((char) peek() != '(') {
			throw new ParseException("( was not found");
		}
		next();//(
		Expression expr = expression();
		spaces();
		if((char) peek() != ')') {
			throw new ParseException(") was not found");
		}
		next();//)
		lfs();
		Block block = block();
		return new While(expr, block);
	}
	
	public For _for() throws ParseException {
		next();//f
		next();//o
		next();//r
		if((char) peek() != '(') {
			throw new ParseException("( was not found");
		}
		next();//(
		spaces();
		DefinitionVariable defVar = defVar();
		spaces();
		if((char) peek() != ';') {
			throw new ParseException("; was not found");
		}
		next();//;
		Expression expr = expression();
		if((char) peek() != ';') {
			throw new ParseException("; was not found");
		}
		next();//;
		Assignment assign = assign();
		if((char) peek() != ')') {
			throw new ParseException(") was not found");
		}
		next();//)
		lfs();
		Block block = block();
		return new For(defVar, expr, assign, block);
	}
	
	public Block block() throws ParseException {
		if((char) peek() != '{') {
			throw new ParseException("{ was not found");
		}
		next();
		Programs progs = programs();
		if((char) peek() != '}') {
			throw new ParseException("} was not found");
		}
		next();
		return new Block(progs);
	}
	
	public Assignment assign() throws ParseException {
		Variable variable = variable();
		if((char) peek() != '=') {
			throw new ParseException("= was not found");
		}
		next();
		Expression expr = expression();
		return new Assignment(variable, expr);
	}
	
 	public Constant constant() throws ParseException {
		StringBuilder sb = new StringBuilder();
		if(peek() < 0) {
			return null;
		} 
		if((char) peek() == '\"') {
			next();
			while(true) {
				char c = (char)peek();
				if (c == '\"') {
					next();
					break;
				}
				sb.append(c);
				next();
			}
			return new Constant(Type.STR, sb.toString());
		} else {
			boolean period = false;
			boolean zeroStart = false;
			if((char) peek() == '0') {
				zeroStart = true;
				sb.append('0');
				next();
			}
			while(true) {
				char c = (char) peek();
				if(zeroStart && !Character.isDigit(c)) {
					break;
				}
				if(zeroStart && c != '.') {
					throw new ParseException("int must not start with 0");
				} else if(c == '.') {
					if (period) {
						throw new ParseException("two periods fownd");
					} else {
						period = true;
					}
				}
				if(!Character.isDigit(c) && c != '.') {
					break;
				}
				sb.append(c);
				next();
			}
			if (period) {
				return new Constant(Type.FLOAT, sb.toString());
			}
			return new Constant(Type.INT, sb.toString());
		}
	}
	
	public Variable variable() {
		return Variable.getVariable(identifier());
	}
	
	public CallFunction callFunc() throws ParseException {
		String name = identifier();
		next();
		Arguments args = args();
		return new CallFunction(name, args);
	}
	
	public DefinitionVariable defVar() throws ParseException {
		DefinitionVariableType defVar = defVarType();
		if((char) peek() != '=') {
			throw new ParseException("variable definition need assignment");
		}
		next();
		Expression expr = expression();
		return new DefinitionVariable(defVar, expr);
	}
	
	public DefinitionVariableType defVarType() throws ParseException {
		return new DefinitionVariableType(type(), variable());
	}
	
	public Type type() throws ParseException {
		StringBuilder sb = new StringBuilder();
		spaces();
		while(true) {
			char c = (char) peek();
			if(!Character.isLetterOrDigit(c)) {
				break;
			}
			sb.append(c);
			next();
		}
		spaces();
		switch(sb.toString()) {
		case "int":
			return Type.INT;
		case "str":
			return Type.STR;
		case "float":
			return Type.FLOAT;
		case "bool":
			return Type.BOOL;
		}
		throw new ParseException(sb.toString() + " is not a type");
	}
	
	public Arguments args() throws ParseException {
		Arguments args = new Arguments();
		while(true) {
			args.addArgs(expression());
			if((char) peek() == ')') {
				next();
				break;
			}
			if((char) peek() != ',') {
				throw new ParseException(", was not found");
			}
			next();
		}
		return args;
	}
	
	public Factor factor() throws ParseException {
		spaces();
		char c = (char) peek();
		Factor factor;
		if (c == '(') {
			next();
			factor = new Factor(expression());
			if(peek() != ')') {
				throw new ParseException(") was not found");
			}
			next();
		} else {
			if(Character.isDigit(c) || c == '\"') {
				factor = new Factor(constant());
			} else {
				if(isFunc()) {
					factor = new Factor(callFunc());
				} else {
					factor = new Factor(variable());
				}
			}
		}
		spaces();
		return factor;
	}
	
	public Term term() throws ParseException {
		Term term = new Term();
		Factor factor = factor();
		term.addFacotr(factor);
		while(true) {
			char c = (char) peek();
			if(c == '*' || c == '/' || c == '%') {
				term.addOperator(c);
				next();
				term.addFacotr(factor());
			} else {
				break;
			}
		}
		return term;
	}
	
	public Expression expression() throws ParseException {
		Expression expr = new Expression();
		Term term = term();
		expr.addTerm(term);
		while(true) {
			char c = (char)peek();
			if (c == '+' || c == '-') {
				expr.addOperator("" + c);
				next();
				expr.addTerm(term());
			} else if(c == '<') {
				next();
				if ((char) peek() == '=') {
					expr.addOperator("<=");
					next();
				} else {
					expr.addOperator("<");
				}
				expr.addTerm(term());
			} else if(c == '>') {
				next();
				if ((char) peek() == '=') {
					expr.addOperator(">=");
					next();
				} else {
					expr.addOperator(">");
				}
				expr.addTerm(term());
			} else if(c == '=') {
				next();
				if ((char) peek() == '=') {
					expr.addOperator("==");
					next();
				} else {
					throw new ParseException("= cannot use here");
				}
				expr.addTerm(term());
			} else if(c == '!') {
				next();
				if ((char) peek() == '=') {
					expr.addOperator("!=");
					next();
				} else {
					throw new ParseException("! cannot use here");
				}
				expr.addTerm(term());
			} else if(c == '&') {
				next();
				if ((char) peek() == '&') {
					expr.addOperator("&&");
					next();
				} else {
					throw new ParseException("& cannot use here");
				}
				expr.addTerm(term());
			} else if(c == '|') {
				next();
				if ((char) peek() == '|') {
					expr.addOperator("||");
					next();
				} else {
					throw new ParseException("| cannot use here");
				}
				expr.addTerm(term());
			} else {
				break;
			}
		}
		return expr;
	}
	
	private String identifier() {
		StringBuilder sb = new StringBuilder();
		spaces();
		int c;
		while((c = peek()) != -1) {
			if(!Character.isLetterOrDigit(c)) {
				break;
			}
			sb.append((char) c);
			next();
		}
		spaces();
		return sb.toString();
	}

	private void spaces() {
		while((char)peek() == '\t' || (char) peek() == ' ') {
			next();
		}
	}
	private void lfs() {
		while((char)peek() == '\t' || (char)peek() == '\n' || (char) peek() == ' ') {
			next();
		}
	}
}

class Source {

	private final String str;
	private int pos;
	
	public Source(String str) {
		this.str = str;
		pos = 0;
	}
	
	public int peek() {
		if(pos >= str.length()) {
			return -1;
		}
		return str.charAt(pos);
	}
	
	public void next() {
		pos++;
	}
	
	public boolean isFunc() {
		int count = 0;
		while(true) {
			if(str.charAt(pos + count) == '(') {
				return true;
			} else if(!Character.isLetterOrDigit(str.charAt(count + pos))) {
				break;
			}
			count++;
		}
		return false;
	}
	
	public boolean isType() {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		while(true) {
			char c = str.charAt(pos + count);
			if(!Character.isLetterOrDigit(c)) {
				break;
			}
			sb.append(c);
			count += 1;
		}
		switch(sb.toString()) {
		case "int":
			return true;
		case "str":
			return true;
		case "float":
			return true;
		case "bool":
			return true;
		}
		return false;
	}
	
	public int isBreak() {
		//continue
		if(str.substring(pos, pos + 5).equals("break")) {
			return 1;
		} else if(str.substring(pos, pos + 8).equals("continue")) {
			return 2;
		}
		return -1;
	}

	public int isBlocks() {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		while(true) {
			if(pos + count >= str.length()) {
				return -1;
			}
			char c = str.charAt(pos + count);
			if(!Character.isLetterOrDigit(c)) {
				break;
			}
			sb.append(c);
			count += 1;
		}
		switch(sb.toString()) {
		case "def":
			return 1;
		case "if":
			return 2;
		case "else":
			if (str.charAt(pos + count + 1) == 'i' && str.charAt(pos + count + 2) == 'f') {
				return 3;
			}
			return 4;
		case "for":
			return 5;
		case "while":
			return 6;
		}
		return -1;
	}
	
}
class Arguments {
	
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
class Assignment implements Program {
	
	private Variable variable;
	private Expression expression;
	
	public Assignment(Variable var, Expression expr) {
		variable = var;
		expression = expr;
	}
	
	@Override
	public void excecute() throws ParseException {
		variable.set(expression.calc());
	}

}class Block {
	
	private Programs programs;
	
	public Block(Programs progs) {
		programs = progs;
	}
	
	public void execute() throws ParseException {
		programs.execute();
	}
	
}
abstract class Blocks implements Executable{

}
class Break implements Program{
	
	public static boolean breaked = false;
	private static boolean used = false;
	private static Break b;
	
	public static Break getBreak() {
		if (!used) {
			b = new Break();
			return b;
		} else {
			return b;
		}
	}

	@Override
	public void excecute() throws ParseException {
		breaked = true;
	}
	
}class CallFunction extends Value implements Program {

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
		case "print2":
			Expression expr = args.getArgs().get(0);
			System.out.print(expr.calc().getValue());
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
		case "charAt":
			Expression str = args.getArgs().get(0);
			Expression num = args.getArgs().get(1);
			int n = Integer.parseInt(num.calc().getValue());
			return new Value(str.calc().getValue().charAt(n) + "", Type.STR);
		case "length":
			Expression str2 = args.getArgs().get(0);
			return new Value(str2.calc().getValue().length() + "", Type.INT);
		}
		return new Value("0", Type.INT);
	}
	
}
class Constant extends Value{
	
	public Constant(Type type, String value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public Value calc() {
		return this;
	}

}
class Continue implements Program{

	public static boolean continued = false;
	public static Continue c = null;
	
	public static Continue getContinue() {
		if(c == null) {
			c = new Continue();
		}
		return c;
	}
	
	@Override
	public void excecute() throws ParseException {
		continued = true;
	}

}
class DefinitionArguments {
	
	private List<DefinitionVariableType> defVarTypes = new ArrayList<>();
	
	public void addArg(DefinitionVariableType defVarType) {
		defVarTypes.add(defVarType);
	}
	
}
class DefinitionFunction extends Blocks {

	private String functionName;
	private DefinitionArguments defArgs;
	private Block block;
	
	public DefinitionFunction(String name, DefinitionArguments defArgs, Block block) {
		functionName = name;
		this.defArgs = defArgs;
		this.block = block;
	}
	
	@Override
	public void excecute() {
	}

}class DefinitionVariable implements Program {
	
	private DefinitionVariableType defVarType;
	private Expression expression;

	public DefinitionVariable(DefinitionVariableType defVar, Expression expression) {
		this.defVarType = defVar;
		this.expression = expression;
	}
	
	@Override
	public void excecute() throws ParseException {
		if(expression != null) {
			Variable variable = defVarType.getVariable();
			variable.setType(defVarType.getType());
			variable.set(expression.calc());
		}
	}

}class DefinitionVariableType {
	
	private Type type;
	private Variable variable;
	
	public DefinitionVariableType(Type type, Variable variable) {
		this.type = type;
		this.variable = variable;
	}
	
	public Type getType() {
		return type;
	}
	
	public Variable getVariable() {
		return variable;
	}
	
} interface Executable {

	public void excecute() throws ParseException;
	
}
class Expression extends Value{
	
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
class Factor {

	private Value value;
	
	public Factor(Value value) {
		this.value = value;
	}
	
	public Value calc() throws ParseException {
		return value.calc();
	}
	
}
class For extends Blocks {

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
class If extends Blocks {
	
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
interface Program extends Executable {
	
}class Programs {
	
	private List<Executable> programs = new ArrayList<>();
	
	public Programs() {
		
	}
	
	public void addProgram(Executable prog) {
		programs.add(prog);
	}
	
	public void execute() throws ParseException {
		for(Executable prog : programs) {
			if(Break.breaked) {
				break;
			} else if(Continue.continued) {
				Continue.continued = false;
				break;
			}
			prog.excecute();
		}
	}
	
}
class Term {
	
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
enum Type {
	
	INT, FLOAT, STR, BOOL, LIST, SET, MAP
	
}class Value {
	
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
	
}class Variable extends Value{

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
class While extends Blocks {

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
class ParseException extends Exception{
	public ParseException(String msg) {
		super(msg);
	}
}