package com.example.messenger.chatroom;

import java.util.List;

public class ChatRoom {
	private String chatroom;
	private List<String> users;
	private int room_id;
	private String option;
	
	public ChatRoom(int room_id) {
		this.setRoom_id(room_id);
	}
	
	public ChatRoom(int room_id, String chatroom, String option) {
		this.setRoom_id(room_id);
		this.chatroom = chatroom;
		this.option = option;
	}
	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}
	
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}

	public int getRoom_id() {
		return room_id;
	}

	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}

	public String getChatroom() {
		return chatroom;
	}

	public void setChatroom(String chatroom) {
		this.chatroom = chatroom;
	}
	
	
	
}
