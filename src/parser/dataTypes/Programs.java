package parser.dataTypes;

import java.util.ArrayList;
import java.util.List;

import parser.ParseException;

public class Programs {
	
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
