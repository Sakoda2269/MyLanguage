package main;

import java.util.Scanner;

import parser.ParseException;
import parser.Parser;

public class Main {

	public static Scanner sc;
	
	public static void main(String[] args) {
		
		sc = new Scanner(System.in);
		
		String program = 
"""
int i = 0;
while(1 == 1) {
	i = toInt(input());
	if(i == 5) {
		print(i + \" is 5\");
		break;
	} else {
		print(i + \" is not 5\");
	}
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
