package UserFacingPackage;

public class Person {

	int age;
	String name;
	
	public Person(String _name, int _age){
		age = _age;
		name = _name;
	}
	
	public Person(){
		age = 0;
		name = "temp";
	}
	
	public String FreakOUT(FakeObject fo){
		return fo.b+" "+(char)(fo.c+1)+" "+(fo.i+fo.d)+" "+fo.s.toUpperCase();
	}
}
