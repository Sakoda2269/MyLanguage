package parser.dataTypes;

import parser.ParseException;

public class Block {
	
	private Programs programs;
	
	public Block(Programs progs) {
		programs = progs;
	}
	
	public void execute() throws ParseException {
		programs.execute();
	}
	
}
