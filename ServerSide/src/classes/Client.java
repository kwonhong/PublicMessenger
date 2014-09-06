package classes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import runnables.HandleMessageRunnable;
import runnables.updateMessageRunnable;

public class Client {
	//not using these at all
	private boolean isUserOn;
	private boolean isUserLogin;
	
	private String friendExistFlag;
	
	private Queue<String> notUpdatedChatRooms;
	private Queue<String> messages;
	private Queue<String> notUpdatedfriend;
	
	private List<String> friendList;
	private List<String> chatRooms;
	
	private Socket socket;
	private String username;
	private String password;
	
	private PrintWriter out;
	private BufferedReader input;
	
	private Thread HandleMessageThread;
	private Thread UpdateMessageThread;
	
	public Client(Socket socket) {
		this.friendList = new ArrayList<String>();
		this.chatRooms = new ArrayList<String>();
		this.setNotUpdatedChatRooms(new LinkedList<String>());
		this.setMessages(new LinkedList<String>());
		this.setNotUpdatedfriend(new LinkedList<String>());
		this.socket = socket;
		try {
			this.socket.setSoTimeout(60*1000);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		this.setFriendExistFlag(null);
		this.isUserOn = true;
		
		try {
			this.input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			this.out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void killPreviosThread(Client client) {
		if (HandleMessageThread.isAlive() && client.HandleMessageThread != HandleMessageThread) {
			HandleMessageThread.interrupt();
		}
		
		if (UpdateMessageThread.isAlive() && client.UpdateMessageThread != UpdateMessageThread) {
			UpdateMessageThread.interrupt();
		}
	}
	
	public void resetTheThreads(Client client, ArrayList<Client> clients, MessengerInfo messengerInfo) throws IOException {
		
		PipedOutputStream pout1 = new PipedOutputStream();
		PipedInputStream pin1 = new PipedInputStream(pout1);
		
		PipedOutputStream pout2 = new PipedOutputStream();
		PipedInputStream pin2 = new PipedInputStream(pout2);
		
		//make Thread to handle messages
		HandleMessageRunnable handleMessageRunnable = 
				new HandleMessageRunnable(client, clients, messengerInfo, pin1, pout2);
		HandleMessageThread = new Thread(handleMessageRunnable);
		HandleMessageThread.start();
		
		//make Thread to update messages
		updateMessageRunnable runnable = new updateMessageRunnable(client, pin2, pout1);
		UpdateMessageThread = new Thread(runnable);
		UpdateMessageThread.start();
	}
	
	public void newFriend(String friend) {
		System.out.println("creating a new friend // in Client class");
		friendList.add(friend);
		
		String string = "FRND#ADD#" + friend + "#";
		notUpdatedfriend.add(string);
	}
	
	//CHAT#ADD#PUBLIC#ROOM_ID#CHATROOM#
	public void newChatRoom(ChatRoom chat, String option) {
		String string = "CHAT#ADD#" + option +"#" + 
							chat.getRoom_id() + "#" + chat.getChatName() + "#";
		notUpdatedChatRooms.add(string);
	}
	
	public void newMessage(String message) {
		messages.add(message);
	}
	
	public void sendMessage(String message) {
		if (out == null)
			return;
		out.println(message);
		System.out.println("sending message " + message);
	}
	
	public String getMessage() throws IOException, SocketTimeoutException {
		if (input == null) 
			return null;
		
		return input.readLine();
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			this.input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			this.out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return socket;
	} 
	
	public void restoreData(Client client) {
		this.isUserOn = client.isUserOn;
		this.friendExistFlag = client.friendExistFlag;
		
		this.notUpdatedChatRooms = client.notUpdatedChatRooms;
		this.messages = client.messages;
		this.notUpdatedfriend = client.notUpdatedfriend;

		
		this.friendList = client.friendList;
		this.chatRooms = client.chatRooms;
	}
	
	
	
	
	
	
	
	
	public void setUserName (String name) {
		this.username = name;
	}
	
	public String getUsername() {
		return username;
	}

	public boolean isUserOn() {
		return isUserOn;
	}

	public void setUserOn(boolean isUserOn) {
		this.isUserOn = isUserOn;
	}

	public Queue<String> getNotUpdatedChatRooms() {
		return notUpdatedChatRooms;
	}

	public void setNotUpdatedChatRooms(Queue<String> notUpdatedChatRooms) {
		this.notUpdatedChatRooms = notUpdatedChatRooms;
	}

	public Queue<String> getMessages() {
		return messages;
	}

	public void setMessages(Queue<String> messages) {
		this.messages = messages;
	}

	public Queue<String> getNotUpdatedfriend() {
		return notUpdatedfriend;
	}

	public void setNotUpdatedfriend(Queue<String> notUpdatedfriend) {
		this.notUpdatedfriend = notUpdatedfriend;
	}

	public String getFriendExistFlag() {
		return friendExistFlag;
	}

	public void setFriendExistFlag(String friendExistFlag) {
		this.friendExistFlag = friendExistFlag;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isUserLogin() {
		return isUserLogin;
	}
	public void setUserLogin(boolean isUserLogin) {
		this.isUserLogin = isUserLogin;
	}
	
}
