package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CRSServer
{
	public enum Command{CREATE,READ,SPLIT}
	private ServerSocket serverSocket;
	
	public CRSServer(int port)
	{				
		while (true) {
        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("Server started");
            Socket clientSocket = serverSocket.accept(); 
        } catch (IOException e) {
        }
                Object ObjectInputStream = null;

                if (ObjectInputStream instanceof Command) {
                    Command command = (Command) ObjectInputStream;

                    if (command.equals(Command.CREATE)) {

                    } else if (command.equals(Command.READ)) {
                    	
                    } else if (command.equals(Command.SPLIT)) {                    }
                }
		}
	}
public static void main(String[] args){
	new CRSServer(1234);
}
}