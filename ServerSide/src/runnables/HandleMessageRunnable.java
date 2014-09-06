package runnables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import classes.ChatRoom;
import classes.Client;
import classes.MessageExtractor;
import classes.MessengerInfo;

public class HandleMessageRunnable implements Runnable {

	private Client client;
	
	private ArrayList<Client> clients;
	private ArrayList<ChatRoom> chatRooms;
	private ArrayList<ChatRoom> publicChatRooms;
	
	private List<Client> users;
	private MessageExtractor messageExtractor;
	
	private BufferedReader in;
	private PrintWriter out;

	public HandleMessageRunnable(Client client, ArrayList<Client> clients, MessengerInfo messengerInfo,
					InputStream is, OutputStream out) {
		this.in = new BufferedReader(new InputStreamReader(
				is));
		this.out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(out)), true);
		
		this.client = client;
		this.clients = clients;
		
		this.chatRooms = messengerInfo.getChatRooms();
		this.publicChatRooms = messengerInfo.getPublicChatRooms();
		
		this.messageExtractor = new MessageExtractor();
	}

	public void run() {
		while (!Thread.currentThread().isInterrupted() && client.isUserOn() == true) {
			String read = null;
			try {
				read = client.getMessage();
			} catch (SocketTimeoutException e) {
				if (Thread.currentThread().isInterrupted()) {
					System.out.println("killing the thread");
					return;
				} else {
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			messageExtractor.setMessage(read);
					
			System.out.println("reading    " + read);
			
			String firstWord = messageExtractor.extractNextWord();
			
			if (firstWord.equals("CREATECHAT")) {
				System.out.println("Creating the chat room");
				createChatRoom();
			}
			
			else if (firstWord.equals("CHAT")) {
				System.out.println("Chat function");
				handleChat();
			}
			
			else if (firstWord.equals("LOGIN")) {
				System.out.println("Logging in");
				handleLogin();
			}
			
			else if (firstWord.equals("MESG")) {
				System.out.println("Handling message");
				handleMessage();
			}
			
			else if (firstWord.equals("ACCOUNT")) {
				handleRegister();
				System.out.println("done creating the account");
			}
			
			else if (firstWord.equals("RECEIVED")) {
				System.out.println("sending message to update Thread");
				out.println(firstWord);
			}
			
			else if (firstWord.equals("FRND")) {
				System.out.println("handling friend function");
				handleFriend();
			}
		}
	}
	
	//CHAT#0#
	
	//need to check if the username&password is correct
	private void handleChat() {
		System.out.println("handling chat segment");
		
		String chat = messageExtractor.extractNextWord();
		String option = messageExtractor.extractNextWord();
		
		if (option.equals("PUBLIC")) {
			for (int i=0; i< publicChatRooms.size(); i++) {
				ChatRoom chatroom = publicChatRooms.get(i);
				if (chatroom.getRoom_id() == Integer.parseInt(chat)) {
					System.out.println("found the chat room id" + chatroom.getChatName());
					chatroom.addUsers(client);
				}
			}
		}
		
		else if (option.equals("SINGLE")) {
			for (int i=0; i< chatRooms.size(); i++) {
				ChatRoom chatroom = chatRooms.get(i);
				if (chatroom.getChatName().equals(chat)) {
					chatroom.addUsers(client);
				}
			}
		}
		//let all the users know
	}

	private void handleLogin() {
		String username = messageExtractor.extractNextWord();
		String password = messageExtractor.extractNextWord();
		
		client.setUserName(username);
		client.setPassword(password);
		
		for (int i=0; i<clients.size(); i++) {
			String name = clients.get(i).getUsername();
			if (name.equals(username)) {
				if (client.getUsername().equals(username) && 
						client.getPassword().equals(password) ) {
					client.sendMessage("LOGIN#SUCCESS#");
					clients.get(i).killPreviosThread(client);
					client.restoreData(clients.get(i));
					clients.remove(i);
					clients.add(client);
					return;
				} 
			}
		}
		
		client.sendMessage("LOGIN#FAIL#");
		return;
	}

	private void handleRegister() {
		System.out.println("Creating accoutn in register function");
		
		String username = messageExtractor.extractNextWord();
		String password = messageExtractor.extractNextWord();
		
		for (int i=0; i<clients.size(); i++) {
			if (username.equals(clients.get(i).getUsername())) {
				//user name already exist!! wrong!!
				String send = "REGIST#EXIST#";
				client.sendMessage(send);
				return;
			} 
		}
		
		client.setUserName(username);
		client.setPassword(password);
		clients.add(client);
		
		String send = "REGIST#SUCCESS#";
		client.sendMessage(send);
		
		//add all the public chat on the stack
		for (int i=0; i<publicChatRooms.size(); i++) {
			client.newChatRoom(publicChatRooms.get(i), "PUBLIC");
			publicChatRooms.get(i).addUsers(client);
		}

	}

	private void handleFriend() {
		String option = messageExtractor.extractNextWord();
		String friend = messageExtractor.extractNextWord();
		if (option.equals("ADD")) {
			for (int i=0; i<clients.size(); i++) {
				Client tempClient = clients.get(i);
				if (tempClient.getUsername().equals(friend)) {
					System.out.println("found the friend from the clients list");
					tempClient.newFriend(client.getUsername());
					client.setFriendExistFlag(friend);
					return;
				}
			}
			client.setFriendExistFlag("NEXIST");
		} else if (option.equals("DEL")) {
			//perform delete friend
		} else {
			System.out.println("no such user found");
		}
		
	}
	
	private void handleMessage() {
		String message = messageExtractor.extractNextWord();
		String option = messageExtractor.extractNextWord();
		String ChatRoomID = messageExtractor.extractNextWord();
		String date = messageExtractor.extractNextWord();
		
		System.out.println("message is: " + message + " chatRoom is: " + ChatRoomID + " date is: " + date);
		
		if (option.equals("PUBLIC")) {
			int index = isPublicChatExist(ChatRoomID);
			if (index == -1) {
				System.out.println("chatroom somehow doesn't exist -> really wrong!!");
			} else {
				System.out.println("message ->> found the chat");
				String send = messageExtractor.getOriginalMessage() + client.getUsername() + "#";
				messageHandle(publicChatRooms.get(index), send);
			}
		} else {
			int index = isChatRoomExist(ChatRoomID);
			if (index == -1) {
				System.out.println("chatroom somehow doesn't exist -> really wrong!!");
			} else {
				System.out.println("message ->> found the user");
				String send = messageExtractor.getOriginalMessage() + client.getUsername() + "#";
				messageHandle(chatRooms.get(index), send);
			}
		}
	}
	
	private int isPublicChatExist(String chatRoomID) {

		int ID = Integer.parseInt(chatRoomID);
		
		for (int i=0; i<publicChatRooms.size(); i++) {
			if (publicChatRooms.get(i).getRoom_id() == ID ) {
				return i;
			}
		}
		return -1;
	}

	private void messageHandle(ChatRoom chat, String message) {
		ArrayList<Client> users = chat.getUsers();
		
		for (int i=0; i<users.size(); i++) {
			if (users.get(i).getUsername().equals(client.getUsername())) {
				
			} else {
				System.out.println("sending the message to " + users.get(i).getUsername());
				users.get(i).newMessage(message);
			}
		}
	}
	

	private void createChatRoom() {
		String chatRoom = messageExtractor.extractNextWord();
		String option = messageExtractor.extractNextWord();
		
		if (option.equals("PUBLIC")) {
			ChatRoom chat = new ChatRoom(chatRoom);
			chat.setRoom_id(publicChatRooms.size());
			chat.addUsers(client);
			publicChatRooms.add(chat);
			notifyOther("PUBLIC", chat);
		}
	}
	
	private void notifyOther(String option, ChatRoom chat) {
		if (option.equals("PUBLIC")) {
			for (int i=0; i<clients.size(); i++) {
				clients.get(i).newChatRoom(chat, option);
			}
		}
		
	}

	private int isUserNameExist(String name) {

		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getUsername().equals(name)) {
				return i;
			}
		}
		return -1;
	}
	
	private int isChatRoomExist(String chat) {
		
		for (int i=0; i< chatRooms.size(); i++) {
			if (chatRooms.get(i).getChatName().equals(chat)) {
				return i;
			}
		}
		return -1;
	}
	

