package UserFacingPackage;

public class ExtraLogic {

	public String makeFun(String fun){
		return "***\t**"+fun.toUpperCase()+"!!";
	}
	
	public String lmfao(int newAge, Person person){
		return newAge*person.age+" "+person.name;
	}
}
