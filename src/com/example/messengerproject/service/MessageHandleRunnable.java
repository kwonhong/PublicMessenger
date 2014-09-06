package com.example.messengerproject.service;

import java.util.List;

import android.content.Context;

import com.example.messenger.chatroom.ChatRoom;
import com.example.messenger.message.Message;
import com.example.messenger.message.MessageExtractor;
import com.example.messengerclient.mysql.DBAdapter;

public class MessageHandleRunnable implements Runnable{

	private Client client;
	private DBAdapter db;
	private MessageExtractor messageExtractor;
	
	public MessageHandleRunnable(Context context, Client client) {
		this.client = client;
		db = new DBAdapter(context);
		messageExtractor = new MessageExtractor();
	}
	
	@Override
	public void run() {
		//case1 - internet has suddenly disconnected
		//case2 - service has been suddenly stopped
		
		System.out.println("messageHandleRunnable is running!!");
		//client.sendMessage("ready");
		while(!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			
			System.out.println("messageHandler still running");
			
			String message = client.getMessage();
			
			//socket has been disconnected somehow
			if (message == null) {
				return;
			}
			System.out.println(message);
			
			if (message != null) {
				
				messageExtractor.setMessage(message);
				String nextWord = messageExtractor.extractNextWord();
				
				if (nextWord.equals("REGIST")){
					handleRegister();
				} else if (nextWord.equals("LOGIN")) {
					handleLogin();
				} else {
					client.sendMessage("RECEIVED#");
					
					
					if (nextWord.equals("CHAT")) {
						handleChat();
					}
					
					else if (nextWord.equals("MESG")) {
						handleMessage();
					}
					
					else if (nextWord.equals("FRND")) {
						handleFriend();
					}
				}
			}
			
			else {
				System.out.println("message was null");
			}
		}
		
		//check if the message is to update the chatrooms
		
		//otherwise handle the rest messages
	}
	
	private void handleLogin() {
		System.out.println("handling handleLogin // in MessageHandleRunnable");
		String option = messageExtractor.extractNextWord();
		
		if (option.equals("SUCCESS")) {
			client.setLoginFlag(option);
		} else if (option.equals("FAIL")){
			//username already exist
			client.setLoginFlag(option);
		}
	}

	private void handleRegister() {
		System.out.println("handling handleRegister // in MessageHandleRunnable");
		String option = messageExtractor.extractNextWord();
		
		if (option.equals("SUCCESS")) {
			client.setAccountFlag(option);
		} else if (option.equals("EXIST")){
			//username already exist
			client.setAccountFlag(option);
		}
		
	}

	private void handleFriend() {
		System.out.println("handling handleFirned // in MessageHandleRunnable");
		String option = messageExtractor.extractNextWord();
		
		if (option.equals("ADD")) {
			String friend = messageExtractor.extractNextWord();
			System.out.println("Adding friend // in MessageHandleRunnable");
			client.addFriend(friend);
		} else if (option.equals("DEL")) {
			
		} else if (option.equals("EXIST")) {
			client.setFriendFlag(option);
		} else if (option.equals("NEXIST")) {
			client.setFriendFlag(option);
		}
	}

	private void handleMessage() {
		System.out.println("Handling the message" );
		String message = messageExtractor.extractNextWord();
		String option = messageExtractor.extractNextWord();
		int chatroomID = Integer.parseInt(messageExtractor.extractNextWord());
		String date = messageExtractor.extractNextWord();
		String sender = messageExtractor.extractNextWord();	
		
		Message mess = new Message(sender, message, chatroomID, option, Integer.parseInt(date));
		db.createMessage(mess);
		client.addMessageOnQueue(mess);
	}

	//CHAT#ADD#PUBLIC#ROOM_ID#CHATROOM#
	private void handleChat() {
		String method = messageExtractor.extractNextWord();
		String option = messageExtractor.extractNextWord();
		int room_id = Integer.parseInt(messageExtractor.extractNextWord());
		String chatName = messageExtractor.extractNextWord();
		
		System.out.println("handlign handlechat method");
		
		if (method.equals("ADD")) {
			System.out.println("extracting word");
			System.out.println("chatName: " + chatName);
			db.createChat(new ChatRoom(room_id, chatName, option));
			client.setChatRoomSet(true);
		}
		
		else  {
			//delete or modify
		}
		
	}
}
