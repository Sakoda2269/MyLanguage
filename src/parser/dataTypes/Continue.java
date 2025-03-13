package parser.dataTypes;

import parser.ParseException;

public class Continue implements Program{

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