//	private void closeUser() {
//		client.sendMessage("closechat");
//		client.setUserOn(false);
//	}
//
//	private void handleMessages() {
//		//get the chatroom name
//		String chatRoom = client.getMessage();
//		client.setChatRoomOn(true);
//		
//		//check if the user is in the userlist for the chatroom
//		int index = getChatRoomIndex(chatRoom);
//		ChatRoom chatroom = chatRooms.get(index);
//		
//		if (chatroom.isClientOnTheList(client) == false) {
//			chatroom.addUsers(client);
//			client.addChatRoomQueue(chatRoom);
//		}
//		
//		//one thread for receiving messages
//		//updateMessageRunnable runnable = new updateMessageRunnable(client, chatRoom);
//		//new Thread(runnable).start();
//		
//		while (client.isChatRoomOn()) {
//			String read = client.getMessage();
//			System.out.println(read);
//			users = chatroom.getUsers();
//			
//			//check if the user is still on
//			if (read.equals("closing")) {
//				client.setChatRoomOn(false);
//				closeUser();
//				return;
//			}
//			
//			//send messages to any user that is in the chatroom
//			for (int i=0; i<users.size() && client.isChatRoomOn(); i++) {
//				Client tempClient = users.get(i);
//				if (tempClient.getUsername().equals(username) == false) {
//					tempClient.putMessageOnQueue(read, chatRoom);
//				}
//			}
//		}
//		
//	}
//	
//
//	private int getChatRoomIndex(String chatRoom ) {
//		for (int i=0; i < chatRooms.size(); i++) {
//			if (chatRooms.get(i).getChatName().equals(chatRoom) )
//				return i;
//		}
//		return -1;
//		
//	}
//
//	private ArrayList<Client> getUsers(String chatRoom) {
//		for (int i=0; i< chatRooms.size(); i++) {
//			if (chatRooms.get(i).getChatName().equals(chatRoom)) 
//				return chatRooms.get(i).getUsers();
//		}
//		return null;
//		
//	}
//
//
//
//	
//
//	private void getClients() {
//		// send the size of the clients
//		// send all the names;
//		client.sendMessage(Integer.toString(clients.size()));
//		for (int i = 0; i < clients.size(); i++) {
//			client.sendMessage(clients.get(i).getUsername());
//		}
//	}

	
	
}
