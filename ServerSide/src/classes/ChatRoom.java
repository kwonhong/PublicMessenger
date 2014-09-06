package classes;

import java.util.ArrayList;

public class ChatRoom {
	private ArrayList<Client> users;
	private String chatName;
	private int room_id;
	
	public ChatRoom(String chatName) {
		this.chatName = chatName;
		users = new ArrayList<Client>();
	}
	
	public String getChatName() {
		return chatName;
	}
	
	public ArrayList<Client> getUsers() {
		return users;
	}
	
	public void addUsers (Client client) {
		users.add(client);
	}
	
	public boolean isClientOnTheList (Client client) {
		String username = client.getUsername();
		for (int i=0; i<users.size(); i++) {
			if (users.get(i).getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	public int getRoom_id() {
		return room_id;
	}

	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}
	
}
