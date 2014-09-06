package runnables;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Queue;

import classes.Client;


public class updateMessageRunnable implements Runnable{

	private Client client;
	
	private BufferedReader in;
	private PrintWriter out;
	
	public updateMessageRunnable(Client client, InputStream is, OutputStream out) {
		this.in = new BufferedReader(new InputStreamReader(
				is));
		this.out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(out)), true);
		
		this.client = client;
	}
	
	public void run() {
		
		while (!Thread.currentThread().isInterrupted()) {
		
			//whenever the connection is made -> send the messages
			Queue<String> chatRooms = client.getNotUpdatedChatRooms();
			Queue<String> messages = client.getMessages();
			Queue<String> friends = client.getNotUpdatedfriend();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				System.out.println("updateMessageRunnable has been interrupted");
				e1.printStackTrace();
				return;
			}
			
			while (!Thread.currentThread().isInterrupted() && chatRooms.size() > 0) {
				client.sendMessage(chatRooms.peek());
				
				try {
					if (in.readLine().equals("RECEIVED")) {
						System.out.println("received the chatroom information");
						chatRooms.remove();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			while (!Thread.currentThread().isInterrupted() && friends.size() > 0) {
				client.sendMessage(friends.peek());
				
				try {
					if (in.readLine().equals("RECEIVED")) {
						System.out.println("received the chatroom information");
						friends.remove();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			while (!Thread.currentThread().isInterrupted() && messages.size() > 0) {
				client.sendMessage(messages.peek());
				
				try {
					if (in.readLine().equals("RECEIVED")) {
						System.out.println("received the message information");
						messages.remove();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (client.getFriendExistFlag() != null) {
				if (client.getFriendExistFlag().equals("NEXIST")) {
					client.sendMessage("FRND#NEXIST#");
				} else {
					client.sendMessage("FRND#EXIST#" + client.getFriendExistFlag() + "#");
				}

				try {
					if (in.readLine().equals("RECEIVED")) {
						System.out.println("received friend exist flag info");
						client.setFriendExistFlag(null);
					} else {
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}

}
