package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientApplication {

	
	Socket clientSocket;
	
	public ClientApplication(String host, int port) throws UnknownHostException, IOException{
		clientSocket = new Socket(host,port);
		
	}
	
	
	private String getClientRequest(){

		Scanner clientInput = new Scanner(System.in);
		
		String request = clientInput.next();
		request+=" "+clientInput.nextInt();
		request+=" "+clientInput.nextInt();
		return request;
	}
	
	public void start() throws IOException{
		System.out.println("Client Socket created");
		
		//Print directions
		System.out.println("Type ADD or SUBTRACT respective");
		System.out.println("Then hit enter and type the first number");
		System.out.println("Followed by another enter and the second number");
		
		//Grab input
		String request = getClientRequest();
		
		
		//SendingRequest
		OutputStream output = clientSocket.getOutputStream();
		PrintWriter writer = new PrintWriter(output);
		
		writer.write(request);
		
		writer.flush();
		
		clientSocket.shutdownOutput();
		
		
		
		//Getting response
		InputStream input = clientSocket.getInputStream();
		Scanner clientInput = new Scanner(input);
		
		String response = "";
		while(clientInput.hasNext()){
			response+=clientInput.nextLine();
		}
		
		System.out.println(response);
		
		//Delete when finished closes socket
		clientSocket.close();
	}
}