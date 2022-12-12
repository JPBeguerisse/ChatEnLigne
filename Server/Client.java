package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;
	private String name;
	
	public Client(Socket s, String name)
	{
		try{
			this.socket = s;
			this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.name = name;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			closeEverything(socket, br, bw);
		}
	}
	
	public void sendMessage()
	{
		try {
			bw.write(name);
			bw.newLine();
			bw.flush();
			
			Scanner sc = new Scanner(System.in);
			while(socket.isConnected()) {
				String messageOut = sc.nextLine();
				bw.write(name + ": " + messageOut);
				bw.newLine();
				bw.flush();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			closeEverything(socket, br, bw);
		}
	}
	
	public void listenMessage()
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				{
					String messageChat;
					
					while(socket.isConnected())
					{
						try {
							messageChat = br.readLine();
							System.out.println(messageChat);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							closeEverything(socket, br, bw);
						}
						
					}
				}
			}
			
		}).start();
	}

	
	public void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw) {
		
			try {
				if(br != null)
				{
					br.close();
				}
				
				if(bw != null)
				{
					bw.close();
				}
				
				if(socket != null)
				{
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez entrer votre pseudo pour le chat: ");
		String name = sc.nextLine();
		Socket socket = new Socket("127.0.0.1", 1234);
		Client client = new Client(socket, name);
		client.listenMessage();
		client.sendMessage();
		
	}
}
