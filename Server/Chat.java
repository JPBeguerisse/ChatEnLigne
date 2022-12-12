package Server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Chat implements Runnable {
	private String pseudo;
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;
	public static ArrayList<Chat> client = new ArrayList<>();
	
	public Chat(Socket socket)
	{
		try{
			this.socket = socket;
			this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.pseudo = br.readLine();
			client.add(this);
			broadcastMessage("SERVER: " + pseudo + " a rejoint le chat");
		}catch(IOException e){
			closeEverything(socket, br, bw);
		}
	}

	public void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw) {
		removeClient();
		
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

	public void broadcastMessage(String messageSend) {
		// TODO Auto-generated method stub
		for(Chat client : client)
		{
			if(!client.pseudo.equals(pseudo))
			{
				try {
					client.bw.write(messageSend);
					client.bw.newLine();
					client.bw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					closeEverything(socket, br, bw);
				}
			}
		}
	}
	
	public void removeClient()
	{
		client.remove(this);
		broadcastMessage("SERVER: " + pseudo + " a quitté le chat");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String messageClient;
		
		while(socket.isConnected())
		{
			try {
				messageClient = br.readLine();
				broadcastMessage(messageClient);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				closeEverything(socket, br, bw);
				break;
				// e.printStackTrace();
			}
		}
	}


}
