package classes;

import java.util.ArrayList;
import java.util.List;

public class MessengerInfo {
	private ArrayList<Client> clients;
	private ArrayList<ChatRoom> chatRooms;
	private ArrayList<ChatRoom> publicChatRooms;
	
	public MessengerInfo() {
		clients = new ArrayList<Client>();
		chatRooms = new ArrayList<ChatRoom>();
		publicChatRooms = new ArrayList<ChatRoom>();
	}
	
	public ArrayList<ChatRoom> getChatRooms() {
		return chatRooms;
	}
	
	public ArrayList<Client> getClients() {
		return clients;
	}

	public ArrayList<ChatRoom> getPublicChatRooms() {
		return publicChatRooms;
	}

	public void setPublicChatRooms(ArrayList<ChatRoom> publicChatRooms) {
		this.publicChatRooms = publicChatRooms;
	}
	
}
