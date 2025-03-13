package parser;

public class Source {

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
