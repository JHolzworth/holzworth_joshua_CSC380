package Controller;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class ClientProcessor implements Runnable{

	MathLogic logic;
	Socket clientSocket;
	ArrayList<Connectable> listeners = new ArrayList<Connectable>();
	public ClientProcessor(MathLogic _logic, Socket _clientSocket) throws NoSuchMethodException, SecurityException{
		logic = _logic;
		clientSocket = _clientSocket;
		setUpMethodTable();
		primativeSetUp();
	}
	
	public void addListener(Connectable toAdd){
		listeners.add(toAdd);
	}
	
	public void removeListener(Connectable toRemove){
		listeners.remove(toRemove);
	}
	
	
	public void connect(){
		for(Connectable c : listeners){
			c.connect();
		}
	}
	
	public void request(String request){
		for(Connectable c : listeners){
			c.request(request);
		}
	}
	public void response(String response){
		for(Connectable c : listeners){
			c.response(response);
		}
	}
	
	public void disconnect(){
		for(Connectable c : listeners){
			c.disconnect();
		}
	}
	

	public String obtainClientOptions(){
		String options = "";
		
		Enumeration<String> methods = lookUpTable.keys();
		
		while(methods.hasMoreElements()){
			String nextMethod = methods.nextElement();
			options+=nextMethod;
			
			Class[] params = lookUpTable.get(nextMethod);
			for(Class nextParam : params){
				options+=" "+nextParam.getName();
			}
			options+="|";
		}
		return options;
	}
	
	private void primativeSetUp() throws NoSuchMethodException, SecurityException{
		
		primLookUp.put("int", "java.lang.Integer");
		primCastLookUp.put("int", Integer.class.getMethod("intValue"));
		primLookUp.put("double", "java.lang.Double");
		primCastLookUp.put("double", Double.class.getMethod("doubleValue"));
		
		primLookUp.put("float", "java.lang.Float");
		primCastLookUp.put("float", Float.class.getMethod("floatValue"));
		primLookUp.put("long", "java.lang.Long");
		primCastLookUp.put("long", Long.class.getMethod("longValue"));
		primLookUp.put("boolean", "java.lang.Boolean");
		primCastLookUp.put("boolean", Boolean.class.getMethod("booleanValue"));
		primLookUp.put("char", "java.lang.Character");
		primCastLookUp.put("character", Character.class.getMethod("charValue"));
		primLookUp.put("short","java.lang.Short");
		primCastLookUp.put("short", Short.class.getMethod("shortValue"));
		primLookUp.put("byte", "java.lang.Byte");
		primCastLookUp.put("byte", Byte.class.getMethod("byteValue"));
	}
	Hashtable<String,Method> primCastLookUp = new Hashtable<String,Method>();
	Hashtable<String,String> primLookUp = new Hashtable<String,String>();
	
	Hashtable<String,Class[]> lookUpTable = new Hashtable<String,Class[]>();
	
	private void setUpMethodTable(){
		Method[] allMethods = MathLogic.class.getDeclaredMethods();
		for(int i=0;i<allMethods.length;i++){
			lookUpTable.put(allMethods[i].getName(),allMethods[i].getParameterTypes());
		}
	}
	
	public void obtainMethodInformation() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException{
		InputStream clientInput = clientSocket.getInputStream();
		Scanner inputReader = new Scanner(clientInput);
		
		String request = inputReader.nextLine();
		Class[] paramTypes = lookUpTable.get(request);
		
		//Send paramNumber
		OutputStream clientOutput = clientSocket.getOutputStream();
		PrintWriter clientWriter = new PrintWriter(clientOutput);
		clientWriter.println(paramTypes.length);
		clientWriter.flush();

		
		Method chosenMethod = MathLogic.class.getMethod(request,paramTypes);
		
		Object[] userParams = new Object[paramTypes.length];
		
		for(int i=0;i<paramTypes.length;i++){

			if(paramTypes[i].getName().equals("java.lang.String")){
				userParams[i] = inputReader.nextLine();
			}
			else{
				Class current =  Class.forName(primLookUp.get(paramTypes[i].getName()));
					//Character can't use valueOf
				if(paramTypes[i].getName().equals("char")){
					userParams[i] = (inputReader.next().charAt(0));//primCastLookUp.get(paramTypes[i].getName()).invoke(cast.invoke(current, inputReader.nextCnextChar()));
				}
				else{
					Method cast = current.getMethod("valueOf", String.class);
					userParams[i] = primCastLookUp.get(paramTypes[i].getName()).invoke(cast.invoke(current, inputReader.nextLine()));
				}
			}
		}
		Class returnType = chosenMethod.getReturnType();
		String value = "";
		if(returnType.getName().equals("java.lang.String")){
			value = (String) (returnType.cast(chosenMethod.invoke(logic, userParams)));
		}
		else if(returnType.getName().equals("char")){
			value = chosenMethod.invoke(logic,userParams).toString();
		}
		else{
			value = ((primCastLookUp.get(returnType.getName()).invoke(chosenMethod.invoke(logic, userParams)))).toString();
		}
		String response = "Value "+value;
		clientWriter.println(response+"\n");
		clientWriter.flush();
		clientSocket.close();
		response(response);
	}
	
	
	public void run() {
		connect();
		try {

			//Sends options
			OutputStream clientOutput = clientSocket.getOutputStream();
			PrintWriter clientWriter = new PrintWriter(clientOutput);
			clientWriter.println(obtainClientOptions());
			clientWriter.flush();

			
			//request
			obtainMethodInformation();
			
			/* Not what was wanted...
			String request = requestedMethod;
			ArrayList<Integer> numbers = new ArrayList<Integer>();
			while(inputReader.hasNext()){
				String nextNumber = inputReader.next();
				request+=" "+nextNumber;
				numbers.add(Integer.parseInt(nextNumber));
			}
			request(request);
			
			int[] realNumbers = new int[numbers.size()];
			for(int i=0;i<numbers.size();i++){
				realNumbers[i] = numbers.get(i);
			}
			
			Method method = logic.getClass().getMethod(requestedMethod, int[].class);
			int value = (int)method.invoke(logic, realNumbers);
			*/
			
			//Return...
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();
	}

}
