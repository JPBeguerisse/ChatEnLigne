package Server;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private ServerSocket serverSocket;
			
	public Server(ServerSocket serverSocket)
	{
		this.serverSocket = serverSocket;
	}
	
	
	public void startServer()
	{
		try {
			while(!serverSocket.isClosed())
			{
				Socket socket = serverSocket.accept();
				System.out.println("Nouveau client connecté");
				Chat client = new Chat(socket);
				
				Thread thread = new Thread(client);
				thread.start();
			}
		}catch(IOException e) {
			
		}
	}
	
	public void closeServer()
	{
		try {
			if(serverSocket != null)
			{
				serverSocket.close();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		try {
			ServerSocket serverSocket = new ServerSocket(1234);
			Server server = new Server(serverSocket); // creation d'un sever
			server.startServer(); // allumer le server
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
