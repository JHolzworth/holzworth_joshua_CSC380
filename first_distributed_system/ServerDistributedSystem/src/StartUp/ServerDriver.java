package StartUp;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import Server.ServerApplication;

public class ServerDriver {

	final static int PORT = 5000;
	
	public static void main(String[]args) throws IOException, NoSuchMethodException, SecurityException, ClassNotFoundException{

		ServerApplication server = new ServerApplication(PORT);
		server.start();
		
		
	}
	
	
}
