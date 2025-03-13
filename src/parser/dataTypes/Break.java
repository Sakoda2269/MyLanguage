package parser.dataTypes;

import parser.ParseException;

public class Break implements Program{
	
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
	
}
