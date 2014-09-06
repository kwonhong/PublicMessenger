package com.example.messenger.message;

import java.util.List;
import java.util.Queue;

import android.content.Context;
import android.os.Handler;
import android.widget.ListView;
import android.widget.TextView;

import com.example.messengerclient.mysql.DBAdapter;
import com.example.messengerproject.service.Client;

public class UpdatingMessageRunnable implements Runnable {

	private int chatRoomID;
	private String option;
	private Client client;
	private DBAdapter db;
	private Handler handler;
	private MessageListAdapter adapter;
	private List<Message> list;

	public UpdatingMessageRunnable(Context context, MessageListAdapter adapter, List<Message> list,
			int chatRoomID, String option, Handler handler, Client client) {
		this.adapter = adapter;
		this.list = list;
		this.option = option;
		this.chatRoomID = chatRoomID;
		this.client = client;
		this.db = new DBAdapter(context);
		this.handler = handler;
		//client.setChatRoomOn(chatRoom);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		Queue<Message> stackMessage = client.getMessages();
		List<Message> messages = db.getMessages(chatRoomID);
		for (int i = 0; i < messages.size(); i++) {
			Message tempMessage = messages.get(i);
			list.add(tempMessage);
			handler.post(new updateUIThread());
		}
		
		while (!Thread.currentThread().isInterrupted()) {
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (stackMessage != null && stackMessage.size() > 0) {
				list.add(stackMessage.poll());
				handler.post(new updateUIThread());
			}
			
			
		}
	}
	
	class updateUIThread implements Runnable {

		@Override
		public void run() {
			adapter.notifyDataSetChanged();
		}
	}

}
