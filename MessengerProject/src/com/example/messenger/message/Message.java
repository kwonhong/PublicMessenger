package com.example.messenger.message;

public class Message {
	
	private String sender;
	private String message;
	private int chatroom_id;
	private long message_id;
	private String option;
	private int date;
	

	
	public Message(String sender, String message, int chatroom_id, String option) {
		this.setChatroom_id(chatroom_id);
		this.message = message;
		this.sender = sender;
		this.option = option;
		this.date = 0;
	}
	
	public Message(String sender, String message, int chatroom_id, String option, int date, long message_id) {
		this.setChatroom_id(chatroom_id);
		this.option = option;
		this.message = message;
		this.sender = sender;
		this.date = date;
		this.message_id = message_id;
	}

	public Message(String sender, String message, int chatroomID, String option,
			int date) {
		this.chatroom_id = chatroomID;
		this.option = option;
		this.message = message;
		this.sender = sender;
		this.date = date;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getMessage_id() {
		return message_id;
	}

	public void setMessage_id(long message_id) {
		this.message_id = message_id;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public int getChatroom_id() {
		return chatroom_id;
	}

	public void setChatroom_id(int chatroom_id) {
		this.chatroom_id = chatroom_id;
	}
}
