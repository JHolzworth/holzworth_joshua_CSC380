package UserFacingPackage;

import java.util.ArrayList;

public class MathLogic{

	public int add(int a, int b) {
		int sum = a+b;
		return sum;
	}

	public int subtract(int a, int b) {
		int difference = a-b;
		return difference;
	}
	
	public String TestMethod(String check){
		return "Test working "+check;
	}
	
	public boolean areTrue(boolean first, boolean second){
		return first&&second;
	}
	
	public char one(char c){
		return 'b';//(char)(c+1);
	}
	
	public byte check(byte t){
		
		return t+=1;
	}
	
	public boolean test(char a, int b){
		return true;
	}
}
