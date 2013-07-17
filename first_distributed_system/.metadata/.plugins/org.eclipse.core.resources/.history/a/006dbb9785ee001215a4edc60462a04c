package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientApplication {

	
	Socket clientSocket;
	
	public ClientApplication(String host, int port) throws UnknownHostException, IOException{
		clientSocket = new Socket(host,port);
		
	}
	
	
	private String getClientRequest(){

		Scanner clientInput = new Scanner(System.in);
		
		String request = clientInput.nextLine();
		return request;
	}
	
	public void start() throws IOException{
		System.out.println("Client Socket created");
		
		
		//Obtaining instructions
		InputStream input = clientSocket.getInputStream();
		Scanner clientInput = new Scanner(input);
		System.out.println("Obtaining instructions");
		String options = clientInput.nextLine();
		System.out.println("Type the desired method name followed by a list of enter delimated parameters. \nPossible options:\n"+options.replace('|','\n'));

		//SendingRequest
		OutputStream output = clientSocket.getOutputStream();
		PrintWriter writer = new PrintWriter(output);
		writer.println(getClientRequest());
		//need to send  back value
		writer.flush();
		
		int paramNum = Integer.parseInt(clientInput.nextLine());
		
		//Send parameters
		Scanner userInput = new Scanner(System.in);
		for(int i=0;i<paramNum;i++){
			writer.println(userInput.nextLine());
		}
		writer.flush();
		
		//Getting response
		
		
		System.out.println(getResponse());//response);
		
		//Delete when finished closes socket
		clientSocket.close();
	}
	
	private String getResponse() throws IOException{
		InputStream input = clientSocket.getInputStream();
		Scanner clientInput = new Scanner(input);
		
		String response=clientInput.nextLine();
		
		return response;
	}
}
