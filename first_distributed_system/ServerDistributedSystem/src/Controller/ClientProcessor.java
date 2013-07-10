package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientProcessor implements Runnable{

	MathLogic logic;
	Socket clientSocket;
	ArrayList<Connectable> listeners = new ArrayList<Connectable>();
	public ClientProcessor(MathLogic _logic, Socket _clientSocket){
		logic = _logic;
		clientSocket = _clientSocket;
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
	
	public void disconnect(){
		for(Connectable c : listeners){
			c.disconnect();
		}
	}
	
	public void run() {
		connect();
		try {
			InputStream clientInput = clientSocket.getInputStream();
		
			Scanner inputReader = new Scanner(clientInput);
		
			String userRequestedMethod = inputReader.next();
			int firstInt = inputReader.nextInt();
			int secondInt = inputReader.nextInt();
		
			int value = userRequestedMethod.equalsIgnoreCase("add")?logic.add(firstInt, secondInt):logic.subtract(firstInt, secondInt);
		
			OutputStream clientOutput = clientSocket.getOutputStream();
			PrintWriter clientWriter = new PrintWriter(clientOutput);
			clientWriter.write("Value " +value);
			clientWriter.flush();
			clientWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
	}

}
