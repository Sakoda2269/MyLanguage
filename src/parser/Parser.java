package parser;

import parser.dataTypes.Arguments;
import parser.dataTypes.Assignment;
import parser.dataTypes.Block;
import parser.dataTypes.Blocks;
import parser.dataTypes.Break;
import parser.dataTypes.CallFunction;
import parser.dataTypes.Constant;
import parser.dataTypes.Continue;
import parser.dataTypes.DefinitionVariable;
import parser.dataTypes.DefinitionVariableType;
import parser.dataTypes.Expression;
import parser.dataTypes.Factor;
import parser.dataTypes.For;
import parser.dataTypes.If;
import parser.dataTypes.Program;
import parser.dataTypes.Programs;
import parser.dataTypes.Term;
import parser.dataTypes.Type;
import parser.dataTypes.Variable;
import parser.dataTypes.While;

public class Parser extends Source{
	
	
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
