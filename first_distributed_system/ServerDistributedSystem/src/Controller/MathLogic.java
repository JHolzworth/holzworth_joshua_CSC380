package Controller;

public class MathLogic implements Operatable{

	public int add(int a, int b) {
		int sum = a+b;
		return sum;
	}

	public int subtract(int a, int b) {
		int difference = a - b;
		return difference;
	}

}